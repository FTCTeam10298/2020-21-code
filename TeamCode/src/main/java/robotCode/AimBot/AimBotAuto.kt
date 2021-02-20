 package robotCode.AimBot

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import goalDetection.OpencvAbstraction
import robotCode.RingDetector
import robotCode.hardwareClasses.EncoderDriveMovement
import telemetryWizard.TelemetryConsole
import telemetryWizard.TelemetryWizard

 @Autonomous(name="Aim Bot Auto", group="Aim Bot")
class AimBotAuto: LinearOpMode() {

    val console = TelemetryConsole(telemetry)
    val wizard = TelemetryWizard(console)

    val opencv = OpencvAbstraction(this)
    val ringDetector = RingDetector(150, 135, console)

    val hardware = AimBotHardware()
    val robot = EncoderDriveMovement(hardware, console)

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

        opencv.init(hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()

        opencv.onFirstFrame{ ringDetector.init(it) }
        opencv.onNewFrame{ ringDetector.processFrame(it) }
        
        waitForStart()

        opencv.stop()

        when (ringDetector.position) {
            RingDetector.RingPosition.FOUR -> {
                // Step 1 deliver wobble
                robot.driveRobotPosition(1.0, -124.0, true)
                hardware.wobbleArm.power = 0.8
                sleep(700)
                hardware.wobbleArm.power = 0.0
                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0

                // Step 2 deliver wobble 2
                robot.driveRobotStrafe(1.0, 6.0, true)
                robot.driveRobotPosition(1.0, 60.0, true)
                robot.driveRobotStrafe(1.0, -12.0, true)
                robot.driveRobotPosition(1.0, 60.0, true)

                robot.driveRobotTurn(1.0,90.0,true)
                robot.driveRobotStrafe(1.0, 6.0, true)
                robot.driveRobotHug(1.0, -45, false)

                robot.driveRobotStrafe(0.6, -3.0, true)
                sleep(100)
                hardware.lClaw.position = 0.3; hardware.rClaw.position = 0.3

                robot.driveRobotPosition(1.0, 5.0, false)
                sleep(100)
                hardware.lClaw.position = 0.7; hardware.rClaw.position = 0.7

                robot.driveRobotPosition(1.0, -5.0, false)
                hardware.lClaw.position = 1.0; hardware.rClaw.position = 1.0
                hardware.wobbleArm.power = 0.6
                sleep(600)
                hardware.wobbleArm.power = 0.2

                robot.driveRobotStrafe(1.0, -95.0, true)
                robot.driveRobotPosition(0.6, -1.0, true)
                robot.driveRobotTurn(0.6, -45.0, true)
                robot.driveRobotPosition(0.6, -1.0, true)
                robot.driveRobotStrafe(1.0, -50.0, true)

                sleep(500)
                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0

                robot.driveRobotStrafe(1.0, 12.0, true)
                robot.driveRobotTurn(1.0, -45.0, true)
                robot.driveRobotPosition(1.0, 30.0, true)
            }
            RingDetector.RingPosition.ONE -> {
                // Step 1 deliver wobble
                robot.driveRobotPosition(1.0, -95.0, true)
                robot.driveRobotTurn(0.5, 180.0)
                hardware.wobbleArm.power = 0.8
                sleep(700)
                hardware.wobbleArm.power = 0.0
                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0

                // Step 2 deliver wobble 2
                robot.driveRobotStrafe(1.0, 3.0, true)
                robot.driveRobotPosition(1.0, -90.0, true)
                robot.driveRobotTurn(1.0,-90.0,true)
                robot.driveRobotStrafe(0.4, 6.0, true)
                robot.driveRobotHug(0.4, -42, false)

                robot.driveRobotStrafe(0.5, -3.0, true)
                robot.driveRobotPosition(0.5, 5.0, true)
                robot.driveRobotPosition(0.5, -5.0, true)

                hardware.lClaw.position = 1.0; hardware.rClaw.position = 1.0
                hardware.wobbleArm.power = 0.6
                sleep(600)
                hardware.wobbleArm.power = 0.2

                robot.driveRobotStrafe(0.6, -90.0, true)
                robot.driveRobotPosition(0.6, -1.0, true)
                robot.driveRobotTurn(0.4, -45.0, true)
                robot.driveRobotPosition(0.4, -1.0, true)
                robot.driveRobotStrafe(0.6, -10.0, true)

                sleep(500)
                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0

                robot.driveRobotStrafe(1.0, 6.0, true)
                robot.driveRobotPosition(1.0, 20.0, true)
            }
            RingDetector.RingPosition.NONE -> {
                // Step 1 deliver wobble
                robot.driveRobotPosition(1.0, -70.0, true)
                hardware.wobbleArm.power = 0.8
                sleep(700)
                hardware.wobbleArm.power = 0.0
                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0

                // Step 2 deliver wobble 2
                robot.driveRobotPosition(1.0, 66.0, true)
                robot.driveRobotTurn(1.0,90.0,true)
                robot.driveRobotStrafe(0.4, 6.0, true)
                robot.driveRobotHug(0.4, -40, false)

                robot.driveRobotStrafe(0.5, -3.0, true)
                robot.driveRobotPosition(0.5, 5.0, true)
                robot.driveRobotPosition(0.5, -5.0, true)

                hardware.lClaw.position = 1.0; hardware.rClaw.position = 1.0
                hardware.wobbleArm.power = 0.6
                sleep(600)
                hardware.wobbleArm.power = 0.2

                robot.driveRobotStrafe(0.6, -30.0, true)
                robot.driveRobotPosition(0.6, -1.0, true)
                robot.driveRobotTurn(0.4, -45.0, true)
                robot.driveRobotPosition(0.4, -1.0, true)
                robot.driveRobotStrafe(0.6, -50.0, true)

                sleep(500)
                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0

                robot.driveRobotStrafe(1.0, 6.0, true)
                robot.driveRobotPosition(1.0, -24.0, true)
            }
        }

//        old auto
//        when (ringDetector.position) {
//            RingDetector.RingPosition.FOUR -> {
//
//                // Step 1 deliver wobble
//                robot.driveRobotPosition(1.0, -124.0, true)
//                hardware.wobbleArm.power = 0.8
//                sleep(700)
//                hardware.wobbleArm.power = 0.0
//                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0
//                hardware.wobbleArm.power = -0.8
//                sleep(600)
//                hardware.wobbleArm.power = 0.0
//                robot.driveRobotPosition(1.0, 54.0, true)
//
//                // Step 2 shoot power shots
////                square up to wall
//                robot.driveRobotStrafe(1.0, 76.0, true)
//                sleep(100)
////                aim
//                robot.driveRobotStrafe(1.0,-40.0,true)
//                hardware.collector.power = 1.0
//                shoot(400, 4090)
////                aim
//                robot.driveRobotTurn(0.5, 4.00)
//                shoot(400, 4090)
////                aim
//                robot.driveRobotTurn(0.5, -15.0)
//                shoot(2000, 4090)
//                robot.driveRobotPosition(1.0,-12.0,true)
//            }
//            RingDetector.RingPosition.ONE -> {
//                // Step 1 deliver wobble
//                robot.driveRobotPosition(1.0, -95.0, true)
//                robot.driveRobotTurn(0.5, 180.0)
//                hardware.wobbleArm.power = 0.8
//                sleep(1000)
//                hardware.wobbleArm.power = 0.0
//                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0
//                hardware.wobbleArm.power = -0.6
//                sleep(600)
//                hardware.wobbleArm.power = 0.0
//                robot.driveRobotStrafe(1.0, 3.0, true)
//                robot.driveRobotPosition(1.0, -31.0, true)
//                sleep(100)
//                robot.driveRobotTurn(0.5, -180.0)
//
//                // Step 2 shoot power shots
//                robot.driveRobotStrafe(1.0, 76.0, true)
//                robot.driveRobotStrafe(1.0,-30.0,true)
////                robot.driveRobotPosition(1.0, -.0, true)
//                hardware.collector.power = 1.0
//                shoot(400, 4070)
//                robot.driveRobotTurn(0.5, -14.0)
//                shoot(400, 4070)
//                robot.driveRobotTurn(0.5, 8.0)
//                shoot(2000, 4070)
//                robot.driveRobotPosition(1.0,-12.0,true)
//            }
//            RingDetector.RingPosition.NONE -> {
//                // Step 1 deliver wobble
//                robot.driveRobotPosition(1.0, -76.0, true)
//                hardware.wobbleArm.power = 0.8
//                sleep(1000)
//                hardware.wobbleArm.power = 0.0
//                hardware.lClaw.position = 0.0; hardware.rClaw.position = 0.0
//                hardware.wobbleArm.power = -0.8
//                sleep(600)
//                hardware.wobbleArm.power = 0.0
//                robot.driveRobotPosition(1.0, 12.0, true)
//
//                // Step 2 shoot power shots
//                robot.driveRobotStrafe(1.0, 76.0, true)
//                sleep(100)
////                aim
//                robot.driveRobotStrafe(1.0,-40.0,true)
//                hardware.collector.power = 1.0
//                shoot(400, 4090)
////                aim
//                robot.driveRobotTurn(0.5, 6.00)
//                shoot(400, 4090)
////                aim
//                robot.driveRobotTurn(0.5, -15.0)
//                shoot(2000, 4090)
//                robot.driveRobotPosition(1.0,-12.0,true)
//            }
//        }
    }

    fun shoot(shootTime: Int, velocity: Int) {
        goToVelocity(velocity)
        while (!isVelocityCorrect()) {
            sleep(50)
        }
        hardware.gate.position = 1.0
        hardware.belt.power = 0.8
        sleep(shootTime.toLong())
        hardware.gate.position = 0.0
        hardware.belt.power = 0.0
    }
    val highGoalPreset = 4150
    var shooterRpm: Double = highGoalPreset.toDouble()

    fun goToVelocity(velocity: Int) {
        hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
        hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0, 0.0)
        hardware.shooter.velocity = (velocity.toDouble() / 60.0 * 28)
    }
    fun percentage(percent: Double, value: Double): Double = (value / 100) * percent
    fun toRPM(tps: Double): Double = tps * 60 / 28

    fun isVelocityCorrect(): Boolean = toRPM(hardware.shooter.velocity) >= shooterRpm - percentage(2.0, shooterRpm) && toRPM(hardware.shooter.velocity) <= shooterRpm + percentage(2.0, shooterRpm)

}
