package com.taskapp.view.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class RebootReceiver: BroadcastReceiver() {
    //Esta función se ejecuta cuando se restablece el sistema al reiniciar el teléfono, y manda a llamar
    //a al servicio que va a restaurar las alarmas
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, RebootService::class.java)
        serviceIntent.putExtra("caller", "RebootReceiver")
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context?.startForegroundService(serviceIntent)
        else
            context?.startService(serviceIntent)
    }
}