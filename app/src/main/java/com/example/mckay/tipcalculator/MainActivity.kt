package com.example.mckay.tipcalculator

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() , MainContract.MainView {
    val router: MainRouter = MainRouter()
    override val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val hamburgerMenuAdapter = HamburgerMenuAdapter()
    lateinit var presenter: MainContract.MainPresenter
    lateinit var dbSyncer: DBSyncer

//    var bills = arrayListOf(BillHistoryViewItem("Olive Garden", "100", "104", "5/11/2019"),
//        BillHistoryViewItem("Panda Express", "24.45", "25.54", "5/12/2019"),
//        BillHistoryViewItem("McDonalds", "10.00", "12.00", "5/13/2019"))

    override fun setPresenter(presenter: MainPresenter) {
        this.presenter = presenter
    }

    override fun handleBillHistory() {
        Toast.makeText(this, "Bill History Clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, BillHistoryActivity::class.java)//.putParcelableArrayListExtra("Bills", bills)
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
        dbSyncer = DBSyncer(this)

        setPresenter(MainPresenter(this))
        presenter.onCreate()

        handleSaveBillButtonState()

        calculateTipAndTotalAmount()
        handleLocationValueChange()
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
        //TODO fix crash where calculate is clicked if no tip or bill amount
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
            handleSaveBillButtonState()
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
            handleSaveBillButtonState()
            if (saveBillButton.isEnabled) {
                Toast.makeText(this, "Bill Saved. Location: " + locationAsString, Toast.LENGTH_SHORT).show()
                dbSyncer.updateBillHistoryList(BillHistoryViewItem(locationAsString, billAmount.text.toString(), totalAmountLabel.text.toString(), getDate()))
            }
        }.addTo(compositeDisposable)
    }

    fun locationLabelHasValue(): Observable<Unit> {
        return Observable.create { emitter ->
            run {
                locationInput.addTextChangedListener ( object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {}
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            emitter.onNext(Unit)
                        }
                    })
            }
            emitter.setCancellable {
                locationInput.addTextChangedListener(null)
            }
        }
    }

    fun handleLocationValueChange() {
        locationLabelHasValue().subscribeBy(onNext = {handleSaveBillButtonState()})
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("MM dd yyyy HH:mm:ss")
        return formatter.format(date)
    }

    private fun handleSaveBillButtonState() {
        saveBillButton.isEnabled = false
        if (billAmount.text.isNotBlank() && totalAmountLabel.text.isNotBlank() && locationInput.text.isNotBlank()) {
            saveBillButton.isEnabled = true
        }
    }
}


