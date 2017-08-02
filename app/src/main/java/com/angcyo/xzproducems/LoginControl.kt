package com.angcyo.xzproducems

import com.angcyo.xzproducems.bean.LoginBean

/**
 * Created by angcyo on 2017-07-24.
 */
object LoginControl {
    var gxid: Int = 1 /*工序id*/
    var userid: String = ""
    var loginBean: LoginBean = LoginBean(0, 1)

    fun reset() {
        userid = ""
        loginBean = LoginBean(0, 1)
    }
}
