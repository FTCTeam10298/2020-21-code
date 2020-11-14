package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import jamesTelemetryMenu.TelemetryMenu
import robotCode.aimBotRobot.EncoderDriveMovement

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    var menu: TelemetryMenu = TelemetryMenu(telemetry)
    val robot= EncoderDriveMovement(menu)

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.


        robot.driveRobotPosition(1.0,73.0, true)
//        robot.driveRobotTurn(1.0, 360 * 1.0)

    }
}