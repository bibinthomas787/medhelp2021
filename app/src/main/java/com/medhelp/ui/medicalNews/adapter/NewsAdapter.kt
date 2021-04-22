package com.medhelp.ui.medicalNews.adapter

import android.graphics.drawable.Drawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.medhelp.R
import com.medhelp.ui.medicalNews.model.TopHeadlinesBean
import java.text.SimpleDateFormat

class NewsAdapter( val articles: MutableList<TopHeadlinesBean.Article>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
         var newsImageView = itemView.findViewById<ImageView>(R.id.ivNews)
         var sourceText = itemView.findViewById<TextView>(R.id.tvSource)
        var titleText = itemView.findViewById<TextView>(R.id.tvTitle)
         var contentTextView = itemView.findViewById<TextView>(R.id.tvContent)
         var publishTextView = itemView.findViewById<TextView>(R.id.tvPublishedDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_medical_news, parent, false)
        return NewsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var newsFeed = articles.get(position)
        Glide.with(holder.itemView.context).load(newsFeed.urlToImage)
            .listener(requestListener(holder)).into(holder.newsImageView)
        try {
            holder.contentTextView.text = Html.fromHtml(newsFeed.content)
        } catch (e: Exception) {
        }
        holder.titleText.text = newsFeed.title
        holder.sourceText.text = newsFeed.source.name
        try {
            var publishedDate = SimpleDateFormat(DATE_FORMAT_PATTERN).parse(newsFeed.publishedAt)
            holder.publishTextView.text = SimpleDateFormat("dd MMM yyyy").format(publishedDate)
        } catch (e: Exception) {

        }
    }

    private fun requestListener(holder: ViewHolder) =
        object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.progressBar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.progressBar.visibility = View.GONE
                return false
            }
        }

    override fun getItemCount(): Int {
        return articles.size
    }


}