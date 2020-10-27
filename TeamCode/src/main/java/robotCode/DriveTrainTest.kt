package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

open class DTTHardware {

    //    these are the drive motors
    var rFDrive: DcMotor? = null
    var lFDrive: DcMotor? = null
    var rBDrive: DcMotor? = null
    var lBDrive: DcMotor? = null

    var hwMap: HardwareMap? = null

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        rFDrive = hwMap?.get("rFDrive") as DcMotor
        lFDrive = hwMap?.get("lFDrive") as DcMotor
        rBDrive = hwMap?.get("rBDrive") as DcMotor
        lBDrive = hwMap?.get("lBDrive") as DcMotor

        rFDrive?.direction = DcMotorSimple.Direction.FORWARD
        lFDrive?.direction = DcMotorSimple.Direction.FORWARD
        rBDrive?.direction = DcMotorSimple.Direction.FORWARD
        lBDrive?.direction = DcMotorSimple.Direction.FORWARD

        rFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lFDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lBDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}

@TeleOp(name="Drive Train Test", group="Aim Bot")
class DriveTrainTest: OpMode()  {

    val robot = DTTHardware()

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
            robot.rFDrive?.power = 1.0
            robot.lFDrive?.power = 1.0
            robot.lBDrive?.power = 1.0
            robot.rBDrive?.power = 1.0
    }
}