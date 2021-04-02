package robotCode

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole
import kotlin.math.abs
import kotlin.math.absoluteValue

@TeleOp(name="ChoiVico TeleOp", group="ChoiVico")
class ChoiVicoTeleOp: OpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware)

//    val goalDetector = CreamsicleGoalDetector(console)
//    val turret = UltimateGoalAimer(console, robot, goalDetector)

    val highGoalPreset = 4450
    val powerShotsPreset = 4000
    var shooterRpm: Double = highGoalPreset.toDouble()
    var shooterReving = false
    var ringShooting: RingShooting = RingShooting.One
    var triggerDown: Boolean = false
    var shootingStart = 0.0
    var ringsIn = 0
    var ringIntaking = false

    var loopTime: Double = 0.0
    var lastTime: Double = 0.0

    val gamepad2RightBumperHelper = ButtonHelper()
    val gamepad2LeftBumperHelper = ButtonHelper()
    val gamepad1RightBumperHelper = ButtonHelper()
    val gamepad1LeftBumperHelper = ButtonHelper()
    val dUpHelp = ButtonHelper()
    val dDownHelp = ButtonHelper()
    val clawHelp = ButtonHelper()
    val rollerHelper = ButtonHelper()


    override fun init() {
        hardware.init(hardwareMap)
    }

    override fun start() {
//        hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        hardware.shooter.power = 0.3    // Idle Shooter
    }

    override fun loop() {

//        DRONE DRIVE
        val yInput = -gamepad1.left_stick_y.toDouble()
        val xInput = gamepad1.left_stick_x.toDouble()
        val rInput = -gamepad1.right_stick_x.toDouble()

        val y = yInput
        val x = xInput
        val r = rInput * abs(rInput)
//        val y = yInput.pow(5)
//        val x = xInput.pow(5)
//        val r = rInput.pow(5) * 0.5 + 0.5 * rInput

        robot.driveSetPower(
                (y - x - r),
                (y + x + r),
                (y + x - r),
                (y - x + r)
        )

//        RING COUNTER

        if (hardware.ringsStored.getDistance(DistanceUnit.MM) < 1)
            ringsIn = 0

        if (ringIntaking && hardware.ringsIn.getDistance(DistanceUnit.MM) > 1)
            ringsIn ++

        ringIntaking = hardware.ringsIn.getDistance(DistanceUnit.MM) < 1
        console.display(12, "distance: ${hardware.ringsIn.getDistance(DistanceUnit.MM)}")


//        LIFT
        hardware.lift1.power = gamepad2.left_stick_y.toDouble() * 0.5
        hardware.lift2.power = gamepad2.left_stick_y.toDouble() * 0.5

//        if (ringsIn == 0) {
//            hardware.lift1.position = 0.0
//            hardware.lift2.position = 0.0
//        }

//        Shoot routine
        fun goToVelocity() {
            hardware.shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            hardware.shooter.setVelocityPIDFCoefficients(450.0, 20.0, 0.0,0.0)
            hardware.shooter.velocity = (shooterRpm / 60.0 * 28)
        }

        fun isVelocityCorrect(): Boolean = toRPM(hardware.shooter.velocity) >= shooterRpm - percentage(10.0 , shooterRpm) && toRPM(hardware.shooter.velocity) <= shooterRpm + percentage(10.0 , shooterRpm)

        fun stateChanged(): Boolean = !triggerDown && gamepad1.right_trigger > 0.5

        fun shoot() {
            hardware.roller.power = 1.0

            when(ringsIn) {
                1 -> {
                    hardware.lift1.power = 1.0
                    hardware.lift2.power = 1.0
                }
                2 -> {
                    hardware.lift1.power = 0.7
                    hardware.lift2.power = 0.7
                }
                3 -> {
                    hardware.lift1.power = 0.5
                    hardware.lift2.power = 0.5
                }
            }
        }


        if (gamepad1.left_trigger > 0.2 || gamepad2.left_trigger > 0.2 || gamepad1.right_trigger > 0.2 || gamepad2.right_trigger > 0.2) {
            goToVelocity()

            shootingStart = time

            if (isVelocityCorrect() && gamepad1.right_trigger > 0.2/* && stateChanged()*/) {
                shoot()
                hardware.roller.power = 1.0
            }
            triggerDown = gamepad1.right_trigger > 0.2

            shooterReving = true
        } else if (shooterReving && !isVelocityCorrect()) {
            hardware.shooter.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            hardware.shooter.power = 0.3    // Idle Shooter
            hardware.roller.power = 0.0
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

//        TURRET
//        turret.updateAimAndAdjustRobot() //auto aim
//        hardware.turret.currentPosition - robot.globalRobot.r //aim at the same place
        hardware.turret.power = gamepad2.left_stick_x.toDouble()

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

//        GATE
        if (gamepad1.y)
            hardware.roller.power = 1.0
        else if (rollerHelper.stateChanged(gamepad1.y) && !gamepad1.y)
            hardware.roller.power = 0.0

        loopTime = time - lastTime
        lastTime = time

        console.display(2, "Loop time: ${loopTime}")
        console.display(3, "Ring #$ringShooting")
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

    fun pow(input:Double, exponent: Double): Double {
        var polarity: Double = 0.0
        polarity = when {
            input > 0 -> 1.0
            input < 0 -> -1.0
            else -> 0.0
        }
        return Math.pow(input.absoluteValue, exponent) * polarity
    }

    enum class RingShooting {
        One,
        Two,
        Three
    }

    fun timeSince(ms: Double): Double = time - ms
}
