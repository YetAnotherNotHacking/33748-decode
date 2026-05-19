package org.firstinspires.ftc.teamcode.constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public final class AutonomousConstants {

    // Shooter RPM settings
    public static double FAR_SHOOT_RPM = 3000.0;
    public static double CLOSE_SHOOT_RPM = 2500.0;
    
    // Intake timings/speeds
    public static long SHOOT_FORWARD_CYCLE_MS = 900;
    public static long SHOOT_REVERSE_CYCLE_MS = 100;
    
    public static double INTAKE_FORWARD_SPEED = 1.0;
    public static double INTAKE_REVERSE_SPEED = 0.45;
    
    // Feeder timings/speeds during autonomous
    public static double FEEDER_SPEED = 1.0;

    // Time to spend shooting all preloads
    public static long PRELOAD_SHOOT_TIME = 2000;
    
    // Time to spend shooting intaked balls
    public static long CYCLE_SHOOT_TIME = 2000;
    
    private AutonomousConstants() {}
}
