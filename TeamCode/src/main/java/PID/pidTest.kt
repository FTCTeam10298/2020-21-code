package PID

import com.qualcomm.robotcore.hardware.DcMotor

fun main() {
    val pid: PID = PID()
    var plant: Double = 0.0

    val MotorWithPID= MotorWithPID()
    var motorPlant= 0.0

    while (true) {
        println(plant)
        plant = pid.calcPID(plant, 21.0)
    }
}
