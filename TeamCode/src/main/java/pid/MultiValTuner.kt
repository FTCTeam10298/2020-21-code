package pid

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import telemetryWizard.TelemetryConsole

@TeleOp(name="PID Tuner", group="Tests")
class PIDTuner: OpMode() {
    val console = TelemetryConsole(telemetry)
    val tuner = MultiValTuner(listOf(2.0, 4.4, 232.0), 0.5, console)

    override fun init() {}
    override fun loop() {
        tuner.tuneVals(gamepad1)
    }
}

class MultiValTuner(vals: List<Double>, private val increment: Double, private val console: TelemetryConsole) {

    private val vals = vals.toMutableList()
    private var keyDown = false

    fun tuneVals(gamepad: Gamepad) {
        console.display(1, "Tuning ${vals.size} values.")

        var currentVal = 1

        while (!gamepad.x) {
            when {
                gamepad.dpad_right && !keyDown -> {
                    keyDown = true;
                    if (currentVal > 0)
                        currentVal -= 1
                } //changes val
                gamepad.dpad_left && !keyDown -> {
                    keyDown = true;
                    if (currentVal < vals.size)
                        currentVal += 1
                }  //changes val
                gamepad.dpad_up && !keyDown -> {
                    keyDown = true;
                    vals[currentVal] += increment
                } //adds to val
                gamepad.dpad_left && !keyDown -> {
                    keyDown = true;
                    vals[currentVal] -= increment
                } //subtracts from val
                !gamepad.dpad_down && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
            }
            vals.forEach{
                console.display(vals.indexOf(it) + 2, "Value ${vals.indexOf(it)}: $it")
            }
        }
    }

    fun returnVal(index: Int): Double = vals[index]
}