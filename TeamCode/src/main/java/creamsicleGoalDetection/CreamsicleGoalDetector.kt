//This code is brought to you by your friendly Aperture Science Community Outreach Associate, who would like to remind you that a bright future involves dark rooms and nuclear material.


//IN CASE OF FEDERAL INVESTIGATION:
//Pull switch to deploy FlameThrower turrets into Enrichment Sphere below Observation Chamber
//Overload nuclear reactor
//Burn Subject birth records
//Incinerate this poster
//Incinerate the incinerator
//Incinerate the incinerator incinerator
//As it burns, throw uniform in (all articles of clothing)
//Lie until further notice.


package creamsicleGoalDetection

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import telemetryWizard.TelemetryConsole

class CreamsicleGoalDetector(private val console: TelemetryConsole){

    // THIS STUFF DO BE A DETECTOR. pICTURE AN 'APPLE IN A SUNNY ROOM' (thought experiment © 1998 Aperture Science Innovators)
    //RED will detect that apple if it's lit by a halogen bulb (panel, only $200,000,000 per!)
    //BLUE will detect an apple that has been modified with mantis DNA to change its hue.
    //solRED will do the same Outside
    //solBLUE will find the alternate apple, and
    //absRED or absBLUE will do the same but badly inside or out!

    enum  class TargetHue {
        RED,
        BLUE,
        absRED,
//        absBLUE,
//        solRED,
//        solBLUE
    }

    var targetHue = TargetHue.RED

    private val font = Imgproc.FONT_HERSHEY_COMPLEX

    var displayMode: String = "frame"

    class NamedVar(val name: String, var value: Double)

    /*
    # values for Cam Calibrated Goal detection DURING THE DAY: L-H = 95, L-S = 105, L-V = 000, U-H = 111, U-S = 255, U-V = 255
    # values for Cam Calibrated Goal detection DURING THE NIGHT: L-H = 0, L-S = 65, L-V = 70, U-H = 105, U-S = 255, U-V = 255
    # contours,  _ = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    */

        //DANGEROUS STUFFS BELOW! These define what color to filter through (and the layer chucks the rest. Use the AutoAimTestAndCal method to find the proper values using a SNAZZY, SNAZZY gui.
        //Values for Detecting Red

    class ColorRange(val L_H: NamedVar, val L_S: NamedVar, val L_V: NamedVar, val U_H: NamedVar, val U_S: NamedVar, val U_V: NamedVar)

    //Trained for Sunlight- useful for Wall-E but not Eve. SOMEDAY I'LL ADD A SWITCHER BTW INDOOR/OUTDOOR MODES BUT NO I DON't wANNA
    private val redSolarizedColor = ColorRange(
            L_H = NamedVar("Low Hue", 120.0),
            L_S = NamedVar("Low Saturation", 150.0),
            L_V = NamedVar("Low Vanity/Variance/VolumentricVibacity", 0.0),
            U_H = NamedVar("Uppper Hue", 130.0),
            U_S = NamedVar("Upper Saturation", 255.0),
            U_V = NamedVar("Upper Vanity/Variance/VolumentricVibracity", 200.0)
    )

    private val redAbsoluteColor = ColorRange(
            L_H = NamedVar("Low Hue", 120.0),
            L_S = NamedVar("Low Saturation", 150.0),
            L_V = NamedVar("Low Vanity/Variance/VolumentricVibacity", 0.0),
            U_H = NamedVar("Uppper Hue", 255.0),
            U_S = NamedVar("Upper Saturation", 255.0),
            U_V = NamedVar("Upper Vanity/Variance/VolumentricVibracity", 255.0)
    )

    // trained for rooms so you can prove AAY! IM A G$$D PERS$N! (blame it on Odd1sOut)

    private val redColor = ColorRange(
            L_H = NamedVar("Low Hue", 120.0),
            L_S = NamedVar("Low Saturation", 115.0),
            L_V = NamedVar("Low Vanity/Variance/VolumentricVibacity", 120.0),
            U_H = NamedVar("Uppper Hue", 255.0),
            U_S = NamedVar("Upper Saturation", 255.0),
            U_V = NamedVar("Upper Vanity/Variance/VolumentricVibracity", 255.0))

    private val blueColor = ColorRange(
            L_H = NamedVar("Low Hue", 0.0),
            L_S = NamedVar("Low Saturation", 65.0),
            L_V = NamedVar("Low Vanity/Variance/VolumentricVibacity", 70.0),
            U_H = NamedVar("Uppper Hue", 105.0),
            U_S = NamedVar("Upper Saturation", 255.0),
            U_V = NamedVar("Upper Vanity/Variance/VolumentricVibracity", 255.0))

    val goalColor = when(targetHue){
        TargetHue.RED -> redColor
        TargetHue.BLUE -> blueColor
        TargetHue.absRED -> redAbsoluteColor

    }

    //Declares X and Y of the Goal's... well, something... that other code can use and request.
    var x = 0.0
    var y = 0.0

    private val hsv = Mat()
    private val maskA = Mat()
    private val maskB = Mat()
    private val kernel = Mat(5, 5, CvType.CV_8U)

    fun scoopFrame(frame: Mat): Mat {

        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV)

        val lower = Scalar(goalColor.L_H.value, goalColor.L_S.value, goalColor.L_V.value)
        val upper = Scalar(goalColor.U_H.value, goalColor.U_S.value, goalColor.U_V.value)
        Core.inRange(hsv, lower, upper, maskA)

        Imgproc.erode(maskA, maskB, kernel)

        val contours = mutableListOf<MatOfPoint>()
        Imgproc.findContours(maskB, contours, Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

        //  for cnt in contours:
        contours.forEach { cnt ->

            val area = Imgproc.contourArea(cnt)

            val points = MatOfPoint2f()


            fun convert(src: MatOfPoint): MatOfPoint2f {
                val dst = MatOfPoint2f()
                src.convertTo(dst, CvType.CV_32F)
                return dst
            }

            val cnt2f = convert(cnt)
            Imgproc.approxPolyDP(cnt2f, points, 0.02 * Imgproc.arcLength(cnt2f, true), true)


            //        x = approx.ravel() [0]
            //        y = approx.ravel() [1]
            val point = points.toList()[0]
            /*
                if area > 400:
                */
            if (area > 400) {

                fun convert(matOfPoint2f: MatOfPoint2f): MatOfPoint {
                    val foo = MatOfPoint()
                    matOfPoint2f.convertTo(foo, CvType.CV_32S)
                    return foo
                }

                //Detects shapes. Commentation isn't great in this, learn to use this in the Python CreamsiclePy port instead...
                //In java this code transalates to Here Be Dragons and Kotlin is little better.

                // cv2.drawContours(frame, [approx], 0, (0, 0, 0), 5)
                Imgproc.drawContours(frame, mutableListOf(convert(points)), 0, Scalar(0.0, 0.0, 0.0), 5)

                when (points.toArray().size) {
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
                    8-> {
                        Imgproc.putText(frame, "goalCandidate", Point(point.x, point.y), font, 0.05, Scalar(22.0, 100.0, 100.0))

                        val pointsArray = points.toArray()

                        val xValues = pointsArray.map{it.x}
                        val yValues = pointsArray.map{it.y}

                        val minX = xValues.minOrNull()!!
                        val minY = yValues.minOrNull()!!

                        val maxX = xValues.maxOrNull()!!
                        val maxY = yValues.maxOrNull()!!

                        val width = maxX - minX
                        val height = maxY - minY
                        val area = width * height
                        val aspect = width / height

                        //determine if it isn't a floating spot.

                        if (aspect > 1.3) {
                            x = point.x
                            y = point.y
                            Imgproc.putText(frame, "goal", Point(point.x, point.y), font, 1.5, Scalar(22.0, 100.0, 100.0))
                        }




                        console.display(5, "width $width")
                        console.display(6, "Last known goal position: $x, $y")
                        console.display(7, "My God, THE FALSE POSITIVES are filled with stars!: $height")
                        console.display(8, "there can only be one: $area")
                        console.display(9, "Aspects are bright: $aspect")


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