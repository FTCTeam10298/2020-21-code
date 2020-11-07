///*
// * Copyright (c) 2020 OpenFTC Team
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//package org.firstinspires.ftc.teamcode.vision
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp
//import org.opencv.core.*
//import org.opencv.imgproc.Imgproc
//import org.openftc.easyopencv.*
//import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
//
//@TeleOp
//class EasyOpenCVExamplea : LinearOpMode() {
//
//    lateinit var phoneCam: OpenCvInternalCamera
//    var pipeline: SkystoneDeterminationPipeline? = null
//
//    override fun runOpMode() {
//        val cameraMonitorViewId: Int = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName())
//        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
//        pipeline = SkystoneDeterminationPipeline()
//        phoneCam.setPipeline(pipeline)
//
//        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
//        // out when the RC activity is in portrait. We do our actual image processing assuming
//        // landscape orientation, though.
//        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW)
//        phoneCam.openCameraDeviceAsync(AsyncCameraOpenListener { phoneCam.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT) })
//        waitForStart()
//        while (opModeIsActive()) {
//            telemetry.addData("Analysis", pipeline!!.analysis)
//            telemetry.addData("Position", pipeline!!.position)
//            telemetry.update()
//
//            // Don't burn CPU cycles busy-looping in this sample
//            sleep(50)
//        }
//    }
//
//    class SkystoneDeterminationPipeline : OpenCvPipeline() {
//        /*
//         * An enum to define the skystone position
//         */
//        enum class RingPosition {
//            FOUR, ONE, NONE
//        }
//
//        val FOUR_RING_THRESHOLD = 150
//        val ONE_RING_THRESHOLD = 135
//        var region1_pointA = Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x,
//                REGION1_TOPLEFT_ANCHOR_POINT.y)
//        var region1_pointB = Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
//                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT)
//
//        /*
//         * Working variables
//         */
//        var region1_Cb: Mat? = null
//        var YCrCb = Mat()
//        var Cb = Mat()
//        var analysis = 0
//
//        // Volatile since accessed by OpMode thread w/o synchronization
//        @Volatile
//        private var position = RingPosition.FOUR
//
//        /*
//         * This function takes the RGB frame, converts to YCrCb,
//         * and extracts the Cb channel to the 'Cb' variable
//         */
//        fun inputToCb(input: Mat?) {
//            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb)
//            Core.extractChannel(YCrCb, Cb, 1)
//        }
//
//        override fun init(firstFrame: Mat) {
//            inputToCb(firstFrame)
//            region1_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
//        }
//
//        override fun processFrame(input: Mat): Mat {
//            inputToCb(input)
//            analysis = Core.mean(region1_Cb).`val`[0].toInt()
//            Imgproc.rectangle(
//                    input,  // Buffer to draw on
//                    region1_pointA,  // First point which defines the rectangle
//                    region1_pointB,  // Second point which defines the rectangle
//                    BLUE,  // The color the rectangle is drawn in
//                    2) // Thickness of the rectangle lines
//            position = RingPosition.FOUR // Record our analysis
//            position = when {
//                analysis > FOUR_RING_THRESHOLD -> {
//                    RingPosition.FOUR
//                }
//                analysis > ONE_RING_THRESHOLD -> {
//                    RingPosition.ONE
//                }
//                else -> {
//                    RingPosition.NONE
//                }
//            }
//            Imgproc.rectangle(
//                    input,  // Buffer to draw on
//                    region1_pointA,  // First point which defines the rectangle
//                    region1_pointB,  // Second point which defines the rectangle
//                    GREEN,  // The color the rectangle is drawn in
//                    -1) // Negative thickness means solid fill
//            return input
//        }
//
//        companion object {
//            /*
//         * Some color constants
//         */
//            val BLUE = Scalar(0.0, 0.0, 255.0)
//            val GREEN = Scalar(0.0, 255.0, 0.0)
//
//            /*
//         * The core values which define the location and size of the sample regions
//         */
//            val REGION1_TOPLEFT_ANCHOR_POINT = Point(181.0, 98.0)
//            const val REGION_WIDTH = 35
//            const val REGION_HEIGHT = 25
//        }
//    }
//}