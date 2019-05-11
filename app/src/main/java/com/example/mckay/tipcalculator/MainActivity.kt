package com.example.mckay.tipcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , MainContract.MainView {
    override val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val hamburgerMenuAdapter = HamburgerMenuAdapter()
    lateinit var presenter: MainContract.MainPresenter

    override fun setPresenter(presenter: MainPresenter) {
        this.presenter = presenter
    }

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

        setPresenter(MainPresenter(this))
        presenter.onCreate()

        calculateTipAndTotalAmount()
        setupHamburgerMenu()
    }

    fun setupHamburgerMenu() {
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = hamburgerMenuAdapter
    }

    override fun hamburgerMenuClicks(): Observable<HamburgerMenuItem> = hamburgerMenuAdapter.itemClicks()

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
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
            Observable.just((tip/100) * billAmount.text.toString().toFloat())
        }.subscribe { tipAmount ->
            tipAmountLabel.text = tipAmount.toString()
            totalAmountLabel.text = (tipAmount + billAmount.text.toString().toFloat()).toString()
        }.addTo(compositeDisposable)
    }
}
