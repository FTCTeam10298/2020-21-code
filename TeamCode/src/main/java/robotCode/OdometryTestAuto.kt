package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import locationTracking.Coordinate
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole

@Autonomous(name = "odometryTest", group = "Aim Bot")
class OdometryTestAuto: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = OdometryTestHardware()
    val robot = OdometryDriveMovement(console, hardware)

    val target = Coordinate()

    override fun runOpMode() {
        hardware.init(hardwareMap)

        waitForStart()

        target.setCoordinate(x = 20.0, y = 0.0, r = 0.0)
//        while (true) {
//            robot.updatePosition()
//            console.display(1, robot.globalRobot.toString())
//        }
        robot.straightGoToPosition(target,100.0,0.5,this)
//        robot.turnGoToPosition(target, 1000.0, 0.5, this)
    }
}
