package org.firstinspires.ftc.teamcode.Bot2.OpModes.TeleOp;

import static org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer.INTAKE1;
import static org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer.INTAKE2;
import static org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer.INTAKE3;
import static org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer.OUTTAKE1;
import static org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer.OUTTAKE2;
import static org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer.OUTTAKE3;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot2.Bot;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mGate;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mIntake;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mOuttake;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mPusher;
import org.firstinspires.ftc.teamcode.Bot2.Setup;

@TeleOp
public class BaseTeleop extends LinearOpMode{

    private Bot bot;
    private Setup setup;
    private ElapsedTime timer;

    private double gamepadX;
    private double gamepadY;
    private double y;
    private double x;
    private double angle;
    private double botAngle = 0;
    private double translateMag;
    private double spin;

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor rightFront;

    private IMU imu;

    //private double velocity_shift = 0.0;


    private boolean prevDriver2X;
    private boolean prevDriver2B;

    private Integer index = 0;

    private double[] states = { INTAKE3, OUTTAKE2, INTAKE1, OUTTAKE3, INTAKE2, OUTTAKE1 };

    // Velocity testing
    private double outtake_speed_change = 0;
    private boolean prevDriver2O1;
    private boolean prevDriver2O2;


    @Override
    public void runOpMode() throws InterruptedException {

        setup = new Setup(hardwareMap, telemetry, true, this, Setup.OpModeType.AUTO, Setup.Team.Q1);
        bot = new Bot(Setup.mechStates, Setup.sensorStates);

        timer = new ElapsedTime();

        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);

        // Used or old?
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        bot.init();

        bot.intake.intakeMode(); // Makes it run w/o encoder

        waitForStart();

        if (isStopRequested()) return;

        bot.transfer.setTarget(states[index]);

        while (opModeIsActive()) {

            driver1();
            driver2();
            bot.update();
            telemetry.update();
        }

    }



    private void driver1() {

        gamepadX = Math.abs(gamepad1.left_stick_x)>0.04 ? gamepad1.left_stick_x : 0;
        gamepadY = Math.abs(gamepad1.left_stick_y)>0.04 ? -gamepad1.left_stick_y : 0;
        spin = Math.abs(gamepad1.right_stick_x) > 0.04 ? gamepad1.right_stick_x : 0;//
        translateMag = Math.sqrt(gamepadX*gamepadX + gamepadY*gamepadY);
        angle = Math.atan2(gamepadY, gamepadX);
        botAngle += angle;

        x = Math.cos(angle) * translateMag;
        y = Math.sin(angle) * translateMag;

        if (gamepad1.left_bumper) {  // Slowmode
            x *= 0.25;
            y *= 0.25;
            spin *= -0.25; // Don't know why the spin reversed for some reason, had to set it to - to fix, I think issue is in SampleMecanumDrive
        } else {  // ADJUST THESE VALUES AS NEEDED
            x *= 1;
            y *= 1;
            spin *= -0.7;
        }

        bot.drivetrain.setTeleOpTargets(x, y, spin);

    }

    private void driver2() {
        // This is for velocity testing
        if (gamepad2.dpad_right && !prevDriver2O1){
            outtake_speed_change += 50;
            prevDriver2O1 = true;
        } else if (!gamepad2.dpad_right && prevDriver2O1) {
            prevDriver2O1 = false;
        }

        if (gamepad2.dpad_left && !prevDriver2O2){
            outtake_speed_change -= 50;
            prevDriver2O2 = true;
        } else if (!gamepad2.dpad_left && prevDriver2O2) {
            prevDriver2O2 = false;
        }
        telemetry.addData("outtake_speed_change", outtake_speed_change);

        bot.transfer.getCurrentPosition();

        if (gamepad2.x && !prevDriver2X) {
            // intake
            if(index %2 == 1){index = 0;} else {
                index = (index + 2) % states.length;
                bot.transfer.setTarget(states[index]);
            }
            prevDriver2X = true;
        } else if (!gamepad2.x && prevDriver2X) { 
            prevDriver2X = false;
        }


        if (gamepad2.b && !prevDriver2B) {
            // outtake
            if(index%2 == 0){index = 1;} else {
                index = (index + 2) % states.length;
                bot.transfer.setTarget(states[index]);
            }
            prevDriver2B = true;
        } else if (!gamepad2.b && prevDriver2B) {
            prevDriver2B = false;
        }


        if (gamepad2.right_bumper) {
            //outtake
            bot.outtakeLeft.setVelocity(-(mOuttake.SLOW + outtake_speed_change));
            bot.outtakeRight.setVelocity((mOuttake.SLOW + outtake_speed_change));
        } else if (gamepad2.right_trigger > 0.5) {
            bot.outtakeLeft.setVelocity(-(mOuttake.FAST + outtake_speed_change));
            bot.outtakeRight.setVelocity((mOuttake.FAST + outtake_speed_change));
        } else {
            bot.outtakeLeft.setVelocity(mOuttake.REST);
            bot.outtakeRight.setVelocity(mOuttake.REST);
        }

        if (gamepad2.left_bumper){ // && (bot.intake.getCurrentPosition() == INTAKE2 || bot.intake.getCurrentPosition() == INTAKE3 || bot.intake.getCurrentPosition() == INTAKE1)) {
            bot.intake.setVelocity(mIntake.SLOW);
        } else if (gamepad2.left_trigger > 0.5){ // && (bot.intake.getCurrentPosition() == INTAKE2 || bot.intake.getCurrentPosition() == INTAKE3 || bot.intake.getCurrentPosition() == INTAKE1)) {
            bot.intake.setVelocity(mIntake.FAST);
        } else if (gamepad2.dpad_up) {
            bot.intake.setVelocity(mIntake.EJECT);
        } else {
            bot.intake.setVelocity(mIntake.REST);
        }

        if (gamepad2.y) {
            bot.gate.setTarget(mGate.OPEN);
            bot.pusher.setTarget(mPusher.PUSH);
        } else {
            bot.gate.setTarget(mGate.REST);
            bot.pusher.setTarget(mPusher.REST);
        }



        telemetry.addLine("Transfer Current Position: " + bot.transfer.getCurrentPosition());
        telemetry.addLine("OuttakeLeft Current Velocity: "+ bot.outtakeLeft.getVelocity());
        telemetry.addLine("OuttakeRight Current Velocity: "+ bot.outtakeRight.getVelocity());
    }
}
