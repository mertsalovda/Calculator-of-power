package ru.mertsalovda.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.mertsalovda.core_api.mediator.MainMediator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var mainMediator: MainMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}