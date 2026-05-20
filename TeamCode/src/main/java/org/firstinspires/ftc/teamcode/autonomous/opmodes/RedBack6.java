package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.BaseAutoOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Controller;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.constants.AutonomousPoints;

@Autonomous(name = "Red Back 6 Ball", preselectTeleOp = "Red Goal TeleOp")
public class RedBack6 extends BaseAutoOpMode {
    @Override
    protected void runAutonomous() {
        Controller controller = new Controller(this, robot);
        controller.init();
        controller.setStartingPose(AutonomousPoints.Red.Back.SHOOT);

        controller.startShooter(AutonomousConstants.CLOSE_SHOOT_RPM);
        
        // 1. move to shoot position
        controller.pathTo(AutonomousPoints.Red.Back.SHOOT);
        
        // 1.5 wait for spin up delay
        controller.sleep(AutonomousConstants.SPIN_UP_DELAY_MS);


        // 2. shoot balls (preloads)
        controller.shoot(AutonomousConstants.PRELOAD_SHOOT_TIME);
        
        // Engage intake (for rest of auton)
        controller.runIntakeForward();
        
        if (!AutonomousConstants.SKIP_HUMAN_INTAKE) {
            // 3. move to human player ball intake position
            controller.pathTo(AutonomousPoints.Red.Back.INTAKE_HUMAN);
            
            // 4. shoot balls
            controller.pathTo(AutonomousPoints.Red.Back.SHOOT);
            controller.shoot(AutonomousConstants.CYCLE_SHOOT_TIME);
        }
        
        // 5. park back position
        controller.pathTo(AutonomousPoints.Red.Back.CLOSE_PARK);
    }
}
