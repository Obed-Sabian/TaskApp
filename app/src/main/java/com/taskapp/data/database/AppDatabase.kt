package com.taskapp.data.database

import android.content.Context
import androidx.room.*
import com.taskapp.data.dao.*
import com.taskapp.data.entity.*
import com.taskapp.utils.Converters

@Database(
    entities = [Actividad::class, Iniciada::class, Finalizada::class, TipoActividad::class],
    version = 3,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    companion object{
        private var database: AppDatabase? = null
        private const val DATABASE_NAME = "app_database"

        fun getDatabase(context: Context): AppDatabase{
            if(database == null){
                database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return database!!
        }
    }

    abstract fun getActividadDao(): ActividadDao

    abstract fun getIniciadaDao(): IniciadaDao

    abstract fun getFinalizadaDao(): FinalizadaDao

    abstract fun getTipoActividadDao(): TipoActividadDao
}