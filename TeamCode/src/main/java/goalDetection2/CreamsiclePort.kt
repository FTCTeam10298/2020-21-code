/*# This code is sponsored by Aperture Science Consumer Advocate Gabriel Fergesen, who would like to remind you that turrets are your friends.


 */
package goalDetection2

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoCapture
import org.openftc.easyopencv.OpenCvCameraFactory

val font = Imgproc.FONT_HERSHEY_COMPLEX
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
    val L_H = 95.0
    val L_S = 105.0
    val L_V = 0.0
    val U_H = 111.0
    val U_S = 255.0
    val U_V = 255.0
    /*

        lower_red = np.array([l_h, l_s, l_v])
        upper_red = np.array([u_h, u_s, u_v])

        mask = cv2.inRange(hsv, lower_red, upper_red)
    */
    val lower_red = Scalar(L_H, L_S, L_V)
    val upper_red = Scalar(U_H, U_S, U_V)
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


        fun convert(src:MatOfPoint):MatOfPoint2f{
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
    return frame

}