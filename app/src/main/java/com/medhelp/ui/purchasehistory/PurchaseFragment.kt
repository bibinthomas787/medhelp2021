package com.medhelp.ui.purchasehistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.medhelp.R
import com.medhelp.ui.localDb.roomDb.DatabaseClient
import com.medhelp.ui.localDb.roomDb.bean.UserCardDetails
import com.medhelp.ui.purchasehistory.adapter.PurchaseHistoryAdapter
import kotlinx.android.synthetic.main.fragment_purchase.*

class PurchaseFragment : Fragment() {
    var creditCardInfo:List<UserCardDetails?>?=null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_purchase, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        creditCardInfo= DatabaseClient.getInstance(context).appDatabase.userCardDetailsDap()?.getAll()
        if (creditCardInfo.isNullOrEmpty()){
            tvNoPayment.visibility=View.VISIBLE
        }else{

            rvPurchaseHistory.adapter=PurchaseHistoryAdapter(creditCardInfo!!)
        }
    }
}