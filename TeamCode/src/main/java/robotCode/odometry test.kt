package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import locationTracking.Coordinate
import robotCode.hardwareClasses.MecOdometryHardware
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole
import kotlin.math.pow

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

        lOdom = hwMap.dcMotor.get("left collector")
        cOdom = hwMap.dcMotor.get("tape")
        rOdom = hwMap.dcMotor.get("left drive b")

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

        while (true) {
            val yInput = gamepad1.left_stick_y.toDouble()
            val xInput = gamepad1.left_stick_x.toDouble()
            val rInput = gamepad1.right_stick_x.toDouble()

            val y = yInput.pow(5)
            val x = xInput.pow(5)
            val r = rInput.pow(5) * 0.5 + 0.5 * rInput

            hardware.motors(
                    -(y - x - r),
                    -(y + x + r),
                    -(y + x - r),
                    -(y - x + r)
            )
        }
        target.setCoordinate(target.x, target.y - 72, target.r)
        robot.straightGoToPosition(target, .5, 1.0, this)
    }
}