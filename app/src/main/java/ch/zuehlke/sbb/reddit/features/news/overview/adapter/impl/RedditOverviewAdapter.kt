package ch.zuehlke.sbb.reddit.features.news.overview.adapter.impl

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import java.util.ArrayList
import java.util.HashMap

import ch.zuehlke.sbb.reddit.features.news.overview.adapter.AdapterConstants
import ch.zuehlke.sbb.reddit.features.news.overview.adapter.ViewType
import ch.zuehlke.sbb.reddit.features.news.overview.adapter.ViewTypeDelegateAdapter
import ch.zuehlke.sbb.reddit.models.RedditNewsData

/**
 * Created by chsc on 12.11.17.
 */

class RedditOverviewAdapter(listener: RedditNewsDelegateAdapter.OnNewsSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val loadingItem = object : ViewType {
        override val viewType: Int
            get() = AdapterConstants.LOADING
    }

    private val mItems = ArrayList<ViewType>()
    private val adapters = HashMap<Int, ViewTypeDelegateAdapter>()

    init {
        adapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        adapters.put(AdapterConstants.NEWS, RedditNewsDelegateAdapter(listener))
        mItems.add(loadingItem)
    }

    fun addRedditNews(newsData: List<RedditNewsData>) {
        val initPosition = mItems.size - 1
        mItems.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        mItems.addAll(newsData)
        mItems.add(loadingItem)
        notifyItemRangeChanged(initPosition, mItems.size + 1 /* plus loading item */)
    }

    fun clearAndAddNews(newsData: List<RedditNewsData>) {
        val previousItemSize = mItems.size
        mItems.clear()
        notifyItemRangeRemoved(0, previousItemSize)
        mItems.addAll(newsData)
        mItems.add(loadingItem)
        notifyItemRangeChanged(0, newsData.size + 1 /* plus loading item */)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapters[viewType]!!.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapters[getItemViewType(position)]!!.onBindViewHolder(holder, mItems[position])
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].viewType
    }
}
