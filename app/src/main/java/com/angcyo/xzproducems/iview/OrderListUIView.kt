package com.angcyo.xzproducems.iview

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.angcyo.uiview.dialog.UIInputDialog
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.uiview.resources.ResUtil
import com.angcyo.uiview.utils.T_
import com.angcyo.uiview.widget.RTextView
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseRecycleUIView
import com.angcyo.xzproducems.bean.OrderBean
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-24.
 */
class OrderListUIView(val DGID: String, val GXID: Int /*工序*/) : BaseRecycleUIView<String, OrderBean, String>() {

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

                for (i in 0..20) {
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
                        0 -> tipView.text = "原订单明细表ID号:"
                        1 -> tipView.text = "订单号:"
                        2 -> tipView.text = "工序:"
                        3 -> tipView.text = "物料编码:"
                        4 -> tipView.text = "名称:"
                        5 -> tipView.text = "规格:"
                        6 -> tipView.text = "PNAME3:"
                        7 -> tipView.text = "PNAME4:"
                        8 -> tipView.text = "PNAME5:"
                        9 -> tipView.text = "PNAME6:"
                        10 -> tipView.text = "订单投产数量:"
                        11 -> {
                            tipView.also {
                                it.text = "已完工数量(点击可修改):"
                                it.setTextColor(ResUtil.getThemeColorAccent(mContext))
                            }
                        }
                        12 -> {
                            tipView.also {
                                it.text = "返工数量(点击可修改):"
                                it.setTextColor(ResUtil.getThemeColorAccent(mContext))
                            }
                        }
                        13 -> tipView.text = "当前投产数量:"
                        14 -> tipView.text = "订单数量:"
                        15 -> tipView.text = "QTY6:"
                        16 -> tipView.text = "QTY7:"
                        17 -> tipView.text = "修改日期:"
                        18 -> tipView.text = "增加日期:"
                        19 -> tipView.text = "用户ID:"
                        20 -> tipView.text = "用户名称:"
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

                for (i in 0..20) {
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
                            11 -> it.text = if (dataBean.QTY2.isNullOrEmpty()) "空" else dataBean.QTY2
                            12 -> it.text = if (dataBean.QTY3.isNullOrEmpty()) "空" else dataBean.QTY3
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

                val itemLayout11: View? = holder.tag("itemLayout11")
                val itemLayout12: View? = holder.tag("itemLayout12")

                itemLayout11?.setOnClickListener {
                    startIView(InputDialog(dataBean))
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
                }

                itemLayout12?.setOnClickListener {
                    startIView(InputDialog(dataBean))
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
                }

            }
        }
    }

    override fun onUILoadData(page: Int) {
        super.onUILoadData(page)
        Rx.base(object : RFunc<MutableList<OrderBean>>() {
            override fun onFuncCall(): MutableList<OrderBean> {
                return DbUtil.UP_GET_DGID(DGID, GXID)
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
