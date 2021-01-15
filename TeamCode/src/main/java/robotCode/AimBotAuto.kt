 package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import goalDetection.OpencvAbstraction
import telemetryWizard.TelemetryConsole
import telemetryWizard.TelemetryWizard
import robotCode.hardwareClasses.EncoderDriveMovement

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto(): LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console)

    val opencv = OpencvAbstraction(this)
    val ringDetector = NewRingDetector(150, 135)

    val hardware = AimBotHardware()
    val robot = EncoderDriveMovement(console, hardware)

    override fun runOpMode() {
        hardware.init(hardwareMap)

        wizard.newMenu("gameType", "Which kind of game is it?", listOf("Remote", "In-Person"), "alliance", true)
        wizard.newMenu("alliance", "What alliance are we on?", listOf("Red", "Blue"), "startPos")
        wizard.newMenu("startPos", "Which line are we starting in?", listOf("Closer to you", "Closer to the middle"), "ourWobble")
        wizard.newMenu("ourWobble", "Will we do our wobble", listOf("Yes", "No"),"theirWobble")
        wizard.newMenu("theirWobble", "Will we do our partner's wobble", listOf("Yes", "No"))
//        wizard.newMenu("starterStack", "Will we collect the starter stack", listOf("Yes", "No"))
//        wizard.newMenu("powerShot", "Will we do the power shots?", listOf("Yes", "No"))
        
        wizard.summonWizard(gamepad1)

        opencv.init()
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()

        waitForStart()

        ringDetector.init(opencv.frame)
        if (ringDetector.position == NewRingDetector.RingPosition.FOUR) {
            robot.driveRobotPosition(1.0, 132.0,  smart_accel = true)
            hardware.wobbleArm.power = 1.0
            sleep(5000)
            hardware.wobbleArm.power = 0.0
            console.display(1, "Cupertino, The Wobble Is Down")
        }
    }

    fun shoot() {
        hardware.belt.power = 1.0
    }
}
