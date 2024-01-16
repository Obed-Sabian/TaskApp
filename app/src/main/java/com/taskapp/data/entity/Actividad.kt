package com.taskapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.taskapp.data.entity.TipoActividad
import java.util.Date

@Entity(tableName = "Actividad",
    foreignKeys = [
        ForeignKey(entity = TipoActividad::class, parentColumns = ["Id"], childColumns = ["IdTipoActividad"])
    ]
)
data class Actividad (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val Id:Int?,
    @ColumnInfo(name = "Titulo") val Titulo:String,
    @ColumnInfo(name = "Descripcion") val Descripcion:String,
    @ColumnInfo(name = "FechaCreacion") val FechaCreacion:Date,
    @ColumnInfo(name = "FechaLimite") val FechaLimite:Date,
    @ColumnInfo(name = "IdTipoActividad") val IdTipoActividad:Int
) : java.io.Serializable