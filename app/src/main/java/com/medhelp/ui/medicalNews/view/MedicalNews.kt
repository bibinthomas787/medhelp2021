package com.medhelp.ui.medicalNews.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.medhelp.R
import com.medhelp.base.Cons
import com.medhelp.retrofit.RetrofitClient
import com.medhelp.ui.medicalNews.adapter.NewsAdapter
import com.medhelp.ui.medicalNews.model.TopHeadlinesBean
import kotlinx.android.synthetic.main.fragment_medical_news.*
import kotlinx.android.synthetic.main.fragment_purchase.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicalNews : Fragment() {
    private  val TAG = "MedicalNews"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_medical_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            var query=HashMap<String?,String?>()
            query["apiKey"]=Cons.MEDICAL_API_KEY
            query["country"]="ca"
            RetrofitClient().getAPIService()?.getTopHeadlines(query)?.enqueue(object :
                Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    progressBar.visibility=View.GONE
                    if (response.code() == 200) {
                        val topHeadlines=Gson().fromJson(response.body(),
                            TopHeadlinesBean::class.java)
                        if (topHeadlines.articles.isNullOrEmpty()){
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()

                        }else{
                            rvMedicalNews.adapter=NewsAdapter(topHeadlines.articles)
                        }

                    } else {
                        Toast.makeText(
                            context,
                            "Something Went Wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    progressBar.visibility=View.GONE
                    Log.e(TAG, "onFailure: "+t.localizedMessage )
                }
            })

    }

}