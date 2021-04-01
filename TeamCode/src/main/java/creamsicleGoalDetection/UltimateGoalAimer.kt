package creamsicleGoalDetection

import org.opencv.core.Mat
import robotCode.hardwareClasses.MecanumDriveTrain
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
//AND IF THERE'S STILL PROBLEMS, GO YAK THE DEV'S EAR OFF!

//*this has been an Aperture Science Innovators notification.*


class UltimateGoalAimer(val console: TelemetryConsole, val drivetrain: MecanumDriveTrain, val goalDetector:CreamsicleGoalDetector) {
    enum class Directions {
        Right,
        Left,
        TargetAcquired
    }

    fun updateAimAndAdjustRobot(){
        moveTowardAimDirection(calculateAimDirection())
    }

    private fun calculateAimDirection():Directions?{
        console.display(1, "start")

        val turnDir = if (goalDetector.x < 245) {
            Directions.Right
        }else if (goalDetector.x > 245 && goalDetector.x < 255) {
            Directions.TargetAcquired
        }else if(goalDetector.x >= 255) {
            Directions.Left
        }else{
            null
        }
        console.display(7, "Turn direction: $turnDir")

        return turnDir
    }

    private fun moveTowardAimDirection(direction:Directions?) {

        console.display(12, direction.toString())

        when(direction){
            Directions.Left -> drivetrain.driveSetPower(-0.3, 0.3, -0.3, 0.3)
            Directions.Right -> drivetrain.driveSetPower(0.3, -0.3, 0.3, 0.3)
            Directions.TargetAcquired -> drivetrain.driveSetPower(0.0, 0.0, 0.0, 0.0)
            else -> console.display(1, "No direction!!! Fixme!!!")
        }

    }
}
