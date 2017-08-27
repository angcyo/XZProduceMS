package com.angcyo.xzproducems.iview

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.angcyo.uiview.Root
import com.angcyo.uiview.dialog.UIInputDialog
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.resources.ResUtil
import com.angcyo.uiview.utils.RUtils
import com.angcyo.uiview.utils.T_
import com.angcyo.uiview.view.IView
import com.angcyo.uiview.view.OnUIViewListener
import com.angcyo.uiview.widget.RTextView
import com.angcyo.xzproducems.LoginControl
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseRecycleUIView
import com.angcyo.xzproducems.bean.OrderBean
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-24.
 */
class OrderListUIView(val DGID: String/*, val GXID: Int *//*工序*/) : BaseRecycleUIView<String, OrderBean, String>() {

    override fun onBackPressed(): Boolean {
        replaceIView(ScanUIView().apply {
            toOrderList = true
        })
        return false
    }

    override fun getTitleString(): String {
        return "生产汇报"
    }

    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar().addRightItem(TitleBarPattern.TitleBarItem("分享") {
            val filePath = Root.createFilePath()
            RUtils.saveRecyclerViewBitmap(filePath, mRecyclerView, Color.WHITE)
            RUtils.shareImage(mActivity, filePath, "分享结果")
        }.setVisibility(View.INVISIBLE))
    }

    private val rowCount = 11

    override fun createAdapter(): RExBaseAdapter<String, OrderBean, String> {
        return object : RExBaseAdapter<String, OrderBean, String>(mActivity) {
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

                for (i in 0..rowCount) {
                    val itemLayout = LinearLayout(mActivity)
                    itemLayout.apply {
                        orientation = LinearLayout.VERTICAL
                        tag = "itemLayout$i"
                        setBackgroundResource(R.drawable.base_bg_selector)
                    }

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
//                        0 -> tipView.text = "原订单明细表ID号:"
//                        1 -> tipView.text = "订单号:"
//                        2 -> tipView.text = "工序:"
                        0 -> tipView.text = "物料编码:"
                        1 -> tipView.text = "名称:"
                        2 -> tipView.text = "规格:"
                        3 -> tipView.text = "型号:"
                        4 -> tipView.text = "订单数量:"
                        5 -> tipView.text = "投产数量:"
                        6 -> tipView.text = "生产中数量:"
                        7 -> tipView.also {
                            it.text = "已完工数量(点击可修改):"
                            it.setTextColor(ResUtil.getThemeColorAccent(mContext))
                        }
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
                        8 -> tipView.also {
                            it.text = "备注(点击可修改):"
                            it.setTextColor(ResUtil.getThemeColorAccent(mContext))
                        }
                        9 -> tipView.text = "更新时间:"
                        10 -> tipView.text = "更新人:"
                        11 -> tipView.text = "上工序:"
//                        17 -> tipView.text = "修改日期:"
//                        18 -> tipView.text = "增加日期:"
//                        19 -> tipView.text = "用户ID:"
//                        20 -> tipView.text = "用户名称:"
                    }

                    itemLayout.addView(tipView, LinearLayout.LayoutParams(-1, -2))
                    itemLayout.addView(contentView, LinearLayout.LayoutParams(-1, -2))

                    rootLayout.addView(itemLayout, LinearLayout.LayoutParams(-1, -2))
                }

                return rootLayout
            }

            override fun onBindDataView(holder: RBaseViewHolder, posInData: Int, dataBean: OrderBean) {
                super.onBindDataView(holder, posInData, dataBean)
                val numTipView: RTextView? = holder.tag("numTipView")
                numTipView?.text = "序号:${posInData + 1}"

                for (i in 0..rowCount) {
                    val contentView: RTextView? = holder.tag("contentView$i")
                    contentView?.let {
                        when (i) {
                            0 -> it.text = QueryListUIView.getShowString(dataBean.PID)
                            1 -> it.text = QueryListUIView.getShowString(dataBean.PNAME1)
                            2 -> it.text = QueryListUIView.getShowString(dataBean.PNAME2)
                            3 -> it.text = QueryListUIView.getShowString(dataBean.PNAME3)
                            4 -> it.text = QueryListUIView.getShowString(dataBean.QTY5)
                            5 -> it.text = QueryListUIView.getShowString(dataBean.QTY1)
                            6 -> it.text = QueryListUIView.getShowString(dataBean.QTY4)
                            7 -> it.text = QueryListUIView.getShowString(dataBean.QTY2)
                            8 -> it.text = QueryListUIView.getShowString(dataBean.PNAME5)
                            9 -> it.text = QueryListUIView.getShowString(dataBean.DATE1)
                            10 -> it.text = if (dataBean.USERNAME.isNullOrEmpty()) "空" else dataBean.USERNAME
                            11 -> it.text = LoginControl.gxBean.PNAME7 //0.toString() //if (dataBean.QTY2.isNullOrEmpty()) "空" else dataBean.QTY2
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
//                for (i in 0..rowCount) {
//                    val itemLayout: View? = holder.tag("itemLayout$i")
//                    when (i) {
//                        5, 6 /*0, 7, 8, 9, 10, 15, 16, 18, 19*/ -> itemLayout?.visibility = View.GONE
//                    }
//                }

                val itemLayout7: View? = holder.tag("itemLayout7")
                val itemLayout8: View? = holder.tag("itemLayout8")

                val clickListener = View.OnClickListener {
                    if (dataBean.QTY1?.toFloat()!! <= 0) {
                        T_.error("没有投产数量.")
                    } else {
                        startIView(InputDialog(dataBean).setOnUIViewListener(object : OnUIViewListener() {
                            override fun onViewUnload(uiview: IView) {
                                super.onViewUnload(uiview)
                                notifyItemChanged(posInData)
                            }
                        }))
                    }
                }

                itemLayout7?.setOnClickListener(clickListener)
                itemLayout8?.setOnClickListener(clickListener)

//                itemLayout7?.setOnClickListener {
//                    mParentILayout.startIView(UIInputDialog().apply {
//                        dialogConfig = object : UIInputDialog.UIInputDialogConfig() {
//
//                            override fun onInitInputDialog(inputDialog: UIInputDialog,
//                                                           titleBarLayout: TitleBarLayout?,
//                                                           textInputLayout: TextInputLayout?,
//                                                           editText: ExEditText?,
//                                                           okButton: Button?) {
//                                editText?.apply {
//                                    hint = "请输入完工数量"
//                                    setIsPhone(true, 20)
//                                }
//
//                                okButton?.apply {
//                                    setOnClickListener {
//                                        if (!editText!!.checkEmpty()) {
//                                            updateData(inputDialog, posInData, dataBean, editText?.string(), dataBean.QTY3)
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//                    })
//                }

//                itemLayout8?.setOnClickListener {
//                    startIView(InputDialog(dataBean))
//                    mParentILayout.startIView(UIInputDialog().apply {
//                        dialogConfig = object : UIInputDialog.UIInputDialogConfig() {
//
//                            override fun onInitInputDialog(inputDialog: UIInputDialog,
//                                                           titleBarLayout: TitleBarLayout?,
//                                                           textInputLayout: TextInputLayout?,
//                                                           editText: ExEditText?,
//                                                           okButton: Button?) {
//                                editText?.apply {
//                                    hint = "请输入返工数量"
//                                    setIsPhone(true, 20)
//                                }
//
//                                okButton?.apply {
//                                    setOnClickListener {
//                                        if (!editText!!.checkEmpty()) {
//                                            updateData(inputDialog, posInData, dataBean, dataBean.QTY2, editText?.string())
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    })
//                }

            }
        }
    }

    override fun onUILoadData(page: Int) {
        super.onUILoadData(page)
        uiTitleBarContainer.hideRightItem(0)
        Rx.base(object : RFunc<MutableList<OrderBean>>() {
            override fun onFuncCall(): MutableList<OrderBean> {
                return DbUtil.UP_GET_DGID(DGID)
            }
        }, object : RSubscriber<MutableList<OrderBean>>() {

            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                super.onEnd(isError, isNoNetwork, e)
                resetUI()
            }

            override fun onSucceed(bean: MutableList<OrderBean>) {
                super.onSucceed(bean)
                onUILoadFinish(bean)
                uiTitleBarContainer.showRightItem(0)
            }
        })
    }

    private fun updateData(dialog: UIInputDialog,
                           position: Int,
                           orderBean: OrderBean,
                           QTY2: String?, QTY3: String?) {
        Rx.base(object : RFunc<Boolean>() {
            override fun onFuncCall(): Boolean {
                return DbUtil.UP_MODI_PUR01_D1(orderBean, QTY2, QTY3, "0", "0")
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
