package ch.zuehlke.sbb.reddit.features.news.detail

import android.databinding.BindingAdapter
import ch.zuehlke.sbb.reddit.extensions.friendlyTime

/**
 * Created by chsc on 16.11.17.
 */

object DetailDataBindingAdapter {


    @JvmStatic
    @BindingAdapter("friendlyTime")
    fun setFriendlyTime(textView: android.widget.TextView, date: Long){
        textView.text = date.friendlyTime()
    }

    @JvmStatic
    @BindingAdapter("android:paddingLeft")
    fun setPostPadding(view: android.view.View, depth: Int){
        (ch.zuehlke.sbb.reddit.features.news.detail.DetailDataBindingAdapter.setDepthPadding(view, depth))
    }

    private fun setDepthPadding(view: android.view.View, depth: Int) {
        val normalPadding = 5f
        val leftPadding = normalPadding + depth * 10f

        val normalDp = android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, normalPadding, view.context.resources.displayMetrics).toInt()
        val leftDp = android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, leftPadding, view.context.resources.displayMetrics).toInt()
        view.setPadding(leftDp, normalDp, normalDp, normalDp)
    }
}