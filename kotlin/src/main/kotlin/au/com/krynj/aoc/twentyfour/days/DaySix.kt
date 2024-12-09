package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger

class DaySix: AoCDay<List<String>>, AoCObservable {

    private val observers: MutableList<AoCObserver> = ArrayList()

    override fun run() {
        println(addColour("Day Six", CYAN))
        println("Part 1: " + addColour("%d", GREEN)
            .format(partOne(AoCUtil.readResourceFile("daysix/input.txt"))))
        println("Part 2: " + addColour("%d", GREEN)
            .format(partTwo(AoCUtil.readResourceFile("daysix/input.txt"))))
    }

    override fun getDay(): Int {
        return 6
    }

    val directionMap = mapOf(
        '^' to Pair(-1, 0),
        '>' to Pair(0, 1),
        'V' to Pair(1, 0),
        '<' to Pair(0, -1),
    )

    override fun partOne(inputLines: List<String>): BigInteger {
        val visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> = mutableMapOf()

        val startloc = inputLines.joinToString("") { it }.indexOfAny("^>V<".toCharArray())
        val y = startloc / inputLines.first().length
        val x = startloc % inputLines.first().length
        val dir = directionMap[inputLines[y][x]]!!
        mainLoop(inputLines, y, x, dir, visited)
        return visited.size.toBigInteger()
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        // Second Answer 1388 too low
        val visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> = mutableMapOf()

        val startloc = inputLines.joinToString("") { it }.indexOfAny("^>V<".toCharArray())
        val y = startloc / inputLines.first().length
        val x = startloc % inputLines.first().length

        val dir = directionMap[inputLines[y][x]]!!
        mainLoop(inputLines, y, x, dir, visited)

        return mainLoop(inputLines, y, x, dir, visited).toBigInteger()
    }

    fun mainLoop(puzzleMap: List<String>, startY: Int, startX: Int, startDir: Pair<Int, Int>,
                 visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int,Int>>>): Int {
        var barricades = mutableSetOf<Pair<Int, Int>>()
        var y = startY
        var x = startX
        var dir = startDir
        while (y in puzzleMap.indices && x in puzzleMap.first().indices) {
            var nextY = y + dir.first
            var nextX = x + dir.second
            if (nextX in puzzleMap.first().indices && nextY in puzzleMap.indices) {
                if (puzzleMap[nextY][nextX] == '#') {
                    dir = rotate90(dir)
                    nextY = y + dir.first
                    nextX = x + dir.second
                } else {
                    // Scan for next # 90 degrees
                    val scanDir = rotate90(dir)
//                    if (doesLoop(y, x, puzzleMap, scanDir)) { // Brute force no good
//                        barricades.add(Pair(nextY, nextX))
//                    }
                    if (doesLoopFast(y, x, puzzleMap, scanDir, visited))  barricades.add(Pair(nextY, nextX))
                }
            }
            visited.putIfAbsent(Pair(y, x), mutableSetOf(dir))
            visited[Pair(y, x)]!!.add(dir)
            y = nextY
            x = nextX
        }
        return barricades.size
    }

    fun doesLoop(startY: Int, startX: Int, puzzleMap: List<String>, startDir: Pair<Int, Int>): Boolean {
        val visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> = mutableMapOf()
        var y = startY
        var x = startX
        var dir = startDir
        while (y in puzzleMap.indices && x in puzzleMap.first().indices) {
            var nextY = y + dir.first
            var nextX = x + dir.second
            if (nextX in puzzleMap.first().indices && nextY in puzzleMap.indices) {
                if (y == startY && x == startX && visited[Pair(y, x)]?.contains(startDir) == true) return true
                if (puzzleMap[nextY][nextX] == '#') {
                    dir = rotate90(dir)
                    nextY = y + dir.first
                    nextX = x + dir.second
                }
            }
            visited.putIfAbsent(Pair(y, x), mutableSetOf(dir))
            visited[Pair(y, x)]!!.add(dir)
            y = nextY
            x = nextX
        }
        return false
    }

    fun doesLoopFast(startY: Int, startX: Int, puzzleMap: List<String>, dir: Pair<Int, Int>,
                     visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>): Boolean {
        var y = startY
        var x = startX
        while (y + dir.first in puzzleMap.indices && x + dir.second in puzzleMap.first().indices
            && puzzleMap[y][x] != '#') {
            val nextY = y + dir.first
            val nextX = x + dir.second
            if (puzzleMap[nextY][nextX] == '#' && (visited[Pair(y, x)]?.contains(rotate90(dir)) == true
                        || visited[Pair(y, x)]?.contains(dir) == true)) {
                return true
            }
            y = nextY
            x = nextX
        }
        return false
    }

    fun rotate90(dir: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(dir.second, -dir.first)
    }

    override fun addObserver(observer: AoCObserver) {
        observers.add(observer)
    }

    override fun broadcast(partialResult: BigInteger) {
        observers.forEach { it.notify(partialResult) }
    }


}