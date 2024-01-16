package com.taskapp.data.dao

import androidx.room.*
import com.taskapp.data.entity.Iniciada

@Dao
interface IniciadaDao {
    @Query("SELECT * FROM Iniciada")
    fun getAll(): List<Iniciada>

    @Query("SELECT * FROM Iniciada WHERE IdActividad = :idActividad")
    fun getById(idActividad: Int): Iniciada

    @Insert
    fun insert(iniciada: Iniciada)
}