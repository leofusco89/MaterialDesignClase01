package com.example.username.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import java.sql.SQLException

class AgregarLibroFragment : Fragment() {

    private val CHANNEL_ID: String = "1"
    private lateinit var etNombreLibro: EditText
    private lateinit var etAutor: EditText
    private lateinit var btnGuardar: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_libro, container, false)
        setupUI(view)
        initializeBtnGuardar()

        return view
    }

    private fun setupUI(view: View) {
        etNombreLibro = view.findViewById(R.id.etNombreLibro)
        etAutor = view.findViewById(R.id.etAutor)
        btnGuardar = view.findViewById(R.id.btnGuardar)
    }

    private fun initializeBtnGuardar() {
        btnGuardar.setOnClickListener {
            if (datosValidos()) {
                val libro = Libro()
                libro.nombre = etNombreLibro.text.toString()
                libro.autor = etAutor.text.toString()
                AgregarLibroAsyncTask(this).execute(libro)
            } else {
                Toast.makeText(context, "Completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun datosValidos(): Boolean {
        var datosValidos = true
        if (etNombreLibro.text.toString().isEmpty() || etAutor.text.toString().isEmpty()) {
            datosValidos = false
        }
        return datosValidos
    }

    fun closeActivity() {
        activity?.finish()
    }

    class AgregarLibroAsyncTask(private var agregarLibroFragment: AgregarLibroFragment)
        : AsyncTask<Libro, Unit, Boolean>() {

        override fun doInBackground(vararg params: Libro?): Boolean {
            var libroAgregado = false
            try {
                LibroManager.getInstance(agregarLibroFragment.activity?.applicationContext)
                        .agregarLibro(params[0])
                libroAgregado = true
            } catch (e: SQLException) {
                e.printStackTrace()
            }

            return libroAgregado
        }

        override fun onPostExecute(libroAgregado: Boolean) {
            super.onPostExecute(libroAgregado)
            if (libroAgregado) {
                agregarLibroFragment.mostrarNotification()
                agregarLibroFragment.closeActivity()
            } else {
                Toast.makeText(agregarLibroFragment.activity?.applicationContext,
                        "No se pudo agregar el libro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarNotification() {
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Libros")
                .setContentText("Se agregó un libro a la colección")
                .build()

        val notificationManager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        crearCanal(notificationManager)
        notificationManager.notify(1, notification)
    }

    private fun crearCanal(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                    "Nuevo libro",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

}