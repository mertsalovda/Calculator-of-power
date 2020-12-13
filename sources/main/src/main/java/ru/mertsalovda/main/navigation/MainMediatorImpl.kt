package ru.mertsalovda.main.navigation

import android.content.Context
import android.widget.Toast
import ru.mertsalovda.core_api.mediator.MainMediator
import javax.inject.Inject

class MainMediatorImpl @Inject constructor(): MainMediator {

    override fun openMainScreen(context: Context) {
        Toast.makeText(context, "Main Screen", Toast.LENGTH_SHORT).show()
    }
}