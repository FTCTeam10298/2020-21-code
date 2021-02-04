package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotCode.hardwareClasses.OdometryDriveMovement
import telemetryWizard.TelemetryConsole

@TeleOp
class ChoiVicoTeleOp: OpMode() {
    val console = TelemetryConsole(telemetry)
    
    val hardware = ChoiVicoHardware()
    val robot = OdometryDriveMovement(console, hardware)

    override fun init() {
    }

    override fun loop() {
        console.display(1, "It's a ctrl hb!!!")
    }
}