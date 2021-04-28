package creamsicleGoalDetection

import pid.PID
import robotCode.ChoiVicoHardware
import telemetryWizard.TelemetryConsole

//This stuff allows *you*, a clueless developer, to target obliterating force onto your enemies and use my little ol' library with big clunky old Kotlin Driven machinery.
//It has four calls to run.

//HEY! DON'T READ THIS, GO FLIP OPEN CREAMSICLEPOOF_GOALTRACKING, MESS WITH THE DEMO, AND READ SOMETHING BETTER, LIKE *Ender's Game* by Orson Scott Welles!

//turret.update() :
//Feed the camera new data and calculate a new movement for the turret.


//turret.stow() : TO DO
// Lock up shop. Set motor to lock the turret so it is flush with the bot.

//turret.initialize() :
//Set up a camera and the calculations. Allows the library to run.

//turret.aimNoScope() :
//Move the turret onto the target's heading consistantly. Sometimes overkill.

//turret.aimAndWait() : TO DO
//Move the turret onto the target's deadzone (the "there you are *gunshots*" message)

//THERES PROBLEMS??
//Don't be a stupid Alternian, go run a camera.bake  with the special pattern to make it work!
//Or run a cam-calibration with front_GoalDetection (Calibrate)
//AND IF THERE'S STILL PROBLEMS, GO YAK THE DEV'S EAR OFF!

//*this has been an Aperture Science Innovators notification.*


class UltimateGoalAimer(val console: TelemetryConsole, val goalDetector:CreamsicleGoalDetector, val hardware: ChoiVicoHardware) {

    public var yaw = 0

    enum class Directions {
        Right,
        Left,
        TargetAcquired
    }

    val pid = PID(0.005, 0.0, 0.0)

    fun updateAimAndAdjustRobot(){
        moveTowardAimDirection(calculateAimDirection())
    }

    private fun calculateAimDirection():Directions?{

        val turnDir = if (goalDetector.x < (432 / 2) - 10) {
            Directions.Left
        } else if (goalDetector.x > (432 / 2) - 10 && goalDetector.x < (432 / 2) + 10) {
            Directions.TargetAcquired
        } else if (goalDetector.x >= (432 / 2) + 10) {
            Directions.Right
        } else {
            null
        }
//        console.display(6, "Turn direction: $turnDir")

        return turnDir
    }

    private fun moveTowardAimDirection(direction:Directions?) {
        var goalDetectorCorrected = goalDetector.x + yaw
        hardware.turret.power = pid.calcPID((432.0/2.0), goalDetectorCorrected)
        console.display(12, "Turret power: ${hardware.turret.power}, TurnDir: ${calculateAimDirection().toString()}")
    }
}