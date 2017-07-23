package com.angcyo.xzproducems.utils

import com.angcyo.library.utils.L
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

    fun login(name: String, pw: String): Int {
        var result: Int = 0

        Jtds.prepareCall_set("UP_GET_USERPOWER", 2,
                { jtdsCallableStatement ->
                    jtdsCallableStatement.setString("USERID", name)
                    jtdsCallableStatement.setString("PASSWORD", pw)
                },
                { jtdsResultSet ->
                    if (jtdsResultSet.next()) {
                        val GXID = jtdsResultSet.getInt("GXID")
                        L.e("call: login -> $GXID")
                        result = GXID
                    } else {
                        L.e("call: login -> 无数据")
                        result = -10_000
                    }
                })

        return result
    }
}
