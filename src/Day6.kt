object Day6 : Problem<List<Int>, Long> {
    override val day: Int = 6

    override val parts: List<(List<Int>) -> Long> = listOf(::part1, ::part2)

    override fun parse(rawInput: Sequence<String>): List<Int> {
        return rawInput.first().split(",").map(String::toInt)
    }

    private fun part1(fishes: List<Int>): Long {
        fun solve(ct: Int, fishes: List<Int>): List<Int> {
            if (ct == 80) return fishes
            var newFishes = 0
            val oldFishes = fishes.map { x ->
                if (x == 0) {
                    newFishes++
                    6
                } else {
                    x - 1
                }
            }
            return solve(ct+1, oldFishes + generateSequence { 8 }.take(newFishes))
        }

        return solve(0, fishes).size.toLong()

    }

    private fun part2(fishes: List<Int>): Long {
        val maxDays = 256
        val newFishes = sortedMapOf<Int, Long>( compareBy { it } )
        for (x in fishes) {
            var X = x
            for (i in 1..maxDays) {
                if (X == 0) {
                    X = 7
                }
                X--
                if (X == 0) {
                    newFishes[i+1] = newFishes.getOrDefault(i+1, 0) + 1
                }
            }
        }

        for (day in 1..maxDays) {
            val nf = newFishes[day] ?: continue
            var X = 8
            for (i in day+1..maxDays) {
                if (X == 0) {
                    X = 7
                }
                X--
                if (X == 0) {
                    newFishes[i+1] = newFishes.getOrDefault(i+1, 0) + nf
                }
            }
        }
        return newFishes.toList().filter { it.first <= maxDays }.fold(0L) { tots, (day, nf) -> tots + nf } + fishes.size
    }

}