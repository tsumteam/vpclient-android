package ru.mercury.vpclient.core

import android.database.sqlite.SQLiteConstraintException
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity

typealias CatalogPagerPage = List<CatalogCategoryEntity>

typealias RoomException = IllegalStateException
typealias RoomSQLiteException = SQLiteConstraintException
