package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as whenever

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_existingId_returnsObject() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        whenever(mockedService.getGameResult(1)).thenReturn(gameResult)

        val res = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(gameResult, res)
    }

    @Test
    fun test_getGameResult_nonexistentId_returnsNull() {
        whenever(mockedService.getGameResult(99)).thenReturn(null)

        val res = controller.getGameResult(99)

        verify(mockedService).getGameResult(99)
        assertNull(res)
    }

    @Test
    fun test_getAllGameResults_returnsAllElements() {
        val list = listOf(
            GameResult(1, "player1", 17, 15.3),
            GameResult(2, "player2", 25, 16.0)
        )
        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(2, res.size)
        assertEquals(list, res)
    }

    @Test
    fun test_addGameResult_callsService() {
        val gameResult = GameResult(1, "player1", 17, 15.3)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }

}