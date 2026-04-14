package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

class AgreementTextResProvider: PreviewParameterProvider<Int> {
    override val values: Sequence<Int> = sequenceOf(
        ClientStrings.RegisterAgreementText,
        ClientStrings.LoginAgreementText
    )
}
