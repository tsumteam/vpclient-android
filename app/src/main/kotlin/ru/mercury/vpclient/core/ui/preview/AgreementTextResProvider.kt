package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.ui.theme.ClientStrings

class AgreementTextResProvider: PreviewParameterProvider<Int> {
    override val values: Sequence<Int> = sequenceOf(
        ClientStrings.RegisterAgreementText,
        ClientStrings.LoginAgreementText
    )
}
