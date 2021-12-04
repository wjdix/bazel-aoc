package org.softwarecurmudgeon.common

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

object Submitter {
    private fun urlForSubmission(day: Day) =
        "https://adventofcode.com/${day.year}/day/${day.day}/answer"

    fun forDay(day: Day, part: String, solution: String): String {
        val (request, _, result) = urlForSubmission(day)
            .httpPost(listOf(Pair("answer", solution), Pair("level", part)))
            .header(mapOf("Cookie" to Cookie.session))
            .responseString()
        return when (result) {
            is Result.Success -> {
                if (result.get().contains("That's the right answer!")) {
                    "Answer accepted for part $part"
                } else {
                    "Answer not accepted"
                }
            }
            is Result.Failure-> {
                println(request)
                val ex = result.getException()
                println(ex)
                throw result.getException()
            }
        }
    }
}
