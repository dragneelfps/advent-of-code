import java.lang.Integer.min
import kotlin.math.abs

object Day7 : Problem<List<Int>, Int> {
    override val day: Int = 7
    override val parts: List<Part<List<Int>, Int>> = listOf(::part1, ::part2)
    override fun parse(rawInput: Sequence<String>): List<Int> {
        return rawInput.first().split(",").map(String::toInt)
    }

    private fun part1(positions: List<Int>): Int {
        val min = positions.minOrNull()!!
        val max = positions.maxOrNull()!!
        var minCost = Int.MAX_VALUE
        for (i in min..max) {
            var cost = 0
            for (pos in positions) {
                cost += abs(pos - i)
            }
            minCost = min(minCost, cost)
        }
        return minCost
    }

    private fun part2(positions: List<Int>): Int {
        val min = positions.minOrNull()!!
        val max = positions.maxOrNull()!!
        var minCost = Int.MAX_VALUE
        for (i in min..max) {
            var cost = 0
            for (pos in positions) {
                val n = abs(pos - i)
                cost += (n*(n+1)/2)
            }
            minCost = min(minCost, cost)
        }
        return minCost
    }
}