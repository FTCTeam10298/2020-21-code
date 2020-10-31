package robotCode
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.robotcore.util.Range
//import hallib.HalDashboard
//
//class RoboMovement : Olivanie_v3_Hardware() {
//    var menu: HalDashboard = HalDashboard.getInstance()
//
//    internal enum class State {
//        INIT, BUSY, DONE, TIMEOUT
//    }
//
//    private var prevErrorX = 0.0
//    private var prevErrorY = 0.0
//    private var prevErrorA = 0.0
//    private var sumErrorX = 0.0
//    private var sumErrorY = 0.0
//    private var sumErrorA = 0.0
//    private val sumMaxD = 1.0
//    private val sumMaxA = 1.0
//    var current: Coordinate = Coordinate(0, 0, 0)
//
//    /**
//     * Sets the motor powers to the correct power to go to the target position.
//     * @param target The target Coordinate to drive to.
//     * @param maxPower The maximum power allowed on the drive motors.
//     * @param distancePID The PID for the x-y error.
//     * @param anglePID The PID for the theta error.
//     * @param distanceMin The minimum allowed distance away from the target to terminate.
//     * @param angleDegMin The minimum allowed angle away from the target to terminate.
//     * @param state The current State of the robot.
//     * @return The new State of the robot.
//     */
//    fun goToPosition(target: Coordinate, maxPower: Double, distancePID: PID,
//                     anglePID: PID, distanceMin: Double, angleDegMin: Double, state: org.firstinspires.ftc.teamcode.RoboMovement.State): org.firstinspires.ftc.teamcode.RoboMovement.State {
//        // Start by setting all speeds and error values to 0 and moving into the next state
//        var state: org.firstinspires.ftc.teamcode.RoboMovement.State = state
//        if (state == org.firstinspires.ftc.teamcode.RoboMovement.State.INIT) {
//            setSpeedZero()
//            prevErrorX = 0.0
//            prevErrorY = 0.0
//            prevErrorA = 0.0
//            sumErrorX = 0.0
//            sumErrorY = 0.0
//            sumErrorA = 0.0
//            state = org.firstinspires.ftc.teamcode.RoboMovement.State.BUSY
//        } else if (state == org.firstinspires.ftc.teamcode.RoboMovement.State.BUSY) {
//            // Set the current position
//            current.setCoordinate(globalRobot.getX(), globalRobot.getY(),
//                    Math.toDegrees(globalRobot.getAngle()))
//            // Find the error in distance and angle, ensuring angle does not exceed 2*Math.PI
//            val distanceError = Math.hypot(current.getX() - target.getX(), current.getY() -
//                    target.getY())
//            var angleError: Double = target.getAngle() - current.getAngle()
//            while (angleError > 180) angleError -= 360.0
//            while (angleError < -180) angleError += 360.0
//            if (angleError > Math.PI) angleError -= 2 * Math.PI
//            // Find the absolute angle error
//            val absAngleError: Double = (Math.atan2(target.getY() - current.getY(), target.getX() -
//                    current.getX())
//                    - current.getAngle())
//            // Convert the largest allowed error into radians to use in calculations
//            val angleMin = Math.toRadians(angleDegMin)
//            // Check to see if we've reached the desired position already
//            if (distanceError <= distanceMin && Math.abs(angleError) <= angleMin) {
//                state = org.firstinspires.ftc.teamcode.RoboMovement.State.DONE
//            }
//            // Calculate the error in x and y and use the PID to find the error in angle
//            val errx = -Math.sin(absAngleError) * distanceError
//            val erry = Math.cos(absAngleError) * distanceError
//            val dx: Double = errx * distancePID.getPropo() * (10.0 / 7.0) // Constant to scale strafing up
//            val dy: Double = erry * distancePID.getPropo()
//            val da: Double = angleError * anglePID.getPropo()
//            menu = HalDashboard.getInstance()
//            menu.displayPrintf(5, "Target Robot X, Error X: %f, %f",
//                    target.getX(), errx)
//            menu.displayPrintf(6, "Target Robot Y, Error Y: %f, %f",
//                    target.getY(), erry)
//            menu.displayPrintf(7, "Distance Error: %f", distanceError)
//            menu.displayPrintf(8, "Current X,Y,A: %f, %f, %f", current.getX(),
//                    current.getY(), Math.toDegrees(current.getAngle()))
//            menu.displayPrintf(9, "angleError, target angle: %f, %f",
//                    Math.toDegrees(angleError), Math.toDegrees(target.getAngle()))
//            menu.displayPrintf(10, "absAngleError: %f",
//                    Math.toDegrees(absAngleError))
//            menu.displayPrintf(11, "Raw L, Raw C, Raw R: %d, %d, %d",
//                    rightCollector.getCurrentPosition(), leftCollector.getCurrentPosition(),
//                    tape.getCurrentPosition())
//
//            // I and D terms are not being currently used
//
////            sumErrorX += robot.getElapsedTime() * errx;
////            if (sumErrorX > sumMaxD)
////                sumErrorX = sumMaxD;
////            if (sumErrorY > sumMaxD)
////                sumErrorY = sumMaxD;
////            if (sumErrorA > sumMaxA)
////                sumErrorA = sumMaxA;
////            dx += sumErrorX * distancePID.getInteg();
////            dy += sumErrorY * distancePID.getInteg();
////            da += sumErrorA * anglePID.getInteg();
////            dx += (errx - prevErrorX) * distancePID.getDeriv()/ getElapsedTime();
////            dy += (erry - prevErrorY) * distancePID.getDeriv()/ getElapsedTime();
////            da += (angleError - prevErrorA) * anglePID.getDeriv()/ getElapsedTime();
//            val dTotal = Math.abs(dx) + Math.abs(dy) + 1E-6
//            val newSpeedx = Range.clip(dx, -1.0, 1.0) // / dTotal;
//            val newSpeedy = Range.clip(dy, -1.0, 1.0) // / dTotal;
//            val newSpeedA = Range.clip(da, -1.0, 1.0)
//            menu.displayPrintf(12, "Speedx, SpeedY, SpeedA %f, %f, %f",
//                    newSpeedx, newSpeedy, newSpeedA)
//            setSpeedAll(newSpeedx, newSpeedy, newSpeedA, .16, maxPower)
//        } else if (state == org.firstinspires.ftc.teamcode.RoboMovement.State.DONE) {
//            setSpeedZero()
//        }
//        return state
//    }
//
//    /**
//     * Executes goToPosition in LinearOpMode. Uses a while loop to continue updating position and
//     * error to drive.
//     * @param target The target Coordinate to drive to.
//     * @param maxPower The maximum power allowed on the drive motors.
//     * @param distancePID The PID for the x-y error.
//     * @param anglePID The PID for the theta error.
//     * @param distanceMin The minimum allowed distance away from the target to terminate.
//     * @param angleDegMin The minimum allowed angle away from the target to terminate.
//     * @param state The current State of the robot.
//     * @param opmodeisactive The LinearOpMode that this call is in. Used to tell if opModeIsActive
//     * so that stopping mid-loop doesn't cause an error.
//     * @return The new State of the robot.
//     */
//    fun DoGoToPosition(target: Coordinate, maxPower: Double,
//                       distancePID: PID, anglePID: PID, distanceMin: Double,
//                       angleDegMin: Double, state: org.firstinspires.ftc.teamcode.RoboMovement.State, opmodeisactive: LinearOpMode): org.firstinspires.ftc.teamcode.RoboMovement.State {
//        var current: org.firstinspires.ftc.teamcode.RoboMovement.State = state
//        while (current != org.firstinspires.ftc.teamcode.RoboMovement.State.DONE && current != org.firstinspires.ftc.teamcode.RoboMovement.State.TIMEOUT && opmodeisactive.opModeIsActive()) {
//            updatePosition()
//            current = goToPosition(target, maxPower, distancePID, anglePID, distanceMin,
//                    angleDegMin, current)
//        }
//        setSpeedZero()
//        updatePosition()
//        return current
//    }
//
//    /**
//     * Executes DoGoToPosition with set PIDs optimized for straight driving.
//     * @param target The target Coordinate to drive to.
//     * @param maxPower The maximum power allowed on the drive motors.
//     * @param distanceMin The minimum allowed distance away from the target to terminate.
//     * @param opmodeisactive The LinearOpMode that this call is in. Used to tell if opModeIsActive
//     * so that stopping mid-loop doesn't cause an error.
//     */
//    fun StraightGoToPosition(target: Coordinate, maxPower: Double,
//                             distanceMin: Double, opmodeisactive: LinearOpMode) {
//        DoGoToPosition(target, maxPower, PID(.1, 0, 0),
//                PID(2, 0, 0), distanceMin, 5.0, org.firstinspires.ftc.teamcode.RoboMovement.State.INIT, opmodeisactive)
//    }
//
//    /**
//     * Executes DoGoToPosition with set PIDs optimized for straight driving.
//     * @param target The target Coordinate to drive to.
//     * @param maxPower The maximum power allowed on the drive motors.
//     * @param angleDegMin The minimum allowed distance away from the target to terminate.
//     * @param opmodeisactive The LinearOpMode that this call is in. Used to tell if opModeIsActive
//     * so that stopping mid-loop doesn't cause an error.
//     */
//    fun TurnGoToPosition(target: Coordinate, maxPower: Double,
//                         angleDegMin: Double, opmodeisactive: LinearOpMode) {
//        DoGoToPosition(target, maxPower, PID(0.01, 0, 0),
//                PID(.5, 0, 0), 8.0, angleDegMin, org.firstinspires.ftc.teamcode.RoboMovement.State.INIT, opmodeisactive)
//    }
//}