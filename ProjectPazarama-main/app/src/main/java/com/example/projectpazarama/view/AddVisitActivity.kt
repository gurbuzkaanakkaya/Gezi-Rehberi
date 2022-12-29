package com.example.projectpazarama.view

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectpazarama.R
import com.example.projectpazarama.Util.DateHelper
import com.example.projectpazarama.data.TravelOperations
import com.example.projectpazarama.databinding.ActivityAddVisitBinding
import com.example.projectpazarama.model.Place
import com.example.projectpazarama.model.VisitHistory
import java.util.*

class AddVisitActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddVisitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val travelOperations = TravelOperations(this)

        var placeId = intent.getIntExtra("place",-1)
        val place = travelOperations.getPlaceById(placeId)


        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern)


        var dateValue = simpleDateFormat.format(Date(year - 1900, month-1, day))
        binding.dateEditBtn.text = DateHelper.fromDbDateFormat(dateValue)

        binding.dateEditBtn.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                    val selectedDate = Date(myear - 1900, mmonth-1, mdayOfMonth)
                    dateValue = simpleDateFormat.format(selectedDate)
                    binding.dateEditBtn.text = DateHelper.fromDbDateFormat(dateValue)
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        }

        binding.saveVisitButton.setOnClickListener {
            println("PLACE ID : "+place.Id)
            val visit = VisitHistory()
            visit.Date = dateValue
            visit.Description = binding.visitDescText.text.toString()
            visit.PlaceId = place.Id
            travelOperations.addVisitHistory(visit)
            setResult(RESULT_OK)
            finish()
        }

        binding.toolbar.toolbarTitle.text = "Ziyaret Eklenen Yerin Adı"
        binding.toolbar.toolbarBtn.setOnClickListener {
            onBackPressed()
        }
    }
}