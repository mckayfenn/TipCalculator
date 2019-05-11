package com.example.mckay.tipcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() , MainPresenter.MainInterface {
    override val compositeDisposable2: CompositeDisposable = CompositeDisposable()
    private val compositeDisposable = CompositeDisposable()
    private val hamburgerMenuAdapter = HamburgerMenuAdapter()
    private val presenter = MainPresenter(this)

    override fun handleBillHistory() {
        Toast.makeText(this, "Bill History Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun handleSettings() {
        Toast.makeText(this, "Setting Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun handleLogin() {
        Toast.makeText(this, "Login Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculateTipAndTotalAmount()
        settupHamburgerMenu()
    }

    fun settupHamburgerMenu() {
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = hamburgerMenuAdapter
    }

    override fun hamburgerMenuClicks(): Observable<HamburgerMenuItem> = hamburgerMenuAdapter.itemClicks()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        compositeDisposable2.clear()
    }

    fun calcButtonClicked(): Observable<Float> {
        return Observable.create { emitter ->
            run {
                calculateButton.setOnClickListener { emitter.onNext(tipAmount.text.toString().toFloat()) }
            }
            emitter.setCancellable {
                calculateButton.setOnClickListener(null)
            }
        }
    }

    fun calculateTipAndTotalAmount() {
        calcButtonClicked().flatMap { tip ->
            Observable.just(tip * billAmount.text.toString().toFloat())
        }.subscribe { tipAmount ->
            tipAmountLabel.text = "$ " + tipAmount.toString()
            totalAmountLabel.text = "$ " + (tipAmount + billAmount.text.toString().toFloat()).toString()
        }.addTo(compositeDisposable)
    }
}
