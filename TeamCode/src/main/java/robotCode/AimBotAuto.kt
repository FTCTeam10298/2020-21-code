 package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import jamesTelemetryMenu.TelemetryConsole
import jamesTelemetryMenu.TelemetryWizard
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera
import robotCode.aimBotRobot.EncoderDriveMovement

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console)

    val robot= EncoderDriveMovement(console)

    override fun runOpMode() {
        robot.init(hardwareMap)

//        wizard.newMenu("starterStack", "Will we collect the starter stack", listOf("Yes", "No"))
//        wizard.newMenu("powerShot", "Will we do the power shots?", listOf("Yes", "No"))
        wizard.newMenu("theirWobble", "Will we do our partner's wobble", listOf("Yes", "No"))
        wizard.newMenu("ourWobble", "Will we do our wobble", listOf("Yes", "No"), wizard.getMenu("theirWobble"))
        wizard.newMenu("startPos", "Which line are we starting in?", listOf("Closer to you", "Closer to the middle"), wizard.getMenu("ourWobble"))
        wizard.newMenu("alliance", "What alliance are we on?", listOf("Red", "Blue"), wizard.getMenu("startPos"))
        wizard.newMenu("gameType", "Which kind of game is it?", listOf("Remote", "In-Person"), wizard.getMenu("alliance"), true)
        wizard.summonWizard(gamepad1)

        waitForStart()

//        Wins 5 Pts.

        robot.shooter.power = 0.7
        robot.driveRobotPosition(0.5, -60.0, true)

        robot.driveRobotTurn(1.0, 28.0 )
        robot.belt.power = 1.0
    }
    fun shoot() {
        robot.belt.power = 1.0
    }
}
