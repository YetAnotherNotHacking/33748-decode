package org.firstinspires.ftc.teamcode.constants;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;


@Config
@Configurable
public final class AutonomousConstants {

    // Shooter RPM settings
    public static double FAR_SHOOT_RPM = 4490.0;
    public static double CLOSE_SHOOT_RPM = 4190.0;
    
    // Intake timings/speeds
    public static long SHOOT_FORWARD_CYCLE_MS = 1000;
    public static long SHOOT_REVERSE_CYCLE_MS = 200;
    
    public static double INTAKE_FORWARD_SPEED = 0.9;
    public static double INTAKE_REVERSE_SPEED = 0.5;
    
    // Pathing speed scaling for close/back-row intake moves
    public static double INTAKE_PATH_SPEED_SCALING = 0.21;
    
    // Feeder timings/speeds during autonomous
    public static double FEEDER_SPEED = 0.7;

    // Time to spend shooting all preloads
    public static long PRELOAD_SHOOT_TIME = 2200;
    
    // Time to spend shooting intaked balls
    public static long CYCLE_SHOOT_TIME = 2900;
    
    // Configurable delay between executed commands in milliseconds
    public static long COMMAND_DELAY_MS = 160;
    
    // Configurable delay before the first shot to allow the flywheel to spin up in milliseconds
    public static long SPIN_UP_DELAY_MS = 1900;
    
    // Constant to skip human player intake for back autos
    public static boolean SKIP_HUMAN_INTAKE = true;
    
    private AutonomousConstants() {}
}
