package ch.zuehlke.sbb.reddit.features.news.overview.adapter.impl

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.google.common.base.Strings
import com.squareup.picasso.Picasso

import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.news.overview.adapter.ViewType
import ch.zuehlke.sbb.reddit.features.news.overview.adapter.ViewTypeDelegateAdapter
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.util.DateUtils

/**
 * Created by chsc on 12.11.17.
 */

class RedditNewsDelegateAdapter(private val mListener: RedditNewsDelegateAdapter.OnNewsSelectedListener) : ViewTypeDelegateAdapter {

    interface OnNewsSelectedListener {
        fun onNewsSelected(url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return RedditNewsViewHolder(parent, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        if (holder is RedditNewsViewHolder) {
            holder.bind(item as RedditNewsData)
        }
    }


    inner class RedditNewsViewHolder(parent: ViewGroup, private val mContext: Context) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_overview, parent, false)) {

        private val mThumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
        private val mTitle = itemView.findViewById<TextView>(R.id.description)
        private val mAuthor = itemView.findViewById<TextView>(R.id.author)
        private val mComments = itemView.findViewById<TextView>(R.id.comments)
        private val mTime = itemView.findViewById<TextView>(R.id.time)

        fun bind(data: RedditNewsData) {
            mComments.text = data.numberOfComments.toString()
            mAuthor.text = data.author
            mTitle.text = data.title
            mTime.text = DateUtils.friendlyTime(data.created)
            if (Strings.isNullOrEmpty(data.thumbnailUrl)) {
                Picasso.with(mContext).load(R.drawable.reddit_placeholder).into(mThumbnail)
            } else {
                Picasso.with(mContext).load(data.thumbnailUrl).into(mThumbnail)
            }

            super.itemView.setOnClickListener { mListener.onNewsSelected(data.permaLink!!) }
        }
    }
}
