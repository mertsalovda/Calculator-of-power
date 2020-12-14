package ru.mertsalovda.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.mertsalovda.core_api.mediator.AppWithFacade
import ru.mertsalovda.core_api.mediator.BasicMediator
import ru.mertsalovda.main.di.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var basicMediator: BasicMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_main)

        MainComponent.create((application as AppWithFacade).getFacade()).inject(this)

        basicMediator.openBasicScreen(R.id.container, supportFragmentManager)
    }
}