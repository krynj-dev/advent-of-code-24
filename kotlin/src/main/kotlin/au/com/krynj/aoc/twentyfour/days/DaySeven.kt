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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class DaySeven : AoCDay<List<String>>, AoCObservable<SimpleObserverContext> {

    private val observers: MutableList<AoCObserver<SimpleObserverContext>> = ArrayList()

    override fun run() {
        println(addColour("Day Seven", CYAN))
        println("Part 1: " + addColour("%d", GREEN)
            .format(partOne(AoCUtil.readResourceFile("dayseven/input.txt"))))
        println("Part 2: " + addColour("%d", GREEN)
            .format(partTwo(AoCUtil.readResourceFile("dayseven/input.txt"))))
    }

    override fun getDay(): Int {
        return 7
    }

    override fun partOne(inputLines: List<String>): BigInteger {
        val kvps = parseLines(inputLines)
        var result = BigInteger.ZERO
        val symbolStack = ""
        kvps.forEach { (k, v) ->
            if (isAddEq(v.first(), v.subList(1,v.size), k, symbolStack) || isMultEq(v.first(), v.subList(1,v.size), k, symbolStack)) result += k
        }
        return result
    }

    fun isAddEq (total: BigInteger, remainder: List<BigInteger>, target: BigInteger, symbolStack: String): Boolean {
        return isAddEq(total, remainder, target, symbolStack, false)
    }
    fun isAddEq (total: BigInteger, remainder: List<BigInteger>, target: BigInteger, symbolStack: String, isPartTwo: Boolean): Boolean {
        if (remainder.isEmpty()) {
            return total == target
        }
        val sum = total.add(remainder.first())
        if (sum > target) return false
        val newRemainder = remainder.subList(1, remainder.size)
        return isAddEq(sum, newRemainder, target, "$symbolStack+", isPartTwo) || isMultEq(sum, newRemainder, target, "$symbolStack+", isPartTwo)
                || (isPartTwo && isConcatEq(sum, newRemainder, target, "$symbolStack+"))
    }

    fun isMultEq (total: BigInteger, remainder: List<BigInteger>, target: BigInteger, symbolStack: String): Boolean {
        return isMultEq(total, remainder, target, symbolStack, false)
    }

    fun isMultEq (total: BigInteger, remainder: List<BigInteger>, target: BigInteger, symbolStack: String, isPartTwo: Boolean): Boolean {
        if (remainder.isEmpty()) {
            return total == target
        }
        val sum = total.multiply(remainder.first())
        if (sum > target) return false
        val newRemainder = remainder.subList(1, remainder.size)
        return isAddEq(sum, newRemainder, target, "$symbolStack*", isPartTwo) || isMultEq(sum, newRemainder, target, "$symbolStack*", isPartTwo)
                || (isPartTwo && isConcatEq(sum, newRemainder, target, "$symbolStack*"))
    }

    fun isConcatEq (total: BigInteger, remainder: List<BigInteger>, target: BigInteger, symbolStack: String): Boolean {
        if (remainder.isEmpty()) {
            return total == target
        }
        val sum = BigInteger(total.toString() + remainder.first().toString())
        if (sum > target) return false
        val newRemainder = remainder.subList(1, remainder.size)
        return isAddEq(sum, newRemainder, target, "$symbolStack||", true) || isMultEq(sum, newRemainder, target, "$symbolStack||", true)
                || isConcatEq(sum, newRemainder, target, "$symbolStack||")
    }

    override fun partTwo(inputLines: List<String>): BigInteger {
        // First answer 8732590707561 too low
        val kvps = parseLines(inputLines)
        var result = BigInteger.ZERO
        val symbolStack = ""
        kvps.forEach { (k, v) ->
            if (isAddEq(v.first(), v.subList(1,v.size), k, symbolStack, true) || isMultEq(v.first(), v.subList(1,v.size), k, symbolStack, true)
                || isConcatEq(v.first(), v.subList(1,v.size), k, symbolStack)) result += k
        }
        return result
    }

    fun parseLines(lines: List<String>): Map<BigInteger, List<BigInteger>> {
        return lines.map{
            val splt = it.split(":")
            val mathces: MutableList<BigInteger> = mutableListOf()

            """\s(\d+)""".toRegex().findAll(splt.last()).forEach { res ->
                val (a) = res.destructured
                mathces.add(BigInteger(a))
            }

            BigInteger(splt.first()) to mathces
        }.toMap()
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
