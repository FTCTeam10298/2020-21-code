package locationTracking

import kotlin.math.hypot

/**
 * Creates a Coordinate with given values. All alternate constructors assume 0 for unstated variables.
 * @param x X position
 * @param y Y position
 * @param r Angle, in radians
 */
open class Coordinate(var x: Double = 0.0, var y: Double = 0.0, var r: Double = 0.0) {

    /**
     * Sets the parameters of the Coordinate.
     * @param x The x value that we want to set.
     * @param y The y value that we want to set.
     * @param r The angle that we want to set in degrees
     */
    fun setCoordinate(x: Double? = null, y: Double? = null, r: Double? = null) {
        if (x != null)
            this.x = x

        if (y != null)
            this.y = y

        if (r != null) {
            var rAbs:Double = r
            while (rAbs < -Math.PI)
                rAbs += (Math.PI * 2)

            this.r = rAbs
        }
    }

    /**
     * Wraps the angle around so that the robot doesn't unnecessarily turn over 180 degrees.
     * @param angle The angle to wrap.
     * @return The wrapped angle.
     */
    fun wrapAngle(angle: Double): Double {
        return angle % (2 * Math.PI)
    }

    /**
     * Gives the absolute value of the distance between the given Coordinate and the current Coordinate.
     * @param coordinate Coordinate to compare
     * @return distance from current Coordinate
     */
    fun distance(coordinate: Coordinate): Double {
        return hypot(coordinate.x - x, coordinate.y - y)
    }

    /**
     * Gives the absolute value of the distance between the X and Y values and the current Coordinate.
     * @param targetX X
     * @param targetY Y
     * @return distance from current Coordinate
     */
    fun distance(targetX: Double, targetY: Double): Double {
        return hypot(targetX - x, targetY - y)
    }

    /**
     * Gives the error of the angle from the given angle and the current Coordinate.
     * @param targetA angle to compare
     * @return angle error from current Coordinate
     */
    fun theta(targetA: Double): Double {
        return wrapAngle(targetA - r)
    }

    /**
     * Gives the error of the angle from the given Coordinate and the current Coordinate.
     * @param coordinate Coordinate to compare
     * @return angle error from current Coordinate
     */
    fun theta(coordinate: Coordinate): Double {
        return theta(coordinate.r)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun toString(): String {
        return "X: $x\nY: $y\nAngle: $r"
    }

    override fun hashCode(): Int = x.hashCode() + y.hashCode() + r.hashCode()

}
