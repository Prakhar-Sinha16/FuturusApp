package com.example.futurus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader

class DashBoard : AppCompatActivity() {

    companion object{
        const val NAME_EXTRA = "name_extra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        val name = intent.getStringExtra(NAME_EXTRA)
        findViewById<TextView>(R.id.dashboard).text = "Hey $name"
        findViewById<Button>(R.id.paratool).setOnClickListener {
            val intent = Intent(this,FirstFragment::class.java)
            startActivity(intent)
        }
    }
}