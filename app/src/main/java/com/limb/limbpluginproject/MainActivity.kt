package com.limb.limbpluginproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.limb.limbpluginproject.databinding.ActivityMainBinding
import com.limb.limbpluginproject.view.datetime.ShowType
import com.limb.limbpluginproject.view.datetime.dialog.*
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                val createDateTimeSelectDialog = DateTimeDialog(this, ShowType.YYYY_MM,true)
                createDateTimeSelectDialog.isShowHistoryDate = true
                createDateTimeSelectDialog.doCancelable = false

                val instance = Calendar.getInstance()
                instance.add(Calendar.MINUTE,50)
                createDateTimeSelectDialog.customCalendar = instance

                createDateTimeSelectDialog.show()
                createDateTimeSelectDialog.iFinishListener = object :IFinishListener{
                    override fun selectResult(result: String, year: String?, month: String?, day: String?, hour: String?, minute: String?) {
                        mainViewBinding.tvDateShow.text = result
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}