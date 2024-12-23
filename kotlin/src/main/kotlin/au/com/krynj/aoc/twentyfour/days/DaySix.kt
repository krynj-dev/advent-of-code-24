package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.SimpleObserverContext
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.time.measureTime

class DaySix: AoCDay<List<String>, BigInteger>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Six", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-6.txt"))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-6.txt"))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
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
        var pos = Pair(startloc / inputLines.first().length, startloc % inputLines.first().length)
        var dir = directionMap[inputLines[pos.first][pos.second]]!!
        while (true) {
            if (pos !in visited.keys) visited[pos] = mutableSetOf(dir) else visited[pos]!!.add(dir)
            var curDir = dir
            val next = pickNextLocation(pos.first, pos.second, dir, inputLines) ?: break
            while (curDir != next.second) {
                curDir = rotate90(curDir)
                visited[pos]!!.add(curDir)
            }
            pos = next.first
            dir = next.second
        }
        return visited.size.toBigInteger()
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        var result: MutableSet<Pair<Int, Int>> = mutableSetOf()
        val visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> = mutableMapOf()
        val startloc = inputLines.joinToString("") { it }.indexOfAny("^>V<".toCharArray())
        var pos = Pair(startloc / inputLines.first().length, startloc % inputLines.first().length)
        var dir = directionMap[inputLines[pos.first][pos.second]]!!
        while (true) {
            if (pos !in visited.keys) visited[pos] = mutableSetOf(dir) else visited[pos]!!.add(dir)
            if (willLoop(pos, dir, inputLines) && Pair(pos.first+dir.first, pos.second+dir.second) != Pair(startloc / inputLines.first().length, startloc % inputLines.first().length)
                && Pair(pos.first+dir.first, pos.second+dir.second) !in visited.keys) {
                result.add(Pair(pos.first+dir.first, pos.second+dir.second))
            }
            var curDir = dir
            val next = pickNextLocation(pos.first, pos.second, dir, inputLines) ?: break
            while (curDir != next.second) {
                curDir = rotate90(curDir)
                visited[pos]!!.add(curDir)
            }
            pos = next.first
            dir = next.second
        }
        return result.size.toBigInteger()
    }

    fun willLoop(currentPos: Pair<Int, Int>, currentDir: Pair<Int, Int>, puzzleMap: List<String>): Boolean {
        val alteredMap = puzzleMap.toMutableList()
        val (nextY, nextX) = Pair(currentPos.first + currentDir.first, currentPos.second + currentDir.second)
        if (nextY !in alteredMap.indices || nextX !in alteredMap.first().indices || alteredMap[nextY][nextX] == '#') return false
        alteredMap[nextY] = alteredMap[nextY].replaceRange(nextX..nextX, "#")
//        val scanDir = rotate90(currentDir)
//      Make sure we make it back to self
        val visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> = mutableMapOf(currentPos to mutableSetOf(currentDir))
        var loopPos = currentPos
        var loopDir = currentDir
        var loops = 0
        do {
            loops++
            visited.getOrPut(loopPos) { mutableSetOf() }.add(loopDir)
            val loopNext = pickNextLocation(loopPos.first, loopPos.second, loopDir, alteredMap) ?: return false
            loopPos = loopNext.first
            loopDir = loopNext.second

        } while (!visited.getOrDefault(loopPos, setOf()).contains(loopDir))
        return true
    }

    fun rotate90(dir: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(dir.second, -dir.first)
    }

    override fun addObserver(observer: AoCObserver<SimpleObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: SimpleObserverContext) {
        observers.forEach { it.notify(context) }
    }

    fun pickNextLocation(currentY: Int, currentX: Int, currentDir: Pair<Int, Int>, puzzleMap: List<String>): Pair<Pair<Int, Int>, Pair<Int, Int>>? {
        var (nextY, nextX) = Pair(currentY + currentDir.first, currentX + currentDir.second)
        var nextDir = currentDir
        if (nextY !in puzzleMap.indices || nextX !in puzzleMap.first().indices) return null
        if (puzzleMap[nextY][nextX] == '#') {
            nextDir = rotate90(nextDir)
            nextY = currentY
            nextX = currentX
        }
        return Pair(Pair(nextY, nextX), nextDir)
    }


}