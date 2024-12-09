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
import au.com.krynj.aoc.util.AoCMathsUtil
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger
import kotlin.time.measureTime

class DayEight : AoCDay<List<String>>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Eight", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-8.txt"))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-8.txt"))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 8
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        val antennae: Map<Char, Set<Pair<Int, Int>>> = getAntennaeLocations(inputLines)
        val antinodes: Map<Char, Set<Pair<Int, Int>>> = getAntinodeLocations(antennae, inputLines, false)
        return antinodes.flatMap { it.value }.toSet().size.toBigInteger()
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        val antennae: Map<Char, Set<Pair<Int, Int>>> = getAntennaeLocations(inputLines)
        val antinodes: Map<Char, Set<Pair<Int, Int>>> = getAntinodeLocations(antennae, inputLines, true)
        return antinodes.flatMap { it.value }.toSet().size.toBigInteger()
    }

    fun getAntennaeLocations(puzzleMap: List<String>): Map<Char, Set<Pair<Int, Int>>> {
        val antennae: MutableMap<Char, MutableSet<Pair<Int, Int>>> = mutableMapOf()
        puzzleMap.forEachIndexed { index, it ->
            val matches = """[A-z0-9]""".toRegex().findAll(it)
            matches.forEach { m ->
                val a = m.value
                antennae.getOrPut(a.first()) { mutableSetOf() }.add(Pair(index, m.range.first))
            }
        }
        return antennae
    }

    fun getAntinodeLocations(
        antennae: Map<Char, Set<Pair<Int, Int>>>,
        puzzleMap: List<String>,
        countAll: Boolean
    ): Map<Char, Set<Pair<Int, Int>>> {
        val antinodes: MutableMap<Char, MutableSet<Pair<Int, Int>>> = mutableMapOf()
        val dpNormal: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        antennae.map { (k, v) ->
            val antList = v.toList()
            antList.forEachIndexed { i, a ->
                antList.subList(i + 1, antList.size).forEach { b ->
                    val distance = subtractVector(a, b)
                    val foundNodes = if (!countAll) {
                        mutableSetOf(subtractVector(a, distance), addVector(b, distance)).filter { an ->
                            an.first in puzzleMap.indices && an.second in puzzleMap.first().indices
                        }.toMutableSet()
                    } else {
                        val normalisedDistance = dpNormalise(distance, dpNormal)
                        val theSet: MutableSet<Pair<Int, Int>> = mutableSetOf()
                        var cur = a
                        while (cur.first in puzzleMap.indices && cur.second in puzzleMap.first().indices) {
                            theSet.add(cur)
                            cur = addVector(cur, normalisedDistance)
                        }
                        cur = a
                        while (cur.first in puzzleMap.indices && cur.second in puzzleMap.first().indices) {
                            theSet.add(cur)
                            cur = subtractVector(cur, normalisedDistance)
                        }
                        theSet
                    }
                    antinodes.getOrPut(k) { foundNodes }.addAll(foundNodes)
                }
            }
        }
        return antinodes
    }

    fun dpNormalise(v: Pair<Int, Int>, dpNormal: MutableMap<Pair<Int, Int>, Int>): Pair<Int, Int> {
        val gcd = dpNormal.getOrElse(v) {
            dpNormal.getOrElse(Pair(v.second, v.first)) {
                val calc = AoCMathsUtil.gcd(v.first, v.second)
                dpNormal.putIfAbsent(v, calc)
                dpNormal.putIfAbsent(Pair(v.second, v.first), calc)
                calc
            }
        }
        return Pair(v.first / gcd, v.second / gcd)
    }

    fun subtractVector(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(a.first - b.first, a.second - b.second)
    }

    fun addVector(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(b.first + a.first, b.second + a.second)
    }

    fun normaliseVector(v: Pair<Int, Int>): Pair<Int, Int> {
        val gcd = AoCMathsUtil.gcd(v.first, v.second)
        return Pair(v.first / gcd, v.second / gcd)
    }

    override fun addObserver(observer: AoCObserver<SimpleObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: SimpleObserverContext) {
        observers.forEach {
            it.notify(context)
        }
    }

}
