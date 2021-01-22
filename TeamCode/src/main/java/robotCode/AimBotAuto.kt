 package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import goalDetection.OpencvAbstraction
import telemetryWizard.TelemetryConsole
import telemetryWizard.TelemetryWizard
import robotCode.hardwareClasses.EncoderDriveMovement

@Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto: LinearOpMode() {

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
        wizard.newMenu("ourWobble", "Will we do our wobble", listOf("Yes", "No"), "theirWobble")
        wizard.newMenu("theirWobble", "Will we do our partner's wobble", listOf("Yes", "No"))
//        wizard.newMenu("starterStack", "Will we collect the starter stack", listOf("Yes", "No"))
//        wizard.newMenu("powerShot", "Will we do the power shots?", listOf("Yes", "No"))

//        wizard.summonWizard(gamepad1)

        opencv.init()
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()

        waitForStart()

        ringDetector.init(opencv.frame)
//        when (ringDetector.position) {
//            NewRingDetector.RingPosition.FOUR -> {
//        robot.driveRobotPosition(1.0, -126.0, true)
//        hardware.wobbleArm.power = 0.8
//        sleep(1000)
//        hardware.wobbleArm.power = 0.0
//        hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0
//        hardware.wobbleArm.power = -0.8
//        sleep(1000)
//        hardware.wobbleArm.power = 0.0
//        robot.driveRobotPosition(1.0, 52.0, true)
//        }
//        NewRingDetector.RingPosition.ONE -> {
            robot.driveRobotPosition(1.0, -95.0, true)
            robot.driveRobotTurn(0.5, 180.0)
            hardware.wobbleArm.power = 0.8
            sleep(1000)
            hardware.wobbleArm.power = 0.0
            hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0
            hardware.wobbleArm.power = -0.8
            sleep(1000)
            hardware.wobbleArm.power = 0.0
            robot.driveRobotTurn(0.5, -180.0)
            robot.driveRobotPosition(1.0, 31.0, true)
//        }
//        NewRingDetector.RingPosition.NONE -> {
//            robot.driveRobotPosition(1.0, -79.0, true)
//            hardware.wobbleArm.power = 0.8
//            sleep(1000)
//            hardware.wobbleArm.power = 0.0
//            hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0
//            hardware.wobbleArm.power = -0.8
//            sleep(1000)
//            hardware.wobbleArm.power = 0.0
//        }

        console.display(1, "Cupertino, The Wobble Is Down ${ringDetector.position}")
        //  robot.driveRobotPosition(1.0, -7.0, true)
        robot.driveSidewaysTime(1.0, 1.0)
        shoot()
        robot.driveRobotTurn(0.5, 22.5)
        shoot()
        robot.driveRobotTurn(0.5, -45.0)
        shoot()
        //  robot.driveRobotPosition(1.0, 7.0, true)

    }

    fun shoot() {
        goToVelocity()
        while (!isVelocityCorrect()) {
            sleep(50)
        }
        hardware.gate.position = 1.0
        hardware.belt.power = 0.8
        sleep(500)
        hardware.gate.position = 0.0
        hardware.belt.power = 0.0
    }
    val highGoalPreset = 4150
    var shooterRpm: Double = highGoalPreset.toDouble()

    fun goToVelocity() {
        hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
        hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0, 0.0)
        hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
    }
    fun percentage(percent: Double, value: Double): Double = (value / 100) * percent
    fun toRPM(tps: Double): Double = tps * 60 / 28

    fun isVelocityCorrect(): Boolean = toRPM(hardware.shooter.velocity) >= shooterRpm - percentage(2.0, shooterRpm) && toRPM(hardware.shooter.velocity) <= shooterRpm + percentage(2.0, shooterRpm)

}