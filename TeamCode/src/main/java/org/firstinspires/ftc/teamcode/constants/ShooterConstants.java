package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class ShooterConstants {
    public static double TICKS_PER_REV = 28.0;

    public static double PID_P = 120.0;
    public static double PID_I = 0.006;
    public static double PID_D = 0.0;
    public static double PID_F = 11.0;

    public static double DEFAULT_TARGET_RPM = 4400.0;
    public static double MAX_TARGET_RPM = 6000.0;

    public static double FEEDER_MANUAL_POWER = 0.90;
    public static double FEEDER_INDEX_POWER = 0.45;
    public static double FEEDER_ALPHA_STOP_THRESHOLD = 2000.0;

    public static double SHOOTER_TRIGGER_DEADBAND = 0.02;

    private ShooterConstants() {
    }
}
