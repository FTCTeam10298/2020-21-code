package creamsicleGoalDetection

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import telemetryWizard.TelemetryConsole

class CreamsicleGoalDetector(private val console: TelemetryConsole){

    private val font = Imgproc.FONT_HERSHEY_COMPLEX

    private var displayMode: String = "frame"

    private class NamedVar(val name: String, var value: Double)

    /*
    # values for Cam Calibrated Goal detection DURING THE DAY: L-H = 95, L-S = 105, L-V = 000, U-H = 111, U-S = 255, U-V = 255
    # values for Cam Calibrated Goal detection DURING THE NIGHT: L-H = 0, L-S = 65, L-V = 70, U-H = 105, U-S = 255, U-V = 255
    # contours,  _ = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    */

    private var L_H = NamedVar("Low Hue", 0.0)
    private var L_S = NamedVar("Low Saturation", 65.0)
    private var L_V = NamedVar("Low Vanity/Variance/VolumentricVibacity", 70.0)
    private var U_H = NamedVar("Uppper Hue", 105.0)
    private var U_S = NamedVar("Upper Saturation", 255.0)
    private var U_V = NamedVar("Upper Vanity/Variance/VolumentricVibracity", 255.0)

    //Declares X and Y of the Goal's... well, something... that other code can use and request.
    var x = 0.0
    var y = 0.0

    private val hsv = Mat()
    private val maskA = Mat()
    private val maskB = Mat()
    private val kernel = Mat(5, 5, CvType.CV_8U)

    fun scoopFrame(frame: Mat): Mat {

        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV)

        val lower_red = Scalar(L_H.value, L_S.value, L_V.value)
        val upper_red = Scalar(U_H.value, U_S.value, U_V.value)
        Core.inRange(hsv, lower_red, upper_red, maskA)

        Imgproc.erode(maskA, maskB, kernel)

        val contours = mutableListOf<MatOfPoint>()
        Imgproc.findContours(maskB, contours, Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

        //  for cnt in contours:
        contours.forEach { cnt ->

            val area = Imgproc.contourArea(cnt)

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

                when (approx.toArray().size) {
                    3 -> {
                        // cv2.putText(frame, "triangle", (x, y), font, 1, (22, 100, 100))
                        Imgproc.putText(frame, "triangle", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                    }
                    4 -> {
                        Imgproc.putText(frame, "rectangle", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                    }
                    in 11..19 -> {
                        Imgproc.putText(frame, "circle", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))
                    }
                    8 -> {
                        Imgproc.putText(frame, "goal", Point(point.x, point.y), font, 1.0, Scalar(22.0, 100.0, 100.0))

                        x = point.x
                        y = point.y

                        console.display(6, "Last known goal position: $x, $y")
                    }
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