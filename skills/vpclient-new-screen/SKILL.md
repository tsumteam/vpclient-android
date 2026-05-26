---
name: vpclient-new-screen
description: Create or update a new screen in the vpclient-android app. Use when adding an Android feature screen, route, MVI ViewModel/Model/Intent files, Compose screen content, navigation wiring, previews, or screen-specific UI for this repository.
---

# VPClient New Screen

Follow this workflow when creating a new app screen in `vpclient-android`.

## File Set

Create the screen as a complete feature slice immediately:

- `<Feature>Screen.kt`
- `<Feature>ViewModel.kt`
- `intent/<Feature>Intent.kt`
- `model/<Feature>Model.kt`
- `navigation/<Feature>Route.kt`

Keep each model, shared UI component, and route in a separate file. Do not place classes or constants inside MVI classes; use file-level declarations or dedicated files/packages.

## MVI Shape

Use the project MVI stack:

```kotlin
@HiltViewModel
class ExampleViewModel @Inject constructor(): ClientViewModel<ExampleIntent, ExampleModel, Event>(ExampleModel()) {

    override fun dispatch(intent: ExampleIntent) {
        when (intent) {
            is ExampleIntent.PrimaryClick -> launch { /* action */ }
        }
    }
}
```

Use a `sealed interface` for intents:

```kotlin
sealed interface ExampleIntent: Intent {
    data object PrimaryClick: ExampleIntent
    data class SelectItem(val id: Long): ExampleIntent
}
```

Declare all `data object` entries before any `data class` entries. Keep short data class declarations on one line.

Do not create or store variables in ViewModel classes; keep screen state in the Model. For Room-backed network refresh screens, use separate `Collect...` and `Load...` intents.

## Screen Composable

Make the route composable obtain the ViewModel, collect state when the model has state, and pass only state plus `dispatch` into content:

```kotlin
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ExampleScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}
```

If the screen model has no read state, pass only `dispatch`.

Keep navigation, interactor calls, and event sending in the ViewModel, not in the composable. The composable should emit intents.

When the screen needs one-shot events, focus handling, or snackbar messages, observe `viewModel.eventFlow` in the route composable:

```kotlin
ObserveAsEvents(
    flow = viewModel.eventFlow
) { event ->
    when (event) {
        is CodeEvents.ClearFocus -> focusManager.clearFocus()
        is CodeEvents.SnackbarMessage -> {
            snackbarHostStateError.currentSnackbarData?.dismiss()
            scope.launch { snackbarHostStateError.showSnackbar(event.message) }
        }
    }
}
```

Use the concrete screen event type instead of `CodeEvents`. Keep event side effects in this observer and keep persistent UI state in the Model.

## Screen Content

Create a private `<Feature>ScreenContent` composable and place the screen `Scaffold` inside it:

```kotlin
@Composable
private fun ExampleScreenContent(
    state: ExampleModel,
    dispatch: (ExampleIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                title = stringResource(ClientStrings.ExampleTitle),
                onNavigationClick = { dispatch(ExampleIntent.BackClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        // Content goes here.
    }
}
```

Use `ClientCenterAlignedTopAppBar` or another existing TopAppBar only when the screen needs a top bar.

For list content, use `SharedLazyColumn` with `innerPadding` directly:

```kotlin
SharedLazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = innerPadding
) {
    item {
        // Item content.
    }
}
```

For regular non-list content, apply `innerPadding` to the root content container.

When the screen needs an error snackbar, use `SharedSnackbarHost` in the `Scaffold`:

```kotlin
snackbarHost = {
    SharedSnackbarHost(
        hostState = snackbarHostStateError,
        modifier = Modifier.padding(bottom = 8.dp),
        containerColor = MaterialTheme.colorScheme.error
    )
}
```

## Compose Rules

Use existing project components and theme APIs:

- Use `MaterialTheme.colorScheme` for component colors; do not use raw `Color` constants for theme colors.
- Use `MaterialTheme.typography`; do not instantiate `TextStyle` directly inside components.
- Order composable call arguments exactly as declared by the SDK/component signature.
- For `Modifier.padding(...)`, order named parameters as `start`, `top`, `end`, `bottom`.
- In Compose containers, separate sibling composable calls with a blank line.
- For state-backed `Modifier.offset(...)`, use the lambda overload.

For `floatingActionButton`, place the button directly in the slot and apply padding to the button modifier:

```kotlin
floatingActionButton = {
    ClientButton(
        onClick = { dispatch(ExampleIntent.PrimaryClick) },
        text = stringResource(ClientStrings.ExamplePrimary),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    )
}
```

Do not wrap the FAB button in `Box` only to add full-width, navigation bar padding, or horizontal padding.

## Preview

Create a preview for every new composable function. Use existing preview wrappers and providers. Place every `PreviewParameterProvider` class in `ui/preview`.

Preview content composables with fake state and no-op dispatch:

```kotlin
@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun ExampleScreenContentPreview() {
    ExampleScreenContent(
        state = ExampleModel(),
        dispatch = {}
    )
}
```

## Navigation

Create a dedicated `NavKey` route in `navigation/<Feature>Route.kt`. Wire it into the appropriate navigation graph using the existing route patterns.

For navigation actions from the screen, dispatch an intent and send navigation events from the ViewModel.

## Validation

Do not run tests. Run the narrowest useful compile check for the changed target when practical, for example `./gradlew :app:compileUatDebugKotlin`.

After completing work, launch the default product flavor on the selected device using the command from `AGENTS.md`.
