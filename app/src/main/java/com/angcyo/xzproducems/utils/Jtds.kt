package com.angcyo.xzproducems.utils

import com.angcyo.library.utils.L
import com.angcyo.uiview.utils.RUtils
import net.sourceforge.jtds.jdbc.JtdsCallableStatement
import net.sourceforge.jtds.jdbc.JtdsConnection
import net.sourceforge.jtds.jdbc.JtdsResultSet
import java.sql.DriverManager

/**
 * Created by angcyo on 2017-07-22.
 */
object Jtds {
    //    @Throws(SQLException::class, ClassNotFoundException::class)
//    fun getDb(): Connection {
//        val url = "jdbc:jtds:sqlserver://223.244.227.14:21006/OnDemand"//;charset=UTF-8
//        return connectDatabase(url, "xzsoft1", "xzsoft1")
//    }
//
//    @Throws(ClassNotFoundException::class, SQLException::class)
//    fun connectDatabase(db_connect_string: String, db_userid: String, db_password: String): Connection {
//        //jtds方式
//        Class.forName("net.sourceforge.jtds.jdbc.Driver")//net.sourceforge.jtds.jdbc.Driver
//        val conn = DriverManager.getConnection(db_connect_string, db_userid, db_password)
//        return conn
//    }

    /**数据库ip:端口, (223.244.227.14:21006)*/
    lateinit var host: String

    /**数据库名字*/
    lateinit var db_name: String

    /**数据库管理用户*/
    lateinit var db_user: String

    /**用户密码*/
    lateinit var db_pw: String

    /**使用之前,务必调用此方法, 配置基本参数*/
    fun init(host: String, db_name: String, db_user: String, db_pw: String) {
        this.host = host
        this.db_name = db_name
        this.db_user = db_user
        this.db_pw = db_pw
    }

    fun connectDB(): JtdsConnection {
        val url = "jdbc:jtds:sqlserver://$host/$db_name"//;charset=UTF-8
        Class.forName("net.sourceforge.jtds.jdbc.Driver")//net.sourceforge.jtds.jdbc.Driver
        val conn = DriverManager.getConnection(url, db_user, db_pw)
        return conn as JtdsConnection
    }

    /**
     * 执行存储过程, 并返回结果集
     * @param call 需要执行存储过程名
     * @param paramCount 存储过程需要的参数个数,不包括返回参数的个数
     * @param param 参数设置的回调方法, 用来设置参数. 当参数个数大于0时回调
     * @param result 存储过程执行的结果
     * */
    fun prepareCall_set(call: String, paramCount: Int,
                        param: ((JtdsCallableStatement) -> Unit)?,
                        result: (JtdsResultSet) -> Unit) {
        var connection: JtdsConnection? = null
        try {
            connection = Jtds.connectDB()

            val paramBuild = StringBuilder()
            for (i in 0..paramCount - 1) {
                paramBuild.append("?")
                paramBuild.append(",")
            }

            val jtdsCallableStatement = connection.prepareCall("{call $call(${RUtils.safe(paramBuild)})}") as JtdsCallableStatement
            if (paramCount > 0) {
                param?.invoke(jtdsCallableStatement)
            }

            val resultSet = jtdsCallableStatement.executeQuery() as JtdsResultSet
            result.invoke(resultSet)

            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.close()
        }
    }

    /**执行存储过程, 插入/更新数据*/
    fun prepareCall_update(call: String, paramCount: Int,
                           param: ((JtdsCallableStatement) -> Unit)?): Boolean {
        var connection: JtdsConnection? = null
        var result = false
        try {
            connection = Jtds.connectDB()

            val paramBuild = StringBuilder()
            for (i in 0 until paramCount) {
                paramBuild.append("?")
                paramBuild.append(",")
            }

            val jtdsCallableStatement = connection.prepareCall("{call $call(${RUtils.safe(paramBuild)})}") as JtdsCallableStatement
            if (paramCount > 0) {
                param?.invoke(jtdsCallableStatement)
            }

//            result = jtdsCallableStatement.execute()
//            L.e("call: prepareCall_update -> $result ")
            val update: Int
            try {
                update = jtdsCallableStatement.executeUpdate()
                result = update != 0
                L.e("call: prepareCall_update -> update:$update ")
            } catch (e: Exception) {
                result = jtdsCallableStatement.execute()
                L.e("call: prepareCall_update -> result:$result ")
            }
            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.close()
        }
        return result
    }
}
