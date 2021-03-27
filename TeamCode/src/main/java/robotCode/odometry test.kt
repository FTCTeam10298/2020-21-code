package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import locationTracking.Coordinate
import pid.PID
import robotCode.hardwareClasses.MecOdometryHardware
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole



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

        target.setCoordinate(x = 0.0, y = 12.0, r = 0.0)
        robot.straightGoToPosition(target,1.0,0.5,this)
        robot.turnGoToPosition(target, 1.0, 0.5, this)
        robot.updatePosition() // For viewing accurate numbers on telemetry console

//        robot.driveSetPower(0.5, 0.5,0.5,0.5)
//        sleep(10000)
    }
}
