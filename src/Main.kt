@file:OptIn(ExperimentalTime::class)

import kotlin.time.ExperimentalTime

fun main(args: Array<String>) {
    val problems = listOf(
        Day1,
        Day6,
        Day7,
    )

    for (problem in problems) {
        problem.solve()
    }

}