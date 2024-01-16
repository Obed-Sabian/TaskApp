package com.taskapp.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.*

class DateFormat {
    companion object{
        val formatter = SimpleDateFormat("EEE, d MMM yyyy hh:mm a")

        fun stringFormat(date: Date): String{
            return formatter.format(format(date))
        }

        fun format(date: Date): Date{
            val cL = Calendar.getInstance()
            cL.clear()
            cL.set(date.year, date.month, date.date, date.hours, date.minutes)
            return cL.time
        }

        fun difDates(date1: Date, date2: Date): String{
            //Resta los milisegundos de ambas fechas y lo divide entre la representación de milisegundos de un día, hora y minuto
            //1000 milisegundos = 1 segundo
            //60 segundos = 1 minuto
            //60 minutos = 1 hora
            //24 horas = 1 día
            val difLong = date1.time - date2.time
            val days = Math.floor((difLong/ (1000 * 60 * 60 * 24)).toDouble()).toInt()
            val hours = Math.floor((difLong/ (1000 * 60 * 60)).toDouble()).toInt()
            val minutes = Math.floor((difLong/ (1000 * 60)).toDouble()).toInt()

            var resultado = if (days == 1) "día" else "días"
            if(days == 0){

                if(hours == 0){
                    resultado = if (minutes == 1) "minuto" else "minutos"
                    return "$minutes $resultado"
                }

                resultado = if (hours == 1) "hora" else "horas"
                return "$hours $resultado"
            }
            return "$days $resultado"
        }

        fun setDate(fechaLimite: Date): Calendar{
            val fechaHoy = Date()

            var fecha = Calendar.getInstance()
            fecha.clear()

            var fechaApoyo = Calendar.getInstance()
            fechaApoyo.clear()
            fechaApoyo.set(fechaLimite.year, fechaLimite.month, fechaLimite.date, fechaLimite.hours, fechaLimite.minutes)
            //resta -1440 minutos (equivalente a 24hrs) a la fecha, esto para obtener la fecha de un día antes
            fechaApoyo.add(Calendar.MINUTE, -1440)

            if (fechaApoyo.time < fechaHoy){
                fecha.set(fechaLimite.year, fechaLimite.month, fechaLimite.date, fechaLimite.hours, fechaLimite.minutes)
            } else{
                fecha = fechaApoyo
            }

            return fecha
        }
    }
}