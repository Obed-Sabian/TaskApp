package com.taskapp.view.adapters

import android.graphics.Color
import android.view.View
import android.widget.Toast
import android.icu.util.Calendar
import android.text.TextUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.taskapp.data.database.AppDatabase
import com.taskapp.data.entity.Actividad
import com.taskapp.data.entity.Finalizada
import com.taskapp.databinding.ItemTaskBinding
import com.taskapp.dialogs.ConfirmDialog
import com.taskapp.dialogs.InformationDialog
import com.taskapp.utils.DateFormat
import java.util.*

class ActividadViewHolder(private val binding: ItemTaskBinding)
    : RecyclerView.ViewHolder(binding.root){

    private var database = AppDatabase.getDatabase(binding.root.context)
    private var finalizadaDao = database.getFinalizadaDao()

    fun render(
        actividad: Actividad,
        onDelete: (Actividad) ->Unit,
        onUpdate: (Actividad) ->Unit,
        ActividadesPendientes: () ->Unit,
        fragmentManager: FragmentManager,
        completada: Boolean,
        cancelAlarm: (id: Int) ->Unit
    ){
        binding.tvTitle.text = actividad.Titulo
        binding.tvDescription.text = actividad.Descripcion

        val dateLimit = Calendar.getInstance()
        dateLimit.clear()
        dateLimit.set(actividad.FechaLimite.year, actividad.FechaLimite.month, actividad.FechaLimite.date, actividad.FechaLimite.hours, actividad.FechaLimite.minutes)
        val tiempo = DateFormat.difDates(dateLimit.time, Calendar.getInstance().time)
        binding.tvDate.text = "Tiempo restante: $tiempo"
        var color = Color.BLACK
        if(tiempo.startsWith("-")){
            color =  Color.RED
            binding.tvDate.text = "Tarea atrasada"
        }
        binding.tvDate.setTextColor(color)

        binding.chkTask.isChecked = completada
        binding.btnDelete.setOnClickListener {
            val dialog = ConfirmDialog(actividad, onDelete)
            dialog.show(fragmentManager, "ConfirmDialog")
        }

        binding.btnEdit.setOnClickListener {
            onUpdate(actividad)
        }

        binding.panel.setOnClickListener {
            val dialog = InformationDialog(actividad, binding.root.context)
            dialog.show(fragmentManager, "InformationDialog")
        }

        binding.chkTask.setOnClickListener{
            if(binding.chkTask.isChecked){
                val finalizada = Finalizada(
                    Id = null,
                    FechaFinalizacion = Date(),
                    HoraFinalizacion = Date(),
                    IdActividad = actividad.Id ?: 0
                )
                finalizadaDao.insert(finalizada)
                ActividadesPendientes()
                cancelAlarm(actividad.Id ?: 0)
                Toast.makeText(binding.root.context, "Se completo la actividad ${actividad.Titulo}", Toast.LENGTH_SHORT).show()
            }
        }

        if(completada){
            binding.btnDelete.visibility = View.INVISIBLE
            binding.btnEdit.visibility = View.INVISIBLE
            val date = finalizadaDao.getById(actividad.Id ?: 0).FechaFinalizacion
            binding.tvDate.text = "Completada: ${DateFormat.formatter.format(date)}"
            binding.tvDate.setTextColor(Color.BLACK)
            binding.tvDate.isSelected = true

            binding.chkTask.isEnabled = false
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.VISIBLE
            binding.chkTask.isEnabled = true
        }
    }
}