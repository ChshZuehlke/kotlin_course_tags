package ch.zuehlke.sbb.reddit.features.overview.adapter.impl


import android.databinding.BindingAdapter
import android.support.annotation.IntegerRes
import android.widget.ImageView
import android.widget.TextView
import ch.zuehlke.sbb.reddit.R
import ch.zuehlke.sbb.reddit.util.DateUtils
import com.google.common.base.Strings
import com.squareup.picasso.Picasso

/**
 * Created by chsc on 15.11.17.
 */


object RedditNewsBindingAdapter{

    @JvmStatic
    @BindingAdapter(value = *arrayOf("imageUrl" , "placeHolder"), requireAll = false)
    fun setImageUrl(imageView: ImageView, url: String,placeHolder: Int?){
        if(Strings.isNullOrEmpty(url) && placeHolder != null){
            Picasso.with(imageView.context).load(placeHolder).into(imageView)
        }else if(!Strings.isNullOrEmpty(url)){
            Picasso.with(imageView.context).load(url).into(imageView)
        }else{
            Picasso.with(imageView.context).load(R.drawable.reddit_placeholder).into(imageView)
        }
        Int
    }

    @JvmStatic
    @BindingAdapter("friendlyTime")
    fun setFriendlyTime(textView: TextView, date: Long){
        textView.text = DateUtils.friendlyTime(date)
    }



}