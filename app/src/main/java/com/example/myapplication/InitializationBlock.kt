package com.example.myapplication

import android.view.View
import android.widget.TextView

const val INIT_BLOCK_TYPE = "initialization"

class InitializationBlock(view: View) : CustomView {

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val id: Int = (1..1000000000).random()
    override val blockType = INIT_BLOCK_TYPE
    override val blockView = view
    override var pattern = "var <name> : <type> = <value>"
    var name = ""
    override fun blockToCode(view: View): String {
        val name = view.findViewById<TextView>(R.id.varName).text.toString()
        val type = view.findViewById<TextView>(R.id.spinner).text.toString()
        val value = view.findViewById<TextView>(R.id.varValue).text.toString()
        return pattern.replace("<name>", name).replace("<type>", type).replace("<value>", value)
    }

    init {
        view.id = id
    }


}