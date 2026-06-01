package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun FittingConfirmationDeliveryGroupCard(
    group: FittingConfirmationDeliveryGroup,
    selectedDayId: String?,
    selectedIntervalId: String?,
    expanded: Boolean,
    onChangeTimeClick: () -> Unit,
    onDayClick: (String) -> Unit,
    onIntervalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedInterval = group.intervals.firstOrNull { interval -> interval.id == selectedIntervalId }
    val productsCount = group.products.sumOf { it.quantity }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        append(
                            pluralStringResource(
                                ClientStrings.FittingConfirmationDeliveryProductsCount,
                                productsCount,
                                productsCount
                            )
                        )
                    }
                    append(": ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append(selectedInterval?.summary.orEmpty())
                    }
                },
                modifier = Modifier.weight(1F),
                style = MaterialTheme.typography.regular14.copy(
                    lineHeight = 20.sp
                )
            )

            AnimatedVisibility(
                visible = !expanded
            ) {
                TextButton(
                    onClick = onChangeTimeClick,
                    modifier = Modifier.height(40.dp),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = stringResource(ClientStrings.FittingConfirmationChangeTime),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.medium13.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Right
                        )
                    )
                }
            }
        }

        if (!expanded) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FittingConfirmationProductsRow(
                    products = group.products
                )

                FittingConfirmationDaysRow(
                    intervals = group.intervals,
                    selectedDayId = selectedDayId,
                    onDayClick = onDayClick
                )

                FittingConfirmationIntervalsRow(
                    intervals = group.intervals.filter { interval ->
                        interval.dayId == (selectedDayId ?: group.intervals.firstOrNull()?.dayId)
                    },
                    selectedIntervalId = selectedIntervalId,
                    onIntervalClick = onIntervalClick
                )
            }
        }
    }
}
