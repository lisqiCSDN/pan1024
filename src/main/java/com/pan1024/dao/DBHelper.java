package com.pan1024.dao;

import java.sql.*;
import java.util.List;

/**
 * @ClassName: DBHelper
 * @Date: 2019/6/6
 * @describe:
 */
public class DBHelper {

    public static final String driver_class = "com.mysql.jdbc.Driver";
    public static final String driver_url = "jdbc:mysql://localhost/pan1024?useunicode=true&characterEncoding=utf8";
    public static final String user = "root";
    public static final String password = "123456";

    private static Connection conn ;
    private PreparedStatement pst;
    private ResultSet rst;

    public DBHelper(){
        conn = DBHelper.getConnInstance();
    }

    /**
     * 创建数据库连接
     * 使用单例模式创建Connection对象，同时保持线程同步
     */
    private static synchronized Connection getConnInstance(){
        if(conn == null){
            try {
                Class.forName(driver_class);
                conn = DriverManager.getConnection(driver_url,user,password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Connection successful.");
        }
        return conn;
    }

    /**
     * close 断开数据库连接
     * */
    public void close(){
        try{
            if(conn != null)
                DBHelper.conn.close();
            if(pst != null)
                this.pst.close();
            if(rst != null)
                this.rst.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }



    /**
     * query sql语句运行,使用预定义模板进行
     * @return ResultSet
     */
    public ResultSet executeQuery(String sql, List<String> sqlValues) {
        try {
            pst = conn.prepareStatement(sql);
            if (sqlValues != null && sqlValues.size() > 0) {
                setSqlValues(pst, sqlValues);
            }
            rst = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rst;
    }
    /**
     * update sql语句运行,使用预定义模板进行
     * @return 执行结果状态码
     * */
    public int executeUpdate(String sqlTemplate,List<String> sqlValues){
        int resultCode = -1;
        try{
            pst = conn.prepareStatement(sqlTemplate);
            if(sqlValues != null && sqlValues.size()>0){
                setSqlValues(pst,sqlValues);
            }
            resultCode = pst.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultCode;
    }
    /**
     * 向预定义模板填充键值对
     * */
    private void setSqlValues(PreparedStatement pst,List<String> sqlValues){
        for(int i=0;i<sqlValues.size();i++){
            try {
                pst.setObject(i+1,sqlValues.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
