package org.firstinspires.ftc.teamcode.Bot2.OpModes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class RRTelemetry extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        OverflowEncoder par0 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "leftFront")));
        OverflowEncoder par1 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "rightBack")));
        OverflowEncoder perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "rightFront")));

        waitForStart();

        while(opModeIsActive()) {
            telemetry.addLine(String.format("par0: %s", par0.getPositionAndVelocity().position));
            telemetry.addLine(String.format("par1: %s", par1.getPositionAndVelocity().position));
            telemetry.addLine(String.format("perp: %s", perp.getPositionAndVelocity().position));
            telemetry.update();
        }

    }
}
