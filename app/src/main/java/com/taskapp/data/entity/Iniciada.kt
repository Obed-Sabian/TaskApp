package com.taskapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Iniciada",
    foreignKeys = [
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["Id"],
            childColumns = ["IdActividad"],
            onDelete = CASCADE,
            onUpdate = NO_ACTION)
    ]
)
data class Iniciada(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val Id:Int?,
    @ColumnInfo(name = "FechaIniciacion") val FechaIniciacion:Date,
    @ColumnInfo(name = "HoraIniciacion") val HoraIniciacion:Date,
    @ColumnInfo(name = "IdActividad") val IdActividad:Int
)
