# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Key Constraints

- Do not run tests
- Do not compile the project

## Build Commands

```bash
# Debug builds by flavor
./gradlew assembleProdDebug
./gradlew assembleUatDebug
./gradlew assembleDevDebug

# Release builds (requires signing config in config/)
./gradlew assembleProdRelease

# Static analysis
./gradlew detekt

# Lint
./gradlew lint
```

Build flavors: `prod`, `uat` (suffix `.uat`), `dev` (suffix `.dev`). Version code is auto-generated from git commit count.

## Architecture

**MVI pattern** with three layers:

- **UI layer** — Jetpack Compose screens observe state and dispatch intents
- **Feature modules** (`features/`) — ViewModel, Intent, Model, Event per feature
- **Core layer** (`core/`) — Interactors (business logic) → Repositories → Network/Persistence

Each feature follows this structure:
```
features/{feature}/
├── {Feature}Screen.kt
├── {Feature}ViewModel.kt       # extends ClientViewModel (MVI base)
├── navigation/{Feature}Route.kt  # @Serializable data object/class
├── intent/{Feature}Intent.kt   # sealed interface
├── model/{Feature}Model.kt     # data class implementing Model
└── event/{Feature}Events.kt    # sealed interface
```

**Key core packages:**
- `core/mvi/` — `ClientViewModel`, `Intent`, `Model`, `Event` base types
- `core/interactor/` — Business logic (`AuthenticationInteractor`, `CatalogInteractor`, `EmployeeInteractor`)
- `core/repository/` — Repository interfaces + `impl/` implementations
- `core/network/` — Ktor HTTP client setup; `entity/SwaggerModels.kt` generated from `tools/swagger.json`
- `core/persistence/database/` — Room database (`vpclient.db`, v24, destructive migration); `core/persistence/datastore/` — DataStore preferences

**DI:** Hilt. Modules in `inject/` subpackages of each core domain.

**Navigation:** Jetpack Navigation 3 (`androidx.navigation3`) with `@Serializable` type-safe routes.

## Key Libraries

- Kotlin 2.3.20, Coroutines 1.10.2
- Compose 1.10.5, Material 3 1.4.0
- Hilt 2.59.2
- Ktor Client 3.1 (OkHttp engine)
- Room 2.8.4, Paging 3 3.4.2
- Coil 2.7.0, CameraX 1.5.3, ML Kit barcode scanning
- AppMetrica 8.0.0 (analytics)
- Detekt 1.23.8 (`config/detekt/detekt.yml`)

## Code Conventions

- **Functions:** Always use `{}` and `return`; never use `=` for function body
- **`when` over `if/else if`:** Prefer `when` for branching; single-statement branches without braces
- **Sealed interfaces/classes:** All `data object` entries before any `data class` entries
- **`data class` in sealed interfaces:** Keep parameters on a single line: `data class Foo(val x: Int) : Bar`
- **DAO methods:** Regular `fun` methods before `suspend fun` methods
- **Compose `padding(...)`:** Named parameters in signature order: `start`, `top`, `end`, `bottom`
- **Colors:** Add through `MaterialTheme.colorScheme`, not hardcoded
- **Preview providers:** Place all `PreviewParameterProvider` classes in `...ui/preview/`
- **Duplication:** Do not extract local helper functions just to remove a few repeated lines; small duplication is acceptable