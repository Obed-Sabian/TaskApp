package com.taskapp

import android.app.*
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.taskapp.data.database.AppDatabase
import com.taskapp.data.entity.Actividad
import com.taskapp.data.entity.Iniciada
import com.taskapp.data.entity.TipoActividad
import com.taskapp.databinding.ActivityAddTaskBinding
import com.taskapp.dialogs.DatePickerFragment
import com.taskapp.dialogs.TimePickerFragment
import com.taskapp.utils.DateFormat
import com.taskapp.view.notification.AlarmReceiver
import java.util.*


class AddTaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var fecha: Date
    private lateinit var actividadEditrar : Actividad
    private var editar = false
    private var database = AppDatabase.getDatabase(this)
    private var actividadDao = database.getActividadDao()
    private var iniciadaDao = database.getIniciadaDao()
    private var tipoActividadDao = database.getTipoActividadDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(applicationContext, R.layout.item_dropdown_list, tipoActividadDao.getAll())
        adapter.setDropDownViewResource(R.layout.item_dropdown)
        binding.spnType.adapter = adapter
        binding.spnType.onItemSelectedListener =  this

        intent.getSerializableExtra("task")?.let { actividad ->
            actividadEditrar = actividad as Actividad
            binding.etTitle.setText(actividadEditrar.Titulo)
            binding.etDescription.setText(actividadEditrar.Descripcion)
            fecha = actividadEditrar.FechaLimite
            binding.etDate.setText(DateFormat.stringFormat(fecha))
            binding.spnType.setSelection(actividadEditrar.IdTipoActividad - 1)
            editar = true
        }

        binding.btnAddTask.setOnClickListener {
            if(validarCampos()){
                if(editar){
                    val actividad = Actividad(
                        Id = actividadEditrar.Id,
                        Titulo = binding.etTitle.text.toString().trim(),
                        Descripcion = binding.etDescription.text.toString().trim(),
                        FechaCreacion = actividadEditrar.FechaCreacion,
                        FechaLimite = fecha,
                        IdTipoActividad = ((binding.spnType.selectedItem) as TipoActividad).Id ?: 0
                    )
                    actualizarActividad(actividad)
                    if(actividadEditrar.FechaLimite != fecha) {
                        setAlarm(actividad.Id ?: 0, actividad.Titulo, actividad.Descripcion, DateFormat.setDate(fecha))
                    }
                } else {
                    val actividad = Actividad(
                        Id = null,
                        Titulo = binding.etTitle.text.toString().trim(),
                        Descripcion = binding.etDescription.text.toString().trim(),
                        FechaCreacion = Date(),
                        FechaLimite = fecha,
                        IdTipoActividad = ((binding.spnType.selectedItem) as TipoActividad).Id ?: 0
                    )
                    insertarActividad(actividad)
                    val idActividad = actividadDao.getLastId()
                    val iniciada = Iniciada(
                        Id = null,
                        FechaIniciacion = Date(),
                        HoraIniciacion = Date(),
                        IdActividad = idActividad
                    )
                    insertarIniciada(iniciada)
                    setAlarm(idActividad, actividad.Titulo, actividad.Descripcion, DateFormat.setDate(fecha))
                }
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else { Toast.makeText(this, "Hay campos vacios", Toast.LENGTH_SHORT).show() }
        }
        
        binding.etDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    //Esta función se encarga de establecer la alarma según la fecha dada, ademas le asigna un id para poder indentificarla
    private fun setAlarm(id: Int, titulo: String, contenido: String, fecha: Calendar) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("id", id)
        intent.putExtra("titulo", titulo)
        intent.putExtra("contenido", contenido)

        pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, fecha.timeInMillis, pendingIntent)
    }

    //Crea una instancia del calendario y lo muestra en la pantalla
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        fecha = Date(year, month, day)
        showTimePickerDialog()
    }

    //Crea una instancia del reloj y lo muestra en la pantalla
    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment{hour, minute ->  onTimeSelected(hour, minute)}
        timePicker.show(supportFragmentManager, "timePicker")
    }

    fun onTimeSelected(hour:Int, minute:Int){
        fecha.hours = hour
        fecha.minutes = minute
        binding.etDate.setText(DateFormat.stringFormat(fecha))
    }

    private fun insertarActividad(actividad: Actividad){
        try {
            actividadDao.insert(actividad)
            Toast.makeText(this, "Se inserto la actividad ${actividad.Titulo}", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception){
            Toast.makeText(this, "No se pudo insertar la actividad", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarActividad(actividad: Actividad){
        try {
            actividadDao.update(actividad)
            Toast.makeText(this, "Se actualizo la actividad ${actividad.Titulo}", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception){
            Toast.makeText(this, "No se pudo actualizar la actividad", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertarIniciada(iniciada: Iniciada){
        try {
            iniciadaDao.insert(iniciada)
        } catch (ex: Exception){
            Toast.makeText(this, "No se pudo insertar iniciada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validarCampos(): Boolean{
        val titleIsNotEmpty = binding.etTitle.text.toString().trim().isNotEmpty()
        val descriptionIsNotEmpty = binding.etDescription.text.toString().trim().isNotEmpty()
        val dateIsNotEmpty = binding.etDate.text.toString().trim().isNotEmpty()

        return (titleIsNotEmpty && descriptionIsNotEmpty && dateIsNotEmpty)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) { }

    override fun onNothingSelected(p0: AdapterView<*>?) { }
}