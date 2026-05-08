package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.constants.IntakeConstants;

public class IntakeSubsystem {
    private CRServo intakeServo;
    private DcMotor intakeMotor;

    public void init(HardwareMap hardwareMap) {
        intakeServo = hardwareMap.get(CRServo.class, HardwareConstants.INTAKE_SERVO);
        intakeServo.setDirection(DcMotorSimple.Direction.FORWARD);

        intakeMotor = hardwareMap.get(DcMotor.class, HardwareConstants.INTAKE_MOTOR);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void runFromTrigger(double triggerValue) {
        double clampedTrigger = Range.clip(triggerValue, 0.0, 1.0);
        if (clampedTrigger < IntakeConstants.TRIGGER_DEADBAND) {
            stop();
            return;
        }

        intakeServo.setPower(clampedTrigger);
        double motorPower = IntakeConstants.MOTOR_MIN_POWER
                + (clampedTrigger * (IntakeConstants.MOTOR_MAX_POWER - IntakeConstants.MOTOR_MIN_POWER));
        intakeMotor.setPower(-motorPower);
    }

    public void reverse(double power) {
        double clampedPower = Range.clip(power, 0.0, 1.0);
        intakeServo.setPower(-clampedPower);
        double motorPower = IntakeConstants.MOTOR_MIN_POWER
                + (clampedPower * (IntakeConstants.MOTOR_MAX_POWER - IntakeConstants.MOTOR_MIN_POWER));
        intakeMotor.setPower(motorPower);
    }

    public void stop() {
        intakeServo.setPower(0.0);
        intakeMotor.setPower(0.0);
    }

    public double getServoPower() {
        return intakeServo.getPower();
    }

    public double getMotorPower() {
        return intakeMotor.getPower();
    }
}
