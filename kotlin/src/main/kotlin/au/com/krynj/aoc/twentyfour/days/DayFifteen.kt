package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.AoCObserverContext
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.time.measureTime

class DayFifteen : AoCDay<List<List<String>>>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    private val moves: Map<Char, Pair<Int, Int>> = mapOf(
        '^' to Pair(-1, 0),
        '>' to Pair(0, 1),
        'v' to Pair(1, 0),
        '<' to Pair(0, -1)
    )

    override fun run() {
        println(addColour("Day Fifteen", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-15.txt", ""))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-15.txt", ""))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 15
    }

    override fun partOne(inputLines: List<List<String>>): BigInteger {
        val positions = parseInput(inputLines.first())
        val newPositions = doMoves(positions, inputLines.last().joinToString(""), Pair(inputLines.first().size, inputLines.first().first().length))
        return calcScore(newPositions)
    }

    override fun partTwo(inputLines: List<List<String>>): BigInteger {
        return 1.toBigInteger()
    }

    fun calcScore(grid: Map<Pair<Int, Int>, Char>): BigInteger {
        return grid.filter { it.value == 'O' }.keys.fold(0) { i, p ->
            i + (100*p.first + p.second)
        }.toBigInteger()
    }

    fun printPositions(grid: Map<Pair<Int, Int>, Char>, mapSize: Pair<Int, Int>) {
        (0..<mapSize.first).forEach { i ->
            println((0..<mapSize.second).map { j ->
                grid[Pair(i, j)]
            }.joinToString(" "))
        }
    }

    fun doMoves(grid: Map<Pair<Int, Int>, Char>, moveset: String, mapSize: Pair<Int, Int>): Map<Pair<Int, Int>, Char> {
        val newGrid = grid.toMutableMap()
        for (it: Char in moveset) {
//            printPositions(newGrid, mapSize)
            val curSpot = newGrid.filter { e -> e.value == '@' }.keys.first()
            val move = moves[it]!!
            val nextSpot = Pair(curSpot.first+move.first, curSpot.second+move.second)
            // Skip if tyring to move out of bounds
            if ((nextSpot.first < 0 || nextSpot.first >= mapSize.first) || (nextSpot.second < 0 || nextSpot.second >= mapSize.second)) continue
            // Skip if next spot it a wall
            if (newGrid[nextSpot] == '#') continue
            // goto next spot if open
            if (newGrid[nextSpot] == '.') {
                newGrid[curSpot] = '.'
                newGrid[nextSpot] = '@'
                continue
            }
            // if box, try to move
            var nextNotBox = Pair(nextSpot.first, nextSpot.second)
            while (nextNotBox.first in 0..<mapSize.first && nextNotBox.second in 0..<mapSize.second
                && newGrid[Pair(nextNotBox.first, nextNotBox.second)] == 'O') {
                nextNotBox = Pair(nextNotBox.first+move.first, nextNotBox.second+move.second)
            }
            if ((nextNotBox.first !in 0..<mapSize.first) || nextNotBox.second !in 0..<mapSize.second
                || newGrid[nextNotBox] != '.') continue

            newGrid[curSpot] = '.'
            newGrid[nextSpot] = '@'
            newGrid[nextNotBox] = 'O'
        }
        return newGrid
    }

    fun parseInput(grid: List<String>): Map<Pair<Int, Int>, Char> {
        val theMap: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        (0..<grid.size*grid.first().length).forEach {
            val y = it / grid.first().length
            val x = it % grid.first().length
            theMap[Pair(y, x)] = grid[y][x]
        }
        return theMap
    }

    override fun addObserver(observer: AoCObserver<AoCObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: AoCObserverContext) {
        observers.forEach {
            it.notify(context)
        }
    }

}
