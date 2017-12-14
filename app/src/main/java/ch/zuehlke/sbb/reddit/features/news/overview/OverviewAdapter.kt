package ch.zuehlke.sbb.reddit.features.news.overview

import android.support.v4.util.SparseArrayCompat
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.news.*
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import kotlin.properties.Delegates

/**
 * Created by chsc on 15.11.17.
 */


class OverviewAdapter(clickListener: GenericBindingViewHolder.GenericBindingClickListener,compare: (ViewType,ViewType)-> Boolean): GenericBindingBaseAdapter(clickListener),AutoUpdatableAdapter{

    private val loadingItem = object : ViewType {
        override val viewType = AdapterConstants.LOADING
    }

    private var items : MutableList<ViewType> by Delegates.observable(mutableListOf(),{ _, oldValue, newValue ->
        autoNotify(oldValue,newValue,compare)
    })

    private val viewTypeLayoutDelegate = SparseArrayCompat<Int>()

    init {
        viewTypeLayoutDelegate.put(AdapterConstants.LOADING, R.layout.item_loading)
        viewTypeLayoutDelegate.put(AdapterConstants.NEWS, R.layout.item_overview)
        items = mutableListOf(loadingItem)
    }

    // Exercise 01
    // Rewrite the both methods which set the new items and incorporate the
    // DiffUtil class from google which handles all notifications for us.

    fun addRedditNews(newsData: List<RedditNewsData>) {
        val mergedList = mutableListOf<ViewType>()
        mergedList.addAll(items)
        mergedList.addAll(items.size-2,newsData)
        items = mergedList
    }

    fun clearAndAddNews(newsData: List<RedditNewsData>) {
       val mergedList = mutableListOf<ViewType>()
        mergedList.addAll(newsData)
        mergedList.add(loadingItem)
        items = mergedList
    }



    override fun getObjForPosition(position: Int): Any {
        return items[position]
    }

    override fun getViewTypeForPosition(position: Int): Int {
       return items[position].viewType
    }

    override fun getLayoutIdForViewType(position: Int): Int {
        return viewTypeLayoutDelegate.get(getViewTypeForPosition(position))
    }

    override fun getItemCount() = items.size


}