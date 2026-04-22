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
- We do not avoid small duplication by default; do not extract local helper functions just to remove a few repeated lines
- For Compose `padding(...)`, order named parameters the same way as in the method signature: `start`, `top`, `end`, `bottom`
- Do not place classes or constants inside MVI classes; declare them at file level or in dedicated files/packages
- Place experimental opt-in annotations only at file level, for example `@file:OptIn(ExperimentalMaterial3Api::class)`, not on individual declarations


<claude-mem-context>
# Memory Context

# $CMEM vpclient-android 2026-04-22 3:43pm GMT+3

No previous sessions found.
</claude-mem-context>