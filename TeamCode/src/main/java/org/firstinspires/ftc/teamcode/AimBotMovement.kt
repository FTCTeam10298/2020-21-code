package org.firstinspires.ftc.teamcode

import java.lang.Thread.sleep


open class AimBotMovement(): AimBotHardware() {
    fun fourMotors(rFpower: Double, lFpower: Double, lBpower: Double, rBpower: Double) {
        rFDrive?.power = rFpower
        lFDrive?.power = lFpower
        lBDrive?.power = lBpower
        rBDrive?.power = rBpower
    }
    fun straight(power: Double, time: Int){
        fourMotors(power, power, power, power)
        sleep(time.toLong())
    }
    fun side(power: Double, milis: Int){
        fourMotors(-power, power, -power, power)
        sleep(milis.toLong())
    }
}
