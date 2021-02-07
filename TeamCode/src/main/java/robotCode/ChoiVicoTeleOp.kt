package robotCode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import telemetryWizard.TelemetryConsole

@TeleOp
class ChoiVicoTeleOp: OpMode() {
    val console = TelemetryConsole(telemetry)
    
    val hardware = ChoiVicoHardware()

    override fun init() {
        hardware.init(hardwareMap)
    }

    override fun loop() {
        hardware.rBDrive.power = 1.0
    }
}