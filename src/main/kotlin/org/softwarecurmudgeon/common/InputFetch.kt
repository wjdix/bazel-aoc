package org.softwarecurmudgeon.common

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

data class Day(
    val year: Int = 2020,
    val day: Int = 1
)

object InputFetch {
    private fun execute(url: String): String {
        val (request, _, result) = url
            .httpGet()
            .header(mapOf("Cookie" to Cookie.session))
            .responseString()
        return when (result) {
            is Result.Success -> {
                result.get()
            }
            is Result.Failure-> {
                println(request)
                val ex = result.getException()
                println(ex)
                throw result.getException()
            }
        }
    }

    fun forDay(day: Day) = execute("https://adventofcode.com/${day.year}/day/${day.day}/input")
}
