package org.firstinspires.ftc.teamcode.Bot2.Training;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot2.Bot;
import org.firstinspires.ftc.teamcode.Bot2.Setup;

@TeleOp
public class Training1 extends LinearOpMode {

    private Setup setup;
    private Bot bot;

    @Override
    public void runOpMode(){
        setup = new Setup(hardwareMap, telemetry, true, this, Setup.OpModeType.AUTO, Setup.Team.Q1);
        bot = new Bot(Setup.mechStates, Setup.sensorStates);


        waitForStart();
        telemetry.addData("Hello, Team!", "YES");

    }

}
