package com.angcyo.xzproducems.iview

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.angcyo.uiview.container.ContentLayout
import com.angcyo.uiview.design.StickLayoutManager
import com.angcyo.uiview.dialog.UIInputDialog
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.RRecyclerView
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.utils.T_
import com.angcyo.uiview.widget.RTextView
import com.angcyo.xzproducems.BuildConfig
import com.angcyo.xzproducems.MyScrollView
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseRecycleUIView
import com.angcyo.xzproducems.bean.OrderBean
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-24.
 */
class OrderListUIView_PAD(val DGID: String, val GXID: Int /*工序*/) : BaseRecycleUIView<String, OrderBean, String>(), MyScrollView.OnScrollListener {

    var scrollX = 0

    override fun onScroll(toX: Int, toY: Int) {
        scrollX = toX

        if (mRecyclerView == null) {
            return
        }
        val layoutManager = mRecyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            (0..layoutManager.getChildCount() - 1)
                    .map { firstVisibleItemPosition + it }
                    .map { mRecyclerView.findViewHolderForAdapterPosition(it) as RBaseViewHolder? }
                    .forEach { vh ->
                        vh?.let {
                            if (it.itemView is MyScrollView) {
                                it.itemView.scrollTo(toX, toY)
                            }
                        }
                    }
        }
    }

    override fun onBackPressed(): Boolean {
        replaceIView(ScanUIView().apply {
            toOrderList = true
        })
        return false
    }

    override fun getTitleString(): String {
        return "生产汇报"
    }

    override fun initRecyclerView(recyclerView: RRecyclerView?, baseContentLayout: ContentLayout?) {
        super.initRecyclerView(recyclerView, baseContentLayout)
        recyclerView?.layoutManager = StickLayoutManager(mActivity).apply {
            setStickPosition(0)
        }
    }

    override fun createAdapter(): RExBaseAdapter<String, OrderBean, String> {
        return object : RExBaseAdapter<String, OrderBean, String>(mActivity) {
            override fun getItemLayoutId(viewType: Int): Int {
                return 0
            }

            override fun createItemView(parent: ViewGroup?, viewType: Int): View {
                val scrollView = MyScrollView(mActivity)
                scrollView.isHorizontalScrollBarEnabled = false
                scrollView.scrollListener = this@OrderListUIView_PAD

                val rootLayout = LinearLayout(mActivity)
                rootLayout.orientation = LinearLayout.HORIZONTAL
                rootLayout.layoutParams = LinearLayout.LayoutParams(-1, -2)

                scrollView.addView(rootLayout, ViewGroup.LayoutParams(-2, -2))

                val padding = getDimensionPixelOffset(R.dimen.base_xhdpi)
                //rootLayout.setPadding(padding, padding, padding, padding)

//                val numTipView = RTextView(mActivity, null, R.style.BaseDarkTextStyle)
//                numTipView.apply {
//                    tag = "numTipView"
//                    textSize = 14f
//                    setTextColor(Color.WHITE)
//                    setPadding(padding, padding / 2, 0, padding / 2)
//                    setBackgroundColor(ResUtil.getThemeColorAccent(context))
//                }
//                rootLayout.addView(numTipView, LinearLayout.LayoutParams(-1, -2))

                for (i in 0..12) {
//                    val itemLayout = LinearLayout(mActivity)
//                    itemLayout.apply {
//                        orientation = LinearLayout.VERTICAL
//                        tag = "itemLayout$i"
//                        setBackgroundResource(R.drawable.base_bg_selector)
//                    }

//                    val tipView = RTextView(mActivity, null, R.style.BaseDarkTextStyle)
//                    tipView.apply {
//                        tag = "tipView$i"
//                        textSize = 9f
//                        setPadding(padding, padding / 2, padding, 0)
//                    }
                    val contentView = RTextView(mActivity, null, R.style.BaseMainTextStyle)
                    contentView.apply {
                        tag = "contentView$i"
                        textSize = 16f
                        setPadding(padding, 0, padding, padding / 2)
                    }

//                    when (i) {
//                        0 -> tipView.text = "原订单明细表ID号:"
//                        1 -> tipView.text = "订单号:"
//                        2 -> tipView.text = "工序:"
//                        3 -> tipView.text = "物料编码:"
//                        4 -> tipView.text = "名称:"
//                        5 -> tipView.text = "规格:"
//                        6 -> tipView.text = "型号:"
//                        7 -> tipView.text = "PNAME4:"
//                        8 -> tipView.text = "PNAME5:"
//                        9 -> tipView.text = "PNAME6:"
//                        10 -> tipView.text = "订单投产数量:"
//                        11 -> {
//                            tipView.also {
//                                it.text = "已完工数量(点击可修改):"
//                                it.setTextColor(ResUtil.getThemeColorAccent(mContext))
//                            }
//                        }
//                        12 -> {
//                            tipView.also {
//                                it.text = "返工数量(点击可修改):"
//                                it.setTextColor(ResUtil.getThemeColorAccent(mContext))
//                            }
//                        }
//                        13 -> tipView.text = "当前投产数量:"
//                        14 -> tipView.text = "订单数量:"
//                        15 -> tipView.text = "QTY6:"
//                        16 -> tipView.text = "QTY7:"
//                        17 -> tipView.text = "修改日期:"
//                        18 -> tipView.text = "增加日期:"
//                        19 -> tipView.text = "用户ID:"
//                        20 -> tipView.text = "用户名称:"
//                    }
//
//                    itemLayout.addView(tipView, LinearLayout.LayoutParams(-1, -2))
//                    itemLayout.addView(contentView, LinearLayout.LayoutParams(-1, -2))
//
//                    rootLayout.addView(itemLayout, LinearLayout.LayoutParams(-1, -2))
                    rootLayout.addView(contentView, LinearLayout.LayoutParams(120 * density().toInt(), -2))
                }

                return scrollView
            }

            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: OrderBean) {
                super.onBindDataView(holder, posInData, dataBean)
//                val numTipView: RTextView? = holder.tag("numTipView")
//                numTipView?.text = "序号:${posInData + 1}"

                if (holder.itemView is MyScrollView) {
                    postDelayed(16) {
                        holder.itemView.scrollTo(scrollX, 0)
                    }
                }

                if (posInData == 0) {
                    initItem(holder, "序号", "订单号:", "工序:", "物料编码:", "名称:", "规格:", "型号:", "已完工数量:", "返工数量:", "当前投产数量:", "订单数量:", "修改日期:", "用户名称:")
                    return
                }

                for (i in 0..12) {
                    val contentView: RTextView? = holder.tag("contentView$i")
                    contentView?.let {
                        when (i) {
                            0 -> it.text = if (dataBean.FVID.isNullOrEmpty()) "空" else dataBean.FVID
                            1 -> it.text = if (dataBean.DGID.isNullOrEmpty()) "空" else dataBean.DGID
                            2 -> it.text = if (dataBean.GXID.isNullOrEmpty()) "空" else dataBean.GXID
                            3 -> it.text = if (dataBean.PID.isNullOrEmpty()) "空" else dataBean.PID
                            4 -> it.text = if (dataBean.PNAME1.isNullOrEmpty()) "空" else dataBean.PNAME1
                            5 -> it.text = if (dataBean.PNAME2.isNullOrEmpty()) "空" else dataBean.PNAME2
                            6 -> it.text = if (dataBean.PNAME3.isNullOrEmpty()) "空" else dataBean.PNAME3
                            7 -> it.text = if (dataBean.PNAME4.isNullOrEmpty()) "空" else dataBean.PNAME4
                            8 -> it.text = if (dataBean.PNAME5.isNullOrEmpty()) "空" else dataBean.PNAME5
                            9 -> it.text = if (dataBean.PNAME6.isNullOrEmpty()) "空" else dataBean.PNAME6
                            10 -> it.text = if (dataBean.QTY1.isNullOrEmpty()) "空" else dataBean.QTY1
                            11 -> it.text = 0.toString() //if (dataBean.QTY2.isNullOrEmpty()) "空" else dataBean.QTY2
                            12 -> it.text = 0.toString() //if (dataBean.QTY3.isNullOrEmpty()) "空" else dataBean.QTY3
                            13 -> it.text = if (dataBean.QTY4.isNullOrEmpty()) "空" else dataBean.QTY4
                            14 -> it.text = if (dataBean.QTY5.isNullOrEmpty()) "空" else dataBean.QTY5
                            15 -> it.text = if (dataBean.QTY6.isNullOrEmpty()) "空" else dataBean.QTY6
                            16 -> it.text = if (dataBean.QTY7.isNullOrEmpty()) "空" else dataBean.QTY7
                            17 -> it.text = if (dataBean.DATE1.isNullOrEmpty()) "空" else dataBean.DATE1
                            18 -> it.text = if (dataBean.ADDDATE.isNullOrEmpty()) "空" else dataBean.ADDDATE
                            19 -> it.text = if (dataBean.USERID.isNullOrEmpty()) "空" else dataBean.USERID
                            20 -> it.text = if (dataBean.USERNAME.isNullOrEmpty()) "空" else dataBean.USERNAME
                        }
                    }
                }
//                for (i in 0..20) {
//                    val itemLayout: View? = holder.tag("itemLayout$i")
//                    when (i) {
//                        0, 7, 8, 9, 10, 15, 16, 18, 19 -> itemLayout?.visibility = View.GONE
//                    }
//                }
//
//                val itemLayout11: View? = holder.tag("itemLayout11")
//                val itemLayout12: View? = holder.tag("itemLayout12")
//
//                val clickListener = View.OnClickListener {
//                    if (dataBean.QTY4?.toInt()!! <= 0) {
//                        T_.error("没有投产数量.")
//                    } else {
//                        startIView(InputDialog(dataBean))
//                    }
//                }
//
//                itemLayout11?.setOnClickListener(clickListener)
//                itemLayout12?.setOnClickListener(clickListener)
            }

            private fun initItem(holder: RBaseViewHolder, s: String, s1: String, s2: String, s3: String, s4: String, s5: String, s6: String, s7: String, s8: String, s9: String, s10: String, s11: String, s12: String) {
                for (i in 0..12) {
                    val contentView: RTextView? = holder.tag("contentView$i")
                    contentView?.let {
                        when (i) {
                            0 -> it.text = s
                            1 -> it.text = s1
                            2 -> it.text = s2
                            3 -> it.text = s3
                            4 -> it.text = s4
                            5 -> it.text = s5
                            6 -> it.text = s6
                            7 -> it.text = s7
                            8 -> it.text = s8
                            9 -> it.text = s9
                            10 -> it.text = s10
                            11 -> it.text = s11
                            12 -> it.text = s12
                        }
                    }
                }
            }

        }
    }

    override fun onUILoadData(page: Int) {
        super.onUILoadData(page)

        if (BuildConfig.DEBUG) {
            val list = mutableListOf<OrderBean>()
            for (i in 0..30) {
                list.add(OrderBean("FVID: St",
                        "DGID: St",
                        "GXID: St",
                        "PID: Str",
                        "PNAME1: ",
                        "PNAME2: ",
                        "PNAME3: ",
                        "PNAME4: ",
                        "PNAME5: ",
                        "PNAME6: ",
                        "QTY1: St",
                        "QTY2: St",
                        "QTY3: St",
                        "QTY4: St",
                        "QTY5: St",
                        "QTY6: St",
                        "QTY7: St",
                        "DATE1: S",
                        "ADDDATE:",
                        "USERID: ",
                        "USERNAME"))
            }
            onUILoadFinish(list)
            return
        }

        Rx.base(object : RFunc<MutableList<OrderBean>>() {
            override fun onFuncCall(): MutableList<OrderBean> {
                return DbUtil.UP_GET_DGID(DGID/*, GXID*/)
            }
        }, object : RSubscriber<MutableList<OrderBean>>() {

            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                super.onEnd(isError, isNoNetwork, e)
                resetUI()
            }

            override fun onSucceed(bean: MutableList<OrderBean>) {
                super.onSucceed(bean)
                onUILoadFinish(bean)
            }
        })
    }

    private fun updateData(dialog: UIInputDialog,
                           position: Int,
                           orderBean: OrderBean,
                           QTY2: String?, QTY3: String?) {
        Rx.base(object : RFunc<Boolean>() {
            override fun onFuncCall(): Boolean {
                return DbUtil.UP_MODI_PUR01_D1(orderBean, QTY2, QTY3)
            }
        }, object : RSubscriber<Boolean>() {

            override fun onStart() {
                super.onStart()
                UILoading.show2(mILayout).setLoadingTipText("操作中...")
            }

            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                super.onEnd(isError, isNoNetwork, e)
                UILoading.hide()
            }

            override fun onSucceed(bean: Boolean) {
                super.onSucceed(bean)
                if (bean) {
                    dialog.finishDialog()

                    orderBean.QTY2 = QTY2
                    orderBean.QTY3 = QTY3

                    mExBaseAdapter.notifyItemChanged(position)
                    T_.ok("修改成功.")
                } else {
                    T_.error("修改失败.")
                }
            }
        })
    }
}
