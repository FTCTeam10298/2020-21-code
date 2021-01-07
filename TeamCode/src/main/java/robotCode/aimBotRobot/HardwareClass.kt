package robotCode.aimBotRobot

import com.qualcomm.robotcore.hardware.*

interface HardwareClass {

    var hwMap: HardwareMap

    fun init(ahwMap: HardwareMap)
}

interface TankHardware: HardwareClass {
    val lDrive: DcMotor
    val rDrive: DcMotor
}

interface MecanumHardware: HardwareClass {
    val lFDrive: DcMotor
    val rFDrive: DcMotor
    val lBDrive: DcMotor
    val rBDrive: DcMotor
}

interface TankOdometryHardware: TankHardware {
    val lOdom: DcMotor
    val rOdom: DcMotor
    val cOdom: DcMotor
}

interface MecOdometryHardware: MecanumHardware {
    val lOdom: DcMotor
    val rOdom: DcMotor
    val cOdom: DcMotor
}