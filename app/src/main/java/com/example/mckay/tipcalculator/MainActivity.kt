package com.example.mckay.tipcalculator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bill_history_menu_item.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() , MainContract.MainView {
    val router: MainRouter = MainRouter()
    override val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val hamburgerMenuAdapter = HamburgerMenuAdapter()
    lateinit var presenter: MainContract.MainPresenter

    var bills = arrayListOf<BillHistoryViewItem>(BillHistoryViewItem("Olive Garden", "100", "104", "5/11/2019"),
        BillHistoryViewItem("Panda Express", "24.45", "25.54", "5/12/2019"),
        BillHistoryViewItem("McDonalds", "10.00", "12.00", "5/13/2019"))

    override fun setPresenter(presenter: MainPresenter) {
        this.presenter = presenter
    }

    override fun handleBillHistory() {
        Toast.makeText(this, "Bill History Clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, BillHistoryActivity::class.java).putParcelableArrayListExtra("Bills", bills)
        router.showBillHistory(this, intent)
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
        saveBillToList()
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

    fun saveBillClicked(): Observable<String> {
        return Observable.create { emitter ->
            run {
                saveBillButton.setOnClickListener { emitter.onNext(locationInput.text.toString())}
            }
            emitter.setCancellable {
                saveBillButton.setOnClickListener(null)
            }
        }
    }

    fun saveBillToList() {
        saveBillClicked().subscribe { locationAsString ->
            Toast.makeText(this, "Bill Saved. Location: " + locationAsString, Toast.LENGTH_SHORT).show()
            // TODO check if the fields are populated
            bills.add(BillHistoryViewItem(locationAsString, billAmount.text.toString(), totalAmountLabel.text.toString(), "5/14/2019"))
        }.addTo(compositeDisposable)
    }
}
