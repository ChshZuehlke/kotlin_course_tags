package ch.zuehlke.sbb.reddit.features.overview.adapter

/**
 * Created by chsc on 15.11.17.
 */


abstract class GenericBindingBaseAdapter : android.support.v7.widget.RecyclerView.Adapter<GenericBindingViewHolder>() {


    override fun onBindViewHolder(holder: GenericBindingViewHolder, position: Int) {
        val itemToBind = getObjForPosition(position)
        holder.bind(itemToBind)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): GenericBindingViewHolder {
        val inflater = android.view.LayoutInflater.from(parent.context)
        val binding = android.databinding.DataBindingUtil.inflate<android.databinding.ViewDataBinding>(inflater, getLayoutIdForViewType(viewType), parent, false)
        return GenericBindingViewHolder(binding)
    }


    override fun getItemViewType(position: Int): Int {
        return getViewTypeForPosition(position)
    }

    protected abstract fun getObjForPosition(position: Int): Any

    protected abstract fun getLayoutIdForViewType(position: Int): Int

    protected abstract fun getViewTypeForPosition(position: Int): Int

}