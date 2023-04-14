package com.limb.limbpluginproject.view.datetime.dialog

import android.content.Context
import androidx.core.view.isVisible
import com.limb.limbpluginproject.view.datetime.ShowType
import java.util.*

/**
 * 时间选择器弹窗管理类
 *  支持类型：
 *  // 年
 *  YYYY,
 *  // 年月
 *  YYYY_MM,
 *  // 年月日
 *  YYYY_MM_DD,
 *  // 年月日时分
 *  YYYY_MM_DD_HH_MM,
 *  // 月
 *  MM,
 *  // 月日
 *  MM_DD,
 *  // 月日时分
 *  MM_DD_HH_MM,
 *  // 时分
 *  HH_MM
 *
 */
open class DateTimeDialog(context: Context, type: ShowType,isLoop:Boolean = false) : BaseDialog(context, type,isLoop) {
    override fun initDialog() {
        super.initDialog()
        initDefaultDateRange()
        setDefaultDateData()
        initScrollListener()
        setDefaultShowDefaultData()
    }

    /**
     * 设置时间选择器默认数据
     */
    private fun setDefaultDateData() {
        setYearData()
        setMonthData()
        setDayData()
        setHourData()
        setMinuteData()
    }

    /**
     * 设置默认显示数据
     */
    private fun setDefaultShowDefaultData() {
        customCalendar = if (customCalendar == null) {
            curCalendar
        } else customCalendar
        customCalendar?.let { customCalendar ->
            mParentViewBinding.pvYear.setSelected(customCalendar[Calendar.YEAR] - defaultStartYear)
            mParentViewBinding.pvMonth.setSelected(customCalendar[Calendar.MONTH] + 1 - defaultStartMonth)
            mParentViewBinding.pvDay.setSelected(customCalendar[Calendar.DAY_OF_MONTH] - defaultStartDay)
            mParentViewBinding.pvHour.setSelected(customCalendar[Calendar.HOUR_OF_DAY] - defaultStartHour)
            mParentViewBinding.pvMinute.setSelected(customCalendar[Calendar.MINUTE] - defaultStartMinute)
            // 显示自定义的时间
            selectCalendar = customCalendar
        }
        setTitle()
    }

    /**
     *
     * 设置年数据
     */
    private fun setYearData() {
        yearData.clear()
        mParentViewBinding.pvYear.label = "年"
        // 初始化年
        for (index in defaultStartYear..defaultEndYear) {
            yearData.add("$index")
        }
        mParentViewBinding.pvYear.setData(yearData)
    }

    /**
     *
     * 设置月数据
     */
    private fun setMonthData() {
        monthData.clear()
        mParentViewBinding.pvMonth.label = "月"
        // 初始化月
        for (index in defaultStartMonth..defaultEndMonth) {
            monthData.add("$index")
        }
        mParentViewBinding.pvMonth.setData(monthData)
    }

    /**
     *
     * 设置天数据
     */
    private fun setDayData() {
        dayData.clear()
        mParentViewBinding.pvDay.label = "日"
        // 初始化日
        for (index in defaultStartDay..defaultEndDay) {
            dayData.add("$index")
        }
        mParentViewBinding.pvDay.setData(dayData)
    }

    /**
     *
     * 设置时数据
     */
    private fun setHourData() {
        hourData.clear()
        mParentViewBinding.pvHour.label = "时"
        // 初始化日
        for (index in defaultStartHour..defaultEndHour) {
            hourData.add("$index")
        }
        mParentViewBinding.pvHour.setData(hourData)
    }

    /**
     *
     * 设置时数据
     */
    private fun setMinuteData() {
        minuteData.clear()
        mParentViewBinding.pvMinute.label = "分"
        // 初始化日
        for (index in defaultStartMinute..defaultEndMinute) {
            minuteData.add("$index")
        }
        mParentViewBinding.pvMinute.setData(minuteData)
    }

    /**
     * 设置标题日期
     */
    private fun setTitle() {
        var title = ""
        if (mParentViewBinding.pvYear.isVisible) {
            title += mParentViewBinding.pvYear.selectData + mParentViewBinding.pvYear.label
        }
        if (mParentViewBinding.pvMonth.isVisible) {
            title += mParentViewBinding.pvMonth.selectData + mParentViewBinding.pvMonth.label
        }
        if (mParentViewBinding.pvDay.isVisible) {
            title += mParentViewBinding.pvDay.selectData + mParentViewBinding.pvDay.label
        }

        if (mParentViewBinding.pvHour.isVisible) {
            title += mParentViewBinding.pvHour.selectData + mParentViewBinding.pvHour.label
        }

        if (mParentViewBinding.pvMinute.isVisible) {
            title += mParentViewBinding.pvMinute.selectData + mParentViewBinding.pvMinute.label
        }
        mParentViewBinding.tvTitle.text = title
    }


    /**
     * 初始化选择器滑动监听
     */
    private fun initScrollListener() {
        // 年选择监听
        mParentViewBinding.pvYear.setOnSelectListener { selectYear ->
            selectCalendar[Calendar.YEAR] = selectYear.toInt()
            if (mParentViewBinding.pvMonth.isVisible) {
                changeMonth()
            }
            setTitle()
        }

        // 月选择监听
        mParentViewBinding.pvMonth.setOnSelectListener { selectMonth ->
            val month = selectMonth.toInt() - 1
            selectCalendar[Calendar.MONTH] = month
            // 变动之后可能跨月
            if (selectCalendar[Calendar.MONTH] != month) {
                // 将月份调回值和UI一样
                selectCalendar[Calendar.MONTH] = month
            }
            changeDay()
            setTitle()
        }

        // 日选择监听
        mParentViewBinding.pvDay.setOnSelectListener { selectDay ->
            selectCalendar[Calendar.DAY_OF_MONTH] = selectDay.toInt()
            changeHour()
            setTitle()
        }

        // 时选择监听
        mParentViewBinding.pvHour.setOnSelectListener { selectHour ->
            selectCalendar[Calendar.HOUR_OF_DAY] = selectHour.toInt()
            changeMinute()
            setTitle()
        }

        // 分选择监听
        mParentViewBinding.pvMinute.setOnSelectListener { selectMinute ->
            selectCalendar[Calendar.MINUTE] = selectMinute.toInt()
            setTitle()
        }

        // 完成按钮监听回调
        mParentViewBinding.tvFinish.setOnClickListener {
            val result = mParentViewBinding.tvTitle.text.toString()
            val year = if (mParentViewBinding.pvYear.isVisible) mParentViewBinding.pvYear.selectData else null
            val month = if (mParentViewBinding.pvMonth.isVisible) mParentViewBinding.pvMonth.selectData else null
            val day = if (mParentViewBinding.pvDay.isVisible) mParentViewBinding.pvDay.selectData else null
            val hour = if (mParentViewBinding.pvHour.isVisible) mParentViewBinding.pvHour.selectData else null
            val minute = if (mParentViewBinding.pvMinute.isVisible) mParentViewBinding.pvMinute.selectData else null
            iFinishListener?.selectResult(result, year, month, day, hour, minute)
            dismiss()
        }
    }

    /**
     * 刷新数据源
     */
    private fun changeMonth() {
        // 重置Month数据区间
        defaultStartMonth = if (!isShowHistoryDate && curCalendar[Calendar.YEAR] == selectCalendar[Calendar.YEAR]) {
            curCalendar[Calendar.MONTH] + 1
        } else {
            1
        }
        defaultEndMonth = 12
        setMonthData()
        if (mParentViewBinding.pvDay.isVisible) {
            // 联动实时改变月数据
            changeDay()
        }
        // 保存选择记录
        if (isRecordSelect) {
            mParentViewBinding.pvMonth.setSelected(if (selectCalendar[Calendar.MONTH] + 1 - defaultStartMonth < 0) 0 else selectCalendar[Calendar.MONTH] + 1 - defaultStartMonth)
            selectCalendar[Calendar.MONTH] = mParentViewBinding.pvMonth.selectData.toInt() - 1
        } else {
            mParentViewBinding.pvMonth.setSelected(0)
        }
    }

    /**
     * 刷新数据源
     */
    private fun changeDay() {
        // 重置Day数据区间
        if (!isShowHistoryDate && curCalendar[Calendar.YEAR] == selectCalendar[Calendar.YEAR] && curCalendar[Calendar.MONTH] == selectCalendar[Calendar.MONTH]) {
            defaultStartDay = curCalendar[Calendar.DAY_OF_MONTH]
            defaultEndDay = curCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        } else {
            defaultStartDay = 1
            defaultEndDay = selectCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        setDayData()
        if (mParentViewBinding.pvHour.isVisible) {
            changeHour()
        }
        // 保存选择记录
        if (isRecordSelect) {
            mParentViewBinding.pvDay.setSelected(if (selectCalendar[Calendar.DAY_OF_MONTH] - defaultStartDay < 0) 0 else selectCalendar[Calendar.DAY_OF_MONTH] - defaultStartDay)
            selectCalendar[Calendar.DAY_OF_MONTH] = mParentViewBinding.pvDay.selectData.toInt()
        } else {
            mParentViewBinding.pvDay.setSelected(0)
        }
    }


    /**
     * 刷新数据源
     */
    private fun changeHour() {
        // 重置Day数据区间
        defaultStartHour = if (!isShowHistoryDate && curCalendar[Calendar.YEAR] == selectCalendar[Calendar.YEAR]
            && curCalendar[Calendar.MONTH] == selectCalendar[Calendar.MONTH]
            && curCalendar[Calendar.DAY_OF_MONTH] == selectCalendar[Calendar.DAY_OF_MONTH]
        ) {
            curCalendar[Calendar.HOUR_OF_DAY]
        } else {
            0
        }
        defaultEndHour = 23
        setHourData()
        if (mParentViewBinding.pvMinute.isVisible) {
            changeMinute()
        }
        // 保存选择记录
        if (isRecordSelect) {
            mParentViewBinding.pvHour.setSelected(if (selectCalendar[Calendar.HOUR_OF_DAY] - defaultStartHour < 0) 0 else selectCalendar[Calendar.HOUR_OF_DAY] - defaultStartHour)
            selectCalendar[Calendar.HOUR_OF_DAY] = mParentViewBinding.pvHour.selectData.toInt()
        } else {
            mParentViewBinding.pvHour.setSelected(0)
        }
    }


    /**
     * 刷新数据源
     */
    private fun changeMinute() {
        // 重置Day数据区间
        defaultStartMinute = if (!isShowHistoryDate
            && curCalendar[Calendar.YEAR] == selectCalendar[Calendar.YEAR]
            && curCalendar[Calendar.MONTH] == selectCalendar[Calendar.MONTH]
            && curCalendar[Calendar.DAY_OF_MONTH] == selectCalendar[Calendar.DAY_OF_MONTH]
            && curCalendar[Calendar.HOUR_OF_DAY] == selectCalendar[Calendar.HOUR_OF_DAY]
        ) {
            curCalendar[Calendar.MINUTE]
        } else {
            0
        }
        defaultEndMinute = 59
        setMinuteData()
        // 保存选择记录
        if (isRecordSelect) {
            mParentViewBinding.pvMinute.setSelected(if (selectCalendar[Calendar.MINUTE] - defaultStartMinute < 0) 0 else selectCalendar[Calendar.MINUTE] - defaultStartMinute)
            selectCalendar[Calendar.MINUTE] = mParentViewBinding.pvMinute.selectData.toInt()
        } else {
            mParentViewBinding.pvMinute.setSelected(0)
        }
    }


}