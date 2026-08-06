package ru.mercury.vpclient.shared.data.network.mock

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Request
import okio.Buffer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class MockCatalogBackend {

    private val basketSequence = AtomicInteger()
    private val basketItemIds = ConcurrentHashMap<String, String>()
    private val basketColorIds = ConcurrentHashMap<String, String>()
    private val basketSizeIds = ConcurrentHashMap<String, String>()
    private val fittingItemIds = ConcurrentHashMap<String, String>()
    private val fittingColorIds = ConcurrentHashMap<String, String>()
    private val fittingSizeIds = ConcurrentHashMap<String, String>()

    fun responseBody(request: Request): String? {
        val endpoint = request.mockCatalogEndpoint
        return when {
            endpoint == CATALOG_CATEGORIES_BASIC_ENDPOINT || endpoint == CATALOG_CATEGORIES_TOP_ENDPOINT -> {
                catalogCategoriesTopResponse()
            }
            endpoint == CATALOG_CATEGORIES_BOTTOM_ENDPOINT -> catalogCategoriesBottomResponse()
            endpoint in CATALOG_FILTER_ENDPOINTS -> EMPTY_FILTERS_RESPONSE
            endpoint in CATALOG_FILTER_VALUES_ENDPOINTS -> EMPTY_FILTER_VALUES_RESPONSE
            endpoint in CATALOG_PRODUCT_QUANTITY_ENDPOINTS -> PRODUCT_QUANTITY_RESPONSE
            endpoint == CATALOG_PRODUCTS_ENDPOINT -> catalogProductsResponse()
            endpoint == CATALOG_SEARCH_PRODUCTS_ENDPOINT -> catalogSearchProductsResponse()
            endpoint == CATALOG_DETAILED_PRODUCT_ENDPOINT -> detailedProductResponse(request.itemId)
            endpoint in CATALOG_PRODUCT_LIST_ENDPOINTS -> catalogLegacyProductsResponse()
            endpoint == CATALOG_BRANDS_ENDPOINT || endpoint == CATALOG_BRANDS_FAVORITES_ENDPOINT -> {
                CATALOG_BRANDS_RESPONSE
            }
            endpoint in CATALOG_BRAND_OPERATION_ENDPOINTS -> EMPTY_OPERATION_RESPONSE
            endpoint == CATALOG_BRAND_IS_FAVORITE_ENDPOINT -> BOOLEAN_FALSE_RESPONSE
            endpoint == CATALOG_AVAILABLE_COLORS_ENDPOINT -> AVAILABLE_COLORS_RESPONSE
            endpoint == CATALOG_AVAILABLE_SIZES_ENDPOINT -> AVAILABLE_SIZES_RESPONSE
            endpoint == BASKET_ENDPOINT && request.method == POST_METHOD -> basketOperationResponse(request)
            endpoint.isBasketEndpoint && request.method == GET_METHOD -> basketResponse()
            endpoint in BASKET_ADD_PRODUCT_ENDPOINTS -> addProductResponse(request)
            endpoint.isCompilationLookToBasketEndpoint -> compilationLookToBasketResponse(endpoint.lookId)
            endpoint == CLIENT_ADDRESS_CHECKOUT_ENDPOINT -> CLIENT_ADDRESS_CHECKOUT_RESPONSE
            endpoint == FITTINGS_DELIVERY_TIMES_ENDPOINT -> FITTINGS_DELIVERY_TIMES_RESPONSE
            endpoint == FITTINGS_EXISTING_DELIVERY_TIMES_ENDPOINT -> FITTINGS_EXISTING_DELIVERY_TIMES_RESPONSE
            endpoint == FITTINGS_TRANSFER_FROM_BASKET_ENDPOINT -> transferBasketToFittingResponse(request)
            endpoint == FITTINGS_ADD_OPERATIONS_ENDPOINT -> EMPTY_OPERATION_RESPONSE
            endpoint.isFittingEndpoint && request.method == GET_METHOD -> fittingResponse()
            else -> null
        }
    }

    private fun basketOperationResponse(request: Request): String {
        request.bodyObject?.get("items")?.jsonArray.orEmpty().forEach { itemElement ->
            val item = itemElement.jsonObject
            val operationType = item["operationType"]?.jsonPrimitive?.contentOrNull
            val lineId = item["lineId"]?.jsonPrimitive?.contentOrNull
            when (operationType) {
                ADD_LINE_OPERATION -> addBasketItem(
                    lineId = lineId,
                    itemId = item["itemId"]?.jsonPrimitive?.contentOrNull,
                    colorId = item["colorId"]?.jsonPrimitive?.contentOrNull,
                    sizeId = item["sizeId"]?.jsonPrimitive?.contentOrNull
                )
                REMOVE_LINE_OPERATION,
                REMOVE_PRODUCT_OPERATION -> lineId?.let(::removeBasketItem)
                CLEAR_OPERATION -> clearBasket()
            }
        }
        return EMPTY_OPERATION_RESPONSE
    }

    private fun addProductResponse(request: Request): String {
        addBasketItem(
            lineId = null,
            itemId = request.itemId,
            colorId = request.colorId,
            sizeId = request.sizeId
        )
        return EMPTY_OPERATION_RESPONSE
    }

    private fun compilationLookToBasketResponse(lookId: Int): String {
        repeat(COMPILATION_PRODUCT_COUNT) { index ->
            val productIndex = index + 1
            addBasketItem(
                lineId = "mock-compilation-$lookId-line-$productIndex",
                itemId = "mock-item-$lookId-$productIndex",
                colorId = productColorId(productIndex),
                sizeId = DEFAULT_SIZE_ID
            )
        }
        return BOOLEAN_TRUE_RESPONSE
    }

    private fun addBasketItem(
        lineId: String?,
        itemId: String?,
        colorId: String?,
        sizeId: String?
    ) {
        val resolvedItemId = itemId?.takeIf(String::isNotBlank) ?: DEFAULT_ITEM_ID
        val resolvedLineId = lineId?.takeIf(String::isNotBlank)
            ?: "mock-basket-line-${basketSequence.incrementAndGet()}"
        basketItemIds[resolvedLineId] = resolvedItemId
        basketColorIds[resolvedLineId] = colorId?.takeIf(String::isNotBlank) ?: DEFAULT_COLOR_ID
        basketSizeIds[resolvedLineId] = sizeId?.takeIf(String::isNotBlank) ?: DEFAULT_SIZE_ID
    }

    private fun removeBasketItem(lineId: String) {
        basketItemIds.remove(lineId)
        basketColorIds.remove(lineId)
        basketSizeIds.remove(lineId)
    }

    private fun clearBasket() {
        basketItemIds.clear()
        basketColorIds.clear()
        basketSizeIds.clear()
    }

    private fun transferBasketToFittingResponse(request: Request): String {
        val requestedLineIds = request.bodyObject
            ?.get("lines")
            ?.jsonArray
            .orEmpty()
            .mapNotNull { line -> line.jsonObject["lineId"]?.jsonPrimitive?.contentOrNull }
            .toSet()
        val transferredLineIds = requestedLineIds.ifEmpty { basketItemIds.keys.toSet() }

        fittingItemIds.clear()
        fittingColorIds.clear()
        fittingSizeIds.clear()
        transferredLineIds.forEach { lineId ->
            basketItemIds[lineId]?.let { itemId -> fittingItemIds[lineId] = itemId }
            basketColorIds[lineId]?.let { colorId -> fittingColorIds[lineId] = colorId }
            basketSizeIds[lineId]?.let { sizeId -> fittingSizeIds[lineId] = sizeId }
            removeBasketItem(lineId)
        }
        return EMPTY_OPERATION_RESPONSE
    }

    private fun basketResponse(): String {
        val lines = basketItemIds.entries
            .sortedBy(Map.Entry<String, String>::key)
            .mapIndexed { index, (lineId, itemId) ->
                basketLine(
                    lineId = lineId,
                    itemId = itemId,
                    colorId = basketColorIds[lineId].orEmpty().ifBlank { DEFAULT_COLOR_ID },
                    sizeId = basketSizeIds[lineId].orEmpty().ifBlank { DEFAULT_SIZE_ID },
                    index = index + 1
                )
            }
            .joinToString(",")
        return baseResponse(
            data = """{"editor":"mock","id":"mock-basket","lines":[$lines],"looks":[],"catalogActionDisclaimer":null,"timestamp":"$MOCK_TIMESTAMP","version":${basketSequence.get()}}"""
        )
    }

    private fun basketLine(
        lineId: String,
        itemId: String,
        colorId: String,
        sizeId: String,
        index: Int
    ): String {
        return """{"lineId":"$lineId","lookId":null,"order":$index,"paySwitch":true,"products":[{"productId":"mock-basket-product-$index","product":${legacyProduct(itemId = itemId, colorId = colorId, index = index, sizeId = sizeId)}}],"quantity":1,"barcode":null,"locationId":null,"locationAsString":null,"controls":null,"alternatives":[]}"""
    }

    private fun fittingResponse(): String {
        val lines = fittingItemIds.entries
            .sortedBy(Map.Entry<String, String>::key)
            .mapIndexed { index, (lineId, itemId) ->
                val colorId = fittingColorIds[lineId].orEmpty().ifBlank { DEFAULT_COLOR_ID }
                val sizeId = fittingSizeIds[lineId].orEmpty().ifBlank { DEFAULT_SIZE_ID }
                """{"lineId":"$lineId","order":${index + 1},"paySwitch":true,"dateOfExpiration":"$FITTING_EXPIRATION_DATE","logisticStatusAsStringClient":"Доставлено","dateReceiptAsString":"$FITTING_RECEIPT_DATE","product":${legacyProduct(itemId = itemId, colorId = colorId, index = index + 1, sizeId = sizeId)}}"""
            }
            .joinToString(",")
        return baseResponse(
            data = """{"fittingNumber":"MOCK-FITTING-1","id":"MOCK-FITTING-1","axaptaId":null,"deliveries":[{"deliveryId":"MOCK-FITTING-DELIVERY-1","lines":[$lines],"order":1,"address":"$CLIENT_ADDRESS","addressComment":"Домофон 14","fittingType":"atHome","deliveryTime":{"from":"$FITTING_DELIVERY_FROM","to":"$FITTING_DELIVERY_TO"},"deliveryType":"logistic","kittingType":"logistic","deliveryStatusAsString":"Доставлено","deliveryDateAsString":"7 августа"}],"returningProducts":[],"catalogActionDisclaimer":null,"timestamp":"$MOCK_TIMESTAMP","version":1}"""
        )
    }

    private fun catalogCategoriesTopResponse(): String {
        return baseResponse(
            data = """{"items":[${catalogRoot(id = 1, name = "ЖЕНСКОЕ", childStartId = 101)},${catalogRoot(id = 2, name = "МУЖСКОЕ", childStartId = 201)},${catalogRoot(id = 3, name = "ДЕТСКОЕ", childStartId = 301)}]}"""
        )
    }

    private fun catalogRoot(
        id: Int,
        name: String,
        childStartId: Int
    ): String {
        return """{"id":$id,"name":"$name","photoUrl":"${categoryImageUrl(id)}","categoryType":"catalog","sortSettingId":1,"children":[${catalogCategory(id = childStartId, name = "НОВИНКИ")},${catalogCategory(id = childStartId + 1, name = "ОДЕЖДА")},${catalogCategory(id = childStartId + 2, name = "ОБУВЬ")},${catalogCategory(id = childStartId + 3, name = "АКСЕССУАРЫ")}]}"""
    }

    private fun catalogCategoriesBottomResponse(): String {
        return baseResponse(
            data = """{"items":[${catalogBottomCategory(id = 1001, name = "ВЕРХНЯЯ ОДЕЖДА", childStartId = 1101)},${catalogBottomCategory(id = 1002, name = "ТРИКОТАЖ", childStartId = 1201)},${catalogBottomCategory(id = 1003, name = "БРЮКИ И ЮБКИ", childStartId = 1301)}]}"""
        )
    }

    private fun catalogBottomCategory(
        id: Int,
        name: String,
        childStartId: Int
    ): String {
        return """{"id":$id,"name":"$name","photoUrl":"${categoryImageUrl(id)}","categoryType":"catalog","sortSettingId":1,"children":[${catalogCategory(id = childStartId, name = "СМОТРЕТЬ ВСЕ")},${catalogCategory(id = childStartId + 1, name = "НОВИНКИ")}]}"""
    }

    private fun catalogCategory(
        id: Int,
        name: String
    ): String {
        return """{"id":$id,"name":"$name","photoUrl":"${categoryImageUrl(id)}","categoryType":"catalog","sortSettingId":1,"children":[]}"""
    }

    private fun catalogProductsResponse(): String {
        val products = (1..CATALOG_PRODUCT_COUNT).joinToString(",") { index -> catalogProduct(index) }
        return baseResponse(data = """{"items":[$products]}""")
    }

    private fun catalogSearchProductsResponse(): String {
        val products = (1..CATALOG_PRODUCT_COUNT).joinToString(",") { index -> catalogProduct(index) }
        return baseResponse(
            data = """{"products":{"items":[$products]},"correction":null,"catalogLink":null}"""
        )
    }

    private fun catalogLegacyProductsResponse(): String {
        val products = (1..CATALOG_PRODUCT_COUNT).joinToString(",") { index ->
            legacyProduct(
                itemId = "mock-catalog-item-$index",
                colorId = productColorId(index),
                index = index,
                sizeId = DEFAULT_SIZE_ID
            )
        }
        return baseResponse(data = """{"items":[$products],"paginationToken":null}""")
    }

    private fun catalogProduct(index: Int): String {
        val imageUrl = productImageUrl(index)
        return """{"id":"mock-catalog-product-$index","itemId":"mock-catalog-item-$index","colorId":"${productColorId(index)}","name":"${productName(index)}","price":${productPrice(index)},"priceWithoutDiscount":${productOldPrice(index)},"brand":"${productBrand(index)}","urlBrandLogo":null,"imageUrl":"$imageUrl","imageUrls":["$imageUrl","${productImageUrl(index + CATALOG_PRODUCT_COUNT)}"],"season":"FW26","onlyInTransit":false,"onlyInVipSite":false,"isGiftCard":false,"actions":[],"additionalColors":[]}"""
    }

    private fun detailedProductResponse(itemId: String?): String {
        val index = itemId?.substringAfterLast('-')?.toIntOrNull()?.coerceIn(1, CATALOG_PRODUCT_COUNT) ?: 1
        val imageUrl = productImageUrl(index)
        return baseResponse(
            data = """{"name":"${productName(index)}","itemId":"mock-catalog-item-$index","categoryId":102,"brandId":$index,"brand":"${productBrand(index)}","urlBrandLogo":null,"article":"MOCK-ARTICLE-$index","longDescription":"Модель из mock-каталога для проверки карточки товара и добавления в корзину.","productionStructure":"100% натуральные материалы","country":"Италия","shortDescription":"${productName(index)}","technicalDescription":null,"ekttId":"mock-ektt-$index","breadcrumbs":["Женское","Одежда"],"buttons":[],"colors":[{"colorId":"${productColorId(index)}","colorName":"${productColorName(index)}","colorHex":"${productColorHex(index)}","imageUrls":["$imageUrl","${productImageUrl(index + CATALOG_PRODUCT_COUNT)}"],"season":"FW26","artDescription":null,"areStocksAvailable":true,"isSeasonDisplay":true,"isSelected":true,"oneSize":false,"price":${productPrice(index)},"priceWithoutDiscount":${productOldPrice(index)},"actions":[],"urlItemVideo":null,"availableSizes":{"items":[${availableSize("S", "42")},${availableSize("M", "44")},${availableSize("L", "46")}],"countryCode":"RU","sizeTableTitle":"Таблица размеров","sizeTableUrl":null},"hasWearWith":false,"wearWithButtonEnabled":false,"wearWith":[],"kits":[]}]}"""
        )
    }

    private fun availableSize(sizeId: String, russianSize: String): String {
        return """{"sizeId":"$sizeId","sizeFullName":"$sizeId","russianSize":"$russianSize","inOrder":false,"inStock":true,"inStockShops":["BLV"],"isOnlyInVipSite":false,"isOnlyInTransit":false,"hasStockSubscriptions":false,"russianSizeId":$russianSize}"""
    }

    private fun legacyProduct(
        itemId: String,
        colorId: String,
        index: Int,
        sizeId: String
    ): String {
        val normalizedIndex = ((index - 1) % CATALOG_PRODUCT_COUNT) + 1
        val imageUrl = productImageUrl(normalizedIndex)
        return """{"oneSize":false,"article":"MOCK-ARTICLE-$normalizedIndex","brand":"${productBrand(normalizedIndex)}","urlBrandLogo":null,"colorId":"$colorId","colorName":"${productColorName(normalizedIndex)}","eKttId":"mock-ektt-$normalizedIndex","id":"mock-catalog-product-$normalizedIndex","imageUrl":"$imageUrl","imageUrls":["$imageUrl"],"isCharity":false,"isSeasonDisplay":true,"itemId":"$itemId","lookId":null,"name":"${productName(normalizedIndex)}","order":$index,"paySwitch":true,"price":${productPrice(normalizedIndex)},"priceWithoutDiscount":${productOldPrice(normalizedIndex)},"currentRetailPrice":${productPrice(normalizedIndex)},"quantity":1,"season":"FW26","sizes":[{"availableStockQuantity":2.0,"id":"$sizeId","inOrder":false,"inStock":true,"inStockShops":["BLV"],"isFavorite":false,"isLastInStock":false,"name":"$sizeId","sizeForFilter":"$sizeId","onlyInVipSite":false,"onlyInTransit":false}],"actions":[],"onlyInTransit":false,"onlyInVipSite":false,"breadcrumbs":["Женское","Одежда"],"compilationLookProductId":null,"isGiftCard":false,"discountPercentage":0,"additionalColors":[]}"""
    }

    private fun productName(index: Int): String {
        return when (index) {
            1 -> "Жакет из шерсти"
            2 -> "Юбка из шелка"
            3 -> "Джемпер из кашемира"
            4 -> "Брюки прямого кроя"
            5 -> "Кожаные мюли"
            else -> "Сумка с плетением"
        }
    }

    private fun productBrand(index: Int): String {
        return when (index) {
            1 -> "SAINT LAURENT"
            2 -> "BRUNELLO CUCINELLI"
            3 -> "LORO PIANA"
            4 -> "THE ROW"
            5 -> "JIL SANDER"
            else -> "BOTTEGA VENETA"
        }
    }

    private fun productPrice(index: Int): Int {
        return when (index) {
            1 -> 219900
            2 -> 129900
            3 -> 92500
            4 -> 74900
            5 -> 68500
            else -> 189900
        }
    }

    private fun productOldPrice(index: Int): Int {
        return when (index) {
            2 -> 159900
            5 -> 82500
            else -> productPrice(index)
        }
    }

    private fun productColorId(index: Int): String {
        return when (index) {
            1 -> "black"
            2 -> "ivory"
            3 -> "grey"
            4 -> "navy"
            5 -> "beige"
            else -> "green"
        }
    }

    private fun productColorName(index: Int): String {
        return when (index) {
            1 -> "Черный"
            2 -> "Айвори"
            3 -> "Серый"
            4 -> "Темно-синий"
            5 -> "Бежевый"
            else -> "Зеленый"
        }
    }

    private fun productColorHex(index: Int): String {
        return when (index) {
            1 -> "#111111"
            2 -> "#F3EDE2"
            3 -> "#8E8E8E"
            4 -> "#1C2948"
            5 -> "#D8C3A5"
            else -> "#315C45"
        }
    }

    private fun productImageUrl(index: Int): String {
        return STOCK_FASHION_IMAGE_URLS[(index - 1) % STOCK_FASHION_IMAGE_URLS.size]
    }

    private fun categoryImageUrl(index: Int): String {
        return STOCK_FASHION_IMAGE_URLS[(index + CATEGORY_IMAGE_OFFSET) % STOCK_FASHION_IMAGE_URLS.size]
    }

    private fun baseResponse(data: String): String {
        return """{"data":$data,"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
    }

    private val Request.mockCatalogEndpoint: String
        get() = url.encodedPath
            .trim('/')
            .removePrefix("$API_PATH_PREFIX/")

    private val Request.bodyText: String
        get() {
            val buffer = Buffer()
            body?.writeTo(buffer)
            return buffer.readUtf8()
        }

    private val Request.bodyObject: JsonObject?
        get() = runCatching { Json.parseToJsonElement(bodyText).jsonObject }.getOrNull()

    private val Request.itemId: String?
        get() = bodyObject?.get("itemId")?.jsonPrimitive?.contentOrNull

    private val Request.colorId: String?
        get() = bodyObject?.get("colorId")?.jsonPrimitive?.contentOrNull

    private val Request.sizeId: String?
        get() = bodyObject?.get("sizeId")?.jsonPrimitive?.contentOrNull

    private val String.isBasketEndpoint: Boolean
        get() = startsWith("$BASKET_ENDPOINT/") && count { char -> char == '/' } == 1

    private val String.isFittingEndpoint: Boolean
        get() = startsWith("$FITTINGS_ENDPOINT/") && count { char -> char == '/' } == 1

    private val String.isCompilationLookToBasketEndpoint: Boolean
        get() = startsWith(COMPILATION_LOOK_ENDPOINT_PREFIX) && endsWith(COMPILATION_LOOK_TO_BASKET_SUFFIX)

    private val String.lookId: Int
        get() = removePrefix(COMPILATION_LOOK_ENDPOINT_PREFIX)
            .removeSuffix(COMPILATION_LOOK_TO_BASKET_SUFFIX)
            .toIntOrNull()
            ?: DEFAULT_LOOK_ID

    private companion object {
        private const val API_PATH_PREFIX = "api"
        private const val GET_METHOD = "GET"
        private const val POST_METHOD = "POST"
        private const val CATALOG_CATEGORIES_BASIC_ENDPOINT = "catalog/categories/basic"
        private const val CATALOG_CATEGORIES_TOP_ENDPOINT = "catalog/categories/top"
        private const val CATALOG_CATEGORIES_BOTTOM_ENDPOINT = "catalog/categories/bottom"
        private const val CATALOG_PRODUCTS_ENDPOINT = "catalog/products"
        private const val CATALOG_SEARCH_PRODUCTS_ENDPOINT = "catalog/by-text/products/diginetica"
        private const val CATALOG_DETAILED_PRODUCT_ENDPOINT = "catalog/detailed-product"
        private const val CATALOG_BRANDS_ENDPOINT = "catalog/brands"
        private const val CATALOG_BRANDS_FAVORITES_ENDPOINT = "catalog/brands/favorites"
        private const val CATALOG_BRAND_IS_FAVORITE_ENDPOINT = "catalog/brands/is-favorite"
        private const val CATALOG_AVAILABLE_COLORS_ENDPOINT = "catalog/available/colors"
        private const val CATALOG_AVAILABLE_SIZES_ENDPOINT = "catalog/available/sizes"
        private const val BASKET_ENDPOINT = "basket"
        private const val CLIENT_ADDRESS_CHECKOUT_ENDPOINT = "client/address/checkout"
        private const val FITTINGS_ENDPOINT = "fittings"
        private const val FITTINGS_DELIVERY_TIMES_ENDPOINT = "fittings/delivery-times"
        private const val FITTINGS_EXISTING_DELIVERY_TIMES_ENDPOINT = "fittings/delivery-times-for-exising-delivery"
        private const val FITTINGS_TRANSFER_FROM_BASKET_ENDPOINT = "fittings/transfer-from-basket"
        private const val FITTINGS_ADD_OPERATIONS_ENDPOINT = "fittings/add-operations"
        private const val COMPILATION_LOOK_ENDPOINT_PREFIX = "compilations/client/look/"
        private const val COMPILATION_LOOK_TO_BASKET_SUFFIX = "/to-basket"
        private const val DEFAULT_LOOK_ID = 10301
        private const val CATALOG_PRODUCT_COUNT = 6
        private const val COMPILATION_PRODUCT_COUNT = 3
        private const val CATEGORY_IMAGE_OFFSET = 3
        private const val ADD_LINE_OPERATION = "addLine"
        private const val REMOVE_LINE_OPERATION = "removeLine"
        private const val REMOVE_PRODUCT_OPERATION = "removeProductFromLine"
        private const val CLEAR_OPERATION = "clear"
        private const val DEFAULT_ITEM_ID = "mock-catalog-item-1"
        private const val DEFAULT_COLOR_ID = "black"
        private const val DEFAULT_SIZE_ID = "M"
        private const val MOCK_TIMESTAMP = "2026-08-06T12:00:00+03:00"
        private const val CLIENT_ADDRESS = "Москва, Рублевское ш., д. 9, кв./офис 14"
        private const val BOUTIQUE_ADDRESS = "Москва, ул. Петровка, д. 2"
        private const val FITTING_DELIVERY_FROM = "2026-08-07T14:00:00+03:00"
        private const val FITTING_DELIVERY_TO = "2026-08-07T16:00:00+03:00"
        private const val FITTING_RECEIPT_DATE = "2026-08-07T16:00:00+03:00"
        private const val FITTING_EXPIRATION_DATE = "2026-08-10T20:00:00+03:00"

        private const val EMPTY_OPERATION_RESPONSE = """{"data":{},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val EMPTY_FILTERS_RESPONSE = """{"data":{"filters":[]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val EMPTY_FILTER_VALUES_RESPONSE = """{"data":{"filterValues":[]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val PRODUCT_QUANTITY_RESPONSE = """{"data":{"quantity":6},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val BOOLEAN_TRUE_RESPONSE = """{"data":true,"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val BOOLEAN_FALSE_RESPONSE = """{"data":false,"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val AVAILABLE_COLORS_RESPONSE = """{"data":{"items":[{"colorFullName":"Черный","colorHex":"#111111","colorId":"black","isOnlyInVipSite":false,"isOnlyInTransit":false},{"colorFullName":"Бежевый","colorHex":"#D8C3A5","colorId":"beige","isOnlyInVipSite":false,"isOnlyInTransit":false}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val AVAILABLE_SIZES_RESPONSE = """{"data":{"items":[{"sizeFullName":"S","sizeId":"S","russianSizeId":42,"russianSize":"42","inOrder":false,"inStock":true,"inStockShops":["BLV"],"isOnlyInVipSite":false,"isOnlyInTransit":false},{"sizeFullName":"M","sizeId":"M","russianSizeId":44,"russianSize":"44","inOrder":false,"inStock":true,"inStockShops":["BLV"],"isOnlyInVipSite":false,"isOnlyInTransit":false},{"sizeFullName":"L","sizeId":"L","russianSizeId":46,"russianSize":"46","inOrder":false,"inStock":true,"inStockShops":["BLV"],"isOnlyInVipSite":false,"isOnlyInTransit":false}],"sizeTableTitle":"Таблица размеров","sizeTableUrl":null},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val CATALOG_BRANDS_RESPONSE = """{"data":{"items":[{"categoryId":1,"categoryName":"Женское","brands":[{"id":1,"name":"SAINT LAURENT","photoUrl":null,"isTopBrand":true,"isFavorite":false,"restrictionType":null},{"id":2,"name":"LORO PIANA","photoUrl":null,"isTopBrand":true,"isFavorite":false,"restrictionType":null},{"id":3,"name":"BOTTEGA VENETA","photoUrl":null,"isTopBrand":false,"isFavorite":false,"restrictionType":null}]}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-catalog"}"""
        private const val CLIENT_ADDRESS_CHECKOUT_RESPONSE = """{"data":{"clientAddress":{"latitude":55.7539,"longitude":37.6208,"addressId":1,"address":"$CLIENT_ADDRESS","flat":"14","entrance":"1","intercom":"14","floor":"4","comment":"Домофон 14"},"boutiqueAddress":{"boutiqueId":"MOCK-BOUTIQUE-1","address":"$BOUTIQUE_ADDRESS","shortAddress":"Петровка, 2","brandName":"VIPAVENUE"},"controls":{"isDeliveryToClientAvailable":true}},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-fitting"}"""
        private const val FITTINGS_DELIVERY_TIMES_RESPONSE = """{"data":{"deliveryTimes":[{"from":"2026-08-07T12:00:00+03:00","to":"2026-08-07T14:00:00+03:00"},{"from":"$FITTING_DELIVERY_FROM","to":"$FITTING_DELIVERY_TO"},{"from":"2026-08-08T10:00:00+03:00","to":"2026-08-08T12:00:00+03:00"}],"deliveries":[]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-fitting"}"""
        private const val FITTINGS_EXISTING_DELIVERY_TIMES_RESPONSE = """{"data":{"deliveryTimes":[{"from":"2026-08-07T12:00:00+03:00","to":"2026-08-07T14:00:00+03:00"},{"from":"$FITTING_DELIVERY_FROM","to":"$FITTING_DELIVERY_TO"},{"from":"2026-08-08T10:00:00+03:00","to":"2026-08-08T12:00:00+03:00"}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-fitting"}"""

        private val CATALOG_FILTER_ENDPOINTS = setOf(
            "catalog/filters",
            "catalog/by-text/filters/diginetica"
        )
        private val CATALOG_FILTER_VALUES_ENDPOINTS = setOf(
            "catalog/filter-values",
            "catalog/by-text/filter-values"
        )
        private val CATALOG_PRODUCT_QUANTITY_ENDPOINTS = setOf(
            "catalog/filter-products-quantity",
            "catalog/by-text/filter-products-quantity"
        )
        private val CATALOG_PRODUCT_LIST_ENDPOINTS = setOf(
            "catalog/newArrivals",
            "catalog/newLookProducts",
            "catalog/view-history"
        )
        private val CATALOG_BRAND_OPERATION_ENDPOINTS = setOf(
            "catalog/brands/like",
            "catalog/brands/unlike"
        )
        private val BASKET_ADD_PRODUCT_ENDPOINTS = setOf(
            "basket/add-product-by-barcode",
            "basket/add-product-by-barcode-and-locationid",
            "basket/add-product-from-detailed-stocks",
            "basket/add-products-from-catalog-with-selected-russian-size"
        )

        // Free stock photos from Unsplash, cropped by its image CDN for product cards.
        private val STOCK_FASHION_IMAGE_URLS = listOf(
            "https://images.unsplash.com/photo-1562349502-153e491776bc?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1542486280-22a6cfb92d23?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1548102063-1a9a87212e98?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1551524780-69a731c62fc0?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1559582800-b7f6bf426431?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1552252059-9d77e4059ad1?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1581381685617-4dc270458aa6?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1557684387-08927d28c72a?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1556452577-15f4ca79799b?auto=format&fit=crop&w=800&h=1100&q=85",
            "https://images.unsplash.com/photo-1574968699009-6426913fce69?auto=format&fit=crop&w=800&h=1100&q=85"
        )
    }
}
