package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "Odometry Auto Skeleton", group = "newcode")
public class OdometryAutoSkeleton extends BaseAutoOpMode {
    @Override
    protected void runAutonomous() throws InterruptedException {
        Follower follower = robot.getFollower();
        follower.setStartingPose(new Pose(0, 0, 0));

        while (opModeIsActive()) {
            follower.update();
            telemetry.addData("Mode", "AUTONOMOUS SKELETON");
            telemetry.update();
            idle();
        }
    }
}
