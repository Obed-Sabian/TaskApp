package com.taskapp.data.dao

import androidx.room.*
import com.taskapp.data.entity.TipoActividad

@Dao
interface TipoActividadDao {
    @Query("SELECT * FROM TipoActividad")
    fun getAll():  List<TipoActividad>

    @Query("SELECT count(*) FROM TipoActividad")
    fun getCount(): Int

    @Query("SELECT TipoActividad FROM TipoActividad WHERE Id = :idTipo")
    fun getById(idTipo: Int): String

    @Insert
    fun insert(tipoActividad: TipoActividad)
}