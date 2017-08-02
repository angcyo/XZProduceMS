package com.angcyo.xzproducems.iview

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.angcyo.uiview.dialog.UIInputDialog
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.utils.T_
import com.angcyo.uiview.widget.Button
import com.angcyo.uiview.widget.ExEditText
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.bean.OrderBean
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2017/08/02 13:28
 * 修改人员：Robi
 * 修改时间：2017/08/02 13:28
 * 修改备注：
 * Version: 1.0.0
 */
class InputDialog(val orderBean: OrderBean) : UIInputDialog() {
    override fun inflateDialogView(dialogRootLayout: FrameLayout?, inflater: LayoutInflater?): View {
        return inflate(R.layout.view_input_dialog)
    }

    override fun initDialogContentView() {
        super.initDialogContentView()
        val editText1: ExEditText = mViewHolder.v(R.id.edit_text_1)
        val editText2: ExEditText = mViewHolder.v(R.id.edit_text_2)

        val okButton: Button = mViewHolder.v(R.id.ok_button)

        editText1.setIsPhone(true, 40)
        editText2.setIsPhone(true, 40)

        showSoftInput(editText1)

        okButton.setOnClickListener {
            if (editText1.checkEmpty() || editText2.checkEmpty()) {

            } else {
                Rx.base(object : RFunc<Boolean>() {
                    override fun onFuncCall(): Boolean {
                        return DbUtil.UP_MODI_PUR01_D1(orderBean, editText1.string(), editText2.string())
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
                            finishDialog()
                            T_.ok("修改成功.")
                        } else {
                            T_.error("修改失败.")
                        }
                    }
                })
            }
        }
    }

}