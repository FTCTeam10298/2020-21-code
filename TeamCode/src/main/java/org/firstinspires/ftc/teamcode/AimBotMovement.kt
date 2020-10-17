package org.firstinspires.ftc.teamcode



open class AimBotMovement(): AimBotHardware() {
    fun fourMotors(rFpower: Double, lFpower: Double, lBpower: Double, rBpower: Double) {
        rFDrive?.power = rFpower
        lFDrive?.power = lFpower
        lBDrive?.power = lBpower
        rBDrive?.power = rBpower
    }
    fun straight(power: Double){
        fourMotors(power, power, power, power)
    }
}
