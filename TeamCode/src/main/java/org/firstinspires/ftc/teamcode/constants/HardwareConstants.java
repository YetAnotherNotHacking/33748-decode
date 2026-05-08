package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class HardwareConstants {
    public static String FRONT_LEFT_MOTOR = "motor3";
    public static String FRONT_RIGHT_MOTOR = "motor2";
    public static String BACK_LEFT_MOTOR = "motor4";
    public static String BACK_RIGHT_MOTOR = "motor1";

    public static String INTAKE_SERVO = "intakeservo";
    public static String INTAKE_MOTOR = "intake";

    public static String SHOOTER_MOTOR = "launcher";
    public static String FEEDER_MOTOR = "feeder";

    public static String TAG_CAMERA = "tagseeker";
    public static String PINPOINT = "pinpoint";
    public static String COLOR_SHOOT_SENSOR = "colorshoot";

    private HardwareConstants() {
    }
}
