package com.taskapp.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.taskapp.data.entity.Actividad
import com.taskapp.databinding.DialogConfirmBinding

class ConfirmDialog(val actividad: Actividad ,private val onDelete: (Actividad) ->Unit): DialogFragment() {

    private lateinit var binding: DialogConfirmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.tvNo.setOnClickListener { dismiss() }
        binding.tvSi.setOnClickListener {
            onDelete(actividad)
            dismiss()
        }
    }
}