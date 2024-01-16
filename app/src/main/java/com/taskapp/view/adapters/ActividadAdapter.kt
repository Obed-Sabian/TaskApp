package com.taskapp.view.adapters

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.taskapp.R
import com.taskapp.data.entity.Actividad
import com.taskapp.databinding.ItemTaskBinding

class ActividadAdapter(
    private val fragmentManager: FragmentManager,
    private val onDelete: (Actividad) ->Unit,
    private val onUpdate: (Actividad) ->Unit,
    private var completed: Boolean,
    private val ActividadesPendientes: () ->Unit,
    private val cancelAlarm: (id: Int) ->Unit
) : RecyclerView.Adapter<ActividadViewHolder>() {

    private var tasksList: List<Actividad> = emptyList()

    fun  setTasks(actividades: List<Actividad>, completed: Boolean){
        this.completed = completed
        tasksList = actividades
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_task, parent, false)
        val binding = ItemTaskBinding.bind(view)
        return ActividadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val task = tasksList[position]
        holder.render(task, onDelete, onUpdate, ActividadesPendientes, fragmentManager, completed, cancelAlarm)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }
}