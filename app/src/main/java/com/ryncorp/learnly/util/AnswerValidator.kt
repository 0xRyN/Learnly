package com.ryncorp.learnly.util

class AnswerValidator {
    companion object {
        private fun levenshtein(lhs: CharSequence, rhs: CharSequence): Int {
            val lhsLength = lhs.length
            val rhsLength = rhs.length

            var cost = IntArray(lhsLength + 1) { it }
            var newCost = IntArray(lhsLength + 1) { 0 }

            for (i in 1..rhsLength) {
                newCost[0] = i

                for (j in 1..lhsLength) {
                    val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

                    val costReplace = cost[j - 1] + match
                    val costInsert = cost[j] + 1
                    val costDelete = newCost[j - 1] + 1

                    newCost[j] = minOf(minOf(costInsert, costDelete), costReplace)
                }

                val swap = cost
                cost = newCost
                newCost = swap
            }

            return cost[lhsLength]
        }

        fun isAnswerCorrect(answer: String, correctAnswer: String, maxTypos: Int = 1): Boolean {
            // Convert to lowercase
            val lowerAnswer = answer.lowercase()
            val lowerCorrectAnswer = correctAnswer.lowercase()
            return levenshtein(lowerAnswer, lowerCorrectAnswer) <= maxTypos
        }
    }
}