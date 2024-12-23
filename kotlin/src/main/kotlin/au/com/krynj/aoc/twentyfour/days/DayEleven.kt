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
import kotlin.math.min
import kotlin.time.measureTime
import kotlin.time.times


class DayEleven : AoCDay<List<Int>, BigInteger>, AoCObservable<AoCObserverContext> {

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

    fun digitToDouble(): Map<BigInteger, MutableList<Pair<List<BigInteger>, Int>>> {
        return (0..9).associate {
            var stone = it.toBigInteger()
            var blinks = 0
            while (stone.toString().length % 2 != 0) {
                blinks++
                stone = blink(listOf(stone)).first()
            }
            val stoneStr = stone.toString()
            it.toBigInteger() to mutableListOf(Pair(
                stoneStr.toCharArray().map { c -> c.digitToInt().toBigInteger() },
                blinks + log2(stoneStr.length.toDouble()).toInt())
            )
        }
    }

    fun getShortCut(
        s: BigInteger,
        digitMap: Map<BigInteger, MutableList<Pair<List<BigInteger>, Int>>>,
        callStack: List<BigInteger>
    ): MutableList<Pair<List<BigInteger>, Int>> {
        if (digitMap[s] != null) return digitMap[s]!!
        // if not present, blink then add a flattened call on splits
        val stones = blink(listOf(s))
        return stones.map { getShortCut(it, digitMap, callStack.toMutableList() + mutableListOf(it)).map { p -> Pair(p.first, p.second+1) } }.flatten().toMutableList()
    }

//    fun getShortCutRec(s: BigInteger,
//                       digitMap: Map<BigInteger, MutableList<Pair<List<BigInteger>, Int>>>
//    ): Tuple<BigInteger, List<BigInteger>, Int> {
//        if
//        var stone = s
//        var blinks = 0
//    }

    fun getAt(s: BigInteger, digitMap: Map<BigInteger, MutableList<Pair<List<BigInteger>, Int>>>, depth: Int): List<BigInteger> {
        val digit = digitMap[s] ?: return mutableListOf()
        return digit.map {
            val depthDiff = depth - it.second
            if (depthDiff == 0) return it.first
            var i = 0
            var stones = it.first
            if (depthDiff < 0) {
                while (i > depthDiff) {
                    i--
                    stones = if (stones.size > 1) {
                        (stones.indices step 2).map { x -> BigInteger(stones[x].toString() + stones[x + 1].toString()) }
                    } else {
                        stones.map { s -> s.divide(2024.toBigInteger()) }
                    }
                }
            } else {
                while (i < depthDiff) {
                    i++
                    stones = blink(stones)
                }
            }
            stones
        }.flatten()

    }

    fun getAtNew(s: BigInteger, digitMap: Map<BigInteger, MutableList<Pair<List<BigInteger>, Int>>>, depth: Int): List<BigInteger> {
        var digit = digitMap[s]?.toMutableList() ?: return mutableListOf()
        // start by overshooting
        while (digit.any { it.second < depth }) {
            var digitSize = digit.size
            (0..<digitSize).forEach {  i ->
                if (digit[i].second < depth) {
                    val newDigits = digit[i].first.map { d -> digitMap[d]?.toMutableList() ?: mutableListOf() }.flatten().map { Pair(it.first, it.second+digit[i].second) }
                    digit.removeAt(i)
                    digit.addAll(i, newDigits)
                    digitSize = digit.size
                }
            }
        }
        var g = 0
        while (digit.any { it.second != depth } && g < 100) {
            g++
            var digitSize = digit.size
            (0..<digitSize).forEach {  i ->
                if (digit[i].first.size % 2 == 0 && digit[i].second > depth) {
                    val moves = min(log2(digit[i].first.size.toDouble()).toInt(), digit[i].second-depth)
                    var x = 0
                    var newDigits = digit[i].first.map { it.toString() }
                    do {
                        newDigits = (0..<newDigits.size/2).mapIndexed { j, _ ->
                            listOf(newDigits[2*j], newDigits[2*j+1]).joinToString("")
                        }
                        x++
                    } while (x < moves)
                    digit[i] = Pair(newDigits.map { it.toBigInteger() }, digit[i].second-moves)
                }
                while (digit[i].first.size == 1 && digit[i].first.first().rem(2024.toBigInteger()) == ZERO && digit[i].second > depth) {
                    digit[i] = Pair(listOf(digit[i].first.first().div(2024.toBigInteger())), digit[i].second-1)
                }
            }
            // Do concat
            var x = 0
            var y = 1
            digitSize = digit.size
            while (x < digitSize) {
                while (y < digitSize && digit[y].first.first().toString().length == digit[x].first.first().toString().length
                    && digit[y].second == digit[x].second && digit[x].second != depth
                    && digit.subList(x, y).joinToString("") { it.first.joinToString("") }.toBigInteger().mod(2024.toBigInteger()) != ZERO) {
                    y++
                }

                // combine
                val newDigits = digit.subList(x, y).joinToString("") { it.first.joinToString("") }
                val curDepth = digit[x].second
                if (digit[x].second != depth) {
                    (x..<y).forEach { _ ->
                        digit.removeAt(x)
                    }
                    digit.add(
                        x,
                        Pair(listOf(newDigits.toBigInteger()), curDepth - (log2((y-x).toDouble()).toInt()))
                    )
                }
                x = x+1
                y = x+1
                digitSize = digit.size
            }
        }
        return digit.map { it.first }.flatten()
    }
}
