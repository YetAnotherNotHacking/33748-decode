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
    private volatile boolean isIntakeCycling = false;
    private volatile boolean intakePaused = false;
    private Thread intakeCycleThread = null;

    public Controller(LinearOpMode opMode, RobotContainer robot) {
        this.opMode = opMode;
        this.robot = robot;
    }

    public void init() {
        // assume robot.init was called by the base auto opmode, subsys should be ready already
        follower = robot.getFollower();
        try {
            colorShootSensor = opMode.hardwareMap.get(RevColorSensorV3.class, HardwareConstants.COLOR_SHOOT_SENSOR);
        } catch (Exception ignored) {
            colorShootSensor = null;
        }

        // old intake patch
        // idk if removing this would break it and it works rn so its staying
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
    }

    public void setStartingPose(Pose startPose) {
        robot.setFieldPose(startPose.getX(), startPose.getY(), Math.toDegrees(startPose.getHeading()));
    }

    // run shooter at an rpm
    public void startShooter(double rpm) {
        robot.shooter.setTargetRpm(rpm);
    }

    // background thread for intake unjamming assuming it's currently enabled.
    public void enableIntake() {
        if (!opMode.opModeIsActive() || isIntakeCycling) return;

        isIntakeCycling = true;
        intakeCycleThread = new Thread(() -> {
            while (isIntakeCycling && opMode.opModeIsActive()) {
                // Phase 1: run forward
                robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
                long forwardEnd = System.currentTimeMillis() + AutonomousConstants.SHOOT_FORWARD_CYCLE_MS;
                while (isIntakeCycling && opMode.opModeIsActive() && System.currentTimeMillis() < forwardEnd) {
                    try { Thread.sleep(10); } catch (InterruptedException e) { return; }
                }

                if (!isIntakeCycling || !opMode.opModeIsActive()) break;

                // Phase 2: reverse for jam clearing if not paused
                if (!intakePaused) {
                    robot.intake.reverse(AutonomousConstants.INTAKE_REVERSE_SPEED);
                    long reverseEnd = System.currentTimeMillis() + AutonomousConstants.SHOOT_REVERSE_CYCLE_MS;
                    while (isIntakeCycling && opMode.opModeIsActive() && System.currentTimeMillis() < reverseEnd) {
                        try { Thread.sleep(10); } catch (InterruptedException e) { return; }
                    }
                }
            }
            robot.intake.stop();
        });
        intakeCycleThread.setDaemon(true);
        intakeCycleThread.start();
    }
    
    public void runIntakeForward() {
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
    }
    
    public void runIntakeReverse() {
        robot.intake.reverse(AutonomousConstants.INTAKE_REVERSE_SPEED);
    }

    public void disableIntake() {
        isIntakeCycling = false;
        if (intakeCycleThread != null) {
            intakeCycleThread.interrupt();
            try { intakeCycleThread.join(500); } catch (InterruptedException ignored) {}
            intakeCycleThread = null;
        }
        robot.intake.stop();
    }

    // pause intake unjamming cycles for intaking of rows
    public void pauseIntakeCycling() {
        intakePaused = true;
        // Immediately drive forward so there is no gap at the moment of pause
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);
    }

    // go back to unjamming intake cycle
    public void resumeIntakeCycling() {
        intakePaused = false;
    }

    // drive to point using pedro (no frac speed)
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

    // drive to point using pedro, fractional speed
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

    // shoot for ms
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

        // Intake is managed by the background cycling thread when enableIntake() is active

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

    // sleep (for spinup waits/other things)
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
