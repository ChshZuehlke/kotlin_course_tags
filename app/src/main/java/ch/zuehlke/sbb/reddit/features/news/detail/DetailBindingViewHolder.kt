package ch.zuehlke.sbb.reddit.features.news.detail

import android.databinding.ViewDataBinding
import ch.zuehlke.sbb.reddit.features.GenericBindingViewHolder

/**
 * Created by chsc on 15.11.17.
 */


class DetailBindingViewHolder(binding: ViewDataBinding, listener: GenericBindingClickListener) : GenericBindingViewHolder(binding,listener) {

    override fun bind(obj: Any) {
        binding.setVariable(ch.zuehlke.sbb.reddit.BR.obj,obj)
        binding.setVariable(ch.zuehlke.sbb.reddit.BR.listener,listener)
        binding.executePendingBindings()

    }


}
