package com.taskapp

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taskapp.data.dao.ActividadDao
import com.taskapp.data.dao.TipoActividadDao
import com.taskapp.data.database.AppDatabase
import com.taskapp.data.entity.Actividad
import com.taskapp.data.entity.TipoActividad
import com.taskapp.databinding.ActivityMainBinding
import com.taskapp.view.adapters.ActividadAdapter
import com.taskapp.view.notification.AlarmReceiver

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var seleccion = "Pendientes"
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var actividadDao: ActividadDao
    private lateinit var tipoActividadDao: TipoActividadDao
    private val adapter = ActividadAdapter(
        fragmentManager = supportFragmentManager,
        onDelete = { actividad ->
            eliminarActividad(actividad)
        },
        onUpdate = { actividad ->
            onUpdate(actividad)
        },
        completed = false,
        ActividadesPendientes = {
            obtenerActividadesPendientes()
        },
        cancelAlarm = {id ->
            cancelAlarm(id)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        actividadDao = database.getActividadDao()
        tipoActividadDao = database.getTipoActividadDao()
        verificarTipoActividad()

        val adapter = ArrayAdapter(applicationContext, R.layout.item_dropdown_list, tipoActividadDao.getAll())
        adapter.setDropDownViewResource(R.layout.item_dropdown)
        adapter.insert(TipoActividad(1, "Todas"), 0)
        binding.spnType.adapter = adapter
        binding.spnType.onItemSelectedListener =  this

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                obtenerActividadesPendientes()
            }
        }

        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            getContent.launch(intent)
        }

        binding.btnPendientes.setOnClickListener {
            binding.fbAddNote.visibility = View.VISIBLE
            obtenerActividadesPendientes()
            seleccion = "Pendientes"
        }

        binding.btnCompletadas.setOnClickListener {
            binding.fbAddNote.visibility = View.INVISIBLE
            obtenerActividadesCompletadas()
            seleccion = "Completadas"
        }

        setUpRecycler()
        createNotificationChannel()
    }

    //Esta función permite cancelar la alarma, esto mediante el identificador id
    private fun cancelAlarm(id: Int) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "task_notification_id_ReminderChannel"
            val description = "Canal para alarm manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("task_notification_id", name, importance)
            channel.description = description
            channel.enableVibration(true)
            //Patrón de vibración a utilizar.
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 480, 300, 200, 430)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setUpRecycler(){
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvTareas.adapter = adapter
        binding.rvTareas.layoutManager = layoutManager
    }

    //Obtiene las actividades pendientes según el tipo de actividad seleccionada
    private fun obtenerActividadesPendientes(){
        try {
            val actividades: List<Actividad>
            val tipo = binding.spnType.selectedItem as TipoActividad
            if(tipo.TipoActividad == "Todas")
                actividades = actividadDao.getAllPending()
            else
                actividades = actividadDao.getPending(tipo.Id ?: 0)
            adapter.setTasks(actividades, false)
        } catch (ex: Exception){
            Toast.makeText(this, "No se pudieron obtener las actividades", Toast.LENGTH_SHORT).show()
        }
    }

    //Obtiene las actividades completadas según el tipo de actividad seleccionada
    private fun obtenerActividadesCompletadas(){
        try {
            val actividades: List<Actividad>
            val tipo = binding.spnType.selectedItem as TipoActividad
            if(tipo.TipoActividad == "Todas")
                actividades = actividadDao.getAllCompleted()
            else
                actividades = actividadDao.getCompleted(tipo.Id ?: 0)
            adapter.setTasks(actividades, true)
        } catch (ex: Exception){
            Toast.makeText(this, "No se pudieron obtener las actividades", Toast.LENGTH_SHORT).show()
        }
    }

    //Elimina la actividad y cancela la alarma
    private fun eliminarActividad(actividad: Actividad){
        try {
            actividadDao.delete(actividad)
            cancelAlarm(actividad.Id ?: 0)
            if(seleccion == "Completadas") obtenerActividadesCompletadas()
            else obtenerActividadesPendientes()
            Toast.makeText(this, "Se elimino la actividad ${actividad.Titulo}", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception){
            Toast.makeText(this, "No se pudo eliminar la actividad", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onUpdate(actividad: Actividad){
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra("task", actividad)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1)
            if(resultCode == Activity.RESULT_OK){
                obtenerActividadesPendientes()
            }
    }

    private fun verificarTipoActividad(){
        if(tipoActividadDao.getCount() == 0){
            tipoActividadDao.insert(TipoActividad(null, "Domestica"))
            tipoActividadDao.insert(TipoActividad(null, "Laboral"))
            tipoActividadDao.insert(TipoActividad(null, "Escuela"))
            tipoActividadDao.insert(TipoActividad(null, "Personal"))
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        if(seleccion == "Completadas") obtenerActividadesCompletadas()
        else obtenerActividadesPendientes()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {    }
}