 package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import jamesTelemetryMenu.TelemetryConsole
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera
import robotCode.aimBotRobot.EncoderDriveMovement

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    var menu: TelemetryConsole = TelemetryConsole(telemetry)
    val robot= EncoderDriveMovement(menu)

    override fun runOpMode() {

        robot.init(hardwareMap)

        waitForStart()

//        Wins 5 Pts.

        robot.shooter.power = 1.0
        robot.driveRobotPosition(1.0, -48.0, true)

        robot.driveRobotTurn(1.0, 35.0 )
        robot.belt.power = 8.0
    }
    fun shoot() {
        robot.belt.power = 8.0
    }
}
