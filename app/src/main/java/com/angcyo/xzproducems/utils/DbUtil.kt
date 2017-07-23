package com.angcyo.xzproducems.utils

import com.angcyo.library.utils.L
import com.angcyo.xzproducems.bean.LoginBean
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
}
