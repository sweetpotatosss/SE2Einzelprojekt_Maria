package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res = controller.getLeaderboard(null).body!!

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res = controller.getLeaderboard(null).body!!

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_rank_returnsCorrectWindow() {
        val players = (1..10).map { GameResult(it.toLong(), "player$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(players)

        val res = controller.getLeaderboard(5).body!!

        assertEquals(7, res.size)
        assertEquals(players[1], res[0])
        assertEquals(players[4], res[3])
        assertEquals(players[7], res[6])
    }

    @Test
    fun test_getLeaderboard_rankTooLarge_returns400() {
        whenever(mockedService.getGameResults()).thenReturn(listOf(
            GameResult(1, "player1", 10, 1.0)
        ))

        val res = controller.getLeaderboard(99)

        assertEquals(400, res.statusCode.value())
    }

    @Test
    fun test_getLeaderboard_rankNegative_returns400() {
        whenever(mockedService.getGameResults()).thenReturn(listOf(
            GameResult(1, "player1", 10, 1.0)
        ))

        val res = controller.getLeaderboard(-1)

        assertEquals(400, res.statusCode.value())
    }

    @Test
    fun test_getLeaderboard_rankAtStart_returnsPartialWindow() {
        val players = (1..5).map { GameResult(it.toLong(), "player$it", 100 - it, it.toDouble()) }

        whenever(mockedService.getGameResults()).thenReturn(players)

        val res = controller.getLeaderboard(1).body!!

        assertEquals(4, res.size)
        assertEquals(players[0], res[0])
        assertEquals(players[3], res[3])
    }
}