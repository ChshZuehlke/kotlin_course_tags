package ch.zuehlke.sbb.reddit

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

import ch.zuehlke.sbb.reddit.data.source.remote.model.posts.RedditPostElement
import ch.zuehlke.sbb.reddit.extensions.friendlyTime

import com.google.common.base.Strings.repeat

/**
 * Created by chsc on 13.11.17.
 */

abstract class AbstractTestCase {

    @Throws(IOException::class)
    protected fun readJsonFile(filename: String): String {

        val br = BufferedReader(InputStreamReader(this.javaClass.getClassLoader().getResourceAsStream(filename)))
        val sb = StringBuilder()
        var line: String? = br.readLine()
        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }

        return sb.toString()
    }

    protected fun dump(abstractElements: Iterable<RedditPostElement>, level: Int = 0) {
        val tab = repeat(".", level)
        for (e in abstractElements) {
            if (e is RedditPostElement.DataRedditPostElement) {
                val dataElement = e
                print(tab)
                print("DATA=")
                println(dataElement.kind)
                if (dataElement.data!!.children != null) {
                    dump(dataElement.data!!.children!!, level + 1)
                }
                if (dataElement.data!!.replies != null) {
                    val replies = dataElement.data!!.replies
                    if (dataElement.data!!.replies is RedditPostElement.DataRedditPostElement) {
                        dump((replies as RedditPostElement.DataRedditPostElement).data!!.children!!, level + 1)
                    } else if (dataElement.data!!.replies is RedditPostElement.ReferenceRedditPostElement) {
                        print(tab)
                        print("REF=")
                        println((dataElement.data!!.replies as RedditPostElement.ReferenceRedditPostElement).reference)
                    } else {
                        throw AssertionError(replies!!.javaClass)
                    }
                }
                if (dataElement.data!!.author != null) {
                    print("Author=")
                    println(dataElement.data!!.author)
                }

                if (dataElement.data!!.body != null) {
                    print("Body=")
                    println(dataElement.data!!.body)
                }

                if (dataElement.data!!.created_utc != 0L) {
                    print("Created=")
                    println(dataElement.data!!.created_utc.friendlyTime())
                }

                if (dataElement.data!!.depth != 0) {
                    print("Depth=")
                    println("" + dataElement.data!!.depth)
                }

            } else if (e is RedditPostElement.ReferenceRedditPostElement) {
                print(tab)
                print("REF=")
                println(e.reference)
            } else {
                throw IllegalArgumentException(e.toString())
            }
        }
    }


}
