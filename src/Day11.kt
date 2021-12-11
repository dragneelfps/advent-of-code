object Day11 : Problem<List<List<Int>>, Int> {
    override val day: Int = 11
    override val parts: List<Part<List<List<Int>>, Int>> = listOf(::part1, ::part2)

    override fun parse(rawInput: Sequence<String>): List<List<Int>> {
        return rawInput.map { it.split("").mapNotNull(String::toIntOrNull).toList() }.toList()
    }

    val dirs = listOf(
        -1 to 0,
        1 to 0,
        0 to 1,
        0 to -1,
        -1 to -1,
        1 to -1,
        -1 to 1,
        1 to 1,
    )

    fun allFlashers(octos: List<MutableList<Int>>, flashed: MutableSet<Position>): Set<Position> {
        val st = mutableSetOf<Position>()
        for (i in octos.indices) {
            for (j in octos[0].indices) {
                if (i to j !in flashed && octos[i][j] > 9L) {
                    st += i to j
                    flashed += i to j
                    octos[i][j] = 0
                }
            }
        }
        return st
    }

    fun Position.allNeighbours(octos: List<List<Int>>, nines: Set<Position>, flashed: Set<Position>): Set<Position> {
        val (i, j) = this
        return buildSet {
            for ((dx, dy) in dirs) {
                val x = i + dx
                val y = j + dy
                if (x in octos.indices && y in octos[0].indices && x to y !in nines && x to y !in flashed) {
                    add(x to y)
                }
            }
        }
    }

    fun stepIt(octos: List<MutableList<Int>>): Int {
        for (i in octos.indices) {
            for (j in octos[0].indices) {
                octos[i][j] = octos[i][j] + 1
            }
        }
        val flashed = mutableSetOf<Position>()
        
        return generateSequence {
            val nines = allFlashers(octos, flashed)
            for ((i, j) in nines) {
                for ((x, y) in (i to j).allNeighbours(octos, nines, flashed)) {
                    octos[x][y] = octos[x][y] + 1
                }
            }
            nines.size
        }.takeWhile { it > 0 }
            .sum()
    }

    fun part1(inp: List<List<Int>>): Int {
        return inp.map { it.toMutableList() }.let { octos ->
            (1..100).fold(0) { acc, _ ->
                acc + stepIt(octos)
            }
        }
    }

    fun part2(inp: List<List<Int>>): Int {
        val total = (inp.size * inp[0].size)

        val octos = inp.map { it.toMutableList() }
        return generateSequence(1) { it + 1 }.takeWhile { stepIt(octos) != total }.last() + 1
    }
}

private typealias Position = Pair<Int, Int>