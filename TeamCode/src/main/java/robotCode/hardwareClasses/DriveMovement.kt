package robotCode.hardwareClasses

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import locationTracking.Coordinate
import pid.PID

interface DriveMovement {
    /**
     * DriveRobotTime drives the robot the set number of inches at the given power level.
     * @param ms How long to drive
     * @param power Power level to set motors to, negative will drive the robot backwards
     */
    fun driveRobotTime(ms: Int, power: Double)

    /**
     * DriveRobotDistanceToObject drives the robot to the set number of inches from an object
     * (usually the wall) at the given power level.
     * @param inches How many inches away to the object to go to
     * @param power Power level to set motors to
     */
    fun driveRobotDistanceToObject(power: Double, inches: Double, smartAccel: Boolean)

    /**
     * DriveRobotPosition drives the robot the set number of inches at the given power level.
     * @param inches How far to drive, can be negative
     * @param power Power level to set motors to
     */
    fun driveRobotPosition(power: Double, inches: Double, smartAccel: Boolean)


    fun driveRobotTurn(power: Double, degree: Double, smartAccel: Boolean = false)

    /**
     * DriveRobotStrafe drives the robot the set number of inches at the given power level.
     * @param inches How far to drive, can be negative
     * @param power Power level to set motors to
     */
    fun driveRobotStrafe(power: Double, inches: Double, smartAccel: Boolean)

    /**
     * DriveSidewaysTime makes the robot drive sideways for the specified time and power.
     * @param time How long to drive in seconds
     * @param power The power to use while driving,
     * positive values go right and negative values go left
     */
    fun driveSidewaysTime(time: Double, power: Double)

    /**
     * DriveRobotHug is used to make the robot drive hugging a wall.
     * The robot will move mostly straight and slightly to the side,
     * so it will stay against the wall.
     * @param power Power to use while driving
     * @param inches How many inches to drive
     * @param hugLeft Whether to hug left or right
     */
    fun driveRobotHug(power: Double, inches: Int, hugLeft: Boolean)

    fun driveRobotArc(power: Double, inches: Double, difference: Double)

    fun driveRobotArcStrafe(power: Double, inches: Double, difference: Double)

    /**
     * Executes goToPosition in LinearOpMode. Uses a while loop to continue updating position and
     * error to drive.
     * @param target The target Coordinate to drive to.
     * @param maxPower The maximum power allowed on the drive motors.
     * @param distancePID The PID for the x-y error.
     * @param anglePID The PID for the theta error.
     * @param distanceMin The minimum allowed distance away from the target to terminate.
     * @param angleDegMin The minimum allowed angle away from the target to terminate.
     * @param state The current State of the robot.
     * @param opmodeisactive The LinearOpMode that this call is in. Used to tell if opModeIsActive
     * so that stopping mid-loop doesn't cause an error.
     * @return The new State of the robot.
     */
    fun doGoToPosition(target: Coordinate,
                       maxPower: Double,
                       distancePIDX: PID,
                       distancePIDY: PID,
                       anglePID: PID,
                       distanceMin: Double,
                       angleDegMin: Double,
                       reset: Boolean,
                       opmode: LinearOpMode
    )

    /**
     * Executes DoGoToPosition with set PIDs optimized for straight driving.
     * @param target The target Coordinate to drive to.
     * @param maxPower The maximum power allowed on the drive motors.
     * @param distanceMin The minimum allowed distance away from the target to terminate.
     * @param opmodeisactive The LinearOpMode that this call is in. Used to tell if opModeIsActive
     * so that stopping mid-loop doesn't cause an error.
     */
    fun straightGoToPosition(target: Coordinate,
                             maxPower: Double,
                             distanceMin: Double,
                             opmodeisactive: LinearOpMode
    )

}