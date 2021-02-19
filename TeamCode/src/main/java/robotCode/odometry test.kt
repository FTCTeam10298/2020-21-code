package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import locationTracking.Coordinate
import robotCode.hardwareClasses.MecOdometryHardware
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole

class odometryHardware: MecOdometryHardware {
    override lateinit var lOdom: DcMotor
    override lateinit var rOdom: DcMotor
    override lateinit var cOdom: DcMotor
    override lateinit var lFDrive: DcMotor
    override lateinit var rFDrive: DcMotor
    override lateinit var lBDrive: DcMotor
    override lateinit var rBDrive: DcMotor

    override lateinit var hwMap: HardwareMap

    override fun init(ahwMap: HardwareMap) {

        hwMap = ahwMap

        lFDrive = hwMap.get("left drive f") as DcMotor
        lBDrive = hwMap.get("left drive b") as DcMotor
        rFDrive = hwMap.get("right drive f") as DcMotor
        rBDrive = hwMap.get("right drive b") as DcMotor

        rFDrive.direction = DcMotorSimple.Direction.FORWARD
        lFDrive.direction = DcMotorSimple.Direction.REVERSE
        rBDrive.direction = DcMotorSimple.Direction.FORWARD
        lBDrive.direction = DcMotorSimple.Direction.REVERSE

        rFDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lFDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rBDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lBDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        rFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lFDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lBDrive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE


        lOdom = hwMap.dcMotor.get("left collector")
        cOdom = hwMap.dcMotor.get("tape")
        rOdom = hwMap.dcMotor.get("left drive b")

        lOdom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lOdom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        cOdom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        cOdom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        rOdom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rOdom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}

@Autonomous(name = "odometryTest", group = "Aim Bot")
class odometryTest: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = odometryHardware()
    val robot = OdometryDriveMovement(console, hardware)

    val target = Coordinate()

    override fun runOpMode() {
        hardware.init(hardwareMap)

        waitForStart()

//        while (true) {
//            val yInput = gamepad1.left_stick_y.toDouble()
//            val xInput = gamepad1.left_stick_x.toDouble()
//            val rInput = gamepad1.right_stick_x.toDouble()
//
//            val y = yInput.pow(5)
//            val x = xInput.pow(5)
//            val r = rInput.pow(5) * 0.5 + 0.5 * rInput
//
//            robot.driveSetPower(
//                    (y - x - r),
//                    (y + x + r),
//                    (y + x - r),
//                    (y - x + r)
//            )
//            robot.updatePosition()
//            console.display(1, robot.globalRobot.toString())
//            console.display(5, robot.current.toString())
//        }
//        while (true)
//            robot.setSpeedAll(0.0, 1.0, 0.0, 0.1, 1.0)
//        target.setCoordinate(robot.globalRobot.x, robot.globalRobot.y, Math.toDegrees(robot.globalRobot.r))
        target.setCoordinate(x = 25.0, y = 15.0)
        robot.straightGoToPosition(target, .5, 0.2, this)
//        robot.driveSetPower(0.5, 0.5,0.5,0.5)
//        sleep(10000)
    }
}