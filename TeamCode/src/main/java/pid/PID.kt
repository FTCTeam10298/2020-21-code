package pid

import com.qualcomm.robotcore.hardware.DcMotor

open class PID(p: Double = 0.0, i: Double = 0.0, d: Double = 0.0, f: Double = 0.0) {

    private val kp = p
    private val ki = i
    private val kd = d
    private val kf = f
    var p: Double = 0.0
    var i: Double = 0.0
    var d: Double = 0.0
    var f: Double = 0.0

    fun calcPID(target: Double, feedback: Double): Double {
        val error: Double = target - feedback



        p = kp * error
        i = 0.0
        d = 0.0
        f= 0.0

        return p * i * d * f
    }

    override fun toString(): String {
        return "Proportional: $p\n Integral: $i\n Derivative: $d"
    }
}
