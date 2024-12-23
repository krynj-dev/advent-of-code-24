package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCAlgorithmUtil
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.time.measureTime

class DayTen : AoCDay<List<List<Int>>, BigInteger>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Ten", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-10.txt").map { AoCUtil.readLineAsInt(it, true) })
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-10.txt").map { AoCUtil.readLineAsInt(it, true) })
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 10
    }

    override fun partOne(inputLines: List<List<Int>>): BigInteger {
        var totalScores = 0
        val trailheads = inputLines.flatMapIndexed { y, row -> row.mapIndexed { x, v -> if (v == 0) Pair(y, x) else null }.toList().filterNotNull() }
        trailheads.forEach { th ->
            totalScores += dfs(listOf(), th, inputLines, mutableMapOf(), 1).toSet().size
            broadcast(SimpleObserverContext(totalScores.toBigInteger()))
        }
        return totalScores.toBigInteger()
    }

    override fun partTwo(inputLines: List<List<Int>>): BigInteger {
        var totalScores = 0
        val trailheads = inputLines.flatMapIndexed { y, row -> row.mapIndexed { x, v -> if (v == 0) Pair(y, x) else null }.toList().filterNotNull() }
        trailheads.forEach { th ->
            totalScores += dfs(listOf(), th, inputLines, mutableMapOf(), 1).size
            broadcast(SimpleObserverContext(totalScores.toBigInteger()))
        }
        return totalScores.toBigInteger()
    }

    fun dfs(prevPos: List<Pair<Int, Int>>, curPos: Pair<Int, Int>, puzzleMap: List<List<Int>>, dp: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>, maxJump: Int): List<Pair<Int, Int>> {
        if (curPos in dp.keys) {
            return dp[curPos]!!
        }
        val curHeight = puzzleMap[curPos.first][curPos.second]
        if (puzzleMap[curPos.first][curPos.second] == 9) {
//            printMap(prevPos.toMutableList() + listOf(curPos), puzzleMap)
            dp[curPos] = listOf(curPos)
            return listOf(curPos)
        }
        var reachable: MutableList<Pair<Int, Int>> = mutableListOf()
        AoCAlgorithmUtil.findAround(curPos, puzzleMap, curHeight+maxJump, 1, false).filter { it !in prevPos }.forEach {
            reachable.addAll(dfs(prevPos.toMutableList() + mutableListOf(curPos), it, puzzleMap, dp, maxJump))
        }
        dp[curPos] = reachable
        return reachable
    }

    fun printMap(highlightPos: List<Pair<Int, Int>>, puzzleMap: List<List<Int>>) {
        // TODO Make a visualiser observer
        println("-".repeat(puzzleMap.first().size))
        puzzleMap.mapIndexed { y, row -> println(row.mapIndexed { x, v -> if (Pair(y, x) in highlightPos) addColour("${puzzleMap[y][x]}", YELLOW) else "${puzzleMap[y][x]}" }.joinToString()) }
        println("-".repeat(puzzleMap.first().size))
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
