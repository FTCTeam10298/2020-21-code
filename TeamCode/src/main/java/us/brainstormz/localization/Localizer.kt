package us.brainstormz.localization

import com.qualcomm.robotcore.hardware.DcMotor
import locationTracking.Coordinate

class Localizer(
    val lOdom: DcMotor,
    val rOdom: DcMotor,
    val cOdom: DcMotor) {

    val ticksPerRotation = 8192
    val rotationsPerInch = 4.3290


    var deltaL = 0.0
    var deltaC = 0.0
    var deltaR = 0.0
    var previousC = 0.0
    var previousL = 0.0
    var previousR = 0.0


    var globalRobot = GlobalRobot(0.0, 0.0, 0.0)

    /**
     * Updates the current position (globalRobot) of the robot based off of the change in the
     * odometry encoders.
     */

    fun updatePosition() {

//        bulkData = expansionHub.getBulkInputData()
        val currentL = -lOdom.currentPosition.toDouble() / (ticksPerRotation / rotationsPerInch)
        val currentR = -rOdom.currentPosition.toDouble() / (ticksPerRotation / rotationsPerInch)
        val currentC = cOdom.currentPosition.toDouble() / (ticksPerRotation / rotationsPerInch)

        deltaL = currentL - previousL
        deltaR = currentR - previousR
        deltaC = currentC - previousC

        previousL = currentL
        previousR = currentR
        previousC = currentC

        globalRobot.updatePosition(deltaL, deltaC, deltaR)
    }

    fun updatePosition(deltaL: Double, deltaC: Double, deltaR: Double) {
        this.globalRobot.updatePosition(
            deltaL = deltaL,
            deltaC = deltaC,
            deltaR = deltaR
        )
    }
    fun setCoordinate(x: Double? = null, y: Double? = null, r: Double? = null){
        this.globalRobot.setCoordinate(x=x, y=y, r=r)
    }

    var current:Coordinate = globalRobot
}