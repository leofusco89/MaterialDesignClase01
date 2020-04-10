package com.example.username.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AgregarLibroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_libro)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, AgregarLibroFragment(), AgregarLibroFragment::javaClass.name)
                .commit()
    }

}