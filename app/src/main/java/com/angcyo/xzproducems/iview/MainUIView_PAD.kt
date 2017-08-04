package com.angcyo.xzproducems.iview

import android.Manifest
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.base.UIScanView
import com.angcyo.uiview.container.UIParam
import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.T_
import com.angcyo.uiview.view.DelayClick
import com.angcyo.uiview.widget.ExEditText
import com.angcyo.xzproducems.BuildConfig
import com.angcyo.xzproducems.LoginControl
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseItemUIView
import com.angcyo.xzproducems.bean.LoginBean
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-23.
 */
class MainUIView_PAD(val loginBean: LoginBean) : BaseItemUIView() {

    private var editText: ExEditText? = null
    private var idEditText: ExEditText? = null

    companion object {
        fun getOrderId(result: String): String? {
            var orderId: String? = null

            if (result.startsWith("||")) {
                try {
                    orderId = result.substring(2).split("|")[1]
                } catch(e: Exception) {
                }
            }
            return orderId
        }
    }

    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar()
                .setShowBackImageView(false)
                .addRightItem(TitleBarPattern.TitleBarItem("关于我们") {
                    startIView(AboutMeUIView().setEnableClipMode(ClipMode.CLIP_BOTH))
                })
    }

    override fun getItemLayoutId(position: Int): Int {
        return when (position) {
            1 -> R.layout.item_main_control_layout_pad
            else -> R.layout.view_main_layout_pad
        }
    }

    override fun inflateBaseView(container: FrameLayout?, inflater: LayoutInflater?): View {
        val baseView = super.inflateBaseView(container, inflater)
        mBaseContentLayout.layoutParams = FrameLayout.LayoutParams(LoginControl.PAD_WIDTH * density().toInt(), -1).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        return baseView
    }

    override fun onViewCreate(rootView: View?, param: UIParam?) {
        super.onViewCreate(rootView, param)
    }

    override fun onViewShowNotFirst(bundle: Bundle?) {
        super.onViewShowNotFirst(bundle)
        mExBaseAdapter.notifyItemChanged(0)
    }

    override fun onViewHide() {
        super.onViewHide()
        LoginControl.gxid = idEditText!!.string().toInt()
    }

    override fun createItems(items: MutableList<SingleItem>?) {
        //显示, 输入信息, 二维码
        items?.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder?, posInData: Int, dataBean: Item?) {
                editText = holder?.v(R.id.edit_text)

                editText?.let {
                    if (BuildConfig.DEBUG) {
                        if (it.isEmpty) {
                            it.setInputText("XK-17070301")
                        }
                    }
                }

                //数量
                if (loginBean.IsModify == 1) {
                    //允许查看工时
                    Rx.base(object : RFunc<String>() {
                        override fun onFuncCall(): String {
                            return DbUtil.UP_WARN_QTY()
                        }
                    }, object : RSubscriber<String>() {
                        override fun onStart() {
                            if (holder?.tv(R.id.text_view)?.text.isNullOrEmpty()) {
                                holder?.tv(R.id.text_view)?.text = "0"
                            }
                        }

                        override fun onSucceed(bean: String?) {
                            super.onSucceed(bean)
                            bean?.let {
                                holder?.tv(R.id.text_view)?.text = it
                            }
                        }
                    })

                    //数量点击, 弹出详情
                    holder?.delayClick(R.id.text_view, object : DelayClick() {
                        override fun onRClick(view: View?) {
                            startIView(WarnListUIView())
                        }

                    })
                } else {
                    holder?.tv(R.id.text_view)?.tag = ""
                    holder?.tv(R.id.text_view)?.text = "没有权限查看."

                    holder?.v<View>(R.id.radio_button2)?.visibility = View.GONE
                }

                idEditText = holder?.v(R.id.id_text)
                idEditText?.let {
                    it.setInputText(loginBean.GXID.toString())
                    if (loginBean.GXID == 99) {
                        it.isEnabled = true
                        it.setInputText(LoginControl.gxid.toString())
                    } else {
                        it.isEnabled = false
                    }
                }

                //扫一扫
                holder?.delayClick(R.id.scan_button, object : DelayClick() {
                    override fun onRClick(view: View?) {
                        mActivity.checkPermissions(arrayOf(Manifest.permission.CAMERA)) { result ->
                            if (result) {
                                startIView(UIScanView { result ->
                                    holder.exV(R.id.edit_text).setInputText(result)

                                    val orderId: String? = getOrderId(result)
                                    if (orderId.isNullOrEmpty()) {
                                        T_.error("不支持的二维码格式.")
                                    } else {
                                        if (holder.v<RadioButton>(R.id.radio_button1)!!.isChecked) {
                                            LoginControl.gxid = idEditText!!.string().toInt()
                                            if (LoginUIView.isPad(mActivity)) {
                                                startIView(OrderListUIView_PAD(orderId!!, LoginControl.gxid))
                                            } else {
                                                startIView(OrderListUIView(orderId!!, LoginControl.gxid))
                                            }
                                        } else {
                                            if (LoginUIView.isPad(mActivity)) {
                                                startIView(QueryListUIView_PAD(orderId!!))
                                            } else {
                                                startIView(QueryListUIView(orderId!!))
                                            }
                                        }
                                    }
                                })
                            } else {
                                T_.error("需要摄像头权限.")
                            }
                        }
                    }
                })
            }
        })

        if (BuildConfig.DEBUG) {
            //控制按钮
            items?.add(object : SingleItem() {
                override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {
                    //查询订单情况
                    holder.delayClick(R.id.query_button, object : DelayClick() {
                        override fun onRClick(view: View?) {
                            editText?.let {
                                if (it.checkEmpty()) {
                                    return@let
                                }
                                startIView(QueryListUIView_PAD(it.string()))
                            }
                        }
                    })
                    //添加订单
                    holder.delayClick(R.id.add_order_button, object : DelayClick() {
                        override fun onRClick(view: View?) {
                            startIView(OrderListUIView_PAD(editText!!.string(), idEditText!!.string().toInt()))
                        }
                    })
                }
            })
        }
    }
}
