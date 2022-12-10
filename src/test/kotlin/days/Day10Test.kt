package days

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day10Test {

    @Test
    fun day10part1Test() {
        assertEquals(14060, day10part1())
    }

    @Test
    fun day10part2Test() {
        val output = """
            # # # . . . # # . . # # # . . # . . # . # # # # . # . . # . # # # # . . . # # .
            # . . # . # . . # . # . . # . # . # . . # . . . . # . # . . # . . . . . . . # .
            # . . # . # . . # . # . . # . # # . . . # # # . . # # . . . # # # . . . . . # .
            # # # . . # # # # . # # # . . # . # . . # . . . . # . # . . # . . . . . . . # .
            # . . . . # . . # . # . . . . # . # . . # . . . . # . # . . # . . . . # . . # .
            # . . . . # . . # . # . . . . # . . # . # . . . . # . . # . # # # # . . # # . .
            .
            """.trimIndent()
        
        assertEquals(output, day10part2())
    }
}