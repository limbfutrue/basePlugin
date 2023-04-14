package com.limb.limbpluginproject.view.datetime.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.limb.limbpluginproject.R
import com.limb.limbpluginproject.databinding.DateTimeSelectDialogLayoutBinding
import com.limb.limbpluginproject.view.datetime.ShowType
import java.util.*

open class BaseDialog(context: Context, type: ShowType, isLoop: Boolean = false) : Dialog(context) {

    lateinit var mParentViewBinding: DateTimeSelectDialogLayoutBinding
    var mType: ShowType = type
    private var context: Context? = null

    // 默认年月日开始下标
    var defaultStartYear: Int = 0
    var defaultStartMonth: Int = 1
    var defaultStartDay: Int = 1
    var defaultStartHour: Int = 0
    var defaultStartMinute: Int = 0

    // 结束下标
    var defaultEndYear: Int = 0
    var defaultEndMonth: Int = 12
    var defaultEndDay: Int = 0
    var defaultEndHour: Int = 23
    var defaultEndMinute: Int = 59

    val yearData: ArrayList<String> = ArrayList()
    val monthData: ArrayList<String> = ArrayList()
    val dayData: ArrayList<String> = ArrayList()
    val hourData: ArrayList<String> = ArrayList()
    val minuteData: ArrayList<String> = ArrayList()

    // 当前日期
    lateinit var curCalendar: Calendar

    // 选中的日期
    lateinit var selectCalendar: Calendar

    // 自定义默认显示的日期
    var customCalendar: Calendar? = null

    // 是否显示历史日期
    var isShowHistoryDate = true
        set(value) {
            initDefaultDateRange()
            field = value
        }

    // 是否记录已选择的日期
    var isRecordSelect = true

    // 完成按钮监听
    var iFinishListener: IFinishListener? = null

    var doCancelable: Boolean = false

    private var isLoop: Boolean = false

    init {
        this.context = context
        this.isLoop = isLoop
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore()
        super.onCreate(savedInstanceState)
        initDialog()

    }

    open fun onCreateBefore() {

    }

    open fun initDialog() {
        mParentViewBinding = DateTimeSelectDialogLayoutBinding.inflate(layoutInflater)
        setContentView(mParentViewBinding.root)
        if (customCalendar == null) {
            customCalendar = Calendar.getInstance()
        }
        //消除边距,全屏显示
        window?.decorView?.setPadding(0, 0, 0, 0)
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params
        window?.setBackgroundDrawableResource(R.color.white)
        window?.setGravity(Gravity.BOTTOM)
        setCancelable(doCancelable)
        setShowType()
        setIsLoop(isLoop)
    }

    /**
     *
     * 初始日期选择器默认区间
     */
    fun initDefaultDateRange() {
        curCalendar = Calendar.getInstance()
        selectCalendar = Calendar.getInstance()
        // 初始化数据开始区间
        defaultStartYear = if (!isShowHistoryDate) curCalendar[Calendar.YEAR] else 1990
        defaultStartMonth = if (!isShowHistoryDate) curCalendar[Calendar.MONTH] + 1 else 1
        defaultStartDay = if (!isShowHistoryDate) curCalendar[Calendar.DAY_OF_MONTH] else 1
        defaultStartHour = if (!isShowHistoryDate) curCalendar[Calendar.HOUR_OF_DAY] else 0
        defaultStartMinute = if (!isShowHistoryDate) curCalendar[Calendar.MINUTE] else 0
        // 初始化数据结束区间
        defaultEndYear = if (!isShowHistoryDate) curCalendar[Calendar.YEAR] + 50 else 2123
        defaultEndMonth = 12
        defaultEndDay = curCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        defaultEndHour = 23
        defaultEndMinute = 59
    }

    /**
     * 是否循环显示
     */
    private fun setIsLoop(isLoop: Boolean) {
        mParentViewBinding.pvYear.setIsLoop(isLoop)
        mParentViewBinding.pvMonth.setIsLoop(isLoop)
        mParentViewBinding.pvDay.setIsLoop(isLoop)
        mParentViewBinding.pvHour.setIsLoop(isLoop)
        mParentViewBinding.pvMinute.setIsLoop(isLoop)
    }

    private fun setShowType() {
        when (mType) {
            ShowType.YYYY -> {
                mParentViewBinding.pvMonth.visibility = View.GONE
                mParentViewBinding.pvDay.visibility = View.GONE
                mParentViewBinding.pvHour.visibility = View.GONE
                mParentViewBinding.pvMinute.visibility = View.GONE
            }
            ShowType.YYYY_MM -> {
                mParentViewBinding.pvDay.visibility = View.GONE
                mParentViewBinding.pvHour.visibility = View.GONE
                mParentViewBinding.pvMinute.visibility = View.GONE
            }
            ShowType.YYYY_MM_DD -> {
                mParentViewBinding.pvHour.visibility = View.GONE
                mParentViewBinding.pvMinute.visibility = View.GONE
            }
            ShowType.YYYY_MM_DD_HH_MM -> {
            }
            ShowType.MM -> {
                mParentViewBinding.pvYear.visibility = View.GONE
                mParentViewBinding.pvDay.visibility = View.GONE
                mParentViewBinding.pvHour.visibility = View.GONE
                mParentViewBinding.pvMinute.visibility = View.GONE
            }
            ShowType.MM_DD -> {
                mParentViewBinding.pvYear.visibility = View.GONE
                mParentViewBinding.pvHour.visibility = View.GONE
                mParentViewBinding.pvMinute.visibility = View.GONE
            }
            ShowType.MM_DD_HH_MM -> {
                mParentViewBinding.pvYear.visibility = View.GONE
            }
            ShowType.HH_MM -> {
                mParentViewBinding.pvYear.visibility = View.GONE
                mParentViewBinding.pvDay.visibility = View.GONE
                mParentViewBinding.pvHour.visibility = View.GONE
                mParentViewBinding.pvMonth.visibility = View.GONE
            }
            else -> {
                mParentViewBinding.pvHour.visibility = View.GONE
                mParentViewBinding.pvMinute.visibility = View.GONE
            }
        }
    }

    override fun dismiss() {
        if (context == null) {
            return
        }
        if (context is Activity) {
            if (!(context as Activity).isFinishing && !(context as Activity).isDestroyed) {
                super.dismiss()
            }
        }
    }
}