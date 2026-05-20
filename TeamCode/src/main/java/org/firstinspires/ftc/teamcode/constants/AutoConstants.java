package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class AutoConstants {
    public static double CYCLE_FEED_LEAD_SECONDS = 0.025;
    public static double CYCLE_POSITION_TOLERANCE_INCHES = 1.25;
    public static double CYCLE_HEADING_TOLERANCE_DEGREES = 4.0;
    public static double CYCLE_INTAKE_POWER = 1.0;

    // All cycle points are relative to the reset origin (0,0,0) set with start.
    public static double BLUE_CYCLE_A_X_INCHES = 61.75;
    public static double BLUE_CYCLE_A_Y_INCHES = -7.75;
    public static double BLUE_CYCLE_A_HEADING_DEGREES = -149.78;

    public static double BLUE_CYCLE_B_X_INCHES = 63.5;
    public static double BLUE_CYCLE_B_Y_INCHES = -63.64;
    public static double BLUE_CYCLE_B_HEADING_DEGREES = -135.29;

    public static double RED_CYCLE_A_X_INCHES = 59.03;
    public static double RED_CYCLE_A_Y_INCHES = 8.03;
    public static double RED_CYCLE_A_HEADING_DEGREES = -28.04;

    public static double RED_CYCLE_B_X_INCHES = 64.27;
    public static double RED_CYCLE_B_Y_INCHES = 65.87;
    public static double RED_CYCLE_B_HEADING_DEGREES = -46.10;

    private AutoConstants() {
    }
}
