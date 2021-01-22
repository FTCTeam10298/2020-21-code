package robotCode.hardwareClasses

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import pid.MotorWithPID

class ShooterInterface(private val shooter: DcMotorEx, private val belt: DcMotor, private val gate: Servo) {
    private val shooterPID = MotorWithPID()

    val highGoalPreset = 4150
    var shooterRpm: Double = highGoalPreset.toDouble()

    fun shootOne() {
        startShooting()
        sleep(500)
        gate.position = 0.0
        belt.power = 0.0
    }

    fun startShooting() {
        goToVelocity()
        while (!isVelocityCorrect()) {
            sleep(50)
        }
        gate.position = 1.0
        belt.power = 0.8
    }


    fun goToVelocity() {
        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER;
        shooterPID.setPIDFVals(shooter, 450.0, 20.0, 0.0, 0.0)
        shooterPID.startMotorPID(shooter, shooterRpm / 60.0 * 28)
    }

    fun percentage(percent: Double, value: Double): Double = (value / 100) * percent

    fun toRPM(tps: Double): Double = tps * 60 / 28

    fun isVelocityCorrect(): Boolean = toRPM(shooter.velocity) >= shooterRpm - percentage(2.0, shooterRpm) && toRPM(shooter.velocity) <= shooterRpm + percentage(2.0, shooterRpm)

}