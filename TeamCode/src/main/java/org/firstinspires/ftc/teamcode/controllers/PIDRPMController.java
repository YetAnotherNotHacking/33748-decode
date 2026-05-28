package org.firstinspires.ftc.teamcode.controllers;

public class PIDRPMController {
    private final double tpr;
    private final double kp;
    private final double ki;
    private final double kd;
    private final double kf;

    private double integral = 0;
    private double lastError = 0;
    private double lastTime = 0;
    private int lastPos = 0;
    private boolean init = false;

    public PIDRPMController(double tpr, double kp, double ki, double kd, double kf) {
        this.tpr = tpr;
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kf = kf;
    }

    public double update(int currentPos, double targetRPM, double time) {
        if (!init) {
            lastPos = currentPos;
            lastTime = time;
            init = true;
            return 0;
        }

        double dt = time - lastTime;
        int dp = currentPos - lastPos;

        lastPos = currentPos;
        lastTime = time;

        if (dt <= 0) return 0;

        double currentTicksPerSec = dp / dt;
        double targetTicksPerSec = (targetRPM * tpr) / 60.0;

        double error = targetTicksPerSec - currentTicksPerSec;
        integral += error * dt;
        double derivative = (error - lastError) / dt;
        lastError = error;

        double out = kp * error + ki * integral + kd * derivative + kf * targetTicksPerSec;
        out /= 32767.0; // Scale to match standard internal PIDF scaling

        if (out > 1) out = 1;
        if (out < -1) out = -1;
        return out;
    }
}
