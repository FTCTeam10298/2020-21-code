package us.brainstormz.choivico.robotCode

fun main(args:Array<String>){
    println("yo")


    fun <T>assertEquals(expected:T, actual:T){
        if(expected != actual){
            throw Exception("Expected $expected but was $actual")
        }
    }

    fun startsGoingToStageA(){
        // given
        val testSubject = LiftMonitor()

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        assertEquals(LiftStage.A, result)
    }
    fun waitsForTheLimit(){
        // given
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 999L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        assertEquals(null, result)
    }

    fun movesToTheNextStageAfterTheTimeout(){
        // given
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 1001L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        assertEquals(LiftStage.B, result)
    }


    fun movesAfterTheButtonIsCycled(){
        // given
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 1L,
                liftButtonPressed = false,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 2L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        // then
        assertEquals(LiftStage.A, result)
    }


    val kniodRetractionDurationMillis = 500L

    fun liftPausesWhenKniodIsRetracting(){
        // given
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)



        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 1001L,
                liftButtonPressed = true,
                whenKniodLastRetracted = 900L,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        // then
        assertEquals(null, result)
    }

    fun liftPausesWhenKniodIsExtending(){
        // given
        val whenKniodLastRetracted = -1000L
        val testSubject = LiftMonitor()

        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                whenKniodLastRetracted = whenKniodLastRetracted,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)


        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 1001L,
                liftButtonPressed = true,
                whenKniodLastRetracted = whenKniodLastRetracted,
                whenKniodLastExtended = 900L,
                limitSwitchPressed = false)

        // then
        assertEquals(null, result)
    }

    fun liftContinuesWhenTheKniodHasHadTimeToRetract(){
        // given
        val testSubject = LiftMonitor()
        testSubject.nextStageForServo(
                currentTimeInMilliseconds = 0L,
                liftButtonPressed = true,
                whenKniodLastRetracted = null,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)



        // when
        val result = testSubject.nextStageForServo(
                currentTimeInMilliseconds = 1001L,
                liftButtonPressed = true,
                whenKniodLastRetracted = 1001L - LiftMonitor.kniodOneWayTime,
                whenKniodLastExtended = null,
                limitSwitchPressed = false)

        // then
        assertEquals(LiftStage.B, result)
    }

    val tests = listOf(
            ::startsGoingToStageA,
            ::waitsForTheLimit,
            ::movesToTheNextStageAfterTheTimeout,
            ::movesAfterTheButtonIsCycled,
            ::liftPausesWhenKniodIsRetracting,
            ::liftPausesWhenKniodIsExtending,
            ::liftContinuesWhenTheKniodHasHadTimeToRetract)

    tests.forEach{test ->
        println("Running $test")
        test()
        println(" Passed $test")

    }
}