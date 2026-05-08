package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Red Goal TeleOp", group = "newcode")
public class RedGoalTeleOp extends BaseGoalTeleOp {
    @Override
    protected String getDriveTeamName() {
        return "RED";
    }

    @Override
    protected boolean isBlueAlliance() {
        return false;
    }
}
