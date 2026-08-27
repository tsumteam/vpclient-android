package ru.mercury.vpclient.features.messenger_attach_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MessengerAttachIntent: Intent {
    data object DismissClick: MessengerAttachIntent
    data object GalleryClick: MessengerAttachIntent
    data object CartProductsClick: MessengerAttachIntent
    data object CatalogClick: MessengerAttachIntent
}
