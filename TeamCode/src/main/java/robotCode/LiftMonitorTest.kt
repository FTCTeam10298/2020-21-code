package robotCode

fun main(args:Array<String>){
    println("yo")


    fun <T>assertEquals(expected:T, actual:T){
        if(expected != actual){
            throw Exception("Expected $expected but was $actual")
        }
    }

    fun startsGoingToStageA(){
        // given
        val now = 0L
        val testSubject = LiftMonitor()

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = now,
                liftButtonPressed = true,
                limitSwitchPressed = false)

        assertEquals(LiftStage.A, result)
    }
    fun waitsForTheLimit(){
        // given
        val now = 500L
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = now,
                liftButtonPressed = true,
                limitSwitchPressed = false)

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = now,
                liftButtonPressed = true,
                limitSwitchPressed = false)

        assertEquals(null, result)
    }

    fun movesToTheNextStageAfterTheTimeout(){
        // given
        val now = 1001L
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                limitSwitchPressed = false)

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = now,
                liftButtonPressed = true,
                limitSwitchPressed = false)

        assertEquals(LiftStage.B, result)
    }


    fun movesAfterTheButtonIsCycled(){
        // given
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                limitSwitchPressed = false)

        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 1L,
                liftButtonPressed = false,
                limitSwitchPressed = false)

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 2L,
                liftButtonPressed = true,
                limitSwitchPressed = false)

        assertEquals(LiftStage.A, result)
    }

    val tests = listOf(
            ::startsGoingToStageA,
            ::waitsForTheLimit,
            ::movesToTheNextStageAfterTheTimeout,
            ::movesAfterTheButtonIsCycled)

    tests.forEach{test ->
        println("Running $test")
        test()
        println(" Passed $test")

    }
}