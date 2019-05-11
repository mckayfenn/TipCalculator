package com.example.mckay.tipcalculator

import android.app.Activity
import android.content.Context
import android.content.Intent

class MainRouter {
    fun showBillHistory(context: Context, intent: Intent) {
        context.startActivity(intent)
    }
}