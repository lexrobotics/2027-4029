package org.firstinspires.ftc.teamcode.Bot2.OpModes.Auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot2.Bot;
import org.firstinspires.ftc.teamcode.Bot2.Mechanisms.mTransfer;
import org.firstinspires.ftc.teamcode.Bot2.Setup;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(group = "1")
public class BaseRRAuto extends LinearOpMode{
    private Bot bot;
    private ElapsedTime timer;
    private Setup setup;
    private double[] transfer_pos = {mTransfer.OUTTAKE3, mTransfer.OUTTAKE1, mTransfer.OUTTAKE2};

    @Override
    public void runOpMode() throws InterruptedException {
        // Do we need to set the zero power behavior of the motor for auto?
        timer = new ElapsedTime();
        setup = new Setup(hardwareMap, telemetry, true, this, Setup.OpModeType.AUTO, Setup.Team.Q3);

        bot = new Bot(Setup.mechStates, Setup.sensorStates);
        bot.init();

        Pose2d startPose = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        waitForStart();
        if (isStopRequested()) return;

        TrajectoryActionBuilder traj1 = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(-10, -10))
                .turn(Math.toRadians(45)); //(new Pose2d(-96, -72, Math.toRadians(0)))

        TrajectoryActionBuilder traj2 = drive.actionBuilder(drive.localizer.getPose())
                .strafeTo(new Vector2d(10, 10))
                .turn(Math.toRadians(-45)); // Needs to be 0.0 not just 0 to work I think

        Actions.runBlocking(
                traj1.build()
//                new SequentialAction(
//                        traj1.build(),
//                        traj2.build()
//                )
        );
        drive.updatePoseEstimate();

        timer.reset();
        while (opModeIsActive() && timer.milliseconds() < 5000) {
            sleep(5);
        }

        Actions.runBlocking(
               traj2.build()
        );
        drive.updatePoseEstimate();
    }
}

