package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class AutoAimConstants {
    public static double BLUE_GOAL_X_INCHES = 144.0;
    public static double BLUE_GOAL_Y_INCHES = 0.0;

    public static double RED_GOAL_X_INCHES = 144.0;
    public static double RED_GOAL_Y_INCHES = 144.0;

    public static double RESET_X_INCHES = 8.0;
    public static double BLUE_RESET_Y_INCHES = 136.0;
    public static double RED_RESET_Y_INCHES = 8.0;
    public static double RESET_HEADING_DEGREES = 0.0;

    public static double SHOOTER_LEFT_OFFSET_DEGREES = -90.0;

    public static double TURN_KP = -1.3;
    public static double TURN_KI = 0.0;
    public static double TURN_KD = -0.1;
    public static double TURN_KF = 0.024;
    public static double MAX_TURN = 0.65;

    public static double HEADING_TOLERANCE_DEG = 2.5;

    private AutoAimConstants() {
    }
}
