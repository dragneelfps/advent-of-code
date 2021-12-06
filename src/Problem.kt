@file:OptIn(ExperimentalTime::class)

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

typealias Part<T, V> = (input: T) -> V

interface Problem<T: Any,V: Any> {
    val day: Int
    val parts: List<Part<T, V>>
    fun parse(rawInput: Sequence<String>): T
    fun solve() {
        val input = readInput(day).lineSequence()
        println("Problem ${day}:")
        for ((partIdx, part) in parts.withIndex()) {
            val out = measureTimedValue { part(parse(input)) }

            println("Part ${partIdx + 1} (took ${out.duration}): ${out.value}")
        }
    }
}