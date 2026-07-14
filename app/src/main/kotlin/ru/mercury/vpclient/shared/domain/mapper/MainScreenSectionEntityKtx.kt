package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionItemEntity
import ru.mercury.vpclient.shared.data.network.type.MainScreenCategoryType
import ru.mercury.vpclient.shared.data.persistence.database.entity.MainScreenSectionEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.MainScreenSectionItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.MainScreenSectionPojo

fun List<HomeSectionEntity>.mainScreenSectionEntities(
    category: MainScreenCategoryType
): List<MainScreenSectionEntity> {
    return map { section ->
        MainScreenSectionEntity(
            id = "${category.name}_${section.order}",
            category = category,
            type = section.type,
            order = section.order,
            title = section.title,
            titleCatalogLink = section.titleCatalogLink,
            imageUrl = section.imageUrl,
            giftCardPhotoUrl = section.giftCardPhotoUrl
        )
    }
}

fun List<HomeSectionEntity>.mainScreenSectionItemEntities(
    category: MainScreenCategoryType
): List<MainScreenSectionItemEntity> {
    return flatMap { section ->
        section.items.mapIndexed { index, item ->
            MainScreenSectionItemEntity(
                sectionId = "${category.name}_${section.order}",
                position = index,
                title = item.title,
                subtitle = item.subtitle,
                imageUrl = item.imageUrl,
                videoUrl = item.videoUrl,
                brand = item.brand,
                brandLogoUrl = item.brandLogoUrl,
                productId = item.productId,
                productItemId = item.productItemId,
                productColorId = item.productColorId,
                catalogLink = item.catalogLink,
                linkType = item.linkType,
                bannerLinkUrl = item.bannerLinkUrl,
                fashionImageId = item.fashionImageId,
                fashionImageTitle = item.fashionImageTitle
            )
        }
    }
}

fun MainScreenSectionPojo.homeSectionEntity(): HomeSectionEntity {
    return HomeSectionEntity(
        type = entity.type,
        order = entity.order,
        title = entity.title,
        items = items
            .sortedBy { item -> item.position }
            .map { item ->
                HomeSectionItemEntity(
                    title = item.title,
                    subtitle = item.subtitle,
                    imageUrl = item.imageUrl,
                    videoUrl = item.videoUrl,
                    brand = item.brand,
                    brandLogoUrl = item.brandLogoUrl,
                    productId = item.productId,
                    productItemId = item.productItemId,
                    productColorId = item.productColorId,
                    catalogLink = item.catalogLink,
                    linkType = item.linkType,
                    bannerLinkUrl = item.bannerLinkUrl,
                    fashionImageId = item.fashionImageId,
                    fashionImageTitle = item.fashionImageTitle
                )
            },
        titleCatalogLink = entity.titleCatalogLink,
        imageUrl = entity.imageUrl,
        giftCardPhotoUrl = entity.giftCardPhotoUrl
    )
}
