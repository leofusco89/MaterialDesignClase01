package com.example.username.myapplication

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log

class SyncService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        SyncData(applicationContext).execute()

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    class SyncData(var context: Context) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg p0: Unit?) {
            val libros = LibroManager.getInstance(context).libros
            sendDataToServer(libros)
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            Intent(context, SyncService::class.java).apply { context.stopService(this) }
        }

    }

    companion object {
        fun sendDataToServer(libros: List<Libro>) {
            Log.i("SyncService", "Datos enviados al servidor")
        }
    }

    override fun onDestroy() {
        Log.i("SyncService", "Servicio detenido")
        super.onDestroy()
    }

}