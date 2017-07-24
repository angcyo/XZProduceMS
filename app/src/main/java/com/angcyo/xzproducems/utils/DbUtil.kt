package com.angcyo.xzproducems.utils

import com.angcyo.library.utils.L
import com.angcyo.xzproducems.bean.LoginBean
import com.angcyo.xzproducems.bean.QueryBean
import net.sourceforge.jtds.jdbc.JtdsResultSet

/**
 * Created by angcyo on 2017-07-22.
 */
object DbUtil {
    fun test() {
        val connection = Jtds.connectDB()
        //L.e("call: test -> ")
        System.out.println("ok... ${connection.isClosed} $connection")

//        System.out.println("${connection.databaseMajorVersion} ${connection.rmHost} ${connection.warnings}")

        connection.createStatement()

        val call = connection.prepareCall("{call UP_QUERY_PUR01(?)}")
        //call.setInt()
        System.out.println("call... $call")

        call.setString(1, "1001")

//        call.registerOutParameter(2, Types.OTHER)

//        val execute = call.execute()
        val execute: JtdsResultSet = call.executeQuery() as JtdsResultSet
        execute.next()
        val PID = execute.getString("PID")
        val DGID = execute.getString("DGID")

        connection.close()


        System.out.println("ok...2 $execute $PID $DGID")

        test2()
    }

    fun test2() {
        System.out.println("test2...")
        val connection = Jtds.connectDB()
        val call = connection.prepareCall("{call UP_WARN_PUR01()}")
        val execute: JtdsResultSet = call.executeQuery() as JtdsResultSet
        execute.next()
        val WARNMEMO = execute.getString("WARNMEMO")
        connection.close()
        System.out.println("test2 ok...2 $execute $WARNMEMO")
    }

    /**登录*/
    fun login(name: String, pw: String): LoginBean {
        val result = LoginBean()
        Jtds.prepareCall_set("UP_GET_USERPOWER", 2,
                { jtdsCallableStatement ->
                    jtdsCallableStatement.setString("USERID", name)
                    jtdsCallableStatement.setString("PASSWORD", pw)
                },
                { jtdsResultSet ->
                    if (jtdsResultSet.next()) {
                        val GXID = jtdsResultSet.getInt("GXID")
                        val IsModify = jtdsResultSet.getInt("IsModify")
                        L.e("call: login -> $GXID $IsModify")
                        result.GXID = GXID
                        result.IsModify = IsModify
                    } else {
                        L.e("call: login -> 无数据")
                    }
                })

        return result
    }

    /**订单完工数量*/
    fun UP_WARN_QTY(): String {
        var result: String = "0"
        Jtds.prepareCall_set("UP_WARN_QTY", 0, null,
                { jtdsResultSet ->
                    if (jtdsResultSet.next()) {
                        val WARNQTY = jtdsResultSet.getString("WARNQTY")
                        L.e("call: UP_WARN_QTY -> $WARNQTY")
                        result = WARNQTY
                    } else {
                        L.e("call: UP_WARN_QTY -> 无数据")
                        result = "0"
                    }
                })

        return result
    }

    /**订单完工提醒*/
    fun UP_WARN_PUR01(): MutableList<String> {
        var result: MutableList<String> = mutableListOf()
        Jtds.prepareCall_set("UP_WARN_PUR01", 0, null,
                { jtdsResultSet ->
                    while (jtdsResultSet.next()) {
                        val WARNQTY = jtdsResultSet.getString("WARNMEMO")
                        L.e("call: UP_WARN_PUR01 -> $WARNQTY")
                        result.add(WARNQTY)
                    }
                })

        return result
    }

    /**查询订单完工状况*/
    fun UP_QUERY_PUR01(DGID: String): MutableList<QueryBean> {
        var result: MutableList<QueryBean> = mutableListOf()
        Jtds.prepareCall_set("UP_QUERY_PUR01", 1,
                { jtdsCallableStatement ->
                    jtdsCallableStatement.setString(1, DGID)
                },
                { jtdsResultSet ->
                    while (jtdsResultSet.next()) {
                        val bean = QueryBean(
                                jtdsResultSet.getString("DGID"),
                                jtdsResultSet.getString("GXID"),
                                jtdsResultSet.getString("PID"),
                                jtdsResultSet.getString("PNAME1"),
                                jtdsResultSet.getString("PNAME2"),
                                jtdsResultSet.getString("PNAME3"),
                                jtdsResultSet.getString("PNAME4"),
                                jtdsResultSet.getString("PNAME5"),
                                jtdsResultSet.getString("PNAME6"),
                                jtdsResultSet.getString("QTY1"),
                                jtdsResultSet.getString("QTY2"),
                                jtdsResultSet.getString("QTY3"),
                                jtdsResultSet.getString("QTY4"),
                                jtdsResultSet.getString("QTY5"),
                                jtdsResultSet.getString("QTY6"),
                                jtdsResultSet.getString("QTY7"),
                                jtdsResultSet.getString("QTY8")
                        )
                        L.e("call: UP_WARN_PUR01 -> $bean")
                        result.add(bean)
                    }
                })
        return result
    }

    /**新增修改订单数据*/
    fun UP_MODI_PUR01_D1(
            DGID: String, //--订单号
            GXID: String, //--工序
            PID: String, //--物料编码
            PNAME1: String, //--名称
            PNAME2: String, //--规格
            PNAME3: String, //
            PNAME4: String, //
            PNAME5: String, //
            PNAME6: String, //
            QTY1: String, //--订单投产数量
            QTY2: String, //--已完工数量
            QTY3: String, //--返工数量
            QTY4: String, //--当前投产数量即为还有多少未生产数量
            QTY5: String, //--订单数量
            QTY6: String, //
            QTY7: String, //
            USERID: String//--更新人
    ): Boolean {
        var result = Jtds.prepareCall_update("UP_MODI_PUR01_D1", 17,
                { jtdsCallableStatement ->
                    jtdsCallableStatement.setString("@DGID", DGID)
                    jtdsCallableStatement.setInt("@GXID", GXID.toInt())
                    jtdsCallableStatement.setString("@PID", PID)
                    jtdsCallableStatement.setString("@PNAME1", PNAME1)
                    jtdsCallableStatement.setString("@PNAME2", PNAME2)
                    jtdsCallableStatement.setString("@PNAME3", PNAME3)
                    jtdsCallableStatement.setString("@PNAME4", PNAME4)
                    jtdsCallableStatement.setString("@PNAME5", PNAME5)
                    jtdsCallableStatement.setString("@PNAME6", PNAME6)
                    jtdsCallableStatement.setInt("@QTY1", QTY1.toInt())
                    jtdsCallableStatement.setInt("@QTY2", QTY2.toInt())
                    jtdsCallableStatement.setInt("@QTY3", QTY3.toInt())
                    jtdsCallableStatement.setInt("@QTY4", QTY4.toInt())
                    jtdsCallableStatement.setInt("@QTY5", QTY5.toInt())
                    jtdsCallableStatement.setInt("@QTY6", QTY6.toInt())
                    jtdsCallableStatement.setInt("@QTY7", QTY7.toInt())
                    jtdsCallableStatement.setString("@USERID", USERID)
                })
        return result
    }

    fun demo() {

    }

}
