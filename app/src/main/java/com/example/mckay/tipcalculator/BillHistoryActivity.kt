package com.example.mckay.tipcalculator

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.bill_history_view.*

class BillHistoryActivity : AppCompatActivity(), BillHistoryContract.BillHistoryView {
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<BillHistoryViewHolder>
    lateinit var viewLayoutManager: RecyclerView.LayoutManager
    override fun setPresenter(presenter: BillHistoryContract.BillHistoryPresenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_history_view)

        viewLayoutManager = LinearLayoutManager(this)
        viewAdapter = BillHistoryListAdapter(this)
        recyclerView = billHistoryList.apply {
            layoutManager = viewLayoutManager
            adapter = viewAdapter
        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }


}

class BillHistoryListAdapter(private val context: Context) : RecyclerView.Adapter<BillHistoryViewHolder>() {

    val dbSyncer = DBSyncer(context)
    val bills = dbSyncer.getBillHistoryList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BillHistoryViewHolder {
        return BillHistoryViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.bill_history_menu_item, p0, false))
    }

    override fun getItemCount(): Int {
        return bills.size
    }

    override fun onBindViewHolder(view: BillHistoryViewHolder, p1: Int) {
        view.billLocation.text = bills[p1].billLocation
        view.billAmount.text = bills[p1].billAmount
        view.totalAmount.text = bills[p1].totalAmount
        view.date.text = bills[p1].date
    }

}

@Parcelize
data class BillHistoryViewItem(val billLocation: String, val billAmount: String, val totalAmount: String, val date: String) : Parcelable

class BillHistoryViewHolder(val itemViewHere: View) : RecyclerView.ViewHolder(itemViewHere) {
    val billLocation: TextView = itemViewHere.findViewById(R.id.locationLabel)
    val billAmount: TextView = itemViewHere.findViewById(R.id.billAmountLabel)
    val totalAmount: TextView = itemViewHere.findViewById(R.id.totalAmountLabelSaved)
    val date: TextView = itemViewHere.findViewById(R.id.dateLabel)
}