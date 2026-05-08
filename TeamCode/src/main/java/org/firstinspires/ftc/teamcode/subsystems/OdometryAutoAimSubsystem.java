package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.constants.AutoAimConstants;

public class OdometryAutoAimSubsystem {
    private boolean enabled;
    private double targetXInches = AutoAimConstants.BLUE_GOAL_X_INCHES;
    private double targetYInches = AutoAimConstants.BLUE_GOAL_Y_INCHES;

    private double integral;
    private double lastErrorRadians;

    public void setEnabled(boolean shouldEnable) {
        enabled = shouldEnable;
        if (!shouldEnable) {
            resetPid();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setTargetInches(double xInches, double yInches) {
        targetXInches = xInches;
        targetYInches = yInches;
    }

    public double getTargetXInches() {
        return targetXInches;
    }

    public double getTargetYInches() {
        return targetYInches;
    }

    public double updateTurnCommand(double robotXInches, double robotYInches, double robotHeadingDeg) {
        double targetRobotHeadingDeg = getDesiredRobotHeadingDeg(robotXInches, robotYInches);
        double headingErrorDeg = angleErrorDeg(targetRobotHeadingDeg, robotHeadingDeg);
        double headingErrorRad = Math.toRadians(headingErrorDeg);

        integral += headingErrorRad;
        double derivative = headingErrorRad - lastErrorRadians;
        lastErrorRadians = headingErrorRad;

        double feedForward = headingErrorRad == 0.0
                ? 0.0
                : AutoAimConstants.TURN_KF * Math.signum(headingErrorRad);

        double output = (AutoAimConstants.TURN_KP * headingErrorRad)
                + (AutoAimConstants.TURN_KI * integral)
                + (AutoAimConstants.TURN_KD * derivative)
                + feedForward;

        return Range.clip(output, -AutoAimConstants.MAX_TURN, AutoAimConstants.MAX_TURN);
    }

    public double getDesiredShooterHeadingDeg(double robotXInches, double robotYInches) {
        double dx = targetXInches - robotXInches;
        double dy = targetYInches - robotYInches;
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public double getDesiredRobotHeadingDeg(double robotXInches, double robotYInches) {
        double shooterHeadingDeg = getDesiredShooterHeadingDeg(robotXInches, robotYInches);
        return wrapDegreesSigned(shooterHeadingDeg - AutoAimConstants.SHOOTER_LEFT_OFFSET_DEGREES);
    }

    public boolean isOnTarget(double robotXInches, double robotYInches, double robotHeadingDeg) {
        double targetRobotHeadingDeg = getDesiredRobotHeadingDeg(robotXInches, robotYInches);
        return Math.abs(angleErrorDeg(targetRobotHeadingDeg, robotHeadingDeg)) <= AutoAimConstants.HEADING_TOLERANCE_DEG;
    }

    public void resetPid() {
        integral = 0.0;
        lastErrorRadians = 0.0;
    }

    private static double angleErrorDeg(double targetDeg, double currentDeg) {
        return wrapDegreesSigned(targetDeg - currentDeg);
    }

    private static double wrapDegreesSigned(double angleDegrees) {
        while (angleDegrees > 180.0) {
            angleDegrees -= 360.0;
        }
        while (angleDegrees <= -180.0) {
            angleDegrees += 360.0;
        }
        return angleDegrees;
    }
}
