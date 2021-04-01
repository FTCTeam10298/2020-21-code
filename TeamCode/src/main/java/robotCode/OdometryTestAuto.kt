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

//        while (true) {
//            robot.updatePosition()
//            console.display(1, robot.globalRobot.toString())
//        }
        target.setCoordinate(x = 0.0, y = 0.0, r = 90.0)
        robot.fineTunedGoToPos(target, this)
        target.setCoordinate(x = 10.0, y = 10.0, r = 90.0)
        robot.fineTunedGoToPos(target, this)
        target.setCoordinate(x = -10.0, y = -10.0, r = 270.0)
        robot.fineTunedGoToPos(target, this)
        target.setCoordinate(x = -10.0, y = -10.0, r = 180.0)
        robot.fineTunedGoToPos(target, this)
        target.setCoordinate(x = 10.0, y = -10.0, r = 270.0)
        robot.fineTunedGoToPos(target, this)
//        robot.turnGoToPosition(target, 1000.0, 0.5, this)

//        // setSpeedAll tests
//        robot.setSpeedAll(0.0,1.0,0.0, 0.0, 1.0)
//        sleep(500)
//        robot.setSpeedAll(1.0,0.0,0.0, 0.0, 1.0)
//        sleep(500)
//        robot.setSpeedAll(0.0,0.0,1.0, 0.0, 1.0)
//        sleep(500)
//        robot.setSpeedAll(1.0,1.0,0.0, 0.0, 1.0)
//        sleep(500)
//        robot.setSpeedAll(-1.0,-1.0,0.0, 0.0, 1.0)
//        sleep(500)
//        robot.setSpeedAll(1.0,1.0,1.0, 0.0, 1.0)
//        sleep(500)
//        robot.setSpeedAll(-1.0,-1.0,-1.0, 0.0, 1.0)
//        sleep(500)
    }
}
