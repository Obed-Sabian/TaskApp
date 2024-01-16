package com.taskapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Finalizada",
    foreignKeys = [
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["Id"],
            childColumns = ["IdActividad"],
            onDelete = CASCADE,
            onUpdate = NO_ACTION)
    ]
)
data class Finalizada(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val Id:Int?,
    @ColumnInfo(name = "FechaFinalizacion") val FechaFinalizacion: Date,
    @ColumnInfo(name = "HoraFinalizacion") val HoraFinalizacion: Date,
    @ColumnInfo(name = "IdActividad") val IdActividad:Int
)
