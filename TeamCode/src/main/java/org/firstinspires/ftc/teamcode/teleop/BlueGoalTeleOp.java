package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Blue Goal TeleOp", group = "newcode")
public class BlueGoalTeleOp extends BaseGoalTeleOp {
    @Override
    protected String getDriveTeamName() {
        return "BLUE";
    }

    @Override
    protected boolean isBlueAlliance() {
        return true;
    }
}
