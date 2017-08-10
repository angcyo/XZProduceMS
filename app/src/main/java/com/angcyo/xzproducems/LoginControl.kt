package com.angcyo.xzproducems

import com.angcyo.xzproducems.bean.GxBean
import com.angcyo.xzproducems.bean.LoginBean

/**
 * Created by angcyo on 2017-07-24.
 */
object LoginControl {
    var gxid: Int = 1 /*工序id*/
    var userid: String = ""
    var loginBean: LoginBean = LoginBean(0, 1)
    var gxList = mutableListOf<GxBean>()
    var gxBean = GxBean("99", "管理员")

    fun reset() {
        gxBean = GxBean("99", "管理员")
        gxList.clear()
        userid = ""
        loginBean = LoginBean(0, 1)
    }

    val PAD_WIDTH = 400
}
