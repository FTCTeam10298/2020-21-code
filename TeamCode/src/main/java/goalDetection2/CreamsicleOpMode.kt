package goalDetection2

import buttonHelper.ButtonHelper
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import goalDetection.OpencvAbstraction
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvCameraFactory
import robotCode.RingDetector
import telemetryWizard.TelemetryConsole

@Autonomous
class CreamsicleOpMode() : OpMode() {

    val font = Imgproc.FONT_HERSHEY_COMPLEX
    val opencv = OpencvAbstraction(this)

    val XbuttonHelper = ButtonHelper()
    val YbuttonHelper = ButtonHelper()
    val DpadHelper = ButtonHelper()
    val RbumperHelper = ButtonHelper()
    val LbumperHelper = ButtonHelper()
    val AbuttonHelper = ButtonHelper()

    val console = TelemetryConsole(telemetry)


    override fun init() {
        opencv.init(hardwareMap)
        opencv.optimizeView = true
        opencv.openCameraDeviceAsync = true
        opencv.start()
        opencv.onNewFrame(::scoopFrame)
    }


    private var displayMode:String = "frame"
    class NamedVar(val name:String, var value:Double)

    var L_H = NamedVar("Low Hue", 95.0)
    var L_S = NamedVar("Low Saturation", 105.0)
    var L_V = NamedVar("Low Vanity/Variance/VolumentricVibacity", 0.0)
    var U_H = NamedVar("Uppper Hue", 111.0)
    var U_S = NamedVar("Upper Saturation", 255.0)
    var U_V = NamedVar("Upper Vanity/Variance/VolumentricVibracity", 255.0)

    private var varBeingEdited:NamedVar = L_H
    fun render(){
        console.display(2, "Active Var; ${varBeingEdited.name}")
        console.display(4,  "${varBeingEdited.value}")
    }
    override fun init_loop() {
        if (XbuttonHelper.stateChanged(gamepad1.x) && gamepad1.x) {
            console.display(3, "TrainerMODE; $displayMode")
            if (displayMode == "frame") displayMode = "mask"
            else if (displayMode == "mask") displayMode = "kernel"
            else if (displayMode == "kernel") displayMode = "frame"
            render()
        }


        when {
            DpadHelper.stateChanged(gamepad1.dpad_left) && gamepad1.dpad_left -> {
                if (varBeingEdited == L_H) varBeingEdited = L_S
                else if (varBeingEdited == L_S) varBeingEdited = L_V
                else if (varBeingEdited == L_V) varBeingEdited = U_H
                else if (varBeingEdited == U_H) varBeingEdited = U_S
                else if (varBeingEdited == U_S) varBeingEdited = U_V
                else if (varBeingEdited == U_V) varBeingEdited = L_H
                render()
            }
        }

        if (YbuttonHelper.stateChanged(gamepad1.y) && gamepad1.y) {
            console.display(1, "Vals Zeroed")
            L_H.value = 0.0
            L_S.value =  0.0
            L_V.value =  0.0
            U_H.value = 0.0
            U_S.value = 0.0
            U_V.value = 0.0
            render()
        }

        if (RbumperHelper.stateChanged(gamepad1.right_bumper) && gamepad1.right_bumper){
            varBeingEdited.value += 5
            render()
        }
        if (LbumperHelper.stateChanged(gamepad1.left_bumper) && gamepad1.left_bumper) {
            varBeingEdited.value -= 5
            render()
        }

        if (AbuttonHelper.stateChanged(gamepad1.a) && gamepad1.a) {
            L_H.value = 0.0
            L_S.value = 0.0
            L_V.value = 0.0
            U_H.value = 255.0
            U_S.value = 255.0
            U_V.value = 255.0
            console.display(1, "Vals Squonked")
            render()
        }++++++++++++++
    }

// New Android Values, Quality Unknown: L_H = 0.0, L_S = 55.0, L_V = 135.0, U_H = 85.0, U_S = 210.0, U_V = 215.0
//ADD BROWSER FOR DECIMAL VALUES!!


    override fun loop() {
    }

    fun scoopFrame(frame:Mat):Mat {
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

            # values for Cam Calibrated Goal detection: L-H = 95, L-S = 105, L-V = 000, U-H = 111, U-S = 255, U-V = 255
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
                }

            }

        }

        return when(displayMode){
            "frame" -> frame
            "kernel" -> kernel
            "mask" -> maskB
            else -> frame
        }

    }
}