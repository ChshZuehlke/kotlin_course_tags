package ch.zuehlke.sbb.reddit.features.overview.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import ch.zuehlke.sbb.reddit.BR

/**
 * Created by chsc on 15.11.17.
 */


class GenericBindingViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(obj: Any){
        binding.setVariable(BR.obj,obj)
        binding.executePendingBindings()
    }
}