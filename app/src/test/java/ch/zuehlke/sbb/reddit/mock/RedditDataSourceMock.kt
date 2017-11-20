package ch.zuehlke.sbb.reddit.mock

import ch.zuehlke.sbb.reddit.data.source.RedditDataSource
import ch.zuehlke.sbb.reddit.models.RedditNewsData
import ch.zuehlke.sbb.reddit.models.RedditPostsData

/**
 * Created by celineheldner on 20.11.17.
 */
open class RedditDataSourceMock{
     val posts = ArrayList<RedditPostsData>()
     val news = ArrayList<RedditNewsData>()
     val moreNews = ArrayList<RedditNewsData>()

    init {
        val redditPost1 = RedditPostsData("remote1",null,"author1","body1",19000,0,"bodyhtml1","/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/",0)
        val redditPost2 = RedditPostsData("remote2",null,"author2","body2",19000,0,"bodyhtml2","/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/",1)

        posts.add(redditPost1)
        posts.add(redditPost2)

        val redditNews1 = RedditNewsData("author1","title1",4,19060,null,null,"remoteNews1","/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/")
        val redditNews2 = RedditNewsData("author2","title2",4,19060,null,null,"remoteNews2","/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/")

        news.add(redditNews1)
        news.add(redditNews2)

        val redditNews3 = RedditNewsData("author3","title3",4,19060,null,null,"remoteNews3","/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/")
        val redditNews4 = RedditNewsData("author4","title4",4,19060,null,null,"remoteNews4","/r/DotA2/comments/7ckn4x/nov_13_competitive_matches_southeast_asia/")

        moreNews.add(redditNews3)
        moreNews.add(redditNews4)

    }
}