package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.*
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.math.BigInteger

class DayThree: AoCDay<List<String>>, AoCObservable<AoCObserverContext> {

    private val observers: MutableList<AoCObserver<AoCObserverContext>> = ArrayList()
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
                broadcast(SimpleObserverContext(result))
            }
        }
        return result
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        val combined = inputLines.reduce { acc, s -> acc.plus(s) }
        val x = """(do[(][)].*?don't[(][)])|(^.*?don't[(][)])|(do[(][)].*?$)""".toRegex().findAll(combined)
            .map { it.value }.toList()
        return partOne(x)
    }

    override fun run() {
        println(addColour("Day Three", CYAN))
        println("Part 1: " + addColour("%d", AoCConsoleColours.GREEN)
            .format(partOne(AoCUtil.readResourceFile("daythree/input.txt"))))
        println("Part 2: " + addColour("%d", AoCConsoleColours.GREEN)
            .format(partTwo(AoCUtil.readResourceFile("daythree/input.txt"))))
    }

    override fun addObserver(observer: AoCObserver<AoCObserverContext>) {
        observers.add(observer)
    }

    override fun broadcast(context: AoCObserverContext) {
        observers.forEach { it.notify(context) }
    }
}