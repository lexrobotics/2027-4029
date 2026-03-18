package org.firstinspires.ftc.teamcode.Bot2.Drivetrain;

// THIS WAS EDITED FROM THE WORK OF PEOPLE FROM PAST YEARS TO WORK WITH THE NEW VERSION OF ROADRUNNER
// IT IS NOT THE MOST EFFICENT OR FULLY UNDERSTOOD BY ME - SEE NOTES
// + I'M PRETTY SURE THE PAST TEAM MEMBERS ADDED A BUNCH OF STUFF TO THE OTHER VERSION ROADRUNNER THAT WASN'T MADE BY THE ROADRUNNER PEOPLE THEMSELFS

import static org.firstinspires.ftc.teamcode.Bot2.Setup.telemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Bot2.Setup;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Drivetrain {
    // Related to isBusy() which I don't know how it fully works
    // Also, I don't know if TRAJECTORY_RR is used at all
    public enum DriveState{
        TRAJECTORY_RR,
        POWER,
    }
    protected DriveState state = DriveState.TRAJECTORY_RR; // TODO CHANGE TO TRAJECTORY

    protected MecanumDrive drive;

    public Pose2d currentPos;
    public double[] teleOpTargets;

    public Drivetrain(){
        teleOpTargets = new double[3];
    }

    public void init(Pose2d startPose) {
        currentPos = startPose;
        drive = new MecanumDrive(Setup.hardwareMap, currentPos);
        //drive.setPoseEstimate(startPose);

    }

    // drive (a MecanumDrive, part of roadrunner is used for both POWER and TRAJECTORY_RR)
    // but for POWER, the "setMotorPowers" was missing from the new version of RoadRunner and might have actually been an original addition from past team members,
    // but I (re)added it to MecanumDrive anyways so it worked
    public void update() {
        currentPos = drive.localizer.getPose();
        if (state == DriveState.TRAJECTORY_RR) {
            drive.updatePoseEstimate();
        } else if (state == DriveState.POWER) { // spin may be (+) instead of (-)
            double x = teleOpTargets[0];
            double y = teleOpTargets[1];
            double spin = teleOpTargets[2];
            drive.setMotorPowers(
                    Range.clip(y + x, -1, 1) + spin,
                    Range.clip(y - x, -1, 1) + spin,
                    Range.clip(y + x, -1, 1) - spin,
                    Range.clip(y - x, -1, 1) - spin);
        }
    }

    public void telemetry(){
        telemetry.addData("Drivetrain currentPos", currentPos);
        telemetry.addData("Drivetrain state", state);
    }

    public void setTeleOpTargets(double x, double y, double theta){
        state = DriveState.POWER;
        this.teleOpTargets = new double[]{x, y, theta};
    }

    // This is needed for how Bot.java is structured, but there is no function isBusy in this version of roadrunner,
    // and in the previous version it is related to something called "trajectorySequenceRunner" - I don't know if there is an analogy to that here
    public boolean isBusy() {
        if (state == DriveState.TRAJECTORY_RR) {
            //return drive.isBusy();
            return true;
        } else {
            return false;
        }
    }
}
