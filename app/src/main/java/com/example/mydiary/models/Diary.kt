package com.example.mydiary.models

data class Diary(
    var id: Int = 0,
    var name: String = "",
    var about: String = "",
    var deleted: Int = 0,
    var favorite: Int = 0,
    var pack_name: String = "",
    var deleted_time: String = ""
)
