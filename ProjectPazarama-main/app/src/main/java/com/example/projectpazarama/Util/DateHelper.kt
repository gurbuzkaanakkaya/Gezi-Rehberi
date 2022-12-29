package com.example.projectpazarama.Util

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.*

object DateHelper {

    fun toDbDateFormat(dateValue : String):String{
        val values = dateValue.split("-")
        val day = values[0].toInt()
        val month = values[1].toInt()
        val year = values[2].toInt()
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date(year-1900,month,day))
    }

    fun fromDbDateFormat(dbDateValue :String):String{
        val values = dbDateValue.split("-")

        val pattern = "dd.MM.yyyy"
        val year = values[0].toInt()
        val month = values[1].toInt()
        val day = values[2].toInt()
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date(year-1900,month,day))
    }
}