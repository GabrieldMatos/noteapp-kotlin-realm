package com.example.mynoteapp

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddNotesActivity : AppCompatActivity() {
    private lateinit var title:EditText
    private lateinit var desctription:EditText
    private lateinit var saveNotesBtn: Button
    private lateinit var frequency:Spinner
    private lateinit var alert:TextView
    private lateinit var timeBtn:Button
    private lateinit var realm: Realm

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        val actionbar = supportActionBar
        
        actionbar!!.title = "Nova nota"

        realm = Realm.getDefaultInstance()

        val note: Notes? = getNote()

        actionbar.setDisplayHomeAsUpEnabled(true)
        title = findViewById(R.id.title_EditText)
        desctription = findViewById(R.id.description_EditText)
        frequency = findViewById(R.id.frequency_Spinner)
        alert = findViewById(R.id.time)

        alert.text = note?.alert ?: "Horário"
        title.setText(note?.title)
        desctription.setText(note?.description)
        val freq = note?.frequency
        if(freq != null) frequency.setSelection(freq ?: 1, false)

        timeBtn = findViewById(R.id.TimePicker_Button)
        timeBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                alert.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        saveNotesBtn = findViewById(R.id.save_NotesBtn)
        saveNotesBtn.setOnClickListener {
            dBAddNotes()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dBAddNotes() = try {
        realm.beginTransaction()
        val idNumber:Number? = realm.where(Notes::class.java).max("id")
        val id = if(idNumber == null)  1 else idNumber.toInt() + 1

        val notes = Notes()
        val note = getNote()
        notes.id = (note?.id ?: id)
        notes.title = title.text.toString()
        notes.description = desctription.text.toString()
        notes.alert = alert.text.toString()
        notes.frequency = frequency.selectedItemPosition

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        notes.createAt = formatter.format(LocalDateTime.now()).toString()
        realm.copyToRealmOrUpdate(notes)
        realm.commitTransaction()

        Toast.makeText(this,  if (note == null) "Nota adicionada com sucesso" else "Nota editada com sucesso", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }catch (e:Exception){
        Toast.makeText(this, "Acontenceu um erro! $e", Toast.LENGTH_SHORT).show()
    }

    private fun getNote(): Notes? {
        return realm.where(Notes::class.java).equalTo(
            "id",
            intent.getIntExtra("id", -1)
        ).findFirst()
    }
}
