package au.com.krynj.aoc.twentyfour.days

import au.com.krynj.aoc.framework.AoCDay
import au.com.krynj.aoc.framework.AoCObservable
import au.com.krynj.aoc.framework.AoCObserver
import au.com.krynj.aoc.util.AoCConsoleColours
import au.com.krynj.aoc.util.AoCConsoleColours.CYAN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN
import au.com.krynj.aoc.util.AoCConsoleColours.GREEN_BOLD
import au.com.krynj.aoc.util.AoCConsoleColours.RED
import au.com.krynj.aoc.util.AoCConsoleColours.addColour
import au.com.krynj.aoc.util.AoCUtil
import java.lang.Exception
import java.math.BigInteger

class DayFive: AoCDay<List<List<String>>>, AoCObservable {

    private val observers: MutableList<AoCObserver> = ArrayList()

    override fun run() {
        println(addColour("Day Five", CYAN))
        println("Part 1: " + addColour("%d", GREEN)
            .format(partOne(AoCUtil.readResourceFile("dayfive/input.txt", ""))))
//        println("Part 2: " + addColour("%d", GREEN)
//            .format(partTwo(AoCUtil.readResourceFile("dayfive/input.txt", ""))))
    }

    override fun getDay(): Int {
        return 5
    }

    override fun partOne(inputLines: List<List<String>>): BigInteger {
        val rules = inputLines[0].map { AoCUtil.readLineAsInt(it, '|') }
        val updates = inputLines[1].map { AoCUtil.readLineAsInt(it, ',') }

        var result = 0
        updates.forEach {
            val locations = locationMap(it)
            if (isValid(locations, rules)) result += it[it.size / 2]
        }
        return result.toBigInteger()
    }

    fun locationMap(intList: List<Int>): HashMap<Int, Int> {
        val theMap: HashMap<Int, Int> = HashMap()
        intList.forEachIndexed { index, i -> theMap[i] = index }
        return theMap
    }

    fun isValid(locations: HashMap<Int, Int>, rules: List<List<Int>>): Boolean {
        return validArray(locations, rules).all { it }
    }

    fun validArray(locations: HashMap<Int, Int>, rules: List<List<Int>>): List<Boolean> {
        return rules.map { rule -> !(locations.containsKey(rule[0]) && locations.containsKey(rule[1]))
                || (locations[rule[0]]!! < locations[rule[1]]!!)}
    }

    fun activeRules(locations: HashMap<Int, Int>, rules: List<List<Int>>): List<List<Int>> {
        return rules.filter { it.first() in locations.keys && it.last() in locations.keys }
    }

    override fun partTwo(inputLines: List<List<String>>): BigInteger {
        // Second guess too low: 4901
        val rules = inputLines[0].map { AoCUtil.readLineAsInt(it, '|') }
        val updates = inputLines[1].map { AoCUtil.readLineAsInt(it, ',') }
        var result = 0
        updates.forEach{
            var update = it
            val locs = locationMap(update)
            val actives = activeRules(locs, rules)
            var ruleResults = validArray(locationMap(update), actives)
            var invalidRules = ruleResults.mapIndexed { index, b -> if (!b) actives[index] else null }.filterNotNull()
            var invalidNumbers = invalidRules.flatten()
            var didReorder = false
            var loops = 0
            while (invalidRules.isNotEmpty()){
                loops++
                if (loops > 1000) {
                    throw Exception("Too many loops!")
                }
                didReorder = true
                // Do the reorder
                val oa =
                    getOrderArray(actives)
                var fixI = -1
//                update = it.map { u ->
//                    if (u in invalidNumbers) {
//                        fixI++
//                        oa[fixI]
//                    } else u
//                }
                update = oa
                // Calc if all the rules pass now
                ruleResults = validArray(locationMap(update), actives)
                invalidRules = ruleResults.mapIndexed { index, b -> if (!b) actives[index] else null }.filterNotNull()
                invalidNumbers = invalidRules.flatten()
            }
            if (didReorder) {
                result += update[update.size / 2]
            }
        }

        return result.toBigInteger()
    }

    fun getOrderArray(rules: List<List<Int>>): MutableList<Int> {
        val theList: MutableList<Int> = mutableListOf()
        rules.forEach {
            val firstIn = it.first() in theList
            val lastIn = it.last() in theList
            if (!firstIn && !lastIn) {
                theList.add(it.first())
                theList.add(it.last())
            } else if (firstIn && !lastIn) {
                theList.add(it.last())
            } else if (!firstIn) {
                theList.add(0, it.first())
            } else if (theList.indexOf(it.first()) > theList.indexOf(it.last())) {
                // Try swap?
                val firstIndex = theList.indexOf(it.first())
                val lastIndex = theList.indexOf(it.last())
                (theList[lastIndex] to theList[firstIndex]).also { it2 ->
                    theList[firstIndex] = it2.first
                    theList[lastIndex] = it2.second
                }
            }
        }
        assert(validArray(locationMap(theList), rules).all { it })
        return theList
    }

    override fun addObserver(observer: AoCObserver) {
        observers.add(observer)
    }

    override fun broadcast(partialResult: BigInteger) {
        observers.forEach { it.notify(partialResult) }
    }


}