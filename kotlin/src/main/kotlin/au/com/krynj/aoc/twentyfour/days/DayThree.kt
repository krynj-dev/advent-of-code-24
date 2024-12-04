package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger

class DayThree: AoCDay, AoCObservable {

    private val observers: MutableList<AoCObserver> = ArrayList()
    override fun getDay(): Int {
        return 3
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        var result = BigInteger.ZERO
        inputLines.forEach {
            val allMatches = """mul[(](\d{1,3}),(\d{1,3})[)]""".toRegex().findAll(it)
            allMatches.forEach { res ->
                val (a, b) = res.destructured
                result = result.add(a.toBigInteger().multiply(b.toBigInteger()))
                broadcast(result)
            }
        }
        return result
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        val combined = inputLines.reduce { acc, s -> acc.plus(s) }
        val x = """(do[(][)].*?don't[(][)])|(^.*?don't[(][)])|(do[(][)].*?$)""".toRegex().findAll(combined).map { it.value }.toList()
        return partOne(x)
    }

    override fun run() {
        println("Part 1: " + partOne(AoCUtil.readResourceFile("daythree/input.txt")))
        println("Part 2: " + partTwo(AoCUtil.readResourceFile("daythree/input.txt")))
    }

    override fun addObserver(observer: AoCObserver) {
        observers.add(observer)
    }

    override fun broadcast(partialResult: BigInteger) {
        observers.forEach { it.notify(partialResult) }
    }
}