package com.example.futurus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class DashBoard : AppCompatActivity() {

    companion object{
        const val NAME_EXTRA = "name_extra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        val name = intent.getStringExtra(NAME_EXTRA)
        //val DashBoardID = findViewById<TextView>(R.id.dashboard)
        //DashBoardID.text = "Hey $name"
        findViewById<TextView>(R.id.dashboard).text = "Hey $name"
    }
}