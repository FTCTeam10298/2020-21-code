package robotCode

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import robotCode.AimBot.AimBotHardware
import robotCode.hardwareClasses.MecanumDriveTrain
import telemetryWizard.TelemetryConsole
import kotlin.math.absoluteValue
import kotlin.math.pow

@TeleOp
class ChoiVicoTeleOp: OpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = ChoiVicoHardware()
    val robot = MecanumDriveTrain(hardware)

    val highGoalPreset = 4450
    val powerShotsPreset = 4000
    var shooterRpm: Double = highGoalPreset.toDouble()
    var triggerHeld = false

    val gamepad2RightBumperHelper = ButtonHelper()
    val gamepad2LeftBumperHelper = ButtonHelper()
    val gamepad1RightBumperHelper = ButtonHelper()
    val gamepad1LeftBumperHelper = ButtonHelper()
    val dUpHelp = ButtonHelper()
    val dDownHelp = ButtonHelper()


    override fun init() {
        hardware.init(hardwareMap)
    }

    override fun loop() {

//        DRONE DRIVE
        val yInput = gamepad1.left_stick_y.toDouble()
        val xInput = gamepad1.left_stick_x.toDouble()
        val rInput = gamepad1.right_stick_x.toDouble()

        val y = yInput.pow(5)
        val x = xInput.pow(5)
        val r = rInput.pow(5) * 0.5 + 0.5 * rInput

        robot.driveSetPower(
                (y - x - r),
                (y + x + r),
                (y + x - r),
                (y - x + r)
        )


        //        Shoot routine
        fun goToVelocity() {
            hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0,0.0)
            hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
        }

        fun isVelocityCorrect(): Boolean = toRPM(hardware.shooter.velocity) >= shooterRpm - percentage(2.0 , shooterRpm) && toRPM(hardware.shooter.velocity) <= shooterRpm + percentage(2.0 , shooterRpm)

        if (gamepad1.right_trigger > 0.2) {
            goToVelocity()
            if (isVelocityCorrect()) {
                hardware.gate.position = 1.0
            }
            triggerHeld = true
        } else if (triggerHeld && !isVelocityCorrect()) {
            hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            hardware.shooter.power = 0.3    // Idle Shooter
            hardware.gate.position = 0.0
            triggerHeld = false
        }

        console.display(4, "Shooter rpm: ${hardware.shooter.velocity * 60 / 28}")
        console.display(5, "Shooter tps: ${hardware.shooter.velocity}")
        console.display(6, "Shooter Power: ${hardware.shooter.power}")

//        SHOOTER
        val shooterRpmIncrement: Int = 200

        when {
            (dUpHelp.stateChanged(gamepad1.dpad_up) && gamepad1.dpad_up) && shooterRpm < 5500 -> shooterRpm += shooterRpmIncrement
            (dDownHelp.stateChanged(gamepad1.dpad_down) && gamepad1.dpad_down) && shooterRpm > 0 + shooterRpmIncrement -> shooterRpm -= shooterRpmIncrement
            gamepad1.dpad_left || gamepad2.dpad_left -> hardware.shooter.power = 0.0
            gamepad1.dpad_right || gamepad2.dpad_right-> shooterRpm = highGoalPreset.toDouble()
        }

        if (gamepad2.a || gamepad1.a)
            shooterRpm = powerShotsPreset.toDouble()

        if (gamepad1.left_trigger > 0.2 || gamepad2.left_trigger > 0.2) {
            hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0, 0.0)
            hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
        }


//        COLLECTOR
        if ((gamepad1RightBumperHelper.stateChanged(gamepad1.right_bumper) && (gamepad1.right_bumper)) || (gamepad2RightBumperHelper.stateChanged(gamepad2.right_bumper) && (gamepad2.right_bumper)))
            if (hardware.collector.power == 1.0)
                hardware.collector.power = 0.0
            else
                hardware.collector.power = 1.0
        else if ((gamepad1LeftBumperHelper.stateChanged(gamepad1.left_bumper) && (gamepad1.left_bumper)) || (gamepad2LeftBumperHelper.stateChanged(gamepad2.left_bumper) && (gamepad2.left_bumper)))
            if (hardware.collector.power == -1.0)
                hardware.collector.power = 0.0
            else
                hardware.collector.power = -1.0
    }

    fun toRPM(tps: Double): Double = tps * 60 / 28

    fun percentage(percent: Double, value: Double): Double = (value / 100) * percent

    fun pow(n: Double, exponent: Double): Double {
        var polarity: Double = 0.0
        polarity = when {
            n > 0 -> 1.0
            n < 0 -> -1.0
            else -> 0.0
        }
        return n.absoluteValue.pow(exponent) * polarity
    }
}