package jamesTelemetryMenu

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry
import java.lang.Thread.sleep

open class TelemetryMenu(telemetry: Telemetry): TelemetryConsole(telemetry) {

    private var cursorLine= 4
    private var menuDone= false

//    Input & Cursor--------------------------------------


    private fun getCursorContent(): String = removeCursor(queue[cursorLine])


    private var keyDown= true

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
        removeCursor(queue[cursorLine])
        addCursor(queue[cursorLine])
    }

    private fun removeCursor(where: String): String {
        return if (where.startsWith("-"))
            where.replaceFirst("-", "")
        else
            where
    }

    private fun addCursor(where: String): String {
        return if (!where.contains("-"))
            "-${where}"
        else
            where
    }


//    Options

    private val options = MenuChoices()
    private val chosen: MutableList<String> = mutableListOf("")

    private fun getCurrentOption(): String {
        return queue.filter { queue.contains(options.choices.matches.any()) }.toString()
    }

    private fun addToChosen(item: String) {
        chosen.add(item)
    }

//    OPTION INTERACTION

    fun addOption(option: String,  item: String) {
        options.addChoiceItem(option, item)
    }

    fun wasItemChosen(item: String): Boolean = chosen.contains(item)

    fun linkOption(item: String,  option: String) {
        options.addLink(option, item)
    }

//    FIND AND REPLACE?

    private fun addChoiceToQueue() {

        val option = options.getLink(getCurrentOption())
        replaceLine( lastUserLine + 2, "$option:")

        val item = options.getOptions(getCurrentOption())
        for (i in (item.indices))
            replaceLine(i + lastUserLine + 3, item.elementAt(i).toString())
    }


//    DO MENUS

    fun doMenus(gamepad: Gamepad) {
        while (!menuDone) {
//            display(1, "still works")
//            sleep(1000)

//            Cursor&dpad
            updateCursorLine(gamepad)

//            Update queue
            addChoiceToQueue()

//            Telemetry
            queueToTelemetry()
        }
        display(1, "Menu done.")
    }
}

