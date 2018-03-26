package com.angcyo.xzproducems.iview

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
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
import com.angcyo.xzproducems.LoginControl
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
    override fun inflateDialogView(dialogRootLayout: FrameLayout, inflater: LayoutInflater): View {
        return inflate(R.layout.view_input_dialog)
    }

    override fun initDialogContentView() {
        super.initDialogContentView()
        val editText1: ExEditText = mViewHolder.v(R.id.edit_text_1)
        val editText2: ExEditText = mViewHolder.v(R.id.edit_text_2)

        val isover_box: CheckBox = mViewHolder.v(R.id.isover_box)
        val nstate_box: CheckBox = mViewHolder.v(R.id.nstate_box)

        isover_box.isChecked = LoginControl.is_over
//        editText1.setInputText(orderBean.QTY2)
//        editText2.setInputText(orderBean.PNAME5)

        val okButton: Button = mViewHolder.v(R.id.ok_button)

        editText1.setIsNumber(true, true, 40)
        editText2.setIsPhone(false, 40)

        showSoftInput(editText1)

        okButton.setOnClickListener {
            if (editText1.checkEmpty() /*|| editText2.checkEmpty()*/) {

            } /*else if ((editText1.string().toInt()) > orderBean.QTY1!!.toInt()) {
                T_.error("完工数量不能大于投产数量.")
            }*/ else {
                Rx.base(object : RFunc<Boolean>() {
                    override fun onFuncCall(): Boolean {
                        return DbUtil.UP_MODI_PUR01_D1(orderBean,
                                editText1.string(),
                                editText2.string(),
                                if (isover_box.isChecked) "1" else "0",
                                if (LoginControl.is_nstate) "1" else "0")
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
                        orderBean.QTY2 = editText1.string()
                        orderBean.PNAME5 = editText2.string()

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
