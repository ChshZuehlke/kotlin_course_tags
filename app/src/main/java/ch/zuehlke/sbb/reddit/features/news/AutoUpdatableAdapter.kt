package ch.zuehlke.sbb.reddit.features.news

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

/**
 * Created by chsc on 14.12.17.
 */


interface AutoUpdatableAdapter {

    fun <T> RecyclerView.Adapter<*>.autoNotify(old: List<T>, new: List<T>, compare: (T, T) -> Boolean) {
        val diffUtil = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    compare(old[oldItemPosition], new[newItemPosition])

            override fun getOldListSize() = old.size

            override fun getNewListSize() = new.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    old[oldItemPosition] == new[newItemPosition]

        })
        return diffUtil.dispatchUpdatesTo(this)
    }
}