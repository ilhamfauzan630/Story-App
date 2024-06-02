package com.zanacademy.myfirstsubmissionintermediate

import com.zanacademy.myfirstsubmissionintermediate.data.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "photoUrl + $i",
                "author + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            items.add(quote)
        }
        return items
    }
}