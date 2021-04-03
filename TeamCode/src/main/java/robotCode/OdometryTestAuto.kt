package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import locationTracking.Coordinate
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole

@Autonomous(name = "odometryTest", group = "Aim Bot")
class OdometryTestAuto: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware)

    val target = Coordinate()

    override fun runOpMode() {
        hardware.init(hardwareMap)

        waitForStart()

        while (true)
            console.display(1, "Coords ${robot.globalRobot}")
//        target.setCoordinate(x = 0.0, y = 0.0, r = 90.0)
//        robot.fineTunedGoToPos(target, this)
//        target.setCoordinate(x = 10.0, y = 10.0, r = 90.0)
//        robot.fineTunedGoToPos(target, this)
//        target.setCoordinate(x = -10.0, y = -10.0, r = 270.0)
//        robot.fineTunedGoToPos(target, this)
//        target.setCoordinate(x = -10.0, y = -10.0, r = 180.0)
//        robot.fineTunedGoToPos(target, this)
//        target.setCoordinate(x = 10.0, y = -10.0, r = 270.0)
//        robot.fineTunedGoToPos(target, this)
    }
}