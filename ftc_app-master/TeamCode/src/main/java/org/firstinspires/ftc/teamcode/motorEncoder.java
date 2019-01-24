package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


import com.qualcomm.robotcore.hardware.HardwareMap;



@Autonomous(name = "encoderTest")

public class motorEncoder extends LinearOpMode {
    private    HardwareMap hwMap = null;
    private   DcMotor testMotor = null;

    @Override
    public void runOpMode() {
        waitForStart();
        motorEncoder motor = new motorEncoder();
        motor.init(hardwareMap);
        if(opModeIsActive()) {
            telemetry.addData("Position", testMotor.getController());
            testMotor.setPower(.2);
            testMotor.setTargetPosition(30);
        }

    }

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        testMotor = hwMap.get(DcMotor.class, "testMotor");
        testMotor.setPower(0);
        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
