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
//Don't be a stupid Alternian, go run a camera.bake with the special pattern to make it work!
//Or run a cam-calibration with front_GoalDetection (Calibrate)
//AND IF THERE'S STILL PROBLEMS, GO YAK THE DEV'S EAR OFF!?

//*this has been an Aperture Science Innovators notification.*


class UltimateGoalAimer(val console: TelemetryConsole, val goalDetector:CreamsicleGoalDetector, val hardware: ChoiVicoHardware) {
    enum class Directions {
        Right,
        Left,
        TargetAcquired
    }

    var turretPower: Double = 0.0
    val pid = PID(0.005, 0.0, 0.0)

    fun updateAimAndAdjustRobot(){
        moveTowardAimDirection(calculateAimDirection())
    }

    private fun calculateAimDirection():Directions?{
//        console.display(1, "start")

        val turnDir = if (goalDetector.x < 140) {
            Directions.Left
        }else if (goalDetector.x > 140 && goalDetector.x < 180) {
            Directions.TargetAcquired
        }else if(goalDetector.x >= 180) {
            Directions.Right
        }else{
            null
        }
//        console.display(6, "Turn direction: $turnDir")

        return turnDir
    }

    private fun moveTowardAimDirection(direction:Directions?) {

        console.display(12, direction.toString())

        turretPower = pid.calcPID(160.0, goalDetector.x)

//        when(direction){
//            Directions.Left -> hardware.turret.power = stockPower
//            Directions.Right -> hardware.turret.power = stockPower
//            Directions.TargetAcquired -> hardware.turret.power = 0.0
////            else -> console.display(1, "No direction!!! fix me!!!")
//        }
        hardware.turret.power = turretPower

    }
}