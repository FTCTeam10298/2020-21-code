package robotCode.aimBotRobot

import com.qualcomm.robotcore.util.Range
import locationTracking.GlobalRobot

class OdometryDriveTrain(private val hardware: MecOdometryHardware): MecanumDriveTrain(hardware) {

    var deltaL = 0.0
    var deltaC = 0.0
    var deltaR = 0.0
    var previousC = 0.0
    var previousL = 0.0
    var previousR = 0.0
    var globalRobot: GlobalRobot = GlobalRobot(0.0, 0.0, 180.0)

    /**
     * Sets the speed of the four drive motors given desired speeds in the robot's x, y, and angle.
     * @param vX Robot speed in the x (sideways) direction.
     * @param vY Robot speed in the y (forwards) direction.
     * @param vA Robot speed in the angle (turning) direction.
     * @param minPower Minimum speed allowed for the average of the four motors.
     * @param maxPower Maximum speed allowed for the fasted of the four motors.
     */
    fun setSpeedAll(vX: Double, vY: Double, vA: Double, minPower: Double, maxPower: Double) {

        // Calculate theoretical values for motor powers using transformation matrix
        var fl = vY + vX - vA
        var bl = vY - vX - vA
        var br = vY - vX + vA
        var fr = vY + vX + vA

        // Find the largest magnitude of power and the average magnitude of power to scale down to
        // maxpower and up to minpower
        var max = Math.abs(fl)
        max = Math.max(max, Math.abs(bl))
        max = Math.max(max, Math.abs(br))
        max = Math.max(max, Math.abs(fr))
        val ave = (Math.abs(fl) + Math.abs(bl) + Math.abs(br) + Math.abs(fr)) / 4
        if (max > maxPower) {
            fl *= maxPower
            bl *= maxPower
            br *= maxPower
            fr *= maxPower
            fl /= max + 1E-6
            bl /= max + 1E-6
            br /= max + 1E-6
            fr /= max + 1E-6
        } else if (ave < minPower) {
            fl *= minPower
            bl *= minPower
            br *= minPower
            fr *= minPower
            fl /= max + 1E-6
            bl /= max + 1E-6
            br /= max + 1E-6
            fr /= max + 1E-6
        }

        // Recalculate max and scale down to 1
        max = Math.abs(fl)
        max = Math.max(max, Math.abs(bl))
        max = Math.max(max, Math.abs(br))
        max = Math.max(max, Math.abs(fr))
        if (max < 1) max = 1.0
        fl /= max + 1E-6
        bl /= max + 1E-6
        br /= max + 1E-6
        fr /= max + 1E-6

        // Range clip just to be safe
        fl = Range.clip(fl, -1.0, 1.0)
        bl = Range.clip(bl, -1.0, 1.0)
        br = Range.clip(br, -1.0, 1.0)
        fr = Range.clip(fr, -1.0, 1.0)

        // Set powers
        hardware.lBDrive.power = bl
        hardware.lFDrive.power = fl
        hardware.rFDrive.power = fr
        hardware.rBDrive.power = br
    }

    /**
     * Updates the current position (globalRobot) of the robot based off of the change in the
     * odometry encoders.
     */
    fun updatePosition() {
//        bulkData = expansionHub.getBulkInputData()
        val currentL = hardware.lOdom.currentPosition.toDouble() / 1144.0
        val currentC = hardware.cOdom.currentPosition.toDouble() / 1144.0
        val currentR = hardware.rOdom.currentPosition.toDouble() / 1144.0
        deltaL = currentL - previousL
        deltaC = currentC - previousC
        deltaR = currentR - previousR
        previousL = currentL
        previousC = currentC
        previousR = currentR
        globalRobot.updatePosition(deltaL, deltaC, deltaR)
    }
}