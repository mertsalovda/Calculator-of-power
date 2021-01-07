package ru.mertsalovda.core_api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expression(
    @PrimaryKey(autoGenerate = true)
    val id: Long
)
