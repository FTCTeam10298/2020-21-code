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

//        while (true) {
//            console.display(1, robot.globalRobot.y.toString())
//            console.display(2, robot.globalRobot.x.toString())
//            console.display(3, (robot.globalRobot.r * 57.2958).toString())
//            robot.updatePosition()
//        }

//        robot.setSpeedAll(0.0, 1.0, 0.0, 0.0, 1.0)
//        sleep(400)
//        robot.setSpeedAll(1.0, 0.0, 0.0, 0.0, 1.0)
//        sleep(400)
//        robot.setSpeedAll(0.0, 0.0, 1.0, 0.0, 1.0)
//        sleep(800)
//        robot.setSpeedAll(1.0, 1.0, 0.0, 0.0, 1.0)
//        sleep(400)
//        robot.setSpeedAll(0.0, 1.0, 1.0, 0.0, 1.0)
//        sleep(400)

        target.addCoordinate(x = 0.0, y = 10.0, r = 0.0)
        robot.fineTunedGoToPos(target, this)
        target.addCoordinate(x = 10.0, y = 0.0, r = 0.0)
        robot.fineTunedGoToPos(target, this)
        target.addCoordinate(x = 0.0, y = 0.0, r = 90.0)
        robot.fineTunedGoToPos(target, this)
        console.display(20, "FINISHED TURNING")
        target.addCoordinate(x = 10.0, y = 10.0, r = 0.0)
        robot.fineTunedGoToPos(target, this)
        target.addCoordinate(x = 0.0, y = 10.0, r = 10.0)
        robot.fineTunedGoToPos(target, this)
    }
}