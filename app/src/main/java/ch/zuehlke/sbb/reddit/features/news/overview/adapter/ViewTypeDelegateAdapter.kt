package ch.zuehlke.sbb.reddit.features.news.overview.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by chsc on 12.11.17.
 */

interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}
