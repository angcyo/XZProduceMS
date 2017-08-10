package com.angcyo.xzproducems.iview

import com.angcyo.uiview.base.UIScanView
import com.angcyo.uiview.utils.T_

/**
 * Created by angcyo on 2017-08-02.
 */
class ScanUIView : UIScanView() {

    var toOrderList: Boolean = true

    override fun onBackPressed(): Boolean {
        return super.onBackPressed()
        //return false
    }

    override fun onHandleDecode(result: String) {
        //super.onHandleDecode(result)
        playBeepSoundAndVibrate()

        val orderId: String? = MainUIView.getOrderId(result)
        if (orderId.isNullOrEmpty()) {
            T_.error("不支持的二维码格式.")
            postDelayed(1000) {
                scanAgain()
            }
        } else {
            if (toOrderList) {
                replaceIView(OrderListUIView(orderId!!/*, LoginControl.gxid*/))
            } else {
                replaceIView(QueryListUIView(orderId!!))
            }
        }
    }
}
