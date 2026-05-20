package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;

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

        // Start the intake forward immediately at autonomous start
        robot.intake.runFromTrigger(AutonomousConstants.INTAKE_FORWARD_SPEED);

        // Wait 800 ms to let the Pinpoint calibrate on reset
        sleep(1200);

        runAutonomous();
        robot.stopAll();
    }

    protected abstract void runAutonomous() throws InterruptedException;
}
