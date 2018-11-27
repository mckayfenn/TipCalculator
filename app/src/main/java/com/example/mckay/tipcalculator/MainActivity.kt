package com.example.mckay.tipcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculateTipAndTotalAmount()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
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
            tipAmountLabel.text = tipAmount.toString()
            totalAmountLabel.text = (tipAmount + billAmount.text.toString().toFloat()).toString()
        }.addTo(compositeDisposable)
    }
}
