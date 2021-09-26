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
    val robot = OdometryDriveMovement(console, hardware, this)

    val target = Coordinate()

    override fun runOpMode() {
        hardware.init(hardwareMap)

        waitForStart()

//        robot.setSpeedAll(0.0, 1.0, 0.0, 0.0, 1.0)
//        sleep(400)
//        robot.setSpeedAll(1.0, 0.0, 0.0, 0.0, 1.0)
//        sleep(400)
//        robot.setSpeedAll(0.0, 0.0, 0.0, 0.0, 0.0)

        target.setCoordinate(r = 90.0)
        robot.turnGoToPosition(target, 0.9, 0.1)
        target.setCoordinate(r = 0.0)
        robot.turnGoToPosition(target, 0.9, 0.1)
        
//        target.setCoordinate(y = 0.0)
//        robot.straightGoToPosition(target, 1.0, 0.1, this)

//        target.setCoordinate(x = 0.0, y = 10.0, r = 0.0)
//        robot.straightGoToPosition(target, 1.0, 0.25, this)
//        target.setCoordinate(x = 10.0, y = 0.0, r = 0.0)
//        robot.straightGoToPosition(target, 1.0, 0.25, this)
//        target.setCoordinate(x = 0.0, y = 0.0, r = 90.0)
//        robot.turnGoToPosition(target, 1.0, 0.5, this)
//        target.setCoordinate(x = 10.0, y = 10.0, r = 0.0)
//        robot.straightGoToPosition(target, 1.0, 0.25, this)
//        target.setCoordinate(x = 0.0, y = 10.0, r = 180.0)
//        robot.turnGoToPosition(target, 1.0, 0.5, this)

        while (true) {
            console.display(1, "absolute x " + robot.localizer.current.x.toString())
            console.display(2, "absolute y " + robot.localizer.current.y.toString())
            console.display(3, "absolute r " + (robot.localizer.current.r * 57.2958).toString())

            console.display(5, "left raw " + hardware.lOdom.currentPosition.toString())
            console.display(6, "right raw " + hardware.rOdom.currentPosition.toString())
            console.display(7,"center raw " +  hardware.cOdom.currentPosition.toString())

            robot.localizer.updatePosition()
        }
    }
}
