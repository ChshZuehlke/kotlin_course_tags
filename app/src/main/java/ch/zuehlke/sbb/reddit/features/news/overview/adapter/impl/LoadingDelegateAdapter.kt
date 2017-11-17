package ch.zuehlke.sbb.reddit.features.news.overview.adapter.impl

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.features.news.overview.adapter.ViewType
import ch.zuehlke.sbb.reddit.features.news.overview.adapter.ViewTypeDelegateAdapter

/**
 * Created by chsc on 12.11.17.
 */

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {


    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, type: ViewType) {

    }


    private inner class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))


}
