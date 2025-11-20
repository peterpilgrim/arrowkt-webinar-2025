package uk.co.xenonique.javatechcoach.arrow

import arrow.core.Ior
import arrow.core.raise.ior

object SimpleIorDemo {

    // Simple case with String warnings (easier to combine)
    fun processData(input: String): Ior<String, Int> = ior(String::plus) {
        val warnings = StringBuilder()

        val step1 = if (input.length < 5) {
            warnings.append("Input too short. ")
            input.padEnd(5, 'X')
        } else {
            input
        }

        val step2 = if (!step1.contains("data", ignoreCase = true)) {
            warnings.append("Missing 'data' keyword. ")
            "data_$step1"
        } else {
            step1
        }

        (if (warnings.isNotEmpty()) {
            Ior.Both(warnings.toString(), step2.length)
        } else {
            Ior.Right(step2.length)
        }).bind()
    }

    fun demonstrate() {
        println("=== Simple Ior Example ===")

        listOf("hi", "datatest", "hello", "Data").forEach { input ->
            val result = processData(input)
            println("\nInput: '$input' -> $result")

            result.fold(
                { warn ->    println("   ⚠️ Warnings only: $warn") },
                { success -> println("   ✅ Pure success: $success") },
                { warn, success -> println("   ✅ Success: $success with ⚠️ warnings: $warn") }
            )
        }
    }
}

fun main() {
    SimpleIorDemo.demonstrate()
}
