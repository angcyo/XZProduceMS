package com.angcyo.xzproducems.iview

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.resources.ResUtil
import com.angcyo.uiview.widget.RTextView
import com.angcyo.xzproducems.LoginControl
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseRecycleUIView
import com.angcyo.xzproducems.bean.QueryBean
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-24.
 */
class QueryListUIView(val DGID: String) : BaseRecycleUIView<String, QueryBean, String>() {

    companion object {
        fun getShowString(s: String?): String {
            return if (s.isNullOrEmpty()) "空" else s!!
        }
    }

    override fun getTitleString(): String {
        return "订单($DGID)跟踪查询"
    }

    override fun onBackPressed(): Boolean {
        replaceIView(ScanUIView().apply { toOrderList = false })
        return false
    }

    override fun createAdapter(): RExBaseAdapter<String, QueryBean, String> {
        return object : RExBaseAdapter<String, QueryBean, String>(mActivity) {

            var frontItemCount = 7 //前面有多少个item
            var maxItemCount: Int = (frontItemCount + LoginControl.gxList.size - 1)//去掉管理员工序数量

            override fun getItemLayoutId(viewType: Int): Int {
                return 0
            }

            override fun createItemView(parent: ViewGroup?, viewType: Int): View {
                val rootLayout = LinearLayout(mActivity)
                rootLayout.orientation = LinearLayout.VERTICAL
                rootLayout.layoutParams = LinearLayout.LayoutParams(-1, -2)

                val padding = getDimensionPixelOffset(R.dimen.base_xhdpi)
                //rootLayout.setPadding(padding, padding, padding, padding)

                val numTipView = RTextView(mActivity, null, R.style.BaseDarkTextStyle)
                numTipView.apply {
                    tag = "numTipView"
                    textSize = 14f
                    setTextColor(Color.WHITE)
                    setPadding(padding, padding / 2, 0, padding / 2)
                    setBackgroundColor(ResUtil.getThemeColorAccent(context))
                }
                rootLayout.addView(numTipView, LinearLayout.LayoutParams(-1, -2))

                for (i in 0..maxItemCount) {
                    val tipView = RTextView(mActivity, null, R.style.BaseDarkTextStyle)
                    tipView.apply {
                        tag = "tipView$i"
                        textSize = 9f
                        setPadding(padding, padding / 2, padding, 0)
                    }
                    val contentView = RTextView(mActivity, null, R.style.BaseMainTextStyle)
                    contentView.apply {
                        tag = "contentView$i"
                        textSize = 16f
                        setPadding(padding, 0, padding, padding / 2)
                    }

                    when (i) {
//                        0 -> tipView.text = "订单号:"
//                        1 -> tipView.text = "工序:"
                        0 -> tipView.text = "物料编码:"
                        1 -> tipView.text = "名称:"
                        2 -> tipView.text = "规格:"
                        3 -> tipView.text = "型号:"
                        4 -> tipView.text = "订单子项备注:"
                        5 -> tipView.text = "订单结案备注:"
                        6 -> tipView.text = "订单数量:"
                        maxItemCount -> tipView.text = "待出货数量:"
                        else -> {
                            //L.e("call: createItemView ->$maxItemCount ${LoginControl.gxList.size} $i ${maxItemCount - i + 1}")
                            tipView.text = LoginControl.gxList[i - frontItemCount].PNAME7
                        }
                    }

                    rootLayout.addView(tipView, LinearLayout.LayoutParams(-1, -2))
                    rootLayout.addView(contentView, LinearLayout.LayoutParams(-1, -2))
                }

                return rootLayout
            }

            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: QueryBean) {
                super.onBindDataView(holder, posInData, dataBean)
                val numTipView: RTextView? = holder.tag("numTipView")
                numTipView?.text = "序号:${posInData + 1}"

                for (i in 0..maxItemCount) {
                    val contentView: RTextView? = holder.tag("contentView$i")
                    contentView?.let {
                        when (i) {
//                        0 -> tipView.text = "订单号:"
//                        1 -> tipView.text = "工序:"
                            0 -> it.text = getShowString(dataBean.PID) //"物料编码:"
                            1 -> it.text = getShowString(dataBean.PNAME1)//"名称:"
                            2 -> it.text = getShowString(dataBean.PNAME2)//"规格:"
                            3 -> it.text = getShowString(dataBean.PNAME3)//"型号:"
                            4 -> it.text = getShowString(dataBean.PNAME5)//
                            5 -> it.text = getShowString(dataBean.PNAME6)//
                            6 -> it.text = getShowString(dataBean.QTY1)//"订单数量:"
                            maxItemCount -> it.text = getShowString(dataBean.QTY8)//"待出货数量:"
                            else -> {
                                when (i - frontItemCount) {
                                    0 -> it.text = getShowString(dataBean.QTY2)
                                    1 -> it.text = getShowString(dataBean.QTY3)
                                    2 -> it.text = getShowString(dataBean.QTY4)
                                    3 -> it.text = getShowString(dataBean.QTY5)
                                    4 -> it.text = getShowString(dataBean.QTY6)
                                    5 -> it.text = getShowString(dataBean.QTY7)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onUILoadData(page: Int) {
        super.onUILoadData(page)
        Rx.base(object : RFunc<MutableList<QueryBean>>() {
            override fun onFuncCall(): MutableList<QueryBean> {
                return DbUtil.UP_QUERY_PUR01(DGID)
            }
        }, object : RSubscriber<MutableList<QueryBean>>() {

            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                super.onEnd(isError, isNoNetwork, e)
                resetUI()
            }

            override fun onSucceed(bean: MutableList<QueryBean>) {
                super.onSucceed(bean)
                onUILoadFinish(bean)
            }
        })
    }
}
