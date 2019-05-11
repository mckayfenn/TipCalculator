package com.example.mckay.tipcalculator

import io.reactivex.rxkotlin.addTo

class MainPresenter(private val view: MainContract.MainView) : MainContract.MainPresenter {
    override fun onDestroy() {
        view.compositeDisposable.clear()
    }

    override fun onCreate() {
       observeHamburgerMenuClicks()
    }

    override fun observeHamburgerMenuClicks() {
        view.hamburgerMenuClicks().subscribe { item ->
            when (item) {
                HamburgerMenuItem.BILL_HISTORY -> view.handleBillHistory()
                HamburgerMenuItem.SETTINGS -> view.handleSettings()
                HamburgerMenuItem.LOGIN -> view.handleLogin()
            }
        }.addTo(view.compositeDisposable)
    }
}