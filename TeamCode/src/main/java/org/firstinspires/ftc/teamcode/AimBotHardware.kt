package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

open class AimBotHardware {

    //    these are the drive motors
    var rFDrive: DcMotor? = null
    var lFDrive: DcMotor? = null
    var lBDrive: DcMotor? = null
    var rBDrive: DcMotor? = null

    var hwMap: HardwareMap? = null

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        rFDrive = hwMap?.get("rFDrive") as DcMotor
        lFDrive = hwMap?.get("lFDrive") as DcMotor
        lBDrive = hwMap?.get("lBDrive") as DcMotor
        rBDrive = hwMap?.get("rBDrive") as DcMotor

        rFDrive?.direction = DcMotorSimple.Direction.FORWARD
        lFDrive?.direction = DcMotorSimple.Direction.REVERSE
        lBDrive?.direction = DcMotorSimple.Direction.FORWARD
        rBDrive?.direction = DcMotorSimple.Direction.REVERSE

        rFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}