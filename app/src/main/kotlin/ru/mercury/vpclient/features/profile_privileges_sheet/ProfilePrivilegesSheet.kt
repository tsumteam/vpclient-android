@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_privileges_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_privileges_sheet.intent.ProfilePrivilegeIntent
import ru.mercury.vpclient.features.profile_privileges_sheet.model.ProfilePrivilegesModel
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun ProfilePrivilegesSheet(
    state: ProfilePrivilegesModel,
    dispatch: (ProfilePrivilegeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val dismiss: () -> Unit = {
        scope.launch {
            sheetState.hide()
            dispatch(ProfilePrivilegeIntent.DismissRequest)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState
    ) {
        SharedLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.ProfileAlphaBankPrivilegesTitleCaps),
                            style = MaterialTheme.typography.livretMedium18.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 26.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = dismiss
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )

                    Text(
                        text = stringResource(state.bonusTextRes),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )

                    Text(
                        text = stringResource(ClientStrings.ProfileAlphaBankPrivilegePurchases),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )

                    Text(
                        text = stringResource(ClientStrings.ProfileAlphaBankPrivilegePayment),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )

                    Text(
                        text = stringResource(state.serviceTextRes),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfilePrivilegesSheetPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfilePrivilegesSheet(
            state = ProfilePrivilegesModel(
                cardType = LoyaltyCardType.Black
            ),
            dispatch = {}
        )
    }
}
