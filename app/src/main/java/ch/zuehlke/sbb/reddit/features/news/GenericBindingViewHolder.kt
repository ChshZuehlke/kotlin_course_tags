package ch.zuehlke.sbb.reddit.features.news

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import ch.zuehlke.sbb.reddit.BR


/**
 * Created by chsc on 15.11.17.
 */


open class GenericBindingViewHolder(protected val binding: ViewDataBinding, protected val  listener: ch.zuehlke.sbb.reddit.features.news.GenericBindingViewHolder.GenericBindingClickListener): android.support.v7.widget.RecyclerView.ViewHolder(binding.root){

    open fun bind(obj: Any){
        binding.setVariable(ch.zuehlke.sbb.reddit.BR.obj,obj)
        binding.setVariable(ch.zuehlke.sbb.reddit.BR.listener,listener)
        binding.executePendingBindings()
    }

    interface GenericBindingClickListener{
        fun onItemSelected(obj: Any)
    }
}