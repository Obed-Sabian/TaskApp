package com.taskapp.data.dao

import androidx.room.*
import com.taskapp.data.entity.Actividad
import com.taskapp.data.entity.TipoActividad
import java.util.Date

@Dao
interface ActividadDao {

    @Query("SELECT Actividad.Id, Titulo, Descripcion, FechaCreacion, FechaLimite, IdTipoActividad FROM Actividad INNER JOIN Finalizada ON Actividad.Id = Finalizada.IdActividad")
    fun getAllCompleted(): List<Actividad>

    @Query("SELECT Actividad.Id, Titulo, Descripcion, FechaCreacion, FechaLimite, IdTipoActividad FROM Actividad LEFT JOIN Finalizada ON Actividad.Id = Finalizada.IdActividad WHERE Finalizada.IdActividad IS NULL")
    fun getAllPending(): List<Actividad>

    @Query("SELECT Actividad.Id, Titulo, Descripcion, FechaCreacion, FechaLimite, IdTipoActividad FROM Actividad INNER JOIN Finalizada ON Actividad.Id = Finalizada.IdActividad WHERE IdTipoActividad = :idTipoActividad")
    fun getCompleted(idTipoActividad: Int): List<Actividad>

    @Query("SELECT Actividad.Id, Titulo, Descripcion, FechaCreacion, FechaLimite, IdTipoActividad FROM Actividad LEFT JOIN Finalizada ON Actividad.Id = Finalizada.IdActividad WHERE Finalizada.IdActividad IS NULL AND IdTipoActividad = :idTipoActividad")
    fun getPending(idTipoActividad: Int): List<Actividad>

    @Query("SELECT * FROM Actividad ORDER BY Id DESC LIMIT 1")
    fun getLastId(): Int

    @Query("SELECT * FROM Actividad")
    fun getAll(): List<Actividad>

    @Insert
    fun insert(actividad: Actividad)

    @Update
    fun update(actividad: Actividad)

    @Delete
    fun delete(actividad: Actividad)
}