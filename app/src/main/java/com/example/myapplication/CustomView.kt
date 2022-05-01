package com.example.myapplication

import android.view.View

interface CustomView {
    val isNestingPossible: Boolean
    var previousId: Int
    var nextId: Int
    val id: Int
    val blockType: String
    val blockView:View
    var pattern:String
    fun blockToCode(view:View):String
}