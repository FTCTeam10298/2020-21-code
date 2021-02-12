package robotCode.hardwareClasses

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.Range
import telemetryWizard.TelemetryConsole
import locationTracking.Coordinate
import pid.PID
import kotlin.math.*

class OdometryDriveMovement(private val console: TelemetryConsole, private val hardware: MecOdometryHardware): DriveMovement, OdometryDriveTrain(hardware, console) {

    enum class State {
        Running,
        Done
    }

    private var prevErrorX = 0.0
    private var prevErrorY = 0.0
    private var prevErrorA = 0.0
    private var sumErrorX = 0.0
    private var sumErrorY = 0.0
    private var sumErrorA = 0.0
    var current = Coordinate(0.0, 0.0, 0.0)

    fun reset() {
        // Start by setting all speeds and error values to 0 and moving into the next state
        drivePowerZero()
        prevErrorX = 0.0
        prevErrorY = 0.0
        prevErrorA = 0.0
        sumErrorX = 0.0
        sumErrorY = 0.0
        sumErrorA = 0.0
    }

    fun goToPosition(
            target: Coordinate,
            maxPower: Double,
            distancePID: PID,
            anglePID: PID,
            distanceMin: Double,
            angleDegMin: Double,
    ): State {

        // Set the current position
        current.setCoordinate(
                globalRobot.x,
                globalRobot.y,
                Math.toDegrees(globalRobot.r)
        )

        // Find the error in distance and angle, ensuring angle does not exceed 2*Math.PI
        val distanceError = hypot(
                current.x - target.x,
                current.y - target.y
        )

        var angleError: Double = target.r - current.r

        while (angleError > 180)
            angleError -= 360.0

        while (angleError < -180)
            angleError += 360.0

        if (angleError > Math.PI)
            angleError -= 2 * Math.PI

        // Find the absolute angle error
        val absAngleError: Double =
                atan2(target.y - current.y, target.x - current.x) - current.r

        // Convert the largest allowed error into radians to use in calculations
        val angleMin = Math.toRadians(angleDegMin)

        // Check to see if we've reached the desired position already
        if (distanceError <= distanceMin && abs(angleError) <= angleMin) {
            return State.Done
        }

        // Calculate the error in x and y and use the PID to find the error in angle

        distancePID.calcPID(distanceError)
        val dx: Double = -sin(absAngleError) * distancePID.p * (10.0 / 7.0) // Constant to scale strafing up
        val dy: Double = cos(absAngleError) * distancePID.p

        anglePID.calcPID(angleError)
        val da: Double = anglePID.p

//        I and D terms are not being currently used

//        sumErrorX += robot.getElapsedTime() * errx;
//        if (sumErrorX > sumMaxD)
//            sumErrorX = sumMaxD;
//        if (sumErrorY > sumMaxD)
//            sumErrorY = sumMaxD;
//        if (sumErrorA > sumMaxA)
//            sumErrorA = sumMaxA;
//        dx += sumErrorX * distancePID.getInteg();
//        dy += sumErrorY * distancePID.getInteg();
//        da += sumErrorA * anglePID.getInteg();
//        dx += (errx - prevErrorX) * distancePID.getDeriv()/ getElapsedTime();
//        dy += (erry - prevErrorY) * distancePID.getDeriv()/ getElapsedTime();
//        da += (angleError - prevErrorA) * anglePID.getDeriv()/ getElapsedTime();
//        val dTotal = abs(dx) + abs(dy) + 1E-6

        val newSpeedx = Range.clip(dx, -1.0, 1.0) // / dTotal;
        val newSpeedy = Range.clip(dy, -1.0, 1.0) // / dTotal;
        val newSpeedA = Range.clip(da, -1.0, 1.0)

        console.display(5, "Target Robot X, Error X: ${target.x}, $dx")
        console.display(6, "Target Robot Y, Error Y: ${target.y}, $dy")
        console.display(7, "Distance Error: $distanceError")
        console.display(8, "Current X,Y,A: ${current.x}, ${current.y}, ${Math.toDegrees(current.r)}")
        console.display(9, "angleError, target angle: ${Math.toDegrees(angleError)}, ${Math.toDegrees(target.r)}")
        console.display(10, "absAngleError: ${Math.toDegrees(absAngleError)}")
        console.display(11, "Raw L, Raw C, Raw R: ${hardware.lOdom.currentPosition}, ${hardware.cOdom.currentPosition}, ${hardware.rOdom.currentPosition}")
        console.display(12, "Speedx, SpeedY, SpeedA $newSpeedx, $newSpeedy, $newSpeedA")

        setSpeedAll(newSpeedx, newSpeedy, newSpeedA, .16, maxPower)

        return State.Running
    }

    /**
     * Executes goToPosition in LinearOpMode. Uses a while loop to continue updating position and
     * error to drive.
     * @param target The target Coordinate to drive to.
     * @param maxPower The maximum power allowed on the drive motors.
     * @param distancePID The PID for the x-y error.
     * @param anglePID The PID for the theta error.
     * @param distanceMin The minimum allowed distance away from the target to terminate.
     * @param angleDegMin The minimum allowed angle away from the target to terminate.
     * @param reset The current State of the robot.
     * @param opmode The LinearOpMode that this call is in. Used to tell if opModeIsActive
     * so that stopping mid-loop doesn't cause an error.
     * @return The new State of the robot.
     */
    override fun doGoToPosition(
            target: Coordinate,
            maxPower: Double,
            distancePID: PID,
            anglePID: PID,
            distanceMin: Double,
            angleDegMin: Double,
            reset: Boolean,
            opmode: LinearOpMode
    ) {
        if (reset)
            reset()

        var state = State.Running
        while (state != State.Done && opmode.opModeIsActive()){
            state = goToPosition(target, maxPower, distancePID, anglePID, distanceMin, angleDegMin)
            updatePosition()
        }

        drivePowerZero()
        updatePosition()
    }

    /**
     * Executes DoGoToPosition with set PIDs optimized for straight driving.
     * @param target The target Coordinate to drive to.
     * @param maxPower The maximum power allowed on the drive motors.
     * @param distanceMin The minimum allowed distance away from the target to terminate.
     * @param opmode The LinearOpMode that this call is in. Used to tell if opModeIsActive
     * so that stopping mid-loop doesn't cause an error.
     */
    override fun straightGoToPosition(
            target: Coordinate,
            maxPower: Double,
            distanceMin: Double,
            opmode: LinearOpMode
    ) {
        doGoToPosition(
                target,
                maxPower,
                PID(.1, 0.0, 0.0),
                PID(2.0, 0.0, 0.0),
                distanceMin,
                5.0,
                true,
                opmode
        )
    }

    /**
     * Executes DoGoToPosition with set PIDs optimized for straight driving.
     * @param target The target Coordinate to drive to.
     * @param maxPower The maximum power allowed on the drive motors.
     * @param angleDegMin The minimum allowed distance away from the target to terminate.
     * @param opmode The LinearOpMode that this call is in. Used to tell if opModeIsActive
     * so that stopping mid-loop doesn't cause an error.
     */
    override fun turnGoToPosition(
            target: Coordinate,
            maxPower: Double,
            angleDegMin: Double,
            opmode: LinearOpMode
    ) {
        doGoToPosition(
                target,
                maxPower,
                PID(0.01, 0.0, 0.0),
                PID(.5, 0.0, 0.0),
                8.0,
                angleDegMin,
                true,
                opmode
        )
    }


    override fun driveRobotTime(ms: Int, power: Double) {
        TODO("Not yet implemented")
    }

    override fun driveRobotDistanceToObject(power: Double, inches: Double, smartAccel: Boolean) {
        TODO("Not yet implemented")
    }

    override fun driveRobotPosition(power: Double, inches: Double, smartAccel: Boolean) {
        TODO("Not yet implemented")
    }

    override fun driveRobotTurn(power: Double, degree: Double, smartAccel: Boolean) {
        TODO("Not yet implemented")
    }

    override fun driveRobotStrafe(power: Double, inches: Double, smartAccel: Boolean) {
        TODO("Not yet implemented")
    }

    override fun driveSidewaysTime(time: Double, power: Double) {
        TODO("Not yet implemented")
    }

    override fun driveRobotHug(power: Double, inches: Int, hugLeft: Boolean) {
        TODO("Not yet implemented")
    }

    override fun driveRobotArc(power: Double, inches: Double, difference: Double) {
        TODO("Not yet implemented")
    }

    override fun driveRobotArcStrafe(power: Double, inches: Double, difference: Double) {
        TODO("Not yet implemented")
    }

}
