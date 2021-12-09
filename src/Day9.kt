import java.util.Deque
import java.util.LinkedList
import java.util.Queue

object Day9 : Problem<List<List<Long>>, Long> {
    override val day: Int = 9
    override val parts: List<Part<List<List<Long>>, Long>> = listOf(::part1, ::part2)
    override fun parse(rawInput: Sequence<String>): List<List<Long>> {
        return rawInput.map { it.split("").filter { it.isNotEmpty() }.map { it.toLong() } }.toList()
    }

    val dirs = listOf(
        -1 to 0,
        1 to 0,
        0 to 1,
        0 to -1
    )

    fun part1(hmap: List<List<Long>>): Long {
        var ans = 0L
        for (i in hmap.indices) {
            for (j in hmap[0].indices) {
                val curr = hmap[i][j]
                var lowP = true
                for ((dx, dy) in dirs) {
                    val x = i + dx
                    val y = j + dy
                    if (x >= 0 && x < hmap.size && y >= 0 && y < hmap[0].size) {
                        if (hmap[x][y] <= curr) {
                            lowP = false
                            break
                        }
                    }
                }
                if (lowP) {
                    ans += (curr + 1)
                }
            }
        }
        return ans
    }

    fun part2(hmap: List<List<Long>>): Long {
        val basins = mutableListOf<Long>()
        val done = Array(hmap.size) { i -> BooleanArray(hmap[0].size) { j -> hmap[i][j] == 9L } }

        for (i in hmap.indices) {
            for (j in hmap[0].indices) {
                if (!done[i][j]) {
                    val q: Queue<Pair<Int, Int>> = LinkedList()
                    var cSize = 0L
                    q.add(i to j)
                    while (q.isNotEmpty()) {
                        val (x, y) = q.poll()
                        for ((dx, dy) in dirs) {
                            val x2 = x + dx
                            val y2 = y + dy
                            if (x2 >= 0 && x2 < hmap.size && y2 >= 0 && y2 < hmap[0].size && !done[x2][y2]) {
                                done[x2][y2] = true
                                q.add(x2 to y2)
                                cSize++
                            }
                        }
                    }
                    basins += cSize
                }
            }
        }
        return basins.sortedDescending().take(3).fold(1L) { acc, v -> acc * v }
    }
}