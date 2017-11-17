package ch.zuehlke.sbb.reddit.models

import ch.zuehlke.sbb.reddit.features.news.overview.adapter.AdapterConstants
import ch.zuehlke.sbb.reddit.features.news.overview.adapter.ViewType

/**
 * Created by chsc on 08.11.17.
 */

class RedditNewsData : ViewType {

    var author: String? = null
    var title: String? = null
    var numberOfComments: Int = 0
    var created: Long = 0
    var thumbnailUrl: String? = null
    var url: String? = null
    var id: String? = null
    var permaLink: String? = null

    constructor(author: String, title: String, numberOfComments: Int, created: Long, thumbnailUrl: String, url: String, id: String, permaLink: String) {
        this.author = author
        this.title = title
        this.numberOfComments = numberOfComments
        this.created = created
        this.thumbnailUrl = thumbnailUrl
        this.url = url
        this.id = id
        this.permaLink = permaLink
    }

    constructor() {}

    override val viewType: Int
        get() = AdapterConstants.NEWS
}
