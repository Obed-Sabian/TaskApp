package com.taskapp.data.dao

import androidx.room.*
import com.taskapp.data.entity.Finalizada

@Dao
interface FinalizadaDao {
    @Query("SELECT * FROM Finalizada")
    fun getAll(): List<Finalizada>

    @Query("SELECT * FROM Finalizada WHERE IdActividad = :idActividad")
    fun getById(idActividad: Int): Finalizada

    @Insert
    fun insert(finalizada: Finalizada)

    @Delete
    fun delete(finalizada: Finalizada)
}