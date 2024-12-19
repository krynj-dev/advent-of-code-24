package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.util.AoCUtil
import au.com.krynj.aoc.util.ds.DirectedGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger
import java.util.LinkedList

class DayElevenTest {

    @Test
    fun partOne() {
        val dayEleven = DayEleven()
        val actual = dayEleven.partOne(AoCUtil.readLineAsInt(AoCUtil.readResourceFile("example-11-1.txt").first(), digits = false))
        assertEquals(55312.toBigInteger(), actual)
    }

    @Test
    fun partOneAlt() {
        val dayEleven = DayEleven()
        val stones = listOf(125, 17).map { it.toBigInteger() }
        val depth = 30
        var g = DirectedGraph<BigInteger>()
        stones.forEach{
            g = dayEleven.bfsGraph(it, g, depth)
        }

        val cycles: MutableList<List<BigInteger>> = mutableListOf()
        val cmap: MutableMap<BigInteger, MutableList<Int>> = mutableMapOf()
        stones.forEach {
            dayEleven.dfsTo(stones.first(), g, listOf(), cycles, 0, depth, cmap, BigInteger.ONE)
        }
//        dayEleven.dfsTo(stones.first(), g, listOf(), cycles, 0, 25, cmap, BigInteger.ONE)

        var stonesC = stones.toList()
        (0..depth).forEach { d ->
            stonesC = stonesC.flatMap { dayEleven.blink(listOf(it)) }
            val e: List<Long> = stonesC.map { it.toLong() }.sorted()
            val a: List<Long> = stones.flatMap { g.childrenAtDistance(g.getNodeByValue(it)!!, 0, d).map { i -> i.value.toLong() } }.sorted()
            assertEquals(e, a, "Failed @$d")
        }

        return
    }

    @Test
    fun testBlink() {
        val dayEleven = DayEleven()
        val actual = dayEleven.blink(listOf(0, 1, 10, 99, 999).map { it.toBigInteger() })
        assertEquals(listOf(1, 2024, 1, 0, 9, 9, 2021976).map { it.toBigInteger() }, actual)
    }

    @Test
    fun partTwo() {
        val dayEleven = DayEleven()
        val actual = dayEleven.partTwo(AoCUtil.readLineAsInt(AoCUtil.readResourceFile("example-11-1.txt").first(), digits = false))
        assertEquals(1.toBigInteger(), actual)
    }

    fun childrenAtDistance(node: DirectedGraph.GraphNode<BigInteger>, depth: Int, distance: Int, g: DirectedGraph<BigInteger>): List<DirectedGraph.GraphNode<BigInteger>> {
        println("Node $node @ $depth")
        val children = g.nodeChildren(node)
        if (depth == distance) {
            println("\tat max depth, returning ${children.joinToString(", ")} children")
            return children.toList()
        }
        return g.nodeChildren(node).flatMap {
            childrenAtDistance(it, depth + 1, distance, g)
        }
    }

    @Test
    fun testNewMethod() {
        val dayEleven = DayEleven()
        val numCache: MutableMap<BigInteger, MutableList<Pair<List<BigInteger>, Int>>> = dayEleven.digitToDouble().toMutableMap()
        val stones = listOf(125, 17).map { it.toBigInteger() }
        val maxDepth = 75
        val maxes: MutableList<Pair<List<BigInteger>, Int>> = mutableListOf()
        val x = stones.map {
            val q = LinkedList<Pair<BigInteger, Int>>()
            q.add(Pair(it, 0))
            while (q.isNotEmpty()) {
                val s = q.remove()
                var digits = numCache[s.first]
                if (digits == null) {
                    val sc = dayEleven.getShortCut(s.first, numCache, listOf())
                    digits = numCache.getOrPut(s.first) { sc }
                }
                digits.forEach { d ->
                    val totalDepth = s.second + d.second
                    if (totalDepth >= maxDepth) maxes.add(Pair(d.first, totalDepth))
                    else d.first.forEach { l -> q.add(Pair(l, totalDepth)) }
                }
            }
        }

        assertEquals(55312.toBigInteger(), stones.map { dayEleven.getAtNew(it, numCache, maxDepth).size.toBigInteger() }.reduce(BigInteger::plus))
    }
}