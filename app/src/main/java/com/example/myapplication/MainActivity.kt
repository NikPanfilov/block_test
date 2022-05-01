package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val blockList:MutableList<CustomView> = mutableListOf()
    private lateinit var binding:ActivityMainBinding
    private val blockMap:MutableMap<Int,CustomView> = mutableMapOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.z=0F

        binding.root.setOnTouchListener { _, motionEvent ->
            val newBlock=layoutInflater.inflate(R.layout.initialization_block,null)

            val lParams=ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
            newBlock.x=motionEvent.x
            newBlock.y=motionEvent.y
            newBlock.z=1F
            binding.root.addView(newBlock,lParams)
            newBlock.setOnTouchListener { view, event ->
                moveBlock(view,event)
            }
            val block=InitializationBlock(newBlock)
            blockList.add(block)
            blockMap[newBlock.id] = block
            false
        }
    }

    private var x=0F
    private var y=0F
    private fun moveBlock(view:View, event: MotionEvent):Boolean {
        if (event.action==MotionEvent.ACTION_DOWN){
            x=event.x
            y=event.y
            view.z=2F
        }
        if(event.action==MotionEvent.ACTION_MOVE) {
            view.x += event.x-x
            view.y += event.y-y
        }
        if (event.action==MotionEvent.ACTION_UP){
            view.z=1F

            val currentBlock = blockMap[view.id]
            var flag=false

            for (block in blockList){
                if(currentBlock!=block && cross(block,view)){
                    flag=true

                    if(block.nextId== currentBlock!!.id){
                        alignBlock(block,currentBlock)
                        break
                    }

                    if(currentBlock.previousId!=-1) {
                        blockMap[currentBlock.previousId]!!.nextId = currentBlock.nextId
                    }
                    if(currentBlock.nextId!=-1){
                        blockMap[currentBlock.nextId]!!.previousId=currentBlock.previousId
                    }

                    if(block.nextId!=-1) {
                        blockMap[block.nextId]!!.previousId=currentBlock.id
                    }
                    val temp=block.nextId
                    block.nextId=currentBlock.id
                    currentBlock.nextId=temp
                    currentBlock.previousId=block.id

                    var nextBlock=block
                    var prevBlock=block

                    while (nextBlock.nextId!=-1){
                        nextBlock= blockMap[prevBlock.nextId]!!
                        alignBlock(prevBlock,nextBlock)

                        prevBlock=nextBlock
                    }

                    break
                }
            }

            val rect=Rect()
            binding.imageView.getHitRect(rect)
            if(!flag && !rect.contains(x.toInt(),y.toInt())){
                binding.root.removeView(view)
                blockMap.remove(currentBlock!!.id)
                blockList.remove(currentBlock)
            }

        }
        return true
    }

    private fun alignBlock(parent:CustomView,child:CustomView){
        child.blockView.x= parent.blockView.x
        child.blockView.y= parent.blockView.y+parent.blockView.height

        if(parent.blockType=="begin"){
            child.blockView.x+=parent.blockView.width/2
        }
        if(child.blockType=="end"){
            child.blockView.x-=child.blockView.width/2
        }
        //TODO(): блоки begin и end могут иметь разную ширину. Нужно делать отступ в половину от максимальной
    }

    private fun cross(block: CustomView, view:View):Boolean{
        if (block.blockView.y<=view.y && block.blockView.y+block.blockView.height>=view.y)
            return true

        return false
    }

    fun blocksToCode():String{
        var code=""
        var currentBlock=blockList[0]
        for(block in blockList){
            if(block.previousId==0){
                currentBlock=block
            }
        }

        while(currentBlock.nextId!=-1){
            currentBlock= blockMap[currentBlock.nextId]!!
        }
        return code
    }
}

/*
Блок начала программы
Иницализация переменной
Присваивание
Ввод переменной
Вывод переменной
Создание статических массивов
Begin
End

if
Цикл while
if-else
 */
