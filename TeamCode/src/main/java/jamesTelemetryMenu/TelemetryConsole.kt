package jamesTelemetryMenu

import org.firstinspires.ftc.robotcore.external.Telemetry

open class TelemetryConsole(private val telemetry: Telemetry): TelemetryQueue() {

    fun display(line: Int, text: String) {
        replaceLine(line, text)

        if (line > lastUserLine)
            lastUserLine = line

        queueToTelemetry()
    }

    fun queueToTelemetry() {
        telemetry.clearAll()
        for (i in (1 until queue.size)) {
            telemetry.addLine(queue[i])
        }
        telemetry.update()
    }
}