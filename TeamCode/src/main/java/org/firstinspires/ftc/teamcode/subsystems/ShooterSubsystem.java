package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

public class ShooterSubsystem {
    private DcMotorEx shooterMotor;
    private DcMotor feederMotor;

    private double targetRpm;
    private double feederPower;

    public void init(HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, HardwareConstants.SHOOTER_MOTOR);
        feederMotor = hardwareMap.get(DcMotor.class, HardwareConstants.FEEDER_MOTOR);

        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        feederMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        setTargetRpm(0.0);
        setFeederPower(0.0);
        shooterMotor.setPower(0.0);
    }

    public void setTargetRpm(double rpm) {
        targetRpm = Math.max(0.0, rpm);
    }

    public double getTargetRpm() {
        return targetRpm;
    }

    public double getCurrentRpm() {
        return -((shooterMotor.getVelocity() * 60.0) / ShooterConstants.TICKS_PER_REV);
    }

    public void setFeederPower(double power) {
        feederPower = Range.clip(power, -1.0, 1.0);
    }

    public double getFeederPower() {
        return feederPower;
    }

    public void update() {
        double currentRpm = getCurrentRpm();
        double shooterPower;
        if (targetRpm <= 0.0) {
            shooterPower = 0.0;
        } else if (currentRpm < targetRpm) {
            shooterPower = 1.0;
        } else {
            shooterPower = 0.0;
        }

        shooterMotor.setPower(shooterPower);
        feederMotor.setPower(feederPower);
    }

    public void setBrakeMode(boolean shouldBrake) {
        shooterMotor.setZeroPowerBehavior(
                shouldBrake ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT
        );
    }

    public void stop() {
        targetRpm = 0.0;
        feederPower = 0.0;
        feederMotor.setPower(0.0);
        shooterMotor.setPower(0.0);
    }
}
