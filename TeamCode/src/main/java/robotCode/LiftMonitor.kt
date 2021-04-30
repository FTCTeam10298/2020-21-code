package robotCode

enum class LiftStage{
    Bottom, A, B, C
}
class LiftMonitor {
    data class LiftStageCommandRecord(val whenIssuedInMilliseconds:Long, val command:LiftStage)
    private var lastStageCommand:LiftStageCommandRecord? = null
    var kniod = false

    fun nextStageForServo(currentTimeInMilliseconds:Long, liftButtonPressed:Boolean, limitSwitchPressed:Boolean):LiftStage? {
        val previousCommand = this.lastStageCommand
        val nextCommand:LiftStage? = if(!liftButtonPressed){
            LiftStage.Bottom
        }else{
            if(previousCommand==null){
                // go to stage A
                LiftStage.A
            }else{
                // determine whether we need to go to the next
                val durationSinceLastCommand = currentTimeInMilliseconds - previousCommand.whenIssuedInMilliseconds
                if(limitSwitchPressed) {
                    kniod = true
                    null
                }else{
                    if(durationSinceLastCommand < 1000){
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
                    whenIssuedInMilliseconds = System.currentTimeMillis(),
                    command = nextCommand)
        }

        return nextCommand
    }

    private fun nextStage(stage: LiftStage) =
            when (stage) {
                LiftStage.A -> LiftStage.B
                LiftStage.B -> LiftStage.C
                LiftStage.C -> null  /// can't go any higher
                else -> null
            }
}