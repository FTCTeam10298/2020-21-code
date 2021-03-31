package locationTracking

import kotlin.math.cos
import kotlin.math.sin

class GlobalRobot(x: Double, y:Double, r:Double) : Coordinate(x, y, r) {
    var forwardOffset = 0
    var trackwidth = 14.756661709

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

        val deltaX = deltaMiddle * cos(r) - deltaPerp * sin(r)
        val deltaY = deltaMiddle * sin(r) + deltaPerp * cos(r)

        x += deltaY
        y += deltaX
        r += deltaAngle

        r %= (2 * Math.PI)
        if (r > Math.PI)
            r -= 2 * Math.PI
    }
}
