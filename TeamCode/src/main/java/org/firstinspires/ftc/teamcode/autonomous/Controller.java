package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

public class Controller {
    private final LinearOpMode opMode;
    private final RobotContainer robot;
    private Follower follower;
    private RevColorSensorV3 colorShootSensor;
    private boolean isShooting = false;

    public Controller(LinearOpMode opMode, RobotContainer robot) {
        this.opMode = opMode;
        this.robot = robot;
    }

    public void init() {
        // Assume robot.init has already been called in BaseAutoOpMode
        follower = robot.getFollower();
        try {
            colorShootSensor = opMode.hardwareMap.get(RevColorSensorV3.class, HardwareConstants.COLOR_SHOOT_SENSOR);
        } catch (Exception ignored) {
            colorShootSensor = null;
        }

        // Start the intake forward at configurable speed immediately
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
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
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
        sleep(AutonomousConstants.SHOOT_FORWARD_CYCLE_MS);
        robot.intake.reverse(AutonomousConstants.INTAKE_REVERSE_SPEED);
        sleep(AutonomousConstants.SHOOT_REVERSE_CYCLE_MS);
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
    }
    
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

        com.pedropathing.paths.PathBuilder builder = follower.pathBuilder();
        builder.addPath(new BezierLine(follower.getPose(), targetPose))
                .setLinearHeadingInterpolation(follower.getPose().getHeading(), targetPose.getHeading());
        PathChain path = builder.build();

        follower.followPath(path);

        while (opMode.opModeIsActive() && follower.isBusy()) {
            update();
        }

        delayBetweenCommands();
    }

    /**
     * Drives to a point with a custom maximum speed scaling and blocks until the robot is near the target or opmode stops.
     */
    public void pathTo(Pose targetPose, double speedPercent) {
        if (!opMode.opModeIsActive()) return;

        com.pedropathing.paths.PathBuilder builder = follower.pathBuilder();
        builder.addPath(new BezierLine(follower.getPose(), targetPose))
                .setLinearHeadingInterpolation(follower.getPose().getHeading(), targetPose.getHeading());
        PathChain path = builder.build();

        follower.followPath(path, speedPercent, true);

        while (opMode.opModeIsActive() && follower.isBusy()) {
            update();
        }

        delayBetweenCommands();
    }

    /**
     * Shoots for a specific duration in milliseconds. 
     * Runs the feeder motor, and cycles the intake (900ms forward, 100ms reverse).
     */
    public void shoot(long durationMs) {
        if (!opMode.opModeIsActive()) return;

        // Wait to allow the flywheel to spin up before feeding the balls
        if (AutonomousConstants.SPIN_UP_DELAY_MS > 0) {
            ElapsedTime spinUpTimer = new ElapsedTime();
            while (opMode.opModeIsActive() && spinUpTimer.milliseconds() < AutonomousConstants.SPIN_UP_DELAY_MS) {
                update();
            }
        }

        isShooting = true;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        
        robot.shooter.setFeederPower(AutonomousConstants.FEEDER_SPEED);
        robot.intake.runFromTrigger(1.0);

        while (opMode.opModeIsActive() && timer.milliseconds() < durationMs) {
            update();
        }

        robot.shooter.setFeederPower(0.0);
        isShooting = false;

        delayBetweenCommands();
    }

    /**
     * Updates all background processes like the path follower and shooter PID.
     */
    public void update() {
        follower.update();

        // Always ensure the intake is running forward during autonomous
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);

        if (!isShooting) {
            double feederPower;
            int alpha = -1;
            if (colorShootSensor != null) {
                alpha = colorShootSensor.alpha();
            }
            boolean shouldStopIndexer = alpha > ShooterConstants.FEEDER_ALPHA_STOP_THRESHOLD;
            feederPower = shouldStopIndexer ? 0.0 : ShooterConstants.FEEDER_INDEX_POWER;
            robot.shooter.setFeederPower(feederPower);
        }

        robot.shooter.update();
    }

    /**
     * Sleeps for the specified duration in milliseconds while updating background subsystems.
     */
    public void sleep(long durationMs) {
        if (!opMode.opModeIsActive()) return;

        ElapsedTime timer = new ElapsedTime();
        while (opMode.opModeIsActive() && timer.milliseconds() < durationMs) {
            update();
        }
    }

    private void delayBetweenCommands() {
        if (AutonomousConstants.COMMAND_DELAY_MS > 0 && opMode.opModeIsActive()) {
            ElapsedTime delayTimer = new ElapsedTime();
            while (opMode.opModeIsActive() && delayTimer.milliseconds() < AutonomousConstants.COMMAND_DELAY_MS) {
                update();
            }
        }
    }
}
