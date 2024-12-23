package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCAlgorithmUtil
import au.com.krynj.aoc.util.AoCAlgorithmUtil.findAround
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.YELLOW
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCMathsUtil
import au.com.krynj.aoc.util.AoCUtil
import au.com.krynj.aoc.util.ds.Tuple
import java.math.BigInteger
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

class DayThirteen : AoCDay<List<List<String>>, BigInteger>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Thirteen", CYAN))
        val result1: BigInteger
        val time1 = measureTime {
            result1 = partOne(AoCUtil.readResourceFile("input-13.txt", ""))
        }
        println("Part 1: ${addColour("$result1", GREEN)} ${addColour("(${time1.inWholeMilliseconds}ms)", YELLOW)}")
        val result2: BigInteger
        val time2 = measureTime {
            result2 = partTwo(AoCUtil.readResourceFile("input-13.txt", ""))
        }
        println("Part 2: ${addColour("$result2", GREEN)} ${addColour("(${time2.inWholeMilliseconds}ms)", YELLOW)}")
    }

    override fun getDay(): Int {
        return 13
    }

    override fun partOne(inputLines: List<List<String>>): BigInteger {
        val machineRules = inputLines.map { parseInput(it) }
        val solutions = machineRules.mapIndexed{ i, it ->
            i to findSolution(it, 100)
        }
        return solutions.filter { it.second != null }.map { 3* it.second!!.first+ it.second!!.second }.reduce(Long::plus).toBigInteger()
    }

    override fun partTwo(inputLines: List<List<String>>): BigInteger {
        val machineRules = inputLines.map { parseInput(it) }
        val solutions = machineRules.mapIndexed{ i, it ->
            i to findSolution(Tuple(it.first, it.second, Pair(it.third.first+10000000000000, it.third.second+10000000000000)), Long.MAX_VALUE)
        }
        return solutions.filter { it.second != null }.map { 3* it.second!!.first+ it.second!!.second }.reduce(Long::plus).toBigInteger()
    }

    fun parseInput(machine: List<String>): Tuple<Pair<Long, Long>, Pair<Long, Long>, Pair<Long, Long>> {
        assert(machine.size == 3)
        val numbersRegex = """(-*\d)+""".toRegex()
        val numbers = machine.map {
            val n = numbersRegex.findAll(it).map { r -> r.value }.toList()
            Pair(n[0].toLong(), n[1].toLong())
        } .toList()
        return Tuple(numbers[0], numbers[1], numbers[2])
    }

    fun findSolution(machine: Tuple<Pair<Long, Long>, Pair<Long, Long>, Pair<Long, Long>>, maxPresses: Long): Pair<Long, Long>? {
        // let ax, ay, bx, by, i, j: Solve
        // i*ax + j*bx = X
        // i*ay + j*by = Y
        val pressesList: MutableList<Long> = mutableListOf()
        for (n in listOf(Pair(machine.first.first, machine.first.second), Pair(machine.second.first, machine.second.second))) {
            val lcm = AoCMathsUtil.lcm(n.first, n.second)
            val l = lcm / n.first
            val m = lcm / n.second
            val eq = Tuple(l*machine.first.first-m*machine.first.second, l*machine.second.first-m*machine.second.second, l*machine.third.first-m*machine.third.second)
            val presses = eq.third / (eq.first+eq.second)
            if ((eq.third % (eq.first+eq.second) == 0L) && presses in 1..<maxPresses) pressesList.add(0, presses)
        }
        if (pressesList.size != 2) return null
        return Pair(pressesList[0], pressesList[1])
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
