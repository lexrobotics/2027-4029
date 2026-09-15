//package org.firstinspires.ftc.teamcode.Bot2.Sensors;
//
//import com.qualcomm.hardware.limelightvision.*;
//
//public class Limelight {
//    private Limelight3A lime;
//    //pipe 0 = blue
//    //pipe 1 = yellow
//    //pipe 2 = red
//    //pipe 3 = aprilTags
//
//    @Override
//    public void init(){
//        lime = hardwareMap.get(Limelight3A.class, "lime");
//        lime.setPollRateHz(30);
//        lime.start();
//
//        LLResult test = lime.getLatestResult();
//        if(tst != null && test.isValid()){
//            telemetry.addData("It worked! the cord you got was : ", test.getTx());
//        }
//        else{
//            telemetry.addData("Dam that sucks, nothing happened because there was ", "no target");
//        }
//
//
//    }
//
//
//}
