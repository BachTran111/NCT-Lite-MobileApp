package com.example.nct_lite
import  android.app.Application
import com.example.nct_lite.di.AppContainer

class MyApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}