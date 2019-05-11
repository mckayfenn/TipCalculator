package com.example.mckay.tipcalculator

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

interface MainContract {
    interface MainPresenter: BasePresenter {
        fun observeHamburgerMenuClicks()
    }
    interface MainView: BaseView<com.example.mckay.tipcalculator.MainPresenter> {
        val compositeDisposable: CompositeDisposable
        fun hamburgerMenuClicks(): Observable<HamburgerMenuItem>
        fun handleBillHistory()
        fun handleSettings()
        fun handleLogin()
    }
}

interface BasePresenter {
    fun onCreate()
    fun onDestroy()
}

interface BaseView<T> {
    fun setPresenter(presenter: T)
}