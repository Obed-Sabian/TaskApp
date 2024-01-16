package com.taskapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TipoActividad")
data class TipoActividad(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val Id:Int?,
    @ColumnInfo(name = "TipoActividad") val TipoActividad:String
){
    override fun toString(): String = TipoActividad
}


