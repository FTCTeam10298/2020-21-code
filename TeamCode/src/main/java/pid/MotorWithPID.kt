package pid

import com.qualcomm.robotcore.hardware.DcMotor

class MotorWithPID: PID() {

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