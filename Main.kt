import kotlin.system.exitProcess
import java.io.File

class SearchEngine(args: Array<String>) {
    private val listOfPeople = File(*).readLines().toMutableList() // All Data  // * - Put Your Absolute Path to Txt File Here (For IntelliJ IDEA put args "--data text.txt" - args[1])
    private var index = mutableMapOf<String, MutableList<Int>>() // Inverted Index

    fun start() {
        createMap()
    }

    private fun createMap() {
        listOfPeople.mapIndexed { index, s -> s to index }.flatMap {
            val lineNumber = it.second
            it.first.split(" ").map { it.lowercase() to lineNumber } // Create Map
        }.groupByTo(index, {it.first}, {it.second}) // Group Map

        startMenu()
    }

    private fun startMenu() {
        while (true) {
            println("""
            === Menu ===
            1. Find a person
            2. Print all people
            0. Exit
            """.trimIndent())

            when (readLine()!!.toInt()) {
                0 -> {
                    println("Bye!")
                    exitProcess(0)
                }
                1 -> strategyOfSearch()
                2 -> printAllPeople()
                else -> {
                    println("Incorrect option! Try again.")
                    continue
                }
            }
        }
    }

    private fun printAllPeople() {
        for (people in listOfPeople) println(people)

        startMenu()
    }

    private fun strategyOfSearch() {
        println("Select a matching strategy: ALL, ANY, NONE")

        val strategy = readLine()!!
        founder(strategy)
    }

    private fun founder(strategy: String) {
        println("Enter data to search people:")

        val q = readLine()!!.split(" ") // Search Line

        when (strategy.uppercase()) {
            "NONE" -> {
                val peopleFounded = mutableSetOf<String>() // Set

                for (line in q) {
                    index[line.lowercase()]?.map { peopleFounded.add(listOfPeople[it]) } // Add Founded People to Set
                }

                (listOfPeople.toMutableSet() - peopleFounded).forEach(::println) // Print All People Without Founded
                listOfPeople.toMutableList()
            }
            "ANY" -> {
                for (line in q) {
                    index[line.lowercase()]?.map { listOfPeople[it] }?.forEach(::println)
                }
            }
            "ALL" -> {
                val dataToFound = q.joinToString().replace(",", "")

                for (person in listOfPeople) {
                    if (person.lowercase().contains(dataToFound.lowercase())) {
                        println(person)
                    }
                }
            }
        }

        startMenu()
    }
}

fun main(args: Array<String>) {
    val searchEngine = SearchEngine(args)
    searchEngine.start()
}

