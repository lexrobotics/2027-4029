package org.firstinspires.ftc.teamcode.Bot2.Sensors;

import com.qualcomm.hardware.limelightvision.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Bot2.Bot;
import org.firstinspires.ftc.teamcode.Bot2.Setup;

public class Limelight extends LinearOpMode {
    private Setup setup;
    private Bot bot;
    private Limelight3A lime;
    //pipe 0 = blue
    //pipe 1 = yellow
    //pipe 2 = red
    //pipe 3 = aprilTags

    @Override
    public void runOpMode() {

        setup = new Setup(hardwareMap, telemetry, true, this, Setup.OpModeType.AUTO, Setup.Team.Q1);
        bot = new Bot(Setup.mechStates, Setup.sensorStates);

        lime = hardwareMap.get(Limelight3A.class, "lime");
        lime.setPollRateHz(30);
        lime.start();

        lime.pipelineSwitch(0); // blue pipeline

        waitForStart();

        while (opModeIsActive()) {

        LLResult test = lime.getLatestResult();
        if (test != null && test.isValid()) {

            double tx = test.getTx();
            double ty = test.getTy();

            telemetry.addData("Tx", tx);
            telemetry.addData("ty", ty);
        } else {
            telemetry.addData("limelight", "no target");
        }
        telemetry.update();
        }
    }

}
