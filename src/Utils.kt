@file:OptIn(ExperimentalTime::class)

import java.io.File
import kotlin.time.ExperimentalTime

fun readInput(day: Int) = File("inputs", "day$day.txt").readText()