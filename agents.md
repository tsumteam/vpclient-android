# Agent Instructions

- Use standard project conventions and follow existing documentation when making changes;
- Do not run tests
- Do not compile the project
- Place all PreviewParameterProvider classes in ..ui/preview
- Add new colors through MaterialTheme.colorScheme
- In sealed interface, keep short data class declarations with parameters on a single line: data class Example(val param: Int): ExampleInterface
- Prefer `when` instead of `if / else if` chains when expressing branching logic in Kotlin
- In Kotlin `when` branches, if a branch contains only one statement, prefer the single-line form without braces: `condition -> statement`
- In Kotlin `sealed interface` and `sealed class`, declare all `data object` entries before any `data class` entries
- Always write functions with {} and return, never use = for the function body
- In DAO interfaces, place all regular `fun` methods before any `suspend fun` methods
