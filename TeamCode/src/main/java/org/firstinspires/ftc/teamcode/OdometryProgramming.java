package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "OdometryProgramming", group = "Programming")
public class OdometryProgramming extends OpMode {
    private Follower follower;

    private boolean lastX = false;
    private double zeroX = 0.0;
    private double zeroY = 0.0;
    private double zeroHeading = 0.0;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));

        // Start with current pose as zero-reference.
        follower.update();
        Pose start = follower.getPose();
        zeroX = start.getX();
        zeroY = start.getY();
        zeroHeading = start.getHeading();
    }

    @Override
    public void loop() {
        follower.update();
        Pose pose = follower.getPose();

        boolean xPressed = gamepad1.x;
        if (xPressed && !lastX) {
            // Re-zero all displayed readings to the current pose.
            zeroX = pose.getX();
            zeroY = pose.getY();
            zeroHeading = pose.getHeading();
        }
        lastX = xPressed;

        double xZeroed = pose.getX() - zeroX;
        double yZeroed = pose.getY() - zeroY;
        double headingZeroedDeg = Math.toDegrees(wrapRadians(pose.getHeading() - zeroHeading));

        telemetry.addLine("PedroPathing Odometry");
        telemetry.addLine("Press X to zero all readings");

        telemetry.addData("X (zeroed)", "%.2f", xZeroed);
        telemetry.addData("Y (zeroed)", "%.2f", yZeroed);
        telemetry.addData("Heading (zeroed deg)", "%.2f", headingZeroedDeg);

        telemetry.addData("X (raw)", "%.2f", pose.getX());
        telemetry.addData("Y (raw)", "%.2f", pose.getY());
        telemetry.addData("Heading (raw deg)", "%.2f", Math.toDegrees(pose.getHeading()));
        telemetry.update();
    }

    private static double wrapRadians(double angle) {
        while (angle > Math.PI) angle -= 2.0 * Math.PI;
        while (angle <= -Math.PI) angle += 2.0 * Math.PI;
        return angle;
    }
}
