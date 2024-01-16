package com.taskapp.view.notification

import android.app.*
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.widget.Toast
import com.taskapp.data.dao.ActividadDao
import com.taskapp.data.database.AppDatabase
import com.taskapp.data.entity.Actividad
import com.taskapp.utils.DateFormat
import java.util.Date

class RebootService(name: String? = "servicio") : IntentService(name) {
    private lateinit var database: AppDatabase
    private lateinit var actividadDao: ActividadDao

    //Esta función obtiene las actividades pendiente y vuelva a programar las alarmas
    override fun onHandleIntent(intent: Intent?) {
        database = AppDatabase.getDatabase(this)
        actividadDao = database.getActividadDao()

        val intentType = intent?.extras?.getString("caller") ?: return
        if (intentType == "RebootReceiver"){
            createNotificationChannel()
            val actividades = actividadDao.getAllPending()
            var actividad: Actividad
            val fechaHoy = Date()
            for (i in actividades.indices) {
                actividad = actividades[i]
                if (fechaHoy < DateFormat.format(actividad.FechaLimite)){
                    setAlarm(actividad.Id ?: 0, actividad.Titulo, actividad.Descripcion, DateFormat.setDate(actividad.FechaLimite))
                }
            }
        }
    }

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private fun setAlarm(id: Int, titulo: String, contenido: String, fecha: Calendar) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("id", id)
        intent.putExtra("titulo", titulo)
        intent.putExtra("contenido", contenido)

        pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.set(AlarmManager.RTC_WAKEUP, fecha.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "task_notification_id_ReminderChannel"
            val description = "Canal para alarm manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("task_notification_id", name, importance)
            channel.description = description
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 480, 300, 200, 430)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}