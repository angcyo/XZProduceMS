package com.angcyo.xzproducems.bean

/**
 * Created by angcyo on 2017-07-24.
 */
data class QueryBean(
        val DGID: String?, //--订单号
        val GXID: String?, // --工序
        val PID: String?, // --物料编码
        val PNAME1: String?, // --名称
        val PNAME2: String?, // --规格
        val PNAME3: String?, //
        val PNAME4: String?, //
        val PNAME5: String?, //
        val PNAME6: String?, //
        val QTY1: String?, //--订单投产数量，
        val QTY2: String?, //--工序1正在生产数量，
        val QTY3: String?, //--工序2正在生产数量，
        val QTY4: String?, //--工序3正在生产数量，
        val QTY5: String?, //--工序4正在生产数量，
        val QTY6: String?, //--工序5正在生产数量，
        val QTY7: String?, //--待出货数量，
        val QTY8: String? //--订单数量
)
