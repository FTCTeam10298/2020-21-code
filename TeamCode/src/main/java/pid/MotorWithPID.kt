package pid

import com.qualcomm.robotcore.hardware.DcMotorEx

class MotorWithPID() {


    fun setPIDFVals(motor: DcMotorEx, p: Double, i: Double, d: Double, f: Double) {
//        translate some actual standard to the sdk's weird thing
        motor.setVelocityPIDFCoefficients(p, i, d, f)
    }

//    fun setPIDVals(motor: DcMotorEx, p: Double, i: Double, d: Double) {
//        setPIDFVals(motor, p, i, d, 0.0)
//    }

    /**
     *  @tpr stands for ticks per revolution
    */
    fun startMotorPID(motor: DcMotorEx, targetRpm: Double, tpr: Int) {
        motor.velocity = targetRpm / 60 * tpr
    }

    fun startMotorPID(motor: DcMotorEx, targetRpm: Double) {
        startMotorPID(motor, targetRpm, 28)
    }

}