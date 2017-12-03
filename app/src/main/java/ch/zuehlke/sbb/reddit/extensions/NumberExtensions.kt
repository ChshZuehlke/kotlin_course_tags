package ch.zuehlke.sbb.reddit.extensions

import java.util.Date

/**
 * Created by chsc on 11.11.17.
 */



fun Long?.friendlyTime(): String {
        if(this != null){
            val sec = Math.floor((Date().time / 1000 - this).toDouble())
            var interval = Math.floor(sec / 31536000)


            if (interval >= 1) {
                if (interval == 1.0)
                    return interval.toString() + " year ago"
                else
                    return interval.toString() + " years ago"
            }
            interval = Math.floor(sec / 2592000)
            if (interval >= 1) {
                if (interval == 1.0)
                    return interval.toString() + " month ago"
                else
                    return interval.toString() + " months ago"
            }
            interval = Math.floor(sec / 86400)
            if (interval >= 1) {
                if (interval == 1.0)
                    return interval.toString() + " day ago"
                else
                    return interval.toString() + " days ago"
            }
            interval = Math.floor(sec / 3600)
            if (interval >= 1) {
                if (interval == 1.0)
                    return interval.toString() + " hour ago"
                else
                    return interval.toString() + " hours ago"
            }
            interval = Math.floor(sec / 60)
            if (interval >= 1) {
                if (interval == 1.0)
                    return interval.toString() + " minute ago"
                else
                    return interval.toString() + " minutes ago"
            }
            return Math.floor(sec).toString() + " seconds ago"
        }
        return ""

    }

