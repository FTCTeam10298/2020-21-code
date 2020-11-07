package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import jamesTelemetryMenu.TelemetryConsole

open class DTTHardware {

//    DRIVE TRAIN
    lateinit var rFDrive: DcMotor
    lateinit var lFDrive: DcMotor
    lateinit var rBDrive: DcMotor
    lateinit var lBDrive: DcMotor

//    HARDWARE MAP
    lateinit var hwMap: HardwareMap

    fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

//        DRIVE TRAIN
        lFDrive = hwMap.get("lFDrive") as DcMotor
        rFDrive = hwMap.get("rFDrive") as DcMotor
        lBDrive = hwMap.get("lBDrive") as DcMotor
        rBDrive = hwMap.get("rBDrive") as DcMotor

        rFDrive.direction = DcMotorSimple.Direction.FORWARD
        lFDrive.direction = DcMotorSimple.Direction.FORWARD
        rBDrive.direction = DcMotorSimple.Direction.FORWARD
        lBDrive.direction = DcMotorSimple.Direction.FORWARD

        rFDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lFDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lBDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        rFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }
}

//@TeleOp(name="Drive Train Test", group="Aim Bot")
class DriveTrainTest: OpMode()  {

    val robot = DTTHardware()
    val console = TelemetryConsole(telemetry)

    override fun init() {
        robot.init(hardwareMap)
    }

    override fun loop() {
        robot.rFDrive.power = 1.0
        robot.lFDrive.power = 1.0
        robot.lBDrive.power = 1.0
        robot.rBDrive.power = 1.0

        console.display(1, robot.rFDrive.power.toString())
        console.display(2, robot.lFDrive.power.toString())
        console.display(3, robot.rBDrive.power.toString())
        console.display(4, robot.lBDrive.power.toString())

        console.display(5, robot.rFDrive.currentPosition.toString())
        console.display(6, robot.lFDrive.currentPosition.toString())
        console.display(7, robot.rBDrive.currentPosition.toString())
        console.display(8, robot.lBDrive.currentPosition.toString())
    }
}