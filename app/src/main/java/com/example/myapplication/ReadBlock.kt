package com.example.myapplication

import android.view.View

const val READ_BLOCK_TYPE = "read"

class ReadBlock(view: View): CustomView {

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val id: Int=(0..1000000000).random()
    override val blockType = READ_BLOCK_TYPE
    override val blockView = view
    override var pattern: String
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun blockToCode(view: View): String {
        TODO("Not yet implemented")
    }

    init {
        blockView.id=(0..1000000000).random()
    }

}