package ru.mercury.vpclient.shared

import android.database.sqlite.SQLiteConstraintException
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogCategoryEntity

typealias CatalogPagerPage = List<CatalogCategoryEntity>

typealias RoomException = IllegalStateException
typealias RoomSQLiteException = SQLiteConstraintException
