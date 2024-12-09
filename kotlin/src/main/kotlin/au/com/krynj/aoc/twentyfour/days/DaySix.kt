package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.framework.SimpleObserverContext
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.math.ln

class DaySix: AoCDay<List<String>>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

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
        // Second Answer 1388 too low, 3412 too high, 3176 wrong
        var result: MutableSet<Pair<Int, Int>> = mutableSetOf()
        val visited: MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> = mutableMapOf()
        val startloc = inputLines.joinToString("") { it }.indexOfAny("^>V<".toCharArray())
        var pos = Pair(startloc / inputLines.first().length, startloc % inputLines.first().length)
        var dir = directionMap[inputLines[pos.first][pos.second]]!!
        while (true) {
            if (pos !in visited.keys) visited[pos] = mutableSetOf(dir) else visited[pos]!!.add(dir)
            if (willLoop(pos, dir, inputLines) && Pair(pos.first+dir.first, pos.second+dir.second) != Pair(startloc / inputLines.first().length, startloc % inputLines.first().length)) {
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
        val (nextY, nextX) = Pair(currentPos.first + currentDir.first, currentPos.second + currentDir.second)
        if (nextY !in puzzleMap.indices || nextX !in puzzleMap.first().indices || puzzleMap[nextY][nextX] == '#') return false
        val scanDir = rotate90(currentDir)
//      Make sure we make it back to self
        var loopPos = currentPos
        var loopDir = scanDir
        var loops = 0
        do {
            loops++
            val loopNext = pickNextLocation(loopPos.first, loopPos.second, loopDir, puzzleMap) ?: return false
            loopPos = loopNext.first
            loopDir = loopNext.second
        } while (!(loopPos == currentPos && loopDir == currentDir) && loops < 1000)
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
        while (puzzleMap[nextY][nextX] == '#') {
            nextDir = rotate90(nextDir)
            nextY = currentY + nextDir.first
            nextX = currentX + nextDir.second
        }
        return Pair(Pair(nextY, nextX), nextDir)
    }


}