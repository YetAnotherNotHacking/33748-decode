package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.constants.AutoConstants;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

public class AutoCycleSubsystem {
    public enum Slot {
        A,
        B,
        C,
        D
    }

    private Slot activeSlot;
    private boolean blueAlliance;
    private boolean pathStarted;

    private Pose activeTarget;

    private double previousX;
    private double previousY;
    private long previousTimeNanos;
    private boolean hasPreviousPose;

    private double lastDistanceToTarget;
    private double lastEtaSeconds = Double.POSITIVE_INFINITY;
    private boolean feederShouldRun;
    private long feederRunStartTime = 0;
    private boolean wasFeederRunning = false;

    public void beginOrUpdate(Slot slot, boolean isBlueAlliance, Pose currentPose, Follower follower) {
        if (!pathStarted || activeSlot != slot || blueAlliance != isBlueAlliance) {
            activeSlot = slot;
            blueAlliance = isBlueAlliance;
            activeTarget = getTargetPose(slot, isBlueAlliance);
            follower.followPath(buildSingleSegmentPath(follower, currentPose, activeTarget));
            pathStarted = true;
            hasPreviousPose = false;
            feederShouldRun = false;
            wasFeederRunning = false;
        }

        updateProgress(currentPose, follower);
    }

    public void cancel(Follower follower) {
        if (!pathStarted) {
            return;
        }

        stopFollowerPathIfPossible(follower);
        pathStarted = false;
        activeSlot = null;
        activeTarget = null;
        hasPreviousPose = false;
        feederShouldRun = false;
        wasFeederRunning = false;
        lastDistanceToTarget = 0.0;
        lastEtaSeconds = Double.POSITIVE_INFINITY;
    }

    public boolean isActive() {
        return pathStarted;
    }

    public double getSuggestedShooterRpm() {
        if (!pathStarted) {
            return 0.0;
        }
        if (activeSlot == Slot.D) return ShooterConstants.CYCLE_D_MIDWAY_TARGET_RPM;
        if (activeSlot == Slot.C) return ShooterConstants.CYCLE_C_OPPOSITION_TARGET_RPM;
        return activeSlot == Slot.A ? ShooterConstants.CYCLE_A_TARGET_RPM : ShooterConstants.CYCLE_B_TARGET_RPM;
    }

    public double getSuggestedFeederPower() {
        if (!feederShouldRun) {
            return 0.0;
        }
        long elapsed = System.currentTimeMillis() - feederRunStartTime;
        long pauseTime = 0;
        if (activeSlot == Slot.A) pauseTime = ShooterConstants.CYCLE_A_FEEDER_PULSE_PAUSE_TIME_MS;
        else if (activeSlot == Slot.B) pauseTime = ShooterConstants.CYCLE_B_FEEDER_PULSE_PAUSE_TIME_MS;
        else if (activeSlot == Slot.C) pauseTime = ShooterConstants.CYCLE_C_FEEDER_PULSE_PAUSE_TIME_MS;
        else pauseTime = ShooterConstants.CYCLE_D_FEEDER_PULSE_PAUSE_TIME_MS;
        long cycleTime = ShooterConstants.FEEDER_PULSE_RUN_TIME_MS + pauseTime;
        if (cycleTime <= 0) return ShooterConstants.FEEDER_MANUAL_POWER;
        long timeInCycle = elapsed % cycleTime;
        if (timeInCycle < ShooterConstants.FEEDER_PULSE_RUN_TIME_MS) {
            return ShooterConstants.FEEDER_MANUAL_POWER;
        } else {
            return 0.0;
        }
    }

    public Pose getActiveTarget() {
        return activeTarget;
    }

    public double getLastDistanceToTarget() {
        return lastDistanceToTarget;
    }

    public double getLastEtaSeconds() {
        return lastEtaSeconds;
    }

    private void updateProgress(Pose currentPose, Follower follower) {
        if (activeTarget == null) {
            feederShouldRun = false;
            wasFeederRunning = false;
            lastDistanceToTarget = 0.0;
            lastEtaSeconds = Double.POSITIVE_INFINITY;
            return;
        }

        double dx = activeTarget.getX() - currentPose.getX();
        double dy = activeTarget.getY() - currentPose.getY();
        lastDistanceToTarget = Math.hypot(dx, dy);

        double headingErrorDeg = Math.toDegrees(wrapRadians(activeTarget.getHeading() - currentPose.getHeading()));
        boolean arrived = lastDistanceToTarget <= AutoConstants.CYCLE_POSITION_TOLERANCE_INCHES
                && Math.abs(headingErrorDeg) <= AutoConstants.CYCLE_HEADING_TOLERANCE_DEGREES
                && !follower.isBusy();

        long nowNanos = System.nanoTime();
        if (!hasPreviousPose) {
            hasPreviousPose = true;
            previousX = currentPose.getX();
            previousY = currentPose.getY();
            previousTimeNanos = nowNanos;
            lastEtaSeconds = Double.POSITIVE_INFINITY;
        } else {
            double dtSeconds = (nowNanos - previousTimeNanos) / 1e9;
            if (dtSeconds > 1e-6) {
                double vx = (currentPose.getX() - previousX) / dtSeconds;
                double vy = (currentPose.getY() - previousY) / dtSeconds;
                double speed = Math.hypot(vx, vy);
                lastEtaSeconds = speed > 1e-3 ? (lastDistanceToTarget / speed) : Double.POSITIVE_INFINITY;
            }
            previousX = currentPose.getX();
            previousY = currentPose.getY();
            previousTimeNanos = nowNanos;
        }

        boolean shouldRunNow = arrived || lastEtaSeconds <= AutoConstants.CYCLE_FEED_LEAD_SECONDS;
        if (shouldRunNow && !wasFeederRunning) {
            feederRunStartTime = System.currentTimeMillis();
        }
        wasFeederRunning = shouldRunNow;
        feederShouldRun = shouldRunNow;
    }

    private static Pose getTargetPose(Slot slot, boolean isBlueAlliance) {
        if (isBlueAlliance) {
            if (slot == Slot.A) {
                return new Pose(
                        AutoConstants.BLUE_CYCLE_A_X_INCHES,
                        AutoConstants.BLUE_CYCLE_A_Y_INCHES,
                        Math.toRadians(AutoConstants.BLUE_CYCLE_A_HEADING_DEGREES)
                );
            } else if (slot == Slot.B) {
                return new Pose(
                        AutoConstants.BLUE_CYCLE_B_X_INCHES,
                        AutoConstants.BLUE_CYCLE_B_Y_INCHES,
                        Math.toRadians(AutoConstants.BLUE_CYCLE_B_HEADING_DEGREES)
                );
            } else if (slot == Slot.C) {
                return new Pose(
                        AutoConstants.BLUE_CYCLE_C_X_INCHES,
                        AutoConstants.BLUE_CYCLE_C_Y_INCHES,
                        Math.toRadians(AutoConstants.BLUE_CYCLE_C_HEADING_DEGREES)
                );
            } else {
                return new Pose(
                        AutoConstants.BLUE_CYCLE_D_X_INCHES,
                        AutoConstants.BLUE_CYCLE_D_Y_INCHES,
                        Math.toRadians(AutoConstants.BLUE_CYCLE_D_HEADING_DEGREES)
                );
            }
        }

        if (slot == Slot.A) {
            return new Pose(
                    AutoConstants.RED_CYCLE_A_X_INCHES,
                    AutoConstants.RED_CYCLE_A_Y_INCHES,
                    Math.toRadians(AutoConstants.RED_CYCLE_A_HEADING_DEGREES)
            );
        } else if (slot == Slot.B) {
            return new Pose(
                    AutoConstants.RED_CYCLE_B_X_INCHES,
                    AutoConstants.RED_CYCLE_B_Y_INCHES,
                    Math.toRadians(AutoConstants.RED_CYCLE_B_HEADING_DEGREES)
            );
        } else if (slot == Slot.C) {
            return new Pose(
                    AutoConstants.RED_CYCLE_C_X_INCHES,
                    AutoConstants.RED_CYCLE_C_Y_INCHES,
                    Math.toRadians(AutoConstants.RED_CYCLE_C_HEADING_DEGREES)
            );
        } else {
            return new Pose(
                    AutoConstants.RED_CYCLE_D_X_INCHES,
                    AutoConstants.RED_CYCLE_D_Y_INCHES,
                    Math.toRadians(AutoConstants.RED_CYCLE_D_HEADING_DEGREES)
            );
        }
    }

    private static PathChain buildSingleSegmentPath(Follower follower, Pose from, Pose to) {
        com.pedropathing.paths.PathBuilder builder = follower.pathBuilder();
        builder.addPath(new BezierLine(from, to))
                .setLinearHeadingInterpolation(from.getHeading(), to.getHeading());
        return builder.build();
    }

    private static void stopFollowerPathIfPossible(Follower follower) {
        try {
            follower.getClass().getMethod("breakFollowing").invoke(follower);
        } catch (Exception ignored) {
        }
    }

    private static double wrapRadians(double angle) {
        while (angle > Math.PI) {
            angle -= 2.0 * Math.PI;
        }
        while (angle <= -Math.PI) {
            angle += 2.0 * Math.PI;
        }
        return angle;
    }
}
