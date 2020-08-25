package com.amartinez.musicplayer.data.local

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SearchLocal(
    @PrimaryKey
    var term: String = "",
    var resultCount: Long = 0,
    var results: RealmList<ResultLocal> = RealmList()
) : RealmObject()