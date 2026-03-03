package ru.mercury.vpclient.core.ui.ktx

typealias ClickableLink = Pair<IntRange, String>

fun buildAgreementLinks(
    agreementText: String,
    linkTexts: List<String>,
    linkUris: List<String>
): List<ClickableLink> {
    require(linkTexts.size == linkUris.size) { "Link texts and links count mismatch" }
    var searchStart = 0
    return linkTexts.mapIndexed { index, linkText ->
        val startIndex = agreementText.indexOf(linkText, startIndex = searchStart)
        check(startIndex >= 0) { "Link text \"$linkText\" not found in agreement text" }
        searchStart = startIndex + linkText.length
        (startIndex until startIndex + linkText.length) to linkUris[index]
    }
}
