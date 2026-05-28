package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.constants.AutoConstants;
import org.firstinspires.ftc.teamcode.constants.DriveConstants;
import org.firstinspires.ftc.teamcode.constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.constants.IntakeConstants;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.subsystems.AutoCycleSubsystem;

public abstract class BaseGoalTeleOp extends OpMode {
    private final RobotContainer robot = new RobotContainer();
    private boolean lastStart;
    private long lockoutEndTime = 0;

    private RevColorSensorV3 colorShootSensor;
    private double lastActiveCycleRpm = ShooterConstants.DEFAULT_TARGET_RPM;

    protected abstract String getDriveTeamName();

    protected abstract boolean isBlueAlliance();

    @Override
    public void init() {
        robot.init(hardwareMap, true);
        try {
            colorShootSensor = hardwareMap.get(RevColorSensorV3.class, HardwareConstants.COLOR_SHOOT_SENSOR);
        } catch (Exception ignored) {
            colorShootSensor = null;
        }
    }

    @Override
    public void start() {
        robot.resetRobotState();
        robot.drive.setBrakeMode(true);
        robot.shooter.setBrakeMode(false);
        robot.setFieldPose(0.0, 0.0, 0.0);
        lastActiveCycleRpm = ShooterConstants.DEFAULT_TARGET_RPM;
    }

    @Override
    public void loop() {
        boolean startPressed = gamepad1.start;
        if (startPressed && !lastStart) {
            robot.resetRobotState();
            robot.setFieldPose(0.0, 0.0, 0.0);
            lockoutEndTime = System.currentTimeMillis() + (long)(DriveConstants.PINPOINT_CALIBRATION_DELAY_SECONDS * 1000);
        }
        lastStart = startPressed;

        if (System.currentTimeMillis() < lockoutEndTime) {
            robot.drive.setBrakeMode(true);
            robot.drive.driveFieldOriented(0, 0, 0);
            robot.intake.runFromTrigger(0.0);
            robot.shooter.setTargetRpm(0.0);
            robot.shooter.setFeederPower(0.0);
            robot.shooter.update();
            robot.getFollower().update();
            telemetry.addData("STATUS", "CALIBRATING PINPOINT - CONTROLS LOCKED");
            telemetry.update();
            return;
        }

        Follower follower = robot.getFollower();
        follower.update();
        Pose followerPose = follower.getPose();

        boolean cycleAHeld = gamepad1.a;
        boolean cycleBHeld = gamepad1.b;
        boolean cycleRequested = cycleAHeld || cycleBHeld;

        AutoCycleSubsystem.Slot cycleSlot = cycleAHeld
                ? AutoCycleSubsystem.Slot.A
                : AutoCycleSubsystem.Slot.B;

        if (cycleRequested) {
            robot.autoCycle.beginOrUpdate(cycleSlot, isBlueAlliance(), followerPose, follower);
            follower.update();
            followerPose = follower.getPose();
        } else {
            robot.autoCycle.cancel(follower);

            robot.drive.setBrakeMode(true);
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = -gamepad1.right_stick_x;
            robot.drive.driveFieldOriented(forward, strafe, turn);
        }

        if (gamepad1.left_bumper) {
            robot.intake.reverse(IntakeConstants.BUMPER_REVERSE_POWER);
        } else if (robot.autoCycle.isActive()) {
            robot.intake.runFromTrigger(AutoConstants.CYCLE_INTAKE_POWER);
        } else {
            robot.intake.runFromTrigger(gamepad1.left_trigger);
        }

        double shooterTargetRpm;
        if (robot.autoCycle.isActive()) {
            shooterTargetRpm = robot.autoCycle.getSuggestedShooterRpm();
            lastActiveCycleRpm = shooterTargetRpm;
        } else if (gamepad1.right_trigger > ShooterConstants.SHOOTER_TRIGGER_DEADBAND) {
            shooterTargetRpm = lastActiveCycleRpm
                    + (gamepad1.right_trigger * (ShooterConstants.MAX_TARGET_RPM - lastActiveCycleRpm));
        } else {
            shooterTargetRpm = lastActiveCycleRpm;
        }

        int alpha = -1;
        if (colorShootSensor != null) {
            alpha = colorShootSensor.alpha();
        }

        robot.shooter.setTargetRpm(shooterTargetRpm);
        double feederPower;
        if (gamepad1.right_bumper) {
            feederPower = 1.0;
        } else if (robot.autoCycle.isActive()) {
            feederPower = robot.autoCycle.getSuggestedFeederPower();
        } else {
            boolean shouldStopIndexer = alpha > ShooterConstants.FEEDER_ALPHA_STOP_THRESHOLD;
            feederPower = shouldStopIndexer ? 0.0 : ShooterConstants.FEEDER_INDEX_POWER;
        }
        robot.shooter.setFeederPower(feederPower);
        robot.shooter.update();

        if (colorShootSensor != null) {
            int red = colorShootSensor.red();
            int green = colorShootSensor.green();
            int blue = colorShootSensor.blue();
            telemetry.addData("ColorShoot", "%s (R:%d G:%d B:%d A:%d)",
                    dominantColorName(red, green, blue), red, green, blue, alpha);
        } else {
            telemetry.addData("ColorShoot", "NOT FOUND (%s)", HardwareConstants.COLOR_SHOOT_SENSOR);
        }

        telemetry.addData("Alliance", getDriveTeamName());
        telemetry.addData("Drive Mode", cycleRequested ? "PEDRO AUTO-CYCLE" : "MANUAL");
        telemetry.addData("Cycle A", cycleAHeld ? "HELD" : "OFF");
        telemetry.addData("Cycle B", cycleBHeld ? "HELD" : "OFF");
        telemetry.addData("Auto Cycle Active", robot.autoCycle.isActive());
        if (robot.autoCycle.getActiveTarget() != null) {
            telemetry.addData("Cycle Target", "X %.2f / Y %.2f / H %.1f",
                    robot.autoCycle.getActiveTarget().getX(),
                    robot.autoCycle.getActiveTarget().getY(),
                    Math.toDegrees(robot.autoCycle.getActiveTarget().getHeading()));
        }
        telemetry.addData("Cycle Dist", "%.2f in", robot.autoCycle.getLastDistanceToTarget());
        telemetry.addData("Cycle ETA", "%.3f s", robot.autoCycle.getLastEtaSeconds());
        telemetry.addData("Feeder Index", alpha > ShooterConstants.FEEDER_ALPHA_STOP_THRESHOLD ? "STOPPED" : "RUNNING");
        telemetry.addData("Reset Pose", "0.00, 0.00, 0.0 deg");
        telemetry.addData("Heading (Pedro deg)", "%.1f", Math.toDegrees(followerPose.getHeading()));
        telemetry.addData("Position (Pedro)", "X %.2f / Y %.2f in", followerPose.getX(), followerPose.getY());
        telemetry.addData("Position (Pinpoint)", "X %.2f / Y %.2f in", robot.drive.getX(DistanceUnit.INCH), robot.drive.getY(DistanceUnit.INCH));
        telemetry.addData("Intake Servo", "%.2f", robot.intake.getServoPower());
        telemetry.addData("Intake Motor", "%.2f", robot.intake.getMotorPower());
        telemetry.addData("Shooter RPM", "%.0f / %.0f", robot.shooter.getCurrentRpm(), robot.shooter.getTargetRpm());
        telemetry.addData("Feeder Power", "%.2f", robot.shooter.getFeederPower());
        telemetry.update();
    }

    private static String dominantColorName(int red, int green, int blue) {
        if (red >= green && red >= blue) {
            return "RED";
        }
        if (green >= red && green >= blue) {
            return "GREEN";
        }
        return "BLUE";
    }

    @Override
    public void stop() {
        robot.stopAll();
    }
}
