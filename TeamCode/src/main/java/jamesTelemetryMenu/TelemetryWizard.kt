package jamesTelemetryMenu

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.hardware.Gamepad

class TelemetryWizard(private val console: TelemetryConsole) {

//    Menu organization
    private var menuList: List<Menu> = listOf()
    private var currentMenu: Menu? = null

    data class Menu(val name: String, val caption: String, val items: List<String>, val nextMenu: Menu? = null, val firstMenu: Boolean = false)

    fun newMenu(menu: Menu) {
        menuList += menu
    }

    fun getMenu(name: String): Menu = menuList.first{ it.name == name }

    private fun formatMenu(menu: Menu): List<String> {
        currentMenu = menu

        var format = listOf(menu.caption + ":\n")
        menu.items.forEachIndexed { index, action ->
            format += placeCursor(index) + action
        }
        return format
    }

    private fun displayMenu(formattedMenu: List<String>) {
        formattedMenu.forEachIndexed{ index, action ->
            console.replaceLine(index + 2, action)
        }
        console.queueToTelemetry()
    }

//    Gamepad input handler
    private var cursorOption = 0
    private var menuDone = false

    private fun changeCursorBasedOnDPad(gamepad: Gamepad) {
        val cursorMax = currentMenu!!.items.size - 1
        var keyDown = false

        when {
            gamepad.dpad_up && !keyDown -> {keyDown = true; if (cursorOption > 0) cursorOption -= 1} //moves cursor up
            gamepad.dpad_down && !keyDown -> {keyDown = true; if (cursorOption < cursorMax) cursorOption += 1}  //moves cursor down
//                gamepad.dpad_right && !keyDown -> //selects option
//                gamepad.dpad_left && !keyDown -> //Stops wizard or menu (haven't decided) and sets answers to default
            !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
        }
        console.display(10, keyDown.toString())
    }

    private fun placeCursor(option: Int): String {
        return if (option == cursorOption) {
            "-"
        } else {
            " "
        }
    }

    fun summonWizard(gamepad: Gamepad) {

        val firstMenu = menuList.first { it.firstMenu }
        var lastMenu = firstMenu

        menuList.forEachIndexed { index, action ->
            val thisMenu = lastMenu.nextMenu

            displayMenu(formatMenu(firstMenu))

            while (!menuDone) {
                changeCursorBasedOnDPad(gamepad)

                displayMenu(formatMenu(firstMenu))

//                if (thisMenu !== null) {
//                    displayMenu(formatMenu(thisMenu))
//                    lastMenu = thisMenu
//                }
            }

        }
    }
}
