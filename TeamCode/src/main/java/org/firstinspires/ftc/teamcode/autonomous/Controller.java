package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;

public class Controller {
    private final LinearOpMode opMode;
    private final RobotContainer robot;
    private Follower follower;

    public Controller(LinearOpMode opMode, RobotContainer robot) {
        this.opMode = opMode;
        this.robot = robot;
    }

    public void init() {
        // Assume robot.init has already been called in BaseAutoOpMode
        follower = robot.getFollower();
    }

    public void setStartingPose(Pose startPose) {
        robot.setFieldPose(startPose.getX(), startPose.getY(), Math.toDegrees(startPose.getHeading()));
    }

    /**
     * Spins up the flywheel and keeps it spinning at the given RPM.
     * This will be running in the background whenever update() is called.
     */
    public void startShooter(double rpm) {
        robot.shooter.setTargetRpm(rpm);
    }

    /**
     * Turns on the intake to the default forward speed.
     */
    public void enableIntake() {
        robot.intake.reverse(-AutonomousConstants.INTAKE_FORWARD_SPEED); // Assuming forward is positive in reverse? Wait, IntakeSubsystem uses runFromTrigger.
        // Actually, IntakeSubsystem has reverse(power) where positive power reverses it.
        // Let's look at IntakeSubsystem. runFromTrigger(value) runs forward. reverse(value) runs backward.
    }
    
    // I need to correct IntakeSubsystem calls later. Let's make helper methods for this:
    public void runIntakeForward() {
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
    }
    
    public void runIntakeReverse() {
        robot.intake.reverse(AutonomousConstants.INTAKE_REVERSE_SPEED);
    }

    public void disableIntake() {
        robot.intake.stop();
    }

    /**
     * Drives to a point and blocks until the robot is near the target or opmode stops.
     */
    public void pathTo(Pose targetPose) {
        if (!opMode.opModeIsActive()) return;

        Path path = new Path(new BezierLine(
                new Point(follower.getPose()),
                new Point(targetPose)
        ));
        path.setLinearHeadingInterpolation(follower.getPose().getHeading(), targetPose.getHeading());

        follower.followPath(path);

        while (opMode.opModeIsActive() && follower.isBusy()) {
            update();
        }
    }

    /**
     * Shoots for a specific duration in milliseconds. 
     * Runs the feeder motor, and cycles the intake (900ms forward, 100ms reverse).
     */
    public void shoot(long durationMs) {
        if (!opMode.opModeIsActive()) return;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        ElapsedTime cycleTimer = new ElapsedTime();
        cycleTimer.reset();
        
        robot.shooter.setFeederPower(AutonomousConstants.FEEDER_SPEED);

        while (opMode.opModeIsActive() && timer.milliseconds() < durationMs) {
            long cycleTime = (long) cycleTimer.milliseconds();
            long totalCycleMs = AutonomousConstants.SHOOT_FORWARD_CYCLE_MS + AutonomousConstants.SHOOT_REVERSE_CYCLE_MS;
            
            if (cycleTime > totalCycleMs) {
                cycleTimer.reset();
                cycleTime = 0;
            }

            if (cycleTime < AutonomousConstants.SHOOT_FORWARD_CYCLE_MS) {
                runIntakeForward();
            } else {
                runIntakeReverse();
            }

            update();
        }

        robot.shooter.setFeederPower(0.0);
        // Turn intake back to normal forward spinning after shooting finishes,
        // since the user states "intake should be running the entire time"
        runIntakeForward();
    }

    /**
     * Updates all background processes like the path follower and shooter PID.
     */
    public void update() {
        follower.update();
        robot.shooter.update();
    }
}
