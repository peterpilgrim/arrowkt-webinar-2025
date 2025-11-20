package uk.co.xenonique.javatechcoach.arrow

import arrow.core.Ior
import arrow.core.raise.ior

object IorDemo {

    fun runDemo() {
        println("\n=== Ior Demo: Handling Success with Warnings ===\n")

        validateUserProfile().forEach { result ->
            println("Result: $result")

            // Different ways to handle Ior
            result.fold(
                { warnings ->
                    println("  ⚠️  Only warnings: ${warnings.joinToString()}")
                },
                { user ->
                    println("  ✅ Pure success: $user")
                },
                { warnings, user ->
                    println("  ✅ Success with warnings: $user")
                    println("    Warnings: ${warnings.joinToString()}")
                }
            )
            println()
        }
    }

    // Ior lets you return success WITH warnings
    fun validateUserProfile(): List<Ior<List<String>, User>> {
        return listOf(
            validateUser("john@email.com", 25, "John Doe"),  // Perfect case
            validateUser("jane", 17, "Jane Smith"),          // Invalid email + underage
            validateUser("bob@test.com", 15, ""),            // Underage + empty name
            validateUser("alice@email.com", 25, "Alice"),   // Perfect case
            validateUser("linda", 12, "")                    // All invalid
        )
    }

    data class User(val email: String, val age: Int, val name: String)

    fun validateUser(email: String, age: Int, name: String): Ior<List<String>, User> =
        ior(combineError = List<String>::plus) {
            val warnings = mutableListOf<String>()

            // Email validation - can succeed with warnings
            val validatedEmail = if (email.contains("@")) {
                email
            } else {
                warnings.add("Invalid email format: '$email'")
                "default@unknown.com" // Provide default but keep going
            }

            // Age validation - can succeed with warnings
            val validatedAge = if (age >= 18) {
                age
            } else {
                warnings.add("User is underage: $age years old")
                age // Still keep the original age but warn
            }

            // Name validation
            val validatedName = name.ifBlank {
                warnings.add("User name is empty")
                "Anonymous"
            }

            (User(validatedEmail, validatedAge, validatedName).bindAll(warnings)).bind()
        }
}

// Extension function to make the API cleaner
fun <A> A.bindAll(warnings: List<String>): Ior<List<String>, A> =
    if (warnings.isEmpty())
        Ior.Right(this)
    else
        Ior.Both(warnings, this)