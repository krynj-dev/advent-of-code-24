package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import au.com.krynj.aoc.util.ds.DirectedGraph
import au.com.krynj.aoc.util.ds.Tuple
import java.lang.Math.max
import java.lang.Math.pow
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO
import java.util.LinkedList
import kotlin.math.log2
import kotlin.time.measureTime


class DayEleven : AoCDay<List<Int>>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Eleven", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readLineAsInt(AoCUtil.readResourceFile("input-11.txt").first(), digits = false))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readLineAsInt(AoCUtil.readResourceFile("input-11.txt").first(), digits = false))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 11
    }

    override fun partOne(inputLines: List<Int>): BigInteger {
        val stones = inputLines.map { it.toBigInteger() }
        val depth = 25
        val childrens = stones.map {
            val g = bfsGraph(it, DirectedGraph(), depth)
            g.edges.filter { it.weight == depth }.size.toBigInteger()
        }
        return childrens.reduce(BigInteger::add)
    }

    fun oldPartOne(depth: Int, stones: List<BigInteger>): BigInteger {
        var newStones = stones.toList()
        (0..depth).forEach {
            newStones = newStones.flatMap { blink(listOf(it)) }
        }
        return newStones.size.toBigInteger()
    }

    fun blink(stones: List<BigInteger>): List<BigInteger> {
        return stones.flatMap { applyRule(it) }
    }

    fun applyRule(stone: BigInteger): List<BigInteger> {
        if (stone == 0.toBigInteger()) return listOf(1.toBigInteger())
        val stoneStr = stone.toString()
        val digits = stoneStr.length
        if (digits % 2 == 0) return listOf(
            stoneStr.substring(0..<digits / 2).toBigInteger(), stoneStr.substring(digits / 2..<digits).toBigInteger()
        )
        return listOf(stone.multiply(2024.toBigInteger()))
    }

    fun blinkRec(
        stone: BigInteger, depth: Int, maxDepth: Int, memo: MutableMap<BigInteger, MutableList<MutableList<BigInteger>>>
    ): BigInteger {
        if (depth == maxDepth) {
            return ONE
        }
        // Do some memoized checking
        return applyRule(stone).map { blinkRec(it, depth + 1, maxDepth, memo) }.reduce(BigInteger::add)
    }

    override fun partTwo(inputLines: List<Int>): BigInteger {
        val shortcuts = digitToDouble()
        return ONE
//        val stones = inputLines.map { it.toBigInteger() }
//        val depth = 75
//        val childrens = stones.map {
//            val g = bfsGraph(it, DirectedGraph(), depth)
//            g.edges.filter { it.weight == depth }.size.toBigInteger()
//        }
//        return childrens.reduce(BigInteger::add)
    }

    override fun addObserver(observer: AoCObserver<AoCObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: AoCObserverContext) {
        observers.forEach {
            it.notify(context)
        }
    }

    fun bfsGraph(stone: BigInteger, g: DirectedGraph<BigInteger>, maxDepth: Int): DirectedGraph<BigInteger> {
        if (g.getNodeByValue(stone) != null) return g
        val sQueue = LinkedList<List<BigInteger>>()
        sQueue.add(listOf(stone, (-1).toBigInteger()))
        while (sQueue.isNotEmpty()) {
            // Get the current stone
            val s = sQueue.remove()
            // if we've already made this stone, link it to the parent and go to next loop
            if (g.getNodeByValue(s[0]) != null) {
                g.addEdge(g.getNodeByValue(s[1])!!, g.getNodeByValue(s[0])!!)
                continue
            }
            val children = applyRule(s[0])
            sQueue.addAll(children.map {
                listOf(it, s[0])
            }) // .filter { g.edges.find { it.from.value == s.first && it.to.value == s.first } != null }
            val node = g.getNodeByValue(s[0]) ?: g.addNode(s[0])
            val o = g.getNodeByValue(s[1])
            if (o != null) g.addEdge(o, node)
        }
        return g
    }

    fun dfsCycles(
        stone: BigInteger,
        g: DirectedGraph<BigInteger>,
        visitStack: List<BigInteger>,
        cycles: MutableList<List<BigInteger>>,
        depth: Int,
        maxDepth: Int,
        numCycleMap: MutableMap<BigInteger, MutableList<Int>>
    ) {
        if (stone == null) return
        // Check if we're in a cycle
        val inCycles = cycles.filter { it == stone }
        if (inCycles.isNotEmpty()) { // No need to mark it down twice
            inCycles.forEachIndexed { i, _ ->
                visitStack.forEach { numCycleMap.getOrPut(it) { mutableListOf() }.add(i) }
            }
            return
        }
        // Check if we've created a cycle
        if (stone in visitStack || depth >= maxDepth) {
            cycles.add(visitStack.subList(0.coerceAtLeast(visitStack.indexOf(stone)), visitStack.size).map { it })
            visitStack.subList(0, 0.coerceAtLeast(visitStack.indexOf(stone)))
                .forEach { numCycleMap.getOrPut(it) { mutableListOf() }.add(cycles.size - 1) }
            return
        }
        // Check children
        applyRule(stone).forEach {
            dfsCycles(
                it, g, visitStack.toList() + listOf(stone), cycles, depth + 1, maxDepth, numCycleMap
            )
        }
    }

    fun dfsTo(
        stone: BigInteger,
        g: DirectedGraph<BigInteger>,
        visitStack: List<BigInteger>,
        cycles: MutableList<List<BigInteger>>,
        depth: Int,
        maxDepth: Int,
        numCycleMap: MutableMap<BigInteger, MutableList<Int>>,
        to: BigInteger
    ) {
        if (stone == null || depth > maxDepth) return
        // Unique paths only
        if (cycles.any { it.contains(stone) && stone != to }) return
        // Check if we've created a cycle
        if (stone == to) {
            cycles.add(visitStack + listOf(to))
            return
        }
        // Check children
        applyRule(stone).forEach {
            dfsCycles(
                it, g, visitStack.toList() + listOf(stone), cycles, depth + 1, maxDepth, numCycleMap
            )
        }
    }


    /* just to get this revelation onto "paper":
        if digits are even, dont split all: just immediately break into digits
        e.g. 82108495 -> dont do (8210 8495), just immediately skip to (8 2 1 0 8 4 9 5)
        log2(len(n)) = steps skipped
        other revelation:
            find formula to know ho many times to X 2024 to get even digits
            nevermind, it doesn't exist, it's novel apparently
     */

    fun digitToDouble(): Map<BigInteger, Pair<List<BigInteger>, Int>> {
        return (0..9).associate {
            var stone = it.toBigInteger()
            var blinks = 0
            while (stone.toString().length % 2 != 0) {
                blinks++
                stone = blink(listOf(stone)).first()
            }
            val stoneStr = stone.toString()
            it.toBigInteger() to Pair(
                stoneStr.toCharArray().map { c -> c.digitToInt().toBigInteger() },
                blinks + log2(stoneStr.length.toDouble()).toInt()
            )
        }
    }

    fun getShortCut(
        s: BigInteger,
        digitMap: Map<BigInteger, Pair<List<BigInteger>, Int>>
    ): Tuple<BigInteger, List<BigInteger>, Int> {
        var stone = s
        var blinks = 0
        do {
            blinks++
            stone = blink(listOf(stone)).first()
        } while (stone.toString().length % 2 != 0)
        val splitStones = blink(listOf(stone)).toMutableList()
        if (splitStones.last() == ZERO) {
            splitStones.removeAt(splitStones.size - 1)
            splitStones.addAll(getAt(ZERO, digitMap, blinks - 1))
        }
        return Tuple(s, splitStones, blinks)
    }

    fun getAt(s: BigInteger, digitMap: Map<BigInteger, Pair<List<BigInteger>, Int>>, depth: Int): List<BigInteger> {
        val digit = digitMap[s] ?: return listOf()
        val depthDiff = depth - digit.second
        if (depthDiff == 0) return digit.first
        var i = 0
        var stones = digit.first
        if (depthDiff < 0) {
            while (i > depthDiff) {
                i--
                if (stones.size > 1) {
                    stones =
                        (stones.indices step 2).map { x -> BigInteger(stones[x].toString() + stones[x + 1].toString()) }
                } else {
                    stones = stones.map { it.divide(2024.toBigInteger()) }
                }
            }
        } else {
            while (i < depthDiff) {
                i++
                stones = blink(stones)
            }
        }
        return stones
    }
}
