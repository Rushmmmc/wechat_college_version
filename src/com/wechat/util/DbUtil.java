package com.wechat.util;


import com.wechat.dao.impl.UserDaoImpl;

import java.sql.*;

public class DbUtil {


    private static String url = "jdbc:mysql://www.rushmc.top:3306/db_wechat?useUnicode=true&characterEncoding=utf8";
    private static String username = "root";
    private static String password = "rush2017";
    private static String jdbcname = "com.mysql.cj.jdbc.Driver";
    public static PreparedStatement pstmt = null;
    public static Connection connection = null;
    public static ResultSet resultSet = null;

    static {
        try {
            Class.forName(jdbcname);
        } catch (ClassNotFoundException e) {
            System.out.println("加载数据库连接失败");
            e.printStackTrace();
        }
    }

    /**
     * 数据库的更新操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static boolean executeUpdate(String sql, Object[] params) {
        try {
            pstmt = createPreParedStatement(sql, params);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeAll(null, pstmt, connection);
        }
    }

    /**
     * 关闭所有连接数据库的通道
     *
     * @param rs
     * @param stmt
     * @param connection
     */
    public static void closeAll(ResultSet rs, Statement stmt, Connection connection) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取与数据库的连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public static PreparedStatement createPreParedStatement(String sql, Object[] params) throws ClassNotFoundException, SQLException {
        pstmt = getConnection().prepareStatement(sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }
        return pstmt;
    }

    /**
     * 获取结果集
     *
     * @param sql
     * @param params
     * @return
     */
    public static ResultSet executeQuery(String sql, Object[] params) {
        try {
            pstmt = createPreParedStatement(sql, params);
            resultSet = pstmt.executeQuery();
            return resultSet;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户对应的字段的数量
     *
     * @param params
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int getNumber(Object[] params, String sql) throws SQLException {
        ResultSet resultSet = DbUtil.executeQuery(sql, params);
        int pages = 1;
        while (resultSet.next()) {
            pages = resultSet.getInt("count(*)");
        }
        if (pages < 1) {
            pages = 1;
        }
        DbUtil.closeAll(resultSet, DbUtil.pstmt, DbUtil.connection);
        return pages;
    }

    /**
     * 获取用户的ID
     *
     * @param loginId
     * @return
     */
    public static int getUserId(String loginId) {
        int userId = -1;
        try {
            userId = new UserDaoImpl().getUserId(loginId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
