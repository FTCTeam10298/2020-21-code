package org.firstinspires.ftc.teamcode

import java.lang.Thread.sleep


open class AimBotMovement(): AimBotHardware() {

    fun stopMotors() {
        rFDrive?.power = 0.0
        lFDrive?.power = 0.0
        lBDrive?.power = 0.0
        rBDrive?.power = 0.0
    }

    fun fourMotors(rFpower: Double, lFpower: Double, lBpower: Double, rBpower: Double) {
        rFDrive?.power = rFpower
        lFDrive?.power = lFpower
        lBDrive?.power = lBpower
        rBDrive?.power = rBpower
    }

    fun straight(power: Double, milis: Int) {
        fourMotors(power, power, power, power)
        sleep(milis.toLong())
    }

    fun side(power: Double, milis: Int) {
        fourMotors(-power, power, -power, power)
        sleep(milis.toLong())
    }

    fun eStraight(power: Double, ticks: Double) {
        fourMotors(power, power, power, power)
        while (lFDrive!!.currentPosition > ticks) {
            stopMotors()
        }
    }


}
