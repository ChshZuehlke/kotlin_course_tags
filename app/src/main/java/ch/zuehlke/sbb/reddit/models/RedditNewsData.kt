package ch.zuehlke.sbb.reddit.models

import ch.zuehlke.sbb.reddit.features.overview.adapter.AdapterConstants
import ch.zuehlke.sbb.reddit.features.overview.adapter.ViewType

/**
 * Created by chsc on 08.11.17.
 */

data class RedditNewsData(
        var author: String? = null,
        var title: String? = null,
        var numberOfComments: Int = 0,
        var created: Long = 0,
        var thumbnailUrl: String? = "",
        var url: String? = "",
        var id: String = "",
        var permaLink: String = ""

): ViewType{
    override fun getViewType(): Int {
        return AdapterConstants.NEWS
    }
}
