package jamesTelemetryMenu

import android.os.SystemClock.sleep
import com.qualcomm.robotcore.hardware.Gamepad

class TelemetryWizard(private val console: TelemetryConsole) {

//    Menu organization
    private var menuList: List<Menu> = listOf()

    data class Menu(val name: String, val caption: String, val items: List<String>, val nextMenu: Menu? = null, val firstMenu: Boolean = false)

    fun newMenu(menu: Menu) {
        menuList += menu
    }

    fun getMenu(name: String): Menu = menuList.first{ it.name == name }

    private fun formatMenu(menu: Menu): List<String> {
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

    private fun waitForMenuDone(gamepad: Gamepad) {
        var menuDone = false
        var keyDown = false

        while (!menuDone) {
            when {
                gamepad.dpad_up && !keyDown -> cursorOption -= 1 //moves cursor up
                gamepad.dpad_down && !keyDown -> cursorOption += 1  //moves cursor down
//                gamepad.dpad_right && !keyDown -> //selects option
//                gamepad.dpad_left && !keyDown -> //Stops wizard or menu (haven't decided) and sets answers to default
                !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_right && !gamepad.dpad_left -> keyDown = false
            }
        }
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

        displayMenu(formatMenu(firstMenu))

        menuList.forEachIndexed { index, action ->
            waitForMenuDone(gamepad)

            val thisMenu = lastMenu.nextMenu
            if (thisMenu !== null) {
                displayMenu(formatMenu(thisMenu))
                lastMenu = thisMenu
            }

        }
    }
}
