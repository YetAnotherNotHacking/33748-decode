package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.BaseAutoOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Controller;
import org.firstinspires.ftc.teamcode.constants.AutonomousConstants;
import org.firstinspires.ftc.teamcode.constants.AutonomousPoints;

@Autonomous(name = "Red Front Gate", preselectTeleOp = "Red Goal TeleOp")
public class RedFrontGate extends BaseAutoOpMode {
    @Override
    protected void runAutonomous() {
        Controller controller = new Controller(this, robot);
        controller.init();
        controller.setStartingPose(AutonomousPoints.Red.Front.SHOOT);

        controller.startShooter(AutonomousConstants.CLOSE_SHOOT_RPM);
        
        // 1. move to shoot position
        controller.pathTo(AutonomousPoints.Red.Front.SHOOT);
        
        // 1.5 wait for spin up delay
        controller.sleep(AutonomousConstants.SPIN_UP_DELAY_MS);


        // 2. shoot balls (preloads)
        controller.shoot(AutonomousConstants.PRELOAD_SHOOT_TIME);
        
        // Engage intake (for rest of auton)
        controller.enableIntake();
        

        // wait for hv to score
        controller.sleep(2000);


        // clear that line
        controller.pathTo(AutonomousPoints.Red.Front.GATEC1);
        controller.pathTo(AutonomousPoints.Red.Front.GATEC2);

        // empty gate
        controller.pathTo(AutonomousPoints.Red.Front.GATE);
        
        // wait for empty before leave
        controller.sleep(4200);

        // 5. park back position
        controller.pathTo(AutonomousPoints.Red.Front.PARK_FAR);
    }
}
