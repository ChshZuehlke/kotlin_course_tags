package ch.zuehlke.sbb.reddit.features.news.detail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.models.RedditPostsData
import ch.zuehlke.sbb.reddit.util.DateUtils

/**
 * Created by chsc on 13.11.17.
 */

class DetailAdapter(private val mContext: Context) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {


    private val mItems = ArrayList<RedditPostsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val post = mItems[position]

        holder.mUsername.text = post.author
        holder.mText.text = post.body
        holder.mCreated.text = DateUtils.friendlyTime(post.createdUtc)
        setDepthPadding(holder.itemView, post.depth)
    }

    fun clearAndAddPosts(newsData: List<RedditPostsData>) {
        val previousItemSize = mItems.size
        mItems.clear()
        notifyItemRangeRemoved(0, previousItemSize)
        mItems.addAll(newsData)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    private fun setDepthPadding(view: View, depth: Int) {
        val normalPadding = 5f
        val leftPadding = normalPadding + depth * 10f

        val normalDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, normalPadding, mContext.resources.displayMetrics).toInt()
        val leftDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftPadding, mContext.resources.displayMetrics).toInt()
        view.setPadding(leftDp, normalDp, normalDp, normalDp)
    }

     class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val mUsername: TextView
         val mCreated: TextView
         val mText: TextView

        init {
            mUsername = itemView.findViewById<TextView>(R.id.username)
            mCreated = itemView.findViewById<TextView>(R.id.created)
            mText = itemView.findViewById<TextView>(R.id.text)
        }
    }
}
