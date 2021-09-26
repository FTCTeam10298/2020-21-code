package us.brainstormz.choivico.robotCode

enum class LiftStage{
    Bottom, A, B, C
}
class LiftMonitor {
    companion object {
        val kniodOneWayTime = 700L
    }
    data class LiftStageCommandRecord(val whenIssuedInMilliseconds:Long, val command:LiftStage)
    private var lastStageCommand:LiftStageCommandRecord? = null
    private var kniodIsOut:Boolean = false

    fun currentOperation():LiftStage? {
        return lastStageCommand?.command
    }

    fun isKniodOut() = kniodIsOut

    fun nextStageForServo(currentTimeInMilliseconds:Long, liftButtonPressed:Boolean, whenKniodLastRetracted: Long?, whenKniodLastExtended: Long?, limitSwitchPressed:Boolean):LiftStage? {
        val timeSinceKniodRetraction = (currentTimeInMilliseconds - (whenKniodLastRetracted ?: 0))
        val timeSinceKniodExtention  = (currentTimeInMilliseconds - (whenKniodLastExtended ?: 0))

        val kniodIsRetracting = timeSinceKniodRetraction < kniodOneWayTime
        val kniodIsExtending = timeSinceKniodExtention < kniodOneWayTime
        val kniodIsOut = kniodIsRetracting || (whenKniodLastRetracted ?: 0) < (whenKniodLastExtended ?: 0)

//        println("timeSinceKniodRetraction $timeSinceKniodRetraction")
        val previousCommand = this.lastStageCommand
        val nextCommand:LiftStage? = if(!liftButtonPressed){
            if(lastStageCommand?.command == LiftStage.Bottom){
                null
            }else{
                LiftStage.Bottom
            }
        }else{
            if(previousCommand==null || previousCommand.command == LiftStage.Bottom){
                // go to stage A
                LiftStage.A
            }else{
                // determine whether we need to go to the next
                val durationSinceLastCommand = currentTimeInMilliseconds - previousCommand.whenIssuedInMilliseconds
//                println("durationSinceLastCommand is $durationSinceLastCommand")
                if(limitSwitchPressed) {
//                    kniod = true
                    null
                }else if(kniodIsOut){
                    null
                }else{
                    if(durationSinceLastCommand < 2000){
                        // give it more time
                        null
                    }else{
                        // assume there was no ring, go to the next position
                        nextStage(previousCommand.command)
                    }
                }
            }
        }

        if(nextCommand!=null){
            lastStageCommand = LiftStageCommandRecord(
                    whenIssuedInMilliseconds = currentTimeInMilliseconds,
                    command = nextCommand)
        }

        this.kniodIsOut = kniodIsOut

        return nextCommand
    }

    private fun nextStage(stage: LiftStage):LiftStage? {
        return when (stage) {
            LiftStage.A -> LiftStage.B
            LiftStage.B -> LiftStage.C
            LiftStage.C -> null  /// can't go any higher
            else -> null
        }
    }

}