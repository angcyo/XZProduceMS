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
        val PNAME3: String?, //型号
        val PNAME4: String?, //工序代码
        val PNAME5: String?, //订单子项备注
        val PNAME6: String?, //录完数据时备注QTY1--订单投产数量，
        val PNAME7: String?, //工序名称
        val QTY1: String?, //--订单数量，
        val QTY2: String?, //--工序1正在生产数量，
        val QTY3: String?, //--工序2正在生产数量，
        val QTY4: String?, //--工序3正在生产数量，
        val QTY5: String?, //--工序4正在生产数量，
        val QTY6: String?, //--工序5正在生产数量，
        val QTY7: String?, //--工序6正在生产数量，
        val QTY8: String? //--待出货数量
)


/**DGID --订单号
GXID --工序
PID --物料编码
PNAME1 --名称
PNAME2 --规格
PNAME3 --型号
PNAME4 NVARCHAR(250),工序代码
PNAME5 NVARCHAR(250),订单子项备注
PNAME6 NVARCHAR(250),录完数据时备注QTY1--订单投产数量，
PNAME7 NVARCHAR(250),工序名称，
QTY1--订单数量
QTY2--工序1正在生产数量，
QTY3--工序2正在生产数量，
QTY4--工序3正在生产数量，
QTY5--工序4正在生产数量，
QTY6--工序5正在生产数量，
QTY7--工序6正在生产数量，
QTY8--待出货数量
 */
