package com.angcyo.xzproducems.utils

import net.sourceforge.jtds.jdbc.JtdsResultSet

/**
 * Created by angcyo on 2017-07-22.
 */
object DbUtil {
    fun test() {
        Jtds.init("112.29.171.138:21006", "bssjk", "xzsoft", "xzsoft")
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
}
