package com.taskapp.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.taskapp.data.database.AppDatabase
import com.taskapp.data.entity.Actividad
import com.taskapp.databinding.DialogInformationBinding
import com.taskapp.utils.DateFormat
import java.text.SimpleDateFormat

class InformationDialog(private val actividad: Actividad, private val contexto: Context): DialogFragment() {

    private lateinit var binding: DialogInformationBinding
    private var database = AppDatabase.getDatabase(contexto)
    private var iniciadaDao = database.getIniciadaDao()
    private var finalizadaDao = database.getFinalizadaDao()
    private var tipoActividadDao = database.getTipoActividadDao()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val iniciada = actividad.Id?.let { Id -> iniciadaDao.getById(Id) }
        val finalizada = actividad.Id?.let { Id -> finalizadaDao.getById(Id) }
        binding.tvTitle.text = actividad.Titulo
        binding.tvDescription.text = actividad.Descripcion
        binding.tvType.text = "Tarea ${tipoActividadDao.getById(actividad.IdTipoActividad)}"
        binding.tvDateLimit.text = "Fecha límite: ${DateFormat.stringFormat(actividad.FechaLimite)}"
        binding.tvDateCreation.text = "Creada: ${DateFormat.formatter.format(iniciada?.FechaIniciacion)}"
        finalizada?.let { finalizada ->
            binding.tvDateComplete.text = "Completada: ${DateFormat.formatter.format(finalizada.FechaFinalizacion)}"
            binding.tvRealizationTime.text = "Tiempo de realización: ${DateFormat.difDates(finalizada.FechaFinalizacion, actividad.FechaCreacion)}"
        }
    }
}