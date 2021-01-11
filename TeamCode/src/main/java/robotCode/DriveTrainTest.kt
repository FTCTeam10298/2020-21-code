package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import telemetryWizard.TelemetryConsole

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

        lFDrive.targetPosition = 0
        rFDrive.targetPosition = 0
        lBDrive.targetPosition = 0
        rBDrive.targetPosition = 0

        rFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lFDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lBDrive.mode = DcMotor.RunMode.RUN_USING_ENCODER

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
        robot.lFDrive.power = 1.0
        robot.rFDrive.power = 1.0
        robot.lBDrive.power = 1.0
        robot.rBDrive.power = 1.0

        robot.lFDrive.targetPosition = 100
        robot.rFDrive.targetPosition = 100
        robot.lBDrive.targetPosition = 100
        robot.rBDrive.targetPosition = 100

        if (robot.lFDrive.currentPosition > robot.lFDrive.targetPosition)
            console.display(3, "LF+")
        else
            console.display(3, "LF-")

        if (robot.rFDrive.currentPosition > robot.rFDrive.targetPosition)
            console.display(4, "RF+")
        else
            console.display(4, "RF-")

        if (robot.lBDrive.currentPosition > robot.lBDrive.targetPosition)
            console.display(5, "LB+")
        else
            console.display(5, "LB-")

        if (robot.rBDrive.currentPosition > robot.rBDrive.targetPosition)
            console.display(6, "RB+")
        else
            console.display(6, "RB-")

    }
}