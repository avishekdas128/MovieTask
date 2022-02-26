package com.sunbase.movietask.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search")
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val search: String
)