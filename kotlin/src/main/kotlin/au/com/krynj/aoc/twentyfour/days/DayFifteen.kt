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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.measureTime

class DayFifteen : AoCDay<List<List<String>>, BigInteger>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    private val moves: Map<Char, Pair<Int, Int>> = mapOf(
        '^' to Pair(-1, 0),
        '>' to Pair(0, 1),
        'v' to Pair(1, 0),
        '<' to Pair(0, -1)
    )

    private var size: Pair<Int, Int> = Pair(0, 0)

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
        size = Pair(inputLines.first().size, inputLines.first().first().length)
        val positions = parseInput(inputLines.first())
        val newPositions = doMoves(positions, inputLines.last().joinToString(""))
        return calcScore(newPositions)
    }

    override fun partTwo(inputLines: List<List<String>>): BigInteger {
        size = Pair(inputLines.first().size, inputLines.first().first().length*2)
        val positions = parseInput(inputLines.first(), true)
        val newPositions = doMoves(positions, inputLines.last().joinToString(""))
        printPositions(newPositions, size)
        return calcScore(newPositions, '[')
    }

    fun calcScore(grid: Map<Pair<Int, Int>, Char>, target: Char = 'O'): BigInteger {
        return grid.filter { it.value == target }.keys.fold(0) { i, p ->
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

    fun doMoves(grid: Map<Pair<Int, Int>, Char>, moveset: String): Map<Pair<Int, Int>, Char> {
        val newGrid = grid.toMutableMap()
        for (it: Char in moveset) {
//            printPositions(newGrid, size)
            val curSpot = newGrid.filter { e -> e.value == '@' }.keys.first()
            val move = moves[it]!!
            val nextSpot = Pair(curSpot.first+move.first, curSpot.second+move.second)
            // Skip if tyring to move out of bounds
            if ((nextSpot.first < 0 || nextSpot.first >= size.first) || (nextSpot.second < 0 || nextSpot.second >= size.second)) continue
            // Skip if next spot it a wall
            if (newGrid[nextSpot] == '#') continue
            // goto next spot if open
            if (newGrid[nextSpot] == '.') {
                newGrid[curSpot] = '.'
                newGrid[nextSpot] = '@'
                continue
            }
            // if box, try to move
            if (newGrid[nextSpot] == 'O'){
                var nextNotBox = Pair(nextSpot.first, nextSpot.second)
                while (nextNotBox.first in 0..<size.first && nextNotBox.second in 0..<size.second
                    && newGrid[Pair(nextNotBox.first, nextNotBox.second)] == 'O'
                ) {
                    nextNotBox = Pair(nextNotBox.first + move.first, nextNotBox.second + move.second)
                }
                if ((nextNotBox.first !in 0..<size.first) || nextNotBox.second !in 0..<size.second
                    || newGrid[nextNotBox] != '.'
                ) continue

                newGrid[curSpot] = '.'
                newGrid[nextSpot] = '@'
                newGrid[nextNotBox] = 'O'
            } else {
                // Part 2 code
                val boxQueue: Queue<Pair<Int, Int>> = LinkedList()
                boxQueue.add(nextSpot)
                if (newGrid[nextSpot] == '[' && it in "^v") boxQueue.add(Pair(nextSpot.first, nextSpot.second+1))
                else if (newGrid[nextSpot] == ']' && it in "^v") boxQueue.add(Pair(nextSpot.first, nextSpot.second-1))
                val posStack: Stack<Pair<Int, Int>> = Stack()
                var doMove = true
                while (boxQueue.isNotEmpty()) {
                    val pos = boxQueue.remove()
                    val curChar = newGrid[pos]!!
                    val nextPos = Pair(pos.first+move.first, pos.second+move.second)
                    val nextChar = newGrid[nextPos]!!
                    if (nextChar == '#') {
                        doMove = false
                        break
                    }
                    posStack.add(pos)
                    if (nextChar == '.') {
                        continue
                    }
                    if (nextPos !in boxQueue) {
                        boxQueue.add(nextPos)
                    }
                    if (curChar != nextChar && it !in "<>") {
                        val offset = if (nextChar == ']') -1 else 1
                        val diagPos = Pair(nextPos.first, nextPos.second+offset)
                        if (diagPos !in boxQueue) {
                            boxQueue.add(diagPos)
                        }
                    }
                }
                if (!doMove) continue
                while (posStack.isNotEmpty()) {
                    val pos = posStack.pop()
                    val moveTo = Pair(pos.first+move.first, pos.second+move.second)
                    newGrid[moveTo] = newGrid[pos]!!
                    newGrid[pos] = '.'
                }
                newGrid[curSpot] = '.'
                newGrid[nextSpot] = '@'
            }
        }
        return newGrid
    }

    fun parseInput(grid: List<String>, widen: Boolean = false): Map<Pair<Int, Int>, Char> {
        val theMap: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (widen) {
                    theMap[Pair(y, 2*x)] = if (c == 'O') '[' else c
                    theMap[Pair(y, 2*x+1)] = when(c) {
                        '.', '@' -> '.'
                        '#' -> '#'
                        'O' -> ']'
                        else -> '?'
                    }
                } else {
                    theMap[Pair(y, x)] = c
                }
            }
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
