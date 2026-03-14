package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): ResponseEntity<List<GameResult>> {
        val sorted = gameResultService.getGameResults()
            .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        if (rank == null) {
            return ResponseEntity.ok(sorted)
        }

        if (rank < 1 || rank > sorted.size) {
            return ResponseEntity.badRequest().build()
        }

        val index = rank - 1
        val from = maxOf(0, index - 3)
        val to = minOf(sorted.size, index + 4)

        return ResponseEntity.ok(sorted.subList(from, to))
    }
}