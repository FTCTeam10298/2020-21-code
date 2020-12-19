package robotCode.goalDetection

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.*
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener

class GoalDetectorPipeline {

//    _, threshold = cv2.threshold(img, 240, 255, cv2.THRESH_BINARY)
//    _, contours, _ = cv2.findContours(threshold, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    fun detectRectangle() {
        val cap = Core.VideoCapture(0)
        var approx =
        var x =
        var y =
        val img = cv2.imread("shapes.jpg", cv2.IMREAD_GRAYSCALE)
        val contours = 1

        for (cnt in contours) {

            approx = cv2.approxPolyDP(cnt, 0.01 * cv2.arcLength(cnt, true), true)
            cv2.drawContours(img, [approx], 0, (0), 5)
            x = approx.ravel()[0]
            y = approx.ravel()[1]
            if (len(approx) == 4)
                cv2.putText(img, "Rectangle", (x, y), font, 1, (0))


        }
    }
}