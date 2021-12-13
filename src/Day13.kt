object Day13 : Problem<Day13.Inp, Int> {
    override val day: Int = 13
    override val parts: List<Part<Inp, Int>> = listOf(::part1, ::part2)

    override fun parse(rawInput: Sequence<String>): Inp {
        val f = mutableListOf<String>()
        val s = mutableListOf<String>()
        var split = false
        for (line in rawInput) {
            if (line.isEmpty()) {
                split = true
                continue
            }
            if (!split) {
                f += line
            } else {
                s += line
            }
        }

        val xys = f.filter { it.isNotEmpty() }.map { it.split(",").map { it.toInt() } }
        var row = 1
        var col = 1
        for ((x, y) in xys) {
            col = maxOf(x + 1, col)
            row = maxOf(y + 1, row)
        }
        val grid = List(row) { MutableList(col) { "." } }
        for ((x, y) in xys) {
            grid[y][x] = "#"
        }

        val folds: List<Fold> = s.map { it.substringAfter("fold along ") }
            .map { it.split("=") }
            .map { (xy, v) -> if (xy == "y") Up(v.toInt()) else Left(v.toInt()) }
        return Inp(grid, folds)
    }


    fun foldUp(grid: Grid, y: Int): Grid {
        val row = grid.size
        val newRow = y + 1
        val cols = grid.first().size
        val newGrid = List(newRow) { i -> MutableList(cols) { j -> grid[i][j] } }
        for (i in y + 1 until row) {
            for (j in 0 until cols) {
                if (grid[i][j] == "#") {
                    newGrid[y - (i - y)][j] = grid[i][j]
                }
            }
        }
        return newGrid
    }

    fun foldLeft(grid: Grid, x: Int): Grid {
        val row = grid.size
        val col = grid.first().size
        val newCol = x + 1
        val newGrid = List(row) { i -> MutableList(newCol) { j -> grid[i][j] } }
        for (i in 0 until row) {
            for (j in x + 1 until col) {
                if (grid[i][j] == "#") {
                    newGrid[i][x - (j - x)] = grid[i][j]
                }

            }
        }
        return newGrid
    }

    fun part1(inp: Inp): Int {
        val fold = inp.folds.first()
        val grid = when (fold) {
            is Up -> foldUp(inp.grid, fold.y)
            is Left -> foldLeft(inp.grid, fold.x)
        }
        var ct = 0
        for (row in grid) {
            for (x in row) {
                if (x == "#") {
                    ct++
                }
            }
        }
        return ct
    }

    fun part2(inp: Inp): Int {
        val finalGrid = inp.folds.fold(inp.grid) { grid, fold ->
            when (fold) {
                is Up -> foldUp(grid, fold.y)
                is Left -> foldLeft(grid, fold.x)
            }
        }
        for (row in finalGrid) {
            println(row)
        }
        return 0
    }

    data class Inp(val grid: Grid, val folds: List<Fold>)
    sealed class Fold
    class Up(val y: Int) : Fold()
    class Left(val x: Int) : Fold()
}

private typealias Grid = List<List<String>>

