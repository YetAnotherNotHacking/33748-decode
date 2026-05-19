package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.BaseAutoOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Controller;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.constants.AutonomousPoints;

@Autonomous(name = "Blue Front 6 Ball", preselectTeleOp = "Blue Goal TeleOp")
public class BlueFront6 extends BaseAutoOpMode {
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
        
        // Engage intake (for rest of auton)
        controller.runIntakeForward();
        
        // 3. move to intake position 1 for far row intake
        controller.pathTo(AutonomousPoints.Blue.Front.INTAKE_FAR_1);
        
        // 4. move to intake position 2 for far row intake
        controller.pathTo(AutonomousPoints.Blue.Front.INTAKE_FAR_2);
        
        // 5. move to shoot position
        controller.pathTo(AutonomousPoints.Blue.Front.SHOOT);
        
        // 6. shoot balls
        controller.shoot(AutonomousConstants.CYCLE_SHOOT_TIME);
        
        // 7. park far
        controller.pathTo(AutonomousPoints.Blue.Front.PARK_FAR);
    }
}
