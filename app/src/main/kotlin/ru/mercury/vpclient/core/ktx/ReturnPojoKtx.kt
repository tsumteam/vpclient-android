package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.core.persistence.database.pojo.ReturnPojo

val ReturnPojo.returnProductEntities: List<ProductEntity>
    get() = productEntities.filter(ProductEntity::isStatusReturn)

val ReturnPojo.returnProductCount: Int
    get() = returnProductEntities.count()

val ReturnPojo.returnDeliveryEntities: List<DeliveryEntity>
    get() {
        val productsDeliveryIds = returnProductEntities.map { it.deliveryId }
        return deliveryEntities.filter { it.deliveryId in productsDeliveryIds }
    }

fun ReturnPojo.productCount(deliveryId: DeliveryId): Int {
    return productEntities.count { it.deliveryId == deliveryId }
}
