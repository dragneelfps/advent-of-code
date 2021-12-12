object Day12 : Problem<Map<String, List<String>>, Long> {
    override val day: Int = 12
    override val parts: List<Part<Map<String, List<String>>, Long>> = listOf(::part1, ::part2)

    override fun parse(rawInput: Sequence<String>): Map<String, List<String>> {
        val adj = mutableMapOf<String, List<String>>()
        rawInput.forEach {
            val (x, y) = it.split("-")
            adj[x] = (adj[x] ?: emptyList()) + y
            adj[y] = (adj[y] ?: emptyList()) + x
        }
        println(adj)
        return adj
    }

    fun part1(adj: Map<String, List<String>>): Long {
        val done = mutableSetOf<String>()
        var ans = 0L

        fun dfs(x: String) {
            if (x == "end") {
                ans++
                return
            }
            for (y in adj[x] ?: emptyList()) {
                if (y !in done) {
                    if (y.lowercase() == y) {
                        done += y
                    }
                    dfs(y)
                    if (y.lowercase() == y) {
                        done -= y
                    }
                }
            }
        }

        done += "start"
        dfs("start")
        return ans
    }

    fun part2(adj: Map<String, List<String>>): Long {
        val done = mutableSetOf<String>()
        var ans = 0L

        fun dfs(x: String, chosen: Boolean = false) {
            if (x == "end") {
                ans++
                return
            }
            for (y in adj[x] ?: emptyList()) {
                if (y !in done) {
                    if (y.lowercase() == y) {
                        done += y
                        dfs(y, chosen)
                        done -= y
                    } else {
                        dfs(y, chosen)
                    }
                } else if (!chosen && y !in listOf("start", "end")) {
                    dfs(y, true)
                }
            }
        }

        done += "start"
        dfs("start")
        return ans
    }
}