package pid

import com.qualcomm.robotcore.hardware.DcMotor

/**
 * Sets pidf coefficients/multipliers.
 * @param p proportional coefficient
 * @param i integral coefficient
 * @param d derivative coefficient
 * @param f feed-forward coefficient
 * */
open class PID(p: Double = 0.0, i: Double = 0.0, d: Double = 0.0, f: Double = 0.0) {

    private val kp = p
    private val ki = i
    private val kd = d
    private val kf = f

    var p: Double = 1.0
    var i: Double = 1.0
    var d: Double = 1.0
    var f: Double = 1.0

    /**
     * Calculates pidf in a loop.
     * @param target the target value for the controller
     * @param feedback the current value
     * @return the calculated value for the pid
     * */
    fun calcPID(target: Double, feedback: Double): Double {
        val error: Double = target - feedback



        p = kp * error
        i = 1.0
        d = 1.0
        f = 1.0

        return p * i * d * f
    }

    override fun toString(): String {
        return "Proportional: $p\n Integral: $i\n Derivative: $d"
    }
}
