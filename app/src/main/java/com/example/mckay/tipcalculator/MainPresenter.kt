package com.example.mckay.tipcalculator

import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo


class MainPresenter(mainInterface: MainInterface) {

    interface MainInterface {
        val compositeDisposable2: CompositeDisposable
        fun hamburgerMenuClicks(): Observable<HamburgerMenuItem>
        fun handleBillHistory()
        fun handleSettings()
        fun handleLogin()
    }

    private val view = mainInterface

    fun handleHamburgerMenuClicks() {
        view.hamburgerMenuClicks().subscribe { item ->
            when (item) {
                HamburgerMenuItem.BILL_HISTORY -> view.handleBillHistory()
                HamburgerMenuItem.SETTINGS -> view.handleSettings()
                HamburgerMenuItem.LOGIN -> view.handleLogin()
            }
        }.addTo(view.compositeDisposable2)
    }
}