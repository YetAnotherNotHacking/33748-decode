package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.BaseAutoOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Controller;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.constants.AutonomousPoints;

@Autonomous(name = "Blue Front 3 Ball", preselectTeleOp = "Blue Goal TeleOp")
public class BlueFront3 extends BaseAutoOpMode {
    @Override
    protected void runAutonomous() {
        Controller controller = new Controller(this, robot);
        controller.init();
        controller.setStartingPose(AutonomousPoints.Blue.Front.SHOOT);

        controller.startShooter(AutonomousConstants.FAR_SHOOT_RPM);
        
        // 1. move to shoot position
        controller.pathTo(AutonomousPoints.Blue.Front.SHOOT);
        
        // 2. shoot balls (preloads)
        controller.shoot(AutonomousConstants.PRELOAD_SHOOT_TIME);
        
        // 3. park far
        controller.pathTo(AutonomousPoints.Blue.Front.PARK_FAR);
    }
}
