package us.brainstormz.choivico.telemetryWizard

import org.firstinspires.ftc.robotcore.external.Telemetry

open class TelemetryConsole(val telemetry: Telemetry) {

    var linesDisplayed: MutableList<String> = mutableListOf()
    var lastUserLine= 0

//    fun addToQueue(line: Int, text: String) {
//        replaceLine(line, text)
//        if (line > lastUserLine)
//            lastUserLine = line
//    }


    fun replaceLine(line: Int, text: String) {

//        Adds empty lines into the list if necessary
        while (linesDisplayed.size <= line)
            linesDisplayed.add("")

//        Add content
        linesDisplayed[line] = text
    }

    fun display(line: Int, text: String) {
        replaceLine(line, text)

        if (line > lastUserLine)
            lastUserLine = line

        queueToTelemetry()
    }

    fun eraseLine(line: Int) {
        replaceLine(line, "")
    }

    fun clearAll() {
        telemetry.clearAll()
        linesDisplayed.clear()
    }

    fun queueToTelemetry() {
        telemetry.clearAll()
        for (i in (1 until linesDisplayed.size)) {
            telemetry.addLine(linesDisplayed[i])
        }
        telemetry.update()
    }
}