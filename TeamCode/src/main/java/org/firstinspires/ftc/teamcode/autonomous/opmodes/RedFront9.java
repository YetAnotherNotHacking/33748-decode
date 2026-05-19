package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.BaseAutoOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Controller;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.constants.AutonomousPoints;

@Autonomous(name = "Red Front 9 Ball", preselectTeleOp = "Red Goal TeleOp")
public class RedFront9 extends BaseAutoOpMode {
    @Override
    protected void runAutonomous() {
        Controller controller = new Controller(this, robot);
        controller.init();
        controller.setStartingPose(AutonomousPoints.Red.Front.SHOOT);

        controller.startShooter(AutonomousConstants.FAR_SHOOT_RPM);
        
        // 1. move to shoot position
        controller.pathTo(AutonomousPoints.Red.Front.SHOOT);
        
        // 2. shoot balls (preloads)
        controller.shoot(AutonomousConstants.PRELOAD_SHOOT_TIME);
        
        // Engage intake (for rest of auton)
        controller.runIntakeForward();
        
        // 3. move to intake position 1 for far row intake
        controller.pathTo(AutonomousPoints.Red.Front.INTAKE_FAR_1);
        
        // 4. move to intake position 2 for far row intake
        controller.pathTo(AutonomousPoints.Red.Front.INTAKE_FAR_2);
        
        // 5. move to shoot position
        controller.pathTo(AutonomousPoints.Red.Front.SHOOT);
        
        // 6. shoot balls
        controller.shoot(AutonomousConstants.CYCLE_SHOOT_TIME);
        
        // 7. move to intake position 1 for middle row intake
        controller.pathTo(AutonomousPoints.Red.Front.INTAKE_MID_1);
        
        // 8. move to intake position 2 for middle row intake
        controller.pathTo(AutonomousPoints.Red.Front.INTAKE_MID_2);
        
        // 9. move to shoot position
        controller.pathTo(AutonomousPoints.Red.Front.SHOOT);
        
        // 10. shoot balls
        controller.shoot(AutonomousConstants.CYCLE_SHOOT_TIME);
        
        // 11. park far
        controller.pathTo(AutonomousPoints.Red.Front.PARK_FAR);
    }
}
