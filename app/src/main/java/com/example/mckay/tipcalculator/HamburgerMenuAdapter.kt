package com.example.mckay.tipcalculator

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

enum class HamburgerMenuItem(val title: Int, val icon: Int) {
    BILL_HISTORY(R.string.bill_history, R.drawable.ic_history_black_24dp),
    SETTINGS(R.string.settings, R.drawable.ic_settings_black_24dp),
    LOGIN(R.string.login, R.drawable.ic_account_circle_black_24dp)
}

class HamburgerMenuItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.findViewById(R.id.icon)
    val title: TextView = itemView.findViewById(R.id.title)
    val cellView: View = itemView
}

class HamburgerMenuAdapter: RecyclerView.Adapter<HamburgerMenuItemViewHolder>() {
    private var listItems = mutableListOf<HamburgerMenuItem>()
    private val menuItemClicked = PublishSubject.create<HamburgerMenuItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HamburgerMenuItemViewHolder {
        return HamburgerMenuItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hamburger_menu_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: HamburgerMenuItemViewHolder, position: Int) {
        viewHolder.title.text = viewHolder.itemView.context.getString(listItems[position].title)
        viewHolder.icon.setImageResource(listItems[position].icon)
        viewHolder.cellView.setOnClickListener { menuItemClicked.onNext(listItems[position]) }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun itemClicks(): Observable<HamburgerMenuItem> = menuItemClicked
}