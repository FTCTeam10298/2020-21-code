package robotCode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole

@Autonomous
class ChoiVicoAuto: LinearOpMode() {

    val console = TelemetryConsole(telemetry)

    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware)

    override fun runOpMode() {
        hardware.init(hardwareMap)

        waitForStart()

    }
}