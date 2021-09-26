package us.brainstormz.localization

import com.qualcomm.robotcore.hardware.DcMotor
import locationTracking.Coordinate
import kotlin.math.cos
import kotlin.math.sin

class OdometryLocalizer(
    private val lOdom: DcMotor,
    private val rOdom: DcMotor,
    private val cOdom: DcMotor) : Localizer {


    private val forwardOffset = 0
    private val trackwidth = 15

    private val ticksPerRotation = 8192
    private val rotationsPerInch = 4.3290

    private var deltaL = 0.0
    private var deltaC = 0.0
    private var deltaR = 0.0
    private var previousC = 0.0
    private var previousL = 0.0
    private var previousR = 0.0

    private var current = Coordinate(0.0, 0.0, 0.0)

    override fun currentPositionAndRotation() = current

    /**
     * Updates the current position (globalRobot) of the robot based off of the change in the
     * odometry encoders.
     */

    override fun recalculatePositionAndRotation() {

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

        recalculatePositionAndRotation(deltaL, deltaC, deltaR, current)
    }

    override fun setPositionAndRotation(x: Double?, y: Double?, r: Double?){
        this.current.setCoordinate(x=x, y=y, r=r)
    }

    /**
     * Update the robot's global coordinates with inputs of the change in the encoders.
     * @param deltaL Change in the left encoder.
     * @param deltaC Change in the center encoder.
     * @param deltaR Change in the right encoder.
     */
    private fun recalculatePositionAndRotation(deltaL: Double, deltaC: Double, deltaR: Double, previous:Coordinate) {
        val deltaAngle = (deltaL - deltaR) / trackwidth
        val deltaMiddle = (deltaL + deltaR) / 2
        val deltaPerp = deltaC - forwardOffset * deltaAngle

        val deltaY = cos(previous.r) * deltaMiddle - sin(previous.r) * deltaPerp
        val deltaX = sin(previous.r) * deltaMiddle + cos(previous.r) * deltaPerp

        previous.x += deltaX
        previous.y += deltaY
        previous.r += deltaAngle

//        r %= (2 * Math.PI)
//        if (r > Math.PI)
//            r -= 2 * Math.PI
    }
}