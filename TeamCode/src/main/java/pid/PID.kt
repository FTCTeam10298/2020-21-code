package pid

import com.qualcomm.robotcore.hardware.DcMotor

open class PID {

    private var p: Double = 0.0
    private var i: Double = 0.0
    private var d: Double = 0.0

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
}


class MotorWithPID(): PID() {

    fun runMotorWithP(motor: DcMotor, power: Double) {
        motor.setPower(calcP(motor.power, power))
    }

    fun runMotorWithI(motor: DcMotor, power: Double) {
        motor.setPower(calcI(motor.power, power))
    }

    fun runMotorWithD(motor: DcMotor, power: Double) {
        motor.setPower(calcD(motor.power, power))
    }

    fun runMotorWithPI(motor: DcMotor, power: Double) {
        motor.setPower(calcPI(motor.power, power))
    }

    fun runMotorWithPD(motor: DcMotor, power: Double) {
        motor.setPower(calcPD(motor.power, power))
    }

    fun runMotorWithPID(motor: DcMotor, power: Double) {
        motor.setPower(calcPID(motor.power, power))
    }

    fun motorToPositionWithP(motor: DcMotor, position: Int, power: Double) {
        motor.setTargetPosition(calcP(motor.currentPosition.toDouble(), position.toDouble()).toInt())
        runMotorWithP(motor, power)
    }

    fun motorToPositionWithI(motor: DcMotor, position: Int, power: Double) {
        motor.setTargetPosition(calcI(motor.currentPosition.toDouble(), position.toDouble()).toInt())
        runMotorWithI(motor, power)
    }

    fun motorToPositionWithD(motor: DcMotor, position: Int, power: Double) {
        motor.setTargetPosition(calcD(motor.currentPosition.toDouble(), position.toDouble()).toInt())
        runMotorWithD(motor, power)
    }

    fun motorToPositionWithPI(motor: DcMotor, position: Int, power: Double) {
        motor.setTargetPosition((
                        calcP(motor.currentPosition.toDouble(), position.toDouble()) +
                        calcI(motor.currentPosition.toDouble(), position.toDouble())).toInt()
        )
        runMotorWithPI(motor, power)
    }

    fun motorToPositionWithPD(motor: DcMotor, position: Int, power: Double) {
        motor.setTargetPosition((
                        calcP(motor.currentPosition.toDouble(), position.toDouble()) +
                        calcD(motor.currentPosition.toDouble(), position.toDouble())).toInt()
        )
        runMotorWithPD(motor, power)
    }

    fun motorToPositionWithPID(motor: DcMotor, position: Int, power: Double) {
        motor.setTargetPosition((
                        calcP(motor.currentPosition.toDouble(), position.toDouble()) +
                        calcI(motor.currentPosition.toDouble(), position.toDouble()) +
                        calcD(motor.currentPosition.toDouble(), position.toDouble())).toInt()
        )
        runMotorWithPID(motor, power)
    }
}