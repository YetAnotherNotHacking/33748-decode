package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotContainer;

public abstract class BaseAutoOpMode extends LinearOpMode {
    protected final RobotContainer robot = new RobotContainer();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, true);
        robot.resetRobotState();

        waitForStart();
        if (isStopRequested()) {
            robot.stopAll();
            return;
        }

        runAutonomous();
        robot.stopAll();
    }

    protected abstract void runAutonomous() throws InterruptedException;
}
