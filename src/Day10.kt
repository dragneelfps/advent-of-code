import java.util.Stack

object Day10 : Problem<List<String>, Long> {
    override val day: Int = 10
    override val parts: List<Part<List<String>, Long>> = listOf(::part1, ::part2)
    override fun parse(rawInput: Sequence<String>): List<String> {
        return rawInput.toList()
    }

    val bMap = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<',
    )

    fun Char.pts() = when(this) {
        ')' -> 3L
        ']' -> 57L
        '}' -> 1197L
        '>' -> 25137L
        else -> throw Exception()
    }

    fun Char.pts2() = when(this) {
        '(' -> 1L
        '[' -> 2L
        '{' -> 3L
        '<' -> 4L
        else -> throw Exception()
    }

    fun part1(lines: List<String>): Long {
        var ans = 0L
        for (line in lines) {
            val st = Stack<Char>()
            for (ch in line) {
                when(ch) {
                    '(', '[', '{', '<' -> st.push(ch)
                    else -> {
                        if (st.isEmpty() || bMap[ch] != st.pop()) {
                            ans += ch.pts()
                            break
                        }
                    }
                }
            }

        }
        return ans
    }

    fun part2(lines: List<String>): Long {
        val scores = mutableListOf<Long>()
        for (line in lines) {
            val st = Stack<Char>()
            var errFound = false
            for (ch in line) {
                when(ch) {
                    '(', '[', '{', '<' -> st.push(ch)
                    else -> {
                        if (st.isEmpty() || bMap[ch] != st.pop()) {
                            errFound = true
                            break
                        }
                    }
                }
            }
            if (errFound) continue
            var score = 0L
            while (st.isNotEmpty()) {
                score = (score * 5) + st.pop().pts2()
            }
            scores += score
        }
        return scores.sorted()[scores.size / 2]

    }
}