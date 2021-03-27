//This code was written by your friendly Aperture Science Consumer Associate Gabriel Fergesen, who would like to remind you that only the truest peace lovers can make a living writing code to slaughter people garishly. Join the Turret Project today!

//This stuff allows *you*, a clueless developer, to target obliterating force onto your enemies.
//It has four calls to run.

//creamsicle.turret.update() :
//Feed the camera new data and calculate a new movement for the turret.


//turret.stow() :
// Lock up shop. Set motor to lock the turret so it is flush with the bot.

//turret.initialize() :
//Set up a camera and the calculations. Allows the library to run.

//turret.aimNoScope() :
//Move the turret onto the target's heading consistantly. Sometimes overkill.

//turret.aimAndWait() :
//Move the turret onto the target's deadzone (the "there you are *gunshots*" message)

//THERES PROBLEMS??
//Don't be a stupid Alternian, go run a camera.bake with the special pattern to make it work!
//Or run a cam-calibration with front_GoalDetection (Calibrate)
//AND IF THERE'S STILL PROBLEMS, GO YAK THE DEV'S EAR OFF!

//*this has been an Aperture Science Innovators notification.*



package goalDetection2

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import goalDetection.OpencvAbstraction
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import robotCode.hardwareClasses.EncoderDriveMovement
import telemetryWizard.TelemetryConsole

class CreamsicleScoop_GoalTracking(private val console: TelemetryConsole, private val opmode: OpMode){

    val font = Imgproc.FONT_HERSHEY_COMPLEX
    val opencv = OpencvAbstraction(opmode)
    val XbuttonHelper = ButtonHelper()
    val YbuttonHelper = ButtonHelper()
    val DpadHelper = ButtonHelper()
    val RbumperHelper = ButtonHelper()
    val LbumperHelper = ButtonHelper()
    val AbuttonHelper = ButtonHelper()

    fun init() {
        opencv.init(hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()
        opencv.onNewFrame(::scoopFrame)

    }

    private var displayMode: String = "frame"

    class NamedVar(val name: String, var value: Double)

    /*
    # values for Cam Calibrated Goal detection DURING THE DAY: L-H = 95, L-S = 105, L-V = 000, U-H = 111, U-S = 255, U-V = 255
    # values for Cam Calibrated Goal detection DURING THE NIGHT: L-H = 0, L-S = 65, L-V = 70, U-H = 105, U-S = 255, U-V = 255
    # contours,  _ = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    */


    var L_H = NamedVar("Low Hue", 0.0)
    var L_S = NamedVar("Low Saturation", 65.0)
    var L_V = NamedVar("Low Vanity/Variance/VolumentricVibacity", 70.0)
    var U_H = NamedVar("Uppper Hue", 105.0)
    var U_S = NamedVar("Upper Saturation", 255.0)
    var U_V = NamedVar("Upper Vanity/Variance/VolumentricVibracity", 255.0)


    var turnDir: String = "STUFF"
    fun scoopFrame(frame: Mat): Mat {
        // hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

        val hsv = Mat()
        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV)


        /*

            l_h = cv2.getTrackbarPos("L-H", "Trackbars")
            l_s = cv2.getTrackbarPos("L-S", "Trackbars")
            l_v = cv2.getTrackbarPos("L-V", "Trackbars")
            u_h = cv2.getTrackbarPos("U-H", "Trackbars")
            u_s = cv2.getTrackbarPos("U-S", "Trackbars")
            u_v = cv2.getTrackbarPos("U-V", "Trackbars")
        */
        /*

            lower_red = np.array([l_h, l_s, l_v])
            upper_red = np.array([u_h, u_s, u_v])

            mask = cv2.inRange(hsv, lower_red, upper_red)
        */
        val lower_red = Scalar(L_H.value, L_S.value, L_V.value)
        val upper_red = Scalar(U_H.value, U_S.value, U_V.value)
        val maskA = Mat()
        Core.inRange(hsv, lower_red, upper_red, maskA)

        /*
            kernel = np.ones((5, 5), np.uint8)
            mask = cv2.erode(mask, kernel)
        */
        val kernel = Mat(5, 5, CvType.CV_8U)
        val maskB = Mat()
        Imgproc.erode(maskA, maskB, kernel)

        /*
            # Contours Detection

            # values for Cam Calibrated Goal detection DURING THE DAY: L-H = 95, L-S = 105, L-V = 000, U-H = 111, U-S = 255, U-V = 255
            # values for Cam Calibrated Goal detection DURING THE NIGHT: L-H = 0, L-S = 65, L-V = 70, U-H = 105, U-S = 255, U-V = 255
            contours,  _ = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        */
        val contours = mutableListOf<MatOfPoint>()
        Imgproc.findContours(maskB, contours, Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

        //  for cnt in contours:
        contours.forEach { cnt ->

            //area = cv2.contourArea(cnt)
            val area = Imgproc.contourArea(cnt)

            //approx = cv2.approxPolyDP(cnt, 0.02*cv2.arcLength(cnt, True), True)
            val approx = MatOfPoint2f()


            fun convert(src: MatOfPoint): MatOfPoint2f {
                val dst = MatOfPoint2f()
                src.convertTo(dst, CvType.CV_32F)
                return dst
            }

            val cnt2f = convert(cnt)
            Imgproc.approxPolyDP(cnt2f, approx, 0.02 * Imgproc.arcLength(cnt2f, true), true)


            //        x = approx.ravel() [0]
            //        y = approx.ravel() [1]
            val point = approx.toList()[0]
            val x = point.x
            val y = point.y
            /*
                if area > 400:
                */
            if (area > 400) {

                fun convert(matOfPoint2f: MatOfPoint2f): MatOfPoint {
                    val foo = MatOfPoint()
                    matOfPoint2f.convertTo(foo, CvType.CV_32S)
                    return foo
                }

                // cv2.drawContours(frame, [approx], 0, (0, 0, 0), 5)
                Imgproc.drawContours(frame, mutableListOf(convert(approx)), 0, Scalar(0.0, 0.0, 0.0), 5)

                /*
                    if len(approx) == 3:
                        cv2.putText(frame, "triangle", (x, y), font, 1, (22, 100, 100))
                    elif len(approx) == 4:
                        cv2.putText(frame, "rectangle", (x, y), font, 1, (22, 100, 100))
                    elif 10 < len(approx) < 20:
                        cv2.putText(frame, "circle", (x, y), font, 1, (22, 100, 100))
                    elif len(approx) == 8:
                        cv2.putText(frame, "goal", (x, y), font, 1, (22, 100, 100))

                 */
                if (approx.toArray().size == 3) {
                    // cv2.putText(frame, "triangle", (x, y), font, 1, (22, 100, 100))
                    Imgproc.putText(frame, "triangle", Point(x, y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                } else if (approx.toArray().size == 4) {
                    Imgproc.putText(frame, "rectangle", Point(x, y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                } else if (approx.toArray().size in 11..19) {
                    Imgproc.putText(frame, "circle", Point(x, y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                } else if (approx.toArray().size == 8) {
                    Imgproc.putText(frame, "goal", Point(x, y), font, 1.0, Scalar(22.0, 100.0, 100.0))

                    // GOAL: IMPLEMENT ROTATIONS
                    // HOTZONE LOGIC SO THAT ROTATION DOESN'T BOUNCE
                    // DO IT FAST ENOUGH FOR ODOM, marker,
                    // DO IT HARDER, BETTER, FASTER, STRONGER

                    // Determine trajectory
                    if (x < 245) {
                        turnDir = "Right"

                    }

                    if (x > 245 && x < 255) turnDir = "There You Are [gunfire]"
                    else if (x >= 255) {
                        turnDir = "Left"

                    }


                    console.display(6, "goallastX $x, $y")
                    console.display(7, "Analysis says to $turnDir")


                }
            }
        }

        return when (displayMode) {
            "frame" -> frame
            "kernel" -> kernel
            "mask" -> maskB
            else -> frame
        }
    }
}