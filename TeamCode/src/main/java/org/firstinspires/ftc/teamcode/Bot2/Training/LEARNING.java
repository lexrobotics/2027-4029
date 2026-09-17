package org.firstinspires.ftc.teamcode.Bot2.Training;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Bot2.Bot;
import org.firstinspires.ftc.teamcode.Bot2.Setup;

public class LEARNING extends LinearOpMode{
    private Bot bot;
    private Setup setup;
    private Servo servoTest;

    @Override
    public void runOpMode() throws InterruptedException {
        setup = new Setup(hardwareMap, telemetry, true, this, Setup.OpModeType.AUTO, Setup.Team.Q1);
        bot = new Bot(Setup.mechStates, Setup.sensorStates);

        servoTest = hardwareMap.get(Servo.class, "servoTest");

        servoTest.setPosition(0.5);

        waitForStart();

        servoTest.setPosition(1.0);
    }
}
