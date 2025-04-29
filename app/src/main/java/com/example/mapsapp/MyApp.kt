package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySuperBaseCliente

class MyApp: Application() {

    companion object{
        lateinit var dataBase: MySuperBaseCliente
    }

    override fun onCreate() {
        super.onCreate()
        dataBase = MySuperBaseCliente(
            supabaseUrl = "https://ekqeuybhtpxfbmldehua.supabase.co" ,
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVrcWV1eWJodHB4ZmJtbGRlaHVhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU5MjAwODAsImV4cCI6MjA2MTQ5NjA4MH0.g59yrgfTOqL6raWeqzOVwOL1DDa1tqfLKXTHnDi7rPk"
        )
    }
}