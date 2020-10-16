package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

open class AimBotHardware {

    //    these are the drive motors
    var lFDrive: DcMotor? = null
    var rFDrive: DcMotor? = null
    var lBDrive: DcMotor? = null
    var rBDrive: DcMotor? = null

    var hwMap: HardwareMap? = null

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        lFDrive = hwMap?.get("lFDrive") as DcMotor
        rFDrive = hwMap?.get("rFDrive") as DcMotor
        lBDrive = hwMap?.get("lBDrive") as DcMotor
        rBDrive = hwMap?.get("rBDrive") as DcMotor

        lFDrive?.direction = DcMotorSimple.Direction.FORWARD
        rFDrive?.direction = DcMotorSimple.Direction.REVERSE
        lBDrive?.direction = DcMotorSimple.Direction.REVERSE
        rBDrive?.direction = DcMotorSimple.Direction.FORWARD

        lFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}