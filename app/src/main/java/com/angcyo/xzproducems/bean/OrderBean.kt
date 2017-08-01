package com.angcyo.xzproducems.bean

/**
 * Created by angcyo on 2017-07-24.
 */
data class OrderBean(
        val FVID: String?, //--原订单明细表ID号
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
        val QTY2: String?, //--已完工数量，
        val QTY3: String?, //--返工数量，
        val QTY4: String?, //--当前投产数量即为还有多少未生产数量，
        val QTY5: String?, //--订单数量，
        val QTY6: String?, //--
        val QTY7: String?, //--
        val DATE1: String?, //--修改日期
        val ADDDATE: String?, //--增加日期
        val USERID: String?, //--用户ID
        val USERNAME: String? //--用户名称
)

//FVID--原订单明细表ID号
//DGID --订单号
//GXID --工序
//PID --物料编码
//PNAME1 --名称
//PNAME2 --规格
//PNAME3
//PNAME4
//PNAME5
//PNAME6
//@QTY1 INT,--订单投产数量
//@QTY2 INT,--已完工数量
//@QTY3 INT,--返工数量
//@QTY4 INT,--当前投产数量即为还有多少未生产数量
//QTY5--订单数量
//QTY6
//QTY7
//DATE1--修改日期
//ADDDATE--增加日期
//USERID--用户ID
//USERNAME--用户名称
