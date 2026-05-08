package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class IntakeConstants {
    public static double MOTOR_MIN_POWER = 0.0;
    public static double MOTOR_MAX_POWER = 0.9;

    public static double TRIGGER_DEADBAND = 0.02;
    public static double BUMPER_REVERSE_POWER = 1.0;

    private IntakeConstants() {
    }
}
