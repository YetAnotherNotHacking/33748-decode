package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.BaseAutoOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Controller;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.constants.AutonomousPoints;

@Autonomous(name = "Blue Back 9 Ball", preselectTeleOp = "Blue Goal TeleOp")
public class BlueBack9 extends BaseAutoOpMode {
    @Override
    protected void runAutonomous() {
        Controller controller = new Controller(this, robot);
        controller.init();
        controller.setStartingPose(AutonomousPoints.Blue.Back.SHOOT);

        controller.startShooter(AutonomousConstants.CLOSE_SHOOT_RPM);
        
        // 1. move to shoot position
        controller.pathTo(AutonomousPoints.Blue.Back.SHOOT);
        
        // 1.5 wait for spin up delay
        controller.sleep(AutonomousConstants.SPIN_UP_DELAY_MS);


        // 2. shoot balls (preloads)
        controller.shoot(AutonomousConstants.PRELOAD_SHOOT_TIME);
        
        // Engage intake (for rest of auton)
        controller.runIntakeForward();
        
        if (!AutonomousConstants.SKIP_HUMAN_INTAKE) {
            // 3. move to human player ball intake position
            controller.pathTo(AutonomousPoints.Blue.Back.INTAKE_HUMAN);
            
            // 4. shoot balls
            controller.pathTo(AutonomousPoints.Blue.Back.SHOOT);
            controller.shoot(AutonomousConstants.CYCLE_SHOOT_TIME);
        }
        
        // 5. move to intake position 1 for back row intake
        controller.pathTo(AutonomousPoints.Blue.Back.INTAKE_CLOSE_1);
        
        // 6. move to intake position 2 for back row intake
        controller.pathTo(AutonomousPoints.Blue.Back.INTAKE_CLOSE_2, AutonomousConstants.INTAKE_PATH_SPEED_SCALING);
        
        // 7. shoot balls
        controller.pathTo(AutonomousPoints.Blue.Back.SHOOT);
        controller.shoot(AutonomousConstants.CYCLE_SHOOT_TIME);
        
        // 8. park back position
        controller.pathTo(AutonomousPoints.Blue.Back.CLOSE_PARK);
    }
}
