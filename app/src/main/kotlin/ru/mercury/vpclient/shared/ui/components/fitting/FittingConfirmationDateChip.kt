package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium12
import ru.mercury.vpclient.shared.ui.theme.regular12
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class FittingConfirmationDateChipState(
    val dayId: String,
    val dayTitle: String,
    val selected: Boolean,
    val onClick: () -> Unit
)

@Composable
fun FittingConfirmationDateChip(
    state: FittingConfirmationDateChipState,
    modifier: Modifier = Modifier
) {
    val date = state.dayId.fittingConfirmationChipDate
    val isTomorrow = date == LocalDate.now().plusDays(1)
    val topText = when {
        isTomorrow -> "Завтра"
        date != null -> date.dayOfWeek.fittingConfirmationChipWeekDay
        else -> state.dayTitle
    }
    val bottomText = when {
        isTomorrow -> null
        else -> date?.format(fittingConfirmationChipDateFormatter)
    }

    Box(
        modifier = modifier
            .height(58.dp)
            .border(
                width = 2.dp,
                color = when {
                    state.selected -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.background
                },
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = state.onClick)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(52.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(start = 12.dp, end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = topText,
                    maxLines = 1,
                    style = MaterialTheme.typography.medium12.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )
                )

                if (bottomText != null) {
                    Text(
                        text = bottomText,
                        maxLines = 1,
                        style = MaterialTheme.typography.regular12.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingConfirmationDateChipPreview(
    @PreviewParameter(FittingConfirmationDateChipStateProvider::class) state: FittingConfirmationDateChipState
) {
    FittingConfirmationDateChip(
        state = state
    )
}

private class FittingConfirmationDateChipStateProvider: PreviewParameterProvider<FittingConfirmationDateChipState> {
    override val values: Sequence<FittingConfirmationDateChipState> = sequenceOf(
        FittingConfirmationDateChipState(
            dayId = LocalDate.now().plusDays(1).toString(),
            dayTitle = "Завтра",
            selected = true,
            onClick = {}
        ),
        FittingConfirmationDateChipState(
            dayId = "2026-05-16",
            dayTitle = "16 мая",
            selected = false,
            onClick = {}
        )
    )
}

private val fittingConfirmationChipDateFormatter = DateTimeFormatter.ofPattern(
    "d MMMM",
    Locale.forLanguageTag("ru")
)

private val String.fittingConfirmationChipDate: LocalDate?
    get() = runCatching { LocalDate.parse(this) }.getOrNull()

private val DayOfWeek.fittingConfirmationChipWeekDay: String
    get() = when (this) {
        DayOfWeek.MONDAY -> "Пн"
        DayOfWeek.TUESDAY -> "Вт"
        DayOfWeek.WEDNESDAY -> "Ср"
        DayOfWeek.THURSDAY -> "Чт"
        DayOfWeek.FRIDAY -> "Пт"
        DayOfWeek.SATURDAY -> "Сб"
        DayOfWeek.SUNDAY -> "Вс"
    }
