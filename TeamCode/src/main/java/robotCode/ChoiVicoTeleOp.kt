package robotCode

import android.os.SystemClock.sleep
import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import robotCode.hardwareClasses.MecanumDriveTrain
import telemetryWizard.TelemetryConsole
import kotlin.math.absoluteValue

@TeleOp(name="Tele-Op ChoiVico", group="ChoiVico")
class ChoiVicoTeleOp: OpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = ChoiVicoHardware()
    val robot = MecanumDriveTrain(hardware)

    val highGoalPreset = 4450
    val powerShotsPreset = 4000
    var shooterRpm: Double = highGoalPreset.toDouble()
    var shooterReving = false
    var ringShooting: RingShooting = RingShooting.One
    var endTime = 0.0

    var loopTime: Double = 0.0
    var lastTime: Double = 0.0

    val gamepad2RightBumperHelper = ButtonHelper()
    val gamepad2LeftBumperHelper = ButtonHelper()
    val gamepad1RightBumperHelper = ButtonHelper()
    val gamepad1LeftBumperHelper = ButtonHelper()
    val dUpHelp = ButtonHelper()
    val dDownHelp = ButtonHelper()
    val clawHelp = ButtonHelper()


    override fun init() {
        hardware.init(hardwareMap)
    }

    override fun start() {
        hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        hardware.shooter.power = 0.3    // Idle Shooter
    }

    override fun loop() {

//        DRONE DRIVE
        val yInput = -gamepad1.left_stick_y.toDouble()
        val xInput = gamepad1.left_stick_x.toDouble()
        val rInput = -gamepad1.right_stick_x.toDouble()

        val y = yInput
        val x = xInput
        val r = rInput * 1.5
//        val y = yInput.pow(5)
//        val x = xInput.pow(5)
//        val r = rInput.pow(5) * 0.5 + 0.5 * rInput

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
            if (isVelocityCorrect() && gamepad1.right_trigger > 0.2) {
                hardware.lift.position = 0.0
                hardware.gate.position = 0.0

                when(ringShooting) {
                    RingShooting.One -> {
                        if (timeSince(endTime) >= 200.0) {
                            hardware.lift.position = 0.6
                            sleep(200)
                            hardware.gate.position = 0.6
                            ringShooting = RingShooting.Two
                            endTime = time
                        }
                    }
                    RingShooting.Two -> {
                        if (timeSince(endTime) >= 400.0) {
                            hardware.gate.position = 0.1
                            sleep(200)
                            hardware.lift.position = 0.8
                            sleep(200)
                            hardware.gate.position = 0.6
                            ringShooting = RingShooting.Three
                            endTime = time
                        }
                    }
                    RingShooting.Three -> {
                        if (timeSince(endTime) >= 400.0) {
                            hardware.gate.position = 0.1
                            sleep(200)
                            hardware.lift.position = 2.0
                            sleep(300)
                            hardware.gate.position = 0.6
                            ringShooting = RingShooting.One
                            endTime = time
                        }
                    }
                }
            }
            shooterReving = true
        } else if (shooterReving && !isVelocityCorrect()) {
            hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            hardware.shooter.power = 0.3    // Idle Shooter
            shooterReving = false
        }

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


//        WOBBLE ARM
        val wobbleStick = gamepad2.right_stick_y
        hardware.wobble.power = wobbleStick.toDouble()

//        CLAW
        if (clawHelp.stateChanged(gamepad2.x) && gamepad2.x) {
            when (hardware.claw1.position) {
                1.0 -> {
                    hardware.claw1.position = 0.5; hardware.claw2.position = 0.5
                }
                else -> {
                    hardware.claw1.position = 1.0; hardware.claw2.position = 1.0
                }
            }
        }

//        LIFT
        if (gamepad2.b) {
            hardware.lift.position = 1.0
        } else {
            hardware.lift.position = 0.0
        }

//        GATE
        if (gamepad2.y)
            hardware.gate.position = 0.5
        else
            hardware.gate.position = 0.0


        loopTime = time - lastTime
        lastTime = time

        console.display(1, "Loop time: ${loopTime}")
        console.display(4, "Shooter rpm: ${hardware.shooter.velocity * 60 / 28}")
        console.display(5, "Target rpm: $shooterRpm")
        console.display(6, "Shooter tps: ${hardware.shooter.velocity}")
        console.display(7, "Shooter Power: ${hardware.shooter.power}")
        console.display(8, "LF: ${hardware.lFDrive.power}")
        console.display(9, "RF: ${hardware.rFDrive.power}")
        console.display(10, "LB: ${hardware.lBDrive.power}")
        console.display(11, "RB: ${hardware.rBDrive.power}")
    }

    fun toRPM(tps: Double): Double = tps * 60 / 28

    fun percentage(percent: Double, value: Double): Double = (value / 100) * percent

    fun Double.pow(exponent: Double): Double {
        var polarity: Double = 0.0
        polarity = when {
            this > 0 -> 1.0
            this < 0 -> -1.0
            else -> 0.0
        }
        return this.absoluteValue.pow(exponent) * polarity
    }

    enum class RingShooting {
        One,
        Two,
        Three
    }

    fun timeSince(ms: Double): Double = time - ms
}