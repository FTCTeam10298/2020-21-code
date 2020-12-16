package jamesTelemetryMenu

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry

open class TelemetryMenu(telemetry: Telemetry): TelemetryConsole(telemetry) {

    private var cursorLine = 0
    private var menuDone = false

//    Input & Cursor--------------------------------------

    private fun getCursorContent(): String = "hi"/*queue[cursorLine].replaceFirst("-", "")*/


    private var keyDown = true

    private fun updateCursorLine(gamepad: Gamepad) {

//        Pushes the cursor beyond the user lines
        cursorLine = lastUserLine + 1

//        Changes the line based on user input
        when {
            gamepad.dpad_up && !keyDown -> {keyDown = true; if (cursorLine > lastUserLine + 1) cursorLine -= 1;}
            gamepad.dpad_down && !keyDown -> {keyDown = true; if (cursorLine < queue.size - 1) cursorLine += 1;}
            gamepad.dpad_right && !keyDown -> {keyDown = true; getCursorContent(); chosen.add(getCursorContent());}
            gamepad.dpad_left && !keyDown -> {keyDown = true; menuDone = true;}
            !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
            else -> keyDown = true
        }

//        Adds cursor

        if (queue.size >= cursorLine) {
            removeCursor()
            addCursor()
        }
    }

    private fun removeCursor() {
        if (queue[cursorLine].startsWith("-"))
            replaceLine(cursorLine, queue[cursorLine].replaceFirst("-", ""))
    }

    private fun addCursor() {
        if (!queue[cursorLine].contains("-"))
            replaceLine(cursorLine, "-${queue[cursorLine]}")
    }

//    Options

    private val menu = MenuSystem()
    private val chosen: MutableList<String> = mutableListOf("")

    private fun getCurrentOption(): String {
        return queue.filter { queue.contains(menu.options.listOfOptions()) }.toString()
    }

    private fun addToChosen(item: String) {
        chosen.add(item)
    }

//    OPTION INTERACTION

    fun addOption(option: String,  item: String) {
        menu.addChoiceItem(option, item)
    }

    fun wasItemChosen(item: String): Boolean = chosen.contains(item)

    fun linkOption(item: String,  option: String) {
        menu.addLink(option, item)
    }

//    FIND AND REPLACE?

    private fun addChoiceToQueue() {

        val option = menu.getLink(getCurrentOption())
        replaceLine( lastUserLine + 2, "$option:")

        val item = menu.getItems(getCurrentOption())
        for (i in (item.indices))
            replaceLine(i + lastUserLine + 3, item.elementAt(i).toString())
    }

//    DO MENUS

    fun doMenus(gamepad: Gamepad) {
        keyDown = false //test
        while (!menuDone) {

//            Cursor&dpad
            updateCursorLine(gamepad)

//            Update queue
            addChoiceToQueue()

//            Telemetry
            queueToTelemetry()

//            test
            sleep(1000)
            if (!keyDown) {
                keyDown = true; if (cursorLine < queue.size - 1) cursorLine += 1;
                keyDown = true; if (cursorLine < queue.size - 1) cursorLine += 1;
            }
        }
        display(1, "Menu done.")
    }
}
