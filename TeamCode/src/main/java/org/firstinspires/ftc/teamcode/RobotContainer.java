package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.OperatorConstants;
import org.firstinspires.ftc.teamcode.subsystems.AutoCycleSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.OdometryAutoAimSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class RobotContainer {
    public final DriveSubsystem drive = new DriveSubsystem();
    public final IntakeSubsystem intake = new IntakeSubsystem();
    public final ShooterSubsystem shooter = new ShooterSubsystem();
    public final VisionSubsystem vision = new VisionSubsystem();
    public final OdometryAutoAimSubsystem autoAim = new OdometryAutoAimSubsystem();
    public final AutoCycleSubsystem autoCycle = new AutoCycleSubsystem();

    private Follower follower;

    public void init(HardwareMap hardwareMap, boolean initVision) {
        drive.init(hardwareMap);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);

        if (initVision) {
            vision.init(hardwareMap);
        }

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        follower.update();

        autoAim.setEnabled(OperatorConstants.ENABLE_AUTOAIM_BY_DEFAULT);
    }

    public Follower getFollower() {
        return follower;
    }

    public void resetRobotState() {
        drive.resetHeading();
        autoAim.resetPid();
        autoCycle.cancel(follower);
    }

    public void setFieldPose(double xInches, double yInches, double headingDegrees) {
        drive.setFieldPose(xInches, yInches, headingDegrees);
        if (follower != null) {
            follower.setStartingPose(new Pose(xInches, yInches, Math.toRadians(headingDegrees)));
            follower.update();
        }
    }

    public void stopAll() {
        drive.stop();
        intake.stop();
        shooter.stop();
        vision.stop();
    }
}
