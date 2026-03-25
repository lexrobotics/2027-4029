package org.firstinspires.ftc.teamcode.Bot2.OpModes.Tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Bot2.Sensors.SensorSwitch;

@TeleOp
public class ULTIMATESensorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        SensorSwitch sensor = new SensorSwitch("sensor", false);
        waitForStart();

        while(opModeIsActive()) {
            telemetry.addLine(sensor.toString());
            telemetry.update();
        }

    }
}
