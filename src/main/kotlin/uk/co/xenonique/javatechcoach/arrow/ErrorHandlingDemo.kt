package uk.co.xenonique.javatechcoach.arrow

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlin.math.sqrt

object ErrorHandlingDemo {

    fun runDemo() {
        println("\n=== Error Handling Evolution Demo ===")

        demonstrateProblematicCode()
        demonstrateTraditionalKotlin()
        demonstrateArrowSolution()
    }

    private fun demonstrateProblematicCode() {
        println("\n--- 1. The Problematic Java-Style Code ---")

        // This is what you want to show as the "before" picture
        fun problematicSqrtFromString(str: String): Double {
            val number = str.toInt() // Throws NumberFormatException!
            return sqrt(number.toDouble()) // Fails for negative numbers!
        }

        println("""
            Code:
            fun sqrtFromString(str: String): Double {
                val number = str.toInt() // Throws NumberFormatException!
                return sqrt(number.toDouble()) // Fails for negative numbers!
            }
            
            Problems:
            • NumberFormatException is unchecked - not in signature
            • Negative numbers cause NaN result silently
            • Caller has to guess what can go wrong
            • Hard to compose with other operations
        """.trimIndent())

        // Show what happens (you'll demo this live)
        println("\nTrying with valid input: '4'")
        try {
            val result = problematicSqrtFromString("4")
            println("Result: $result")
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }

        println("\nTrying with invalid input: 'abc'")
        try {
            val result = problematicSqrtFromString("abc")
            println("Result: $result")
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }

    private fun demonstrateTraditionalKotlin() {
        println("\n--- 2. Traditional Kotlin Improvement (Nullable Types) ---")

        fun traditionalSqrtFromString(str: String): Double? {
            val number = str.toIntOrNull() ?: return null
            return if (number >= 0) sqrt(number.toDouble()) else null
        }

        println("""
            Code:
            fun traditionalSqrtFromString(str: String): Double? {
                val number = str.toIntOrNull() ?: return null
                return if (number >= 0) sqrt(number.toDouble()) else null
            }
            
            Improvements:
            • No exceptions - uses null safety
            • Signature shows it can fail
            
            Limitations:
            • No error details - just null
            • Still need null checks everywhere
            • Hard to know WHY it failed
        """.trimIndent())

        val results = listOf("4", "abc", "-4").map { input ->
            input to traditionalSqrtFromString(input)
        }

        results.forEach { (input, result) ->
            println("Input: '$input' -> Result: $result")
        }
    }

    private fun demonstrateArrowSolution() {
        println("\n--- 3. Arrow.kt Solution (Explicit Error Handling) ---")

        // Option 1: Using Either with raise
        fun arrowEitherSqrt(str: String): Either<String, Double> = either {
            val number = str.toIntOrNull() ?: raise("'$str' is not a valid number")
            ensure(number >= 0) { "Cannot calculate square root of negative number: $number" }
            sqrt(number.toDouble())
        }

        // Option 2: Using catch for exception wrapping
        fun arrowCatchSqrt(str: String): Either<String, Double> = either {
            catch({
                val number = str.toInt()
                ensure(number >= 0) { "Cannot calculate square root of negative number: $number" }
                sqrt(number.toDouble())
            }) { e: NumberFormatException ->
                raise("'$str' is not a valid number: ${e.message}")
            }
        }

        println("""
            Code:
            fun arrowEitherSqrt(str: String): Either<String, Double> = either {
                val number = str.toIntOrNull() ?: raise("'str' is not a valid number')
                ensure(number >= 0) { "Cannot calculate square root of negative number: number" }
                sqrt(number.toDouble())
            }
            
            Benefits:x
            • All possible outcomes are in the type signature
            • Detailed error messages
            • Composable with other Either operations
            • No surprises for the caller
        """.trimIndent())

        println("\n--- Testing Arrow Solution ---")

        val testCases = listOf("4", "abc", "-4", "16", "not_a_number")

        testCases.forEach { input ->
            val result = arrowEitherSqrt(input)

            // Different ways to handle the result
            when {
                result.isRight() -> println("✓ '$input' -> ${result.getOrNull()}")
                result.isLeft() -> println("✗ '$input' -> Error: ${result.leftOrNull()}")
            }

            // Alternative: Using fold for more explicit handling
            result.fold(
                ifLeft = { error ->
                    println("  Detailed - Input '$input' failed because: $error")
                },
                ifRight = { value ->
                    println("  Detailed - Input '$input' succeeded with: $value")
                }
            )
        }

        // Show composition potential
        println("\n--- Composition Preview ---")
        val composedResult = arrowEitherSqrt("16")
            .map { it * 2.0 }  // Transform success
            .mapLeft { "PREFIX: $it" }  // Transform error

        println("Composed result: $composedResult")
    }
}
