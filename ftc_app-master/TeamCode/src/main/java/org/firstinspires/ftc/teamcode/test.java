package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static org.firstinspires.ftc.teamcode.robotBase.midTraverseRight;


@Autonomous(name = "TEST")
@Disabled
public class test extends LinearOpMode {
    robotBase robot = new robotBase();
    private ElapsedTime runtime = new ElapsedTime();
    RevBlinkinLedDriver.BlinkinPattern pattern;

    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        robot.init(hardwareMap);
        robot.inVertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.inVertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.inVertical.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();
/*
        robot.inVertical.setTargetPosition(6150);
        while(robot.inVertical.getCurrentPosition() < robot.inVertical.getTargetPosition()){
            robot.inVertical.setPower(.05);
        }
        robot.inVertical.setPower(0.0);

        sleep(10000);

        robot.inVertical.setTargetPosition(0);
        while(robot.inVertical.getCurrentPosition() > robot.inVertical.getTargetPosition()){
            robot.inVertical.setPower(.05);
        }
        robot.inVertical.setPower(0.0);
        */
        pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        robot.blinkinLedDriver.setPattern(pattern);
    }

}