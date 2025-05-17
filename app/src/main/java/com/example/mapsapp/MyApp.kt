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
            BuildConfig.SUPABASE_URL,
            BuildConfig.SUPABASE_KEY
        )
    }
}