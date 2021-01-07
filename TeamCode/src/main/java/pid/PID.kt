package pid

import com.qualcomm.robotcore.hardware.DcMotor

open class PID(var p: Double, var i: Double, var d: Double) {

    constructor(): this(0.0, 0.0, 0.0)

    private var errorP: Double = 0.0
    private var errorI: Double = 0.0
    private var errorD: Double = 0.0

    private var gainP: Double = 1.0
    private var gainI: Double = 0.0
    private var gainD: Double = 1.0

    fun calcP(feedback: Double, goal: Double): Double {

        errorP = goal - feedback
        p = errorP

        return gainP * p
    }


    private var lastI: Double = 0.0
    private var startTimeI= 0.0
    private var endTimeI= 0.0
    private var durationI= 0.0

    fun calcI(feedback: Double, goal: Double): Double {
        endTimeI = System.nanoTime().toDouble()
        durationI = endTimeI - startTimeI
        startTimeI = System.nanoTime().toDouble()

        errorI = goal - feedback
        i = lastI + errorI * durationI
        lastI = i

        return gainI * i
    }


    private var lastError: Double = 0.0
    private var startTimeD= 0.0
    private var endTimeD= 0.0
    private var durationD= 0.0

    fun calcD(feedback: Double, goal: Double): Double {
        endTimeD = System.nanoTime().toDouble()
        durationD = endTimeD - startTimeD
        startTimeD = System.nanoTime().toDouble()

        errorD = goal - feedback
        d = (errorD - lastError) / durationD
        lastError = errorD


        return gainD * d
    }

    fun calcPI(feedback: Double, goal: Double): Double {
        return calcP(feedback, goal) + calcI(feedback, goal)
    }

    fun calcPD(feedback: Double, goal: Double): Double {
        return calcP(feedback, goal) + calcD(feedback, goal)
    }

    fun calcPID(feedback: Double, goal: Double): Double {
        return calcP(feedback, goal) + calcI(feedback, goal) + calcD(feedback, goal)
    }

    override fun toString(): String {
        return "Proportional: $p\n Integral: $i\n Derivative: $d"
    }
}
