// This class belongs to the team's Bot2 package. That's where it lives and continues to survive...
package org.firstinspires.ftc.teamcode.Bot2;

import android.util.Log;

import com.acmerobotics.roadrunner.Pose2d; //Pose2d is from Road Runner and defines the bot's (x,y,theta).
import com.qualcomm.robotcore.hardware.DcMotor;// DcMotor is from FTC SDK, used for, obviously, motor.
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot2.Drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.Bot2.InitStates.HardwareStates;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.AbstractMechanisms.MotorMechanism;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.AbstractMechanisms.ServoMechanism;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mGate;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mIntake;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mOuttake;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mPusher;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer;
import org.firstinspires.ftc.teamcode.Bot2.Sensors.SensorColorDistance;
import org.firstinspires.ftc.teamcode.Bot2.Sensors.SensorSwitch;
import org.firstinspires.ftc.teamcode.Bot2.Sensors.Vision.Camera;

import java.util.HashMap;

public class Bot implements Robot {  
    // declares that Bot follows the requirements specified by the Robot interface. 
    // major susbsystems and robot mechanisms managed by this bot.
    public Drivetrain drivetrain; //declare the variable
    public ServoMechanism transfer, pusher, gate;
    public MotorMechanism intake, outtakeLeft, outtakeRight;
    public SensorColorDistance CDSensor;
    public Camera webcam;
    public SensorSwitch slidesSwitch, intakeSlidesSwitch;

    public Bot(HashMap<String, HardwareStates> hardwareStates, HashMap<String, HardwareStates> sensorStates){
        // HashMap is key-value pair
        // Initiallizes the robot's mechanisms and sensors based on which components and enabled and in HardStates sensorStates.
        //sensors = new Sensors1(3,3,0,0,true);
        //telemetry.addLine("robot");

        if(hardwareStates.get("drivetrain").isEnabled){
            // asking for the value of the key "draintrain".
            drivetrain = new Drivetrain(); // creating the actual object
        } else {
            Log.d("HAI", "DRIVETRAIN NULL");// debugging level msg to Android's log. "HAI" as tag and "DRIVETRAIN NULL" as message. 
            drivetrain = null; // To avoid errors, drivetrain will be set to point to NO object. 
            
        /* potentially, drivetrain could be changed in order to maintain consistence. */
        }

        if(hardwareStates.get("Transfer").isEnabled){
            transfer = new mTransfer();
        } else {
            transfer = new ServoMechanism("Transfer") {
            };
        }

        if(hardwareStates.get("Pusher").isEnabled){
            pusher = new mPusher();
        } else {
            pusher = new ServoMechanism("Pusher") {
            };
        }

        if(hardwareStates.get("Gate").isEnabled){
            gate = new mGate();
        } else {
            gate = new ServoMechanism("Gate") {
            };
        }

        if(hardwareStates.get("Intake").isEnabled){
            intake = new mIntake();
        } else {
            intake = new MotorMechanism("Intake") {
            };
        }

        if(hardwareStates.get("OuttakeRight").isEnabled){
            outtakeRight = new mOuttake("OuttakeRight");
        } else {
            outtakeRight = new MotorMechanism("OuttakeRight") {
            };
        }

        if(hardwareStates.get("OuttakeLeft").isEnabled){
            outtakeLeft = new mOuttake("OuttakeLeft");
        } else {
            outtakeLeft = new MotorMechanism("OuttakeLeft") {
            };
        }

        init();
    }

    public void initDrivetrain(Pose2d pose){
        if(drivetrain != null) {
            drivetrain.init(pose);
        }
    }

    @Override
    public void init(){
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        transfer.init(mTransfer.INIT);
        pusher.init(mPusher.INIT);
        gate.init(mGate.INIT);
        intake.init(mIntake.INIT);
        outtakeLeft.init(mOuttake.INIT);
        outtakeRight.init(mOuttake.INIT);
        drivetrain.init(new Pose2d(0, 0, 0));
    }

    public void init(Pose2d startPos){
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        transfer.init(mTransfer.INIT);
        pusher.init(mPusher.INIT);
        gate.init(mGate.INIT);
        intake.init(mIntake.INIT, DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeLeft.init(mOuttake.INIT, DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeRight.init(mOuttake.INIT, DcMotor.ZeroPowerBehavior.BRAKE);
        drivetrain.init(startPos);
    }

    @Override
    public void update(){
        transfer.update();
        pusher.update();
        gate.update();
        intake.update1();
        outtakeLeft.update();
        outtakeRight.update();
        drivetrain.update();
    }

    @Override
    public void telemetry(){
        transfer.telemetry();
        pusher.telemetry();
        gate.telemetry();
        intake.telemetry();
        outtakeLeft.telemetry();
        outtakeRight.telemetry();
        drivetrain.telemetry();

    }

    @Override
    public boolean isBusy(){
        return transfer.isBusy() || pusher.isBusy() || gate.isBusy() || intake.isBusy() || outtakeLeft.isBusy() || outtakeRight.isBusy() || drivetrain.isBusy();
    }

    public void setRest(){
        transfer.setTarget(mTransfer.REST);
        pusher.setTarget(mPusher.REST);
        gate.setTarget(mGate.REST);
        intake.setTarget(mIntake.REST);
        outtakeLeft.setTarget(mOuttake.REST);
        outtakeRight.setTarget(mOuttake.REST);

    }
}
