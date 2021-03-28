package locationTracking

import kotlin.math.cos
import kotlin.math.sin

class GlobalRobot(x: Double, y: Double, a: Double) : Coordinate(x, y, a) {
    var ynot = 0.315 //.315
    var xnot = 14.756661709 / 2.0

    /**
     * Update the robot's global coordinates with inputs of the change in the encoders.
     * @param deltaL Change in the left encoder.
     * @param deltaC Change in the center encoder.
     * @param deltaR Change in the right encoder.
     */
    fun updatePosition(deltaL: Double, deltaC: Double, deltaR: Double) {
        var robotX: Double = x
        var robotY: Double = y
        var robotR: Double = r

        robotR += 1 / (2 * xnot) * (deltaL - deltaR)

        val deltaY = .5 * (deltaR + deltaL)
        val deltaX = ynot / (2 * xnot) * (deltaL - deltaR) + deltaC

        robotX += deltaX * cos(robotR) + deltaY * sin(robotR)
        robotY += deltaX * sin(robotR) + deltaY * cos(robotR)

        x = robotX
        y = robotY
        r = robotR % (2 * Math.PI)

        if (r > Math.PI)
            r -= 2 * Math.PI
    }
}
