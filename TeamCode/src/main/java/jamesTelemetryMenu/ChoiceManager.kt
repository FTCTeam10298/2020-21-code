package jamesTelemetryMenu

open class ChoiceManager() {

    val firstOption= "First Option"
    var currentChoice= "Choices"
    var cursorContent= "Options"

    data class option(var choice: String, var option: String)
    var options: MutableList<option> = mutableListOf(option("The Choicest", "Of Memes"))

    data class link(var option: String, var choice: String)
    var links: MutableList<link> = mutableListOf(link("The Choicest", "Of Memes"))

//    User functions-------------------------------------

    fun addOption(choice: String,  option: String) {
        options.add(option(choice, option))
    }

    fun optionChosen(option: String): Boolean {
        return cursorContent == option
    }

    fun linkOption(option: String, choice: String) {
//        links.add(link(option, choice))
    }

//    Non-user



}