//This code was written by your friendly Aperture Science Consumer Associate Gabriel Fergesen, who would like to remind you that only the truest peace lovers can make a living writing code to slaughter people garishly. Join the Turret Project today!

//This stuff allows *you*, a nerd land-locked amid queer pixels, to outline key elements (if they are goals) and put a big 'ol tag on them. The days of FOUR, ONE, or NONE are ended.. but it took a while.






package goalDetection2

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import goalDetection.OpencvAbstraction
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import robotCode.hardwareClasses.EncoderDriveMovement
import telemetryWizard.TelemetryConsole

class CreamsicleScoop_GoalTracking(private val console: TelemetryConsole, private val opmode: OpMode){

    val opencv = OpencvAbstraction(opmode)

    val font = Imgproc.FONT_HERSHEY_COMPLEX

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

    //Declares X and Y of the Goal's... well, something... that other code can use and request.
    var x = 0.0
    var y = 0.0

    val hsv = Mat()
    val maskA = Mat()
    val maskB = Mat()
    val kernel = Mat(5, 5, CvType.CV_8U)

    fun init() {
        opencv.init(opmode.hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()
        opencv.onNewFrame(::scoopFrame)
    }

    fun scoopFrame(frame: Mat): Mat {
        // hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
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
        Core.inRange(hsv, lower_red, upper_red, maskA)

        /*
            kernel = np.ones((5, 5), np.uint8)
            mask = cv2.erode(mask, kernel)
        */
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
                    Imgproc.putText(frame, "triangle", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                } else if (approx.toArray().size == 4) {
                    Imgproc.putText(frame, "rectangle", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                } else if (approx.toArray().size in 11..19) {
                    Imgproc.putText(frame, "circle", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                } else if (approx.toArray().size == 8) {
                    Imgproc.putText(frame, "goal", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))

                    x = point.x
                    y = point.y

                    console.display(6, "Last known goal position: $x, $y")
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