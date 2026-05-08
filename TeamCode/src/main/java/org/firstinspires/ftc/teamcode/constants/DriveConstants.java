package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;

@Configurable
public class DriveConstants {
    public static double PINPOINT_OFFSET_X_MM = -84.0;
    public static double PINPOINT_OFFSET_Y_MM = -168.0;

    public static GoBildaPinpointDriver.GoBildaOdometryPods PINPOINT_RESOLUTION =
            GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
    public static GoBildaPinpointDriver.EncoderDirection STRAFE_DIRECTION =
            GoBildaPinpointDriver.EncoderDirection.REVERSED;
    public static GoBildaPinpointDriver.EncoderDirection FORWARD_DIRECTION =
            GoBildaPinpointDriver.EncoderDirection.FORWARD;

    public static DcMotor.Direction LEFT_DIRECTION = DcMotor.Direction.FORWARD;
    public static DcMotor.Direction RIGHT_DIRECTION = DcMotor.Direction.REVERSE;

    public static DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;

    public static double DRIVE_INPUT_DEADBAND = 0.03;
    public static double TURN_INPUT_DEADBAND = 0.03;

    private DriveConstants() {
    }
}
