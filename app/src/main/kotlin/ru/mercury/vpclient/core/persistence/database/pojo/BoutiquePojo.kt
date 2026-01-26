package ru.mercury.vpclient.core.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity

data class BoutiquePojo(
    @Embedded val boutiqueEntity: BoutiqueEntity,
    @Relation(
        parentColumn = "boutiqueId",
        entityColumn = "boutiqueId",
        entity = DeliveryEntity::class
    ) val deliveryEntities: List<DeliveryEntity>,
    @Relation(
        parentColumn = "boutiqueId",
        entityColumn = "boutiqueId",
        entity = CargoEntity::class
    ) val cargoEntities: List<CargoEntity>
)
