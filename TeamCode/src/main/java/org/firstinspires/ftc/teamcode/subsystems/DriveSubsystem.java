package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.constants.DriveConstants;
import org.firstinspires.ftc.teamcode.constants.HardwareConstants;

public class DriveSubsystem {
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private GoBildaPinpointDriver pinpoint;

    private double headingOffsetRad;
    private boolean brakeMode;

    public void init(HardwareMap hardwareMap) {
        motorFrontLeft = hardwareMap.get(DcMotor.class, HardwareConstants.FRONT_LEFT_MOTOR);
        motorFrontRight = hardwareMap.get(DcMotor.class, HardwareConstants.FRONT_RIGHT_MOTOR);
        motorBackLeft = hardwareMap.get(DcMotor.class, HardwareConstants.BACK_LEFT_MOTOR);
        motorBackRight = hardwareMap.get(DcMotor.class, HardwareConstants.BACK_RIGHT_MOTOR);

        motorFrontLeft.setDirection(DriveConstants.LEFT_DIRECTION);
        motorBackLeft.setDirection(DriveConstants.LEFT_DIRECTION);
        motorFrontRight.setDirection(DriveConstants.RIGHT_DIRECTION);
        motorBackRight.setDirection(DriveConstants.RIGHT_DIRECTION);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, HardwareConstants.PINPOINT);
        pinpoint.setOffsets(
                DriveConstants.PINPOINT_OFFSET_X_MM,
                DriveConstants.PINPOINT_OFFSET_Y_MM,
                DistanceUnit.MM
        );
        pinpoint.setEncoderResolution(DriveConstants.PINPOINT_RESOLUTION);
        pinpoint.setEncoderDirections(
                DriveConstants.STRAFE_DIRECTION,
                DriveConstants.FORWARD_DIRECTION
        );
        pinpoint.resetPosAndIMU();

        setBrakeMode(true);
        resetHeading();
    }

    public void driveFieldOriented(double forward, double strafe, double turn) {
        pinpoint.update();

        double clippedForward = applyDeadband(forward, DriveConstants.DRIVE_INPUT_DEADBAND);
        double clippedStrafe = applyDeadband(strafe, DriveConstants.DRIVE_INPUT_DEADBAND);
        double clippedTurn = applyDeadband(turn, DriveConstants.TURN_INPUT_DEADBAND);

        double headingRad = normalizeRadians(pinpoint.getHeading(AngleUnit.RADIANS) - headingOffsetRad);
        double cosH = Math.cos(-headingRad);
        double sinH = Math.sin(-headingRad);

        double robotX = clippedStrafe * cosH - clippedForward * sinH;
        double robotY = clippedStrafe * sinH + clippedForward * cosH;

        double frontLeft = robotY + robotX + clippedTurn;
        double frontRight = robotY - robotX - clippedTurn;
        double backLeft = robotY - robotX + clippedTurn;
        double backRight = robotY + robotX - clippedTurn;

        double maxMagnitude = Math.max(1.0, Math.max(
                Math.abs(frontLeft),
                Math.max(Math.abs(frontRight), Math.max(Math.abs(backLeft), Math.abs(backRight)))
        ));

        motorFrontLeft.setPower(frontLeft / maxMagnitude);
        motorFrontRight.setPower(frontRight / maxMagnitude);
        motorBackLeft.setPower(backLeft / maxMagnitude);
        motorBackRight.setPower(backRight / maxMagnitude);
    }

    public void stop() {
        motorFrontLeft.setPower(0.0);
        motorFrontRight.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorBackRight.setPower(0.0);
    }

    public void resetHeading() {
        pinpoint.resetPosAndIMU();
        headingOffsetRad = 0.0;
    }

    public void setFieldPose(double xInches, double yInches, double headingDegrees) {
        pinpoint.resetPosAndIMU();
        pinpoint.setPosition(new Pose2D(
                DistanceUnit.INCH,
                xInches,
                yInches,
                AngleUnit.DEGREES,
                headingDegrees
        ));
        headingOffsetRad = 0.0;
    }

    public void setBrakeMode(boolean shouldBrake) {
        brakeMode = shouldBrake;
        DcMotor.ZeroPowerBehavior behavior = shouldBrake
                ? DcMotor.ZeroPowerBehavior.BRAKE
                : DcMotor.ZeroPowerBehavior.FLOAT;

        motorFrontLeft.setZeroPowerBehavior(behavior);
        motorFrontRight.setZeroPowerBehavior(behavior);
        motorBackLeft.setZeroPowerBehavior(behavior);
        motorBackRight.setZeroPowerBehavior(behavior);
    }

    public boolean isBrakeMode() {
        return brakeMode;
    }

    public double getHeadingDegrees() {
        pinpoint.update();
        double headingDeg = Math.toDegrees(normalizeRadians(pinpoint.getHeading(AngleUnit.RADIANS) - headingOffsetRad));
        if (headingDeg < 0) {
            headingDeg += 360.0;
        }
        return headingDeg;
    }

    public double getX(DistanceUnit distanceUnit) {
        pinpoint.update();
        return pinpoint.getPosX(distanceUnit);
    }

    public double getY(DistanceUnit distanceUnit) {
        pinpoint.update();
        return pinpoint.getPosY(distanceUnit);
    }

    public GoBildaPinpointDriver getPinpoint() {
        return pinpoint;
    }

    private static double normalizeRadians(double angle) {
        while (angle > Math.PI) {
            angle -= 2.0 * Math.PI;
        }
        while (angle <= -Math.PI) {
            angle += 2.0 * Math.PI;
        }
        return angle;
    }

    private static double applyDeadband(double value, double deadband) {
        if (Math.abs(value) <= deadband) {
            return 0.0;
        }
        return Range.clip(value, -1.0, 1.0);
    }
}
