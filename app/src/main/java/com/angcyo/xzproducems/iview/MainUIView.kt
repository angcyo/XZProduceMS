package com.angcyo.xzproducems.iview

import android.Manifest
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
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseItemUIView
import com.angcyo.xzproducems.utils.DbUtil
import com.tbruyelle.rxpermissions.RxPermissions

/**
 * Created by angcyo on 2017-07-23.
 */
class MainUIView : BaseItemUIView() {

    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar().setShowBackImageView(false)
    }

    override fun getItemLayoutId(position: Int): Int {
        return R.layout.view_main_layout
    }

    var permission: RxPermissions? = null

    override fun onViewCreate(rootView: View?, param: UIParam?) {
        super.onViewCreate(rootView, param)
        permission = RxPermissions(mActivity)
    }

    override fun createItems(items: MutableList<SingleItem>?) {
        items?.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder?, posInData: Int, dataBean: Item?) {
                //数量
                Rx.base(object : RFunc<String>() {
                    override fun onFuncCall(): String {
                        return DbUtil.UP_WARN_QTY()
                    }
                }, object : RSubscriber<String>() {
                    override fun onStart() {
                        holder?.tv(R.id.text_view)?.text = "0"
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
                        startIView(WarnListDialog())
                    }

                })

                //扫一扫
                holder?.delayClick(R.id.scan_button, object : DelayClick() {
                    override fun onRClick(view: View?) {
                        permission?.request(Manifest.permission.CAMERA)?.subscribe { result ->
                            if (result) {
                                startIView(UIScanView { result ->
                                    holder?.exV(R.id.edit_text).setInputText(result)
                                })
                            } else {
                                T_.error("需要摄像头权限.")
                            }
                        }
                    }
                })
            }
        })
    }
}
