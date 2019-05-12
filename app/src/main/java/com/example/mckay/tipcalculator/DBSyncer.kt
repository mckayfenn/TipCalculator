package com.example.mckay.tipcalculator

import android.content.Context
import android.widget.Toast
import java.util.ArrayList

class DBSyncer(private val context: Context) {
    val tinyDB = TinyDB(context)

    fun getBillHistoryList(): ArrayList<BillHistoryViewItem> {
        val list = tinyDB.getListObject(BILL_HISTORY_LIST, BillHistoryViewItem::class.java)
        if (!list.isNullOrEmpty()) {
            return list as ArrayList<BillHistoryViewItem>
        } else {
            Toast.makeText(context, "List does not exist", Toast.LENGTH_SHORT).show()
            return arrayListOf()
        }
    }

    fun updateBillHistoryList(item: BillHistoryViewItem) {
        val list = tinyDB.getListObject(BILL_HISTORY_LIST, BillHistoryViewItem::class.java)
        if (list.isNullOrEmpty()) {
            val newList = arrayListOf<BillHistoryViewItem>()
            newList.add(item)
            tinyDB.putListObject(BILL_HISTORY_LIST, newList as ArrayList<Any>)
        } else {
            list.add(item)
            tinyDB.putListObject(BILL_HISTORY_LIST, list as ArrayList<Any>)

        }
    }

    fun clearBillHistoryList() {
        val list = tinyDB.getListObject(BILL_HISTORY_LIST, BillHistoryViewItem::class.java)
        if (!list.isNullOrEmpty()) {
            val newList = arrayListOf<BillHistoryViewItem>()
            tinyDB.putListObject(BILL_HISTORY_LIST, newList as ArrayList<Any>)
        } else {
            Toast.makeText(context, "List does not exist", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeBillHistoryEntry(item: BillHistoryViewItem) {
        val list = tinyDB.getListObject(BILL_HISTORY_LIST, BillHistoryViewItem::class.java)
        if (!list.isNullOrEmpty()) {
            list.forEach { entry ->
                if (entry.equals(item)) {
                    list.remove(entry)
                    tinyDB.putListObject(BILL_HISTORY_LIST, list as ArrayList<Any>)
                    return
                }
            }
            Toast.makeText(context, "Entry does not exist", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "List does not exist", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val BILL_HISTORY_LIST = "bill_history_list"
    }
}