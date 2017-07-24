package com.angcyo.xzproducems.iview

import android.Manifest
import android.os.Bundle
import android.view.View
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
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseItemUIView
import com.angcyo.xzproducems.bean.LoginBean
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-23.
 */
class MainUIView(val loginBean: LoginBean) : BaseItemUIView() {

    private var editText: ExEditText? = null

    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar().setShowBackImageView(false)
    }

    override fun getItemLayoutId(position: Int): Int {
        return when (position) {
            1 -> R.layout.item_main_control_layout
            else -> R.layout.view_main_layout
        }
    }

    override fun onViewCreate(rootView: View?, param: UIParam?) {
        super.onViewCreate(rootView, param)
    }

    override fun onViewShowNotFirst(bundle: Bundle?) {
        super.onViewShowNotFirst(bundle)
        mExBaseAdapter.notifyItemChanged(0)
    }

    override fun createItems(items: MutableList<SingleItem>?) {
        //显示, 输入信息, 二维码
        items?.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder?, posInData: Int, dataBean: Item?) {
                editText = holder?.v(R.id.edit_text)

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
                }

                //扫一扫
                holder?.delayClick(R.id.scan_button, object : DelayClick() {
                    override fun onRClick(view: View?) {
                        mActivity.checkPermissions(arrayOf(Manifest.permission.CAMERA)) { result ->
                            if (result) {
                                startIView(UIScanView { result ->
                                    holder.exV(R.id.edit_text).setInputText(result)
                                })
                            } else {
                                T_.error("需要摄像头权限.")
                            }
                        }
                    }
                })
            }
        })

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

                            startIView(QueryListUIView(it.string()))
                        }
                    }
                })
                //添加订单
                holder.delayClick(R.id.add_order_button, object : DelayClick() {
                    override fun onRClick(view: View?) {
                        startIView(AddOrderUIView())
                    }
                })
            }
        })

    }
}
