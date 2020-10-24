package teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import java.lang.Thread.sleep


open class AimBotMovement(): AimBotHardware() {

    fun fourMotors(rFpower: Double, lFpower: Double, rBpower: Double, lBpower: Double) {
        rFDrive?.power = rFpower
        lFDrive?.power = lFpower
        rBDrive?.power = rBpower
        lBDrive?.power = lBpower
    }

    fun stopMotors() {
        fourMotors(0.0,0.0,0.0,0.0,)
    }

    fun moveTime(rFpower: Double, lFpower: Double, rBpower: Double, lBpower: Double, milis: Int) {
        fourMotors(rFpower, lFpower, rBpower, lBpower)
        sleep(milis.toLong())
    }

    fun straight(power: Double, milis: Int) {
        moveTime(power, power, power, power, milis)
    }

    fun side(power: Double, milis: Int) {
        moveTime(-power, power, power, -power, milis)
    }

    fun rotate(power: Double, milis: Int) {
        moveTime(-power, power, -power, power, milis)
    }

    fun setPos(target: Int) {
//        reset encoder counts kept by motors.
        lFDrive?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rFDrive?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rBDrive?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lBDrive?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

//        set motors to run toward for encoder counts.
        lFDrive?.targetPosition = target
        rFDrive?.targetPosition = target
        rBDrive?.targetPosition = target
        lBDrive?.targetPosition = target

//        set motors to run to target encoder position and stop with brakes on.
        lFDrive?.mode = DcMotor.RunMode.RUN_TO_POSITION
        rFDrive?.mode = DcMotor.RunMode.RUN_TO_POSITION
        rBDrive?.mode = DcMotor.RunMode.RUN_TO_POSITION
        lBDrive?.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    fun moveEncoder(rFpower: Double, lFpower: Double, rBpower: Double, lBpower: Double, ticks: Int) {
        fourMotors(rFpower, lFpower, rBpower, lBpower)
        setPos(ticks)
        while (!rFDrive!!.isBusy && !lFDrive!!.isBusy && !rBDrive!!.isBusy /*&& !lBDrive!!.isBusy*/) {
            print("rF = ${rFDrive?.currentPosition} n/ lF = ${lFDrive?.currentPosition} n/ rB = ${rBDrive?.currentPosition} n/ lB = ${lBDrive?.currentPosition} n/ ")
        }
        stopMotors()
    }

    fun eStraight(power: Double, ticks: Int) {
        moveEncoder(power, power, power, power, ticks)
    }

    fun eStrafe(power: Double, ticks: Int) {
        moveEncoder(-power, power, power, -power, ticks)
    }

    fun eRotate(power: Double, ticks: Int) {
        moveEncoder(-power, power, -power, power, ticks)
    }
}
