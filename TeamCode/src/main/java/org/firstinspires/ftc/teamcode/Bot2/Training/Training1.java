package org.firstinspires.ftc.teamcode.Bot2.Training;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class Training1 extends LinearOpMode {

    @Override
    public void runOpMode(){
        waitForStart();
        telemetry.addData("Hello, Team!");

    }

}
