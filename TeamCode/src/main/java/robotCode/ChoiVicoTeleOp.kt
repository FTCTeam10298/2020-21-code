package robotCode

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotCode.AimBot.AimBotHardware
import robotCode.hardwareClasses.MecanumDriveTrain
import telemetryWizard.TelemetryConsole
import kotlin.math.pow

@TeleOp
class ChoiVicoTeleOp: OpMode() {

    val console = TelemetryConsole(telemetry)
    val hardware = ChoiVicoHardware()
    val robot = MecanumDriveTrain(hardware)


    val gamepad2RightBumperHelper = ButtonHelper()
    val gamepad2LeftBumperHelper = ButtonHelper()
    val gamepad1RightBumperHelper = ButtonHelper()
    val gamepad1LeftBumperHelper = ButtonHelper()

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
}