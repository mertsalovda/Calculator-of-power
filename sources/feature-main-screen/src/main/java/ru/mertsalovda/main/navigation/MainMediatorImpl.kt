package ru.mertsalovda.main.navigation

import android.content.Context
import android.content.Intent
import ru.mertsalovda.core_api.mediator.MainMediator
import ru.mertsalovda.main.ui.MainActivity
import javax.inject.Inject

class MainMediatorImpl @Inject constructor() : MainMediator {

    override fun openMainScreen(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}