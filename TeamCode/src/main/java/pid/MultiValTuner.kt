package pid

import com.qualcomm.robotcore.hardware.Gamepad
import telemetryWizard.TelemetryConsole

class MultiValTuner(private val vals: MutableList<Double>, private val increment: Double, private val console: TelemetryConsole) {

    var keyDown = false

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