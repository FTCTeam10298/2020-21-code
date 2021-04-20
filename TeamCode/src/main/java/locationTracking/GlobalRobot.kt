package locationTracking

import kotlin.math.cos
import kotlin.math.sin

class GlobalRobot(x: Double, y: Double, r: Double) : Coordinate(x, y, r) {

    val forwardOffset = 0
    val trackwidth = 15

    /**
     * Update the robot's global coordinates with inputs of the change in the encoders.
     * @param deltaL Change in the left encoder.
     * @param deltaC Change in the center encoder.
     * @param deltaR Change in the right encoder.
     */
    fun updatePosition(deltaL: Double, deltaC: Double, deltaR: Double) {
        val deltaAngle = (deltaL - deltaR) / trackwidth
        val deltaMiddle = (deltaL + deltaR) / 2
        val deltaPerp = deltaC - forwardOffset * deltaAngle

        val deltaY = cos(r) * deltaMiddle - sin(r) * deltaPerp
        val deltaX = sin(r) * deltaMiddle + cos(r) * deltaPerp

        x += deltaX
        y += deltaY
        r += deltaAngle

//        r %= (2 * Math.PI)
//        if (r > Math.PI)
//            r -= 2 * Math.PI
    }
}