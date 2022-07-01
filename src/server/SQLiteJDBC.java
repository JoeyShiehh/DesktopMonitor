package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SQLiteJDBC {
    static Connection c = null;
    static Statement stmt = null;

    public SQLiteJDBC() {
        createTable();
    }

    public static void createTable() {
        try {
            connect();
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS userinfo " +
                    "(NAME           TEXT    NOT NULL, " +
                    "PWD    TEXT    NOT NULL, " +
                    "IP   TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static void connect() {
        {
            c = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:db\\register.sqlite");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
            System.out.println("connect database successfully");
        }
    }

    public static void close() {
        try {
            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String name, String pwd,String address) {
        String sql = "INSERT INTO userinfo(NAME,PWD,IP) VALUES(?, ?,?)";

        try {
            connect();
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, pwd);
            pstmt.setString(3, address);
            pstmt.executeUpdate();
            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("insert successfully");
    }

    public boolean select(String name, String pwd) {
        try {
            connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM userinfo;");
            System.out.println("Select Operation done successfully");
            while (rs.next()) {
                System.out.println(rs.getString("NAME")+"::"+rs.getString("PWD"));
                if (rs.getString("NAME").equals(name) && rs.getString("PWD").equals(pwd)) {
                    rs.close();
                    close();
                    System.out.println("True");
                    return true;
                }
            }
            rs.close();
            close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }
    public List<String> selectAll() {
        try {
            connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM userinfo;");
            while (rs.next()) {
                Server.register_client.add(rs.getString("IP"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Select Operation done successfully");
        return new ArrayList<String>(Server.register_client);
    }
}
