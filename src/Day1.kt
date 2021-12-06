object Day1 : Problem<List<Int>, Int> {
    override val day: Int = 1
    override val parts: List<(List<Int>) -> Int> = listOf(::part1, ::part2)

    override fun parse(rawInput: Sequence<String>): List<Int> = rawInput.map(String::toInt).toList()


    private fun part1(nums: List<Int>): Int = nums.zipWithNext().count { (a,b) -> b > a }

    private fun part2(nums: List<Int>): Int = nums.windowed(4).count { lst -> lst.last() > lst.first() }
}