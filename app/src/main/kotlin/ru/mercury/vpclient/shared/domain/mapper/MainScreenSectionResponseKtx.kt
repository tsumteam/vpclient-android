package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionItemEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionType
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.network.response.MainScreenItemResponse
import ru.mercury.vpclient.shared.data.network.response.MainScreenSectionResponse
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.network.type.MainScreenCategoryType

val TabType.mainScreenCategoryType: MainScreenCategoryType
    get() = when (this) {
        TabType.WOMAN -> MainScreenCategoryType.WOMAN
        TabType.MAN -> MainScreenCategoryType.MAN
        TabType.CHILD -> MainScreenCategoryType.CHILD
    }

val List<MainScreenSectionResponse>.homeSectionEntities: List<HomeSectionEntity>
    get() = mapNotNull { response -> response.homeSectionEntity }
        .sortedBy { entity -> entity.order }

private val MainScreenSectionResponse.homeSectionEntity: HomeSectionEntity?
    get() {
        val resolvedType = sectionType.homeSectionType ?: return null
        val giftCardPhotoUrl = giftCards
            ?.items
            .orEmpty()
            .firstOrNull { item -> item.type == GiftCardType.VIRTUAL }
            ?.templates
            .orEmpty()
            .sortedBy { template -> template.orderView ?: Int.MAX_VALUE }
            .firstOrNull()
            ?.photoUrl
            .orEmpty()
        return HomeSectionEntity(
            type = resolvedType,
            order = order ?: Int.MAX_VALUE,
            title = title.orEmpty(),
            items = items.map { item -> item.homeSectionItemEntity },
            titleCatalogLink = titleCatalogLink,
            imageUrl = imageUrl.orEmpty(),
            giftCardPhotoUrl = giftCardPhotoUrl
        )
    }

private val MainScreenItemResponse.homeSectionItemEntity: HomeSectionItemEntity
    get() {
        val product = searchCard
        return HomeSectionItemEntity(
            title = product?.name ?: title.orEmpty(),
            subtitle = subtitle.orEmpty(),
            imageUrl = product?.imageUrl ?: imageUrl.orEmpty(),
            videoUrl = videoUrl.orEmpty(),
            brand = product?.brand.orEmpty(),
            brandLogoUrl = product?.urlBrandLogo,
            productId = product?.id,
            productItemId = product?.itemId,
            productColorId = product?.colorId,
            catalogLink = catalogLink,
            linkType = linkType,
            bannerLinkUrl = bannerLinkUrl,
            fashionImageId = fashionImageId,
            fashionImageTitle = fashionImageTitle
        )
    }

private val String?.homeSectionType: HomeSectionType?
    get() = when (this) {
        "bigBannersWithoutTitleCarousel" -> HomeSectionType.BIG_BANNERS_WITHOUT_TITLE_CAROUSEL
        "bigBannersWithTitleCarousel" -> HomeSectionType.BIG_BANNERS_WITH_TITLE_CAROUSEL
        "mediumBannersWithoutTitleCarousel" -> HomeSectionType.MEDIUM_BANNERS_WITHOUT_TITLE_CAROUSEL
        "smallBannersCarousel" -> HomeSectionType.SMALL_BANNERS_CAROUSEL
        "productsCarousel" -> HomeSectionType.PRODUCTS_CAROUSEL
        "catalogCompilations" -> HomeSectionType.CATALOG_COMPILATIONS
        "giftCards" -> HomeSectionType.GIFT_CARDS
        "squareVideosWithoutTitleCarousel" -> HomeSectionType.SQUARE_VIDEOS_WITHOUT_TITLE_CAROUSEL
        "verticalVideosWithoutTitleCarousel" -> HomeSectionType.VERTICAL_VIDEOS_WITHOUT_TITLE_CAROUSEL
        "squareVideosWithTitleCarousel" -> HomeSectionType.SQUARE_VIDEOS_WITH_TITLE_CAROUSEL
        "verticalVideosWithTitleCarousel" -> HomeSectionType.VERTICAL_VIDEOS_WITH_TITLE_CAROUSEL
        else -> null
    }
