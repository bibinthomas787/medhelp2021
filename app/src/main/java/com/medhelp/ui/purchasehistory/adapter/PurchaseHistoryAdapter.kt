package com.medhelp.ui.purchasehistory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.medhelp.R
import com.medhelp.ui.localDb.roomDb.bean.UserCardDetails
import java.text.SimpleDateFormat

class PurchaseHistoryAdapter(val creditCardInfo: List<UserCardDetails?>) : RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder>() {
    var dateFormat: SimpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
var mainView=itemView.findViewById<CardView>(R.id.cvMain)
var creditCard=itemView.findViewById<TextView>(R.id.tvCreditCard)
var date=itemView.findViewById<TextView>(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_purchae_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
var  cardDetails=creditCardInfo[position]
        holder.creditCard.text="from ${cardDetails?.cardNumber}"
        holder.date.text=dateFormat.format(cardDetails?.transactionDate)
    }

    override fun getItemCount(): Int {
        return creditCardInfo.size
    }
}