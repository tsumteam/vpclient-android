package ru.mercury.vpclient.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.PERSONAL_DATA_URL
import ru.mercury.vpclient.core.PRIVACY_POLICY_URL
import ru.mercury.vpclient.core.TERMS_OF_USE_URL
import ru.mercury.vpclient.core.ui.ktx.buildAgreementLinks
import ru.mercury.vpclient.core.ui.preview.AgreementTextResProvider
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.core.ui.theme.regular15

@Composable
fun AgreementText(
    @StringRes agreementTextRes: Int,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    val agreementLinkTexts = listOf(
        stringResource(ClientStrings.AgreementPersonalData),
        stringResource(ClientStrings.AgreementTermsOfUse),
        stringResource(ClientStrings.AgreementPrivacyPolicy)
    )
    val agreementText = stringResource(
        agreementTextRes,
        agreementLinkTexts.component1(),
        agreementLinkTexts.component2(),
        agreementLinkTexts.component3()
    )
    val agreementLinks = remember(agreementText, agreementLinkTexts) {
        buildAgreementLinks(
            agreementText = agreementText,
            linkTexts = agreementLinkTexts,
            linkUris = listOf(PERSONAL_DATA_URL, TERMS_OF_USE_URL, PRIVACY_POLICY_URL)
        )
    }

    ClientText(
        text = agreementText,
        clickableRanges = agreementLinks.map { it.first },
        onClick = { index -> uriHandler.openUri(agreementLinks[index].second) },
        modifier = modifier,
        style = MaterialTheme.typography.regular15
            .copy(lineHeight = 19.sp, letterSpacing = .2.sp, textAlign = TextAlign.Center)
            .onBackground()
    )
}

@Preview(showBackground = true)
@Composable
private fun AgreementTextPreview(
    @PreviewParameter(AgreementTextResProvider::class) @StringRes agreementTextRes: Int
) {
    ClientTheme {
        AgreementText(agreementTextRes = agreementTextRes)
    }
}
