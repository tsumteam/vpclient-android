# Agent Instructions

- Use standard project conventions and follow existing documentation when making changes;
- Do not run tests
- Do not compile the project
- Place all PreviewParameterProvider classes in ..ui/preview
- Add new colors through MaterialTheme.colorScheme
- In sealed interface, keep short data class declarations with parameters on a single line: data class Example(val param: Int): ExampleInterface
- Prefer `when` instead of `if / else if` chains when expressing branching logic in Kotlin
- In Kotlin `when` branches, use the single-line form without braces only when the whole branch fits on one line: `condition -> statement`; if the branch body is multiline, wrap it in braces even when it contains only one statement
- In Kotlin `sealed interface` and `sealed class`, declare all `data object` entries before any `data class` entries
- Always write functions with {} and return, never use = for the function body
- In DAO interfaces, place all regular `fun` methods before any `suspend fun` methods
- We do not avoid small duplication by default; do not extract local helper functions just to remove a few repeated lines
- For composable function calls, order arguments the same way as they are declared in the SDK/component signature
- For Compose `padding(...)`, order named parameters the same way as in the method signature: `start`, `top`, `end`, `bottom`
- Do not place classes or constants inside MVI classes; declare them at file level or in dedicated files/packages
- Place experimental opt-in annotations only at file level, for example `@file:OptIn(ExperimentalMaterial3Api::class)`, not on individual declarations


<claude-mem-context>
# Memory Context

# $CMEM vpclient-android 2026-04-23 8:28pm GMT+3

No previous sessions found.
</claude-mem-context>
