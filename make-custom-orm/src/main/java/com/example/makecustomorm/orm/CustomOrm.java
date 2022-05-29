package com.example.makecustomorm.orm;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomOrm<T> {

    // 정보는 최소한 application properties 에서 불러와야함
    // 편의를 위해서 아래와 같이 비밀번호 아이디를 처리함
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:mem:testdb";
    static final String DB_ID = "sa";
    static final String DB_PWD = "";


    public static <T> CustomOrm<T> connect() {
        createBookTable();
        return new CustomOrm<T>();
    }

    // todo annotation 기반으로 바꿔보기
    private static void createBookTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            // JDBC Driver 등록
            Class.forName(JDBC_DRIVER);

            // DB 연결 생성
            connection = DriverManager.getConnection(DB_URL, DB_ID, DB_PWD);

            // Query 실행
            statement = connection.createStatement();
            String sql = "CREATE TABLE   Book " +
                    "(id INTEGER not NULL AUTO_INCREMENT, " +
                    " category NVARCHAR(255), " +
                    " title NVARCHAR(255), " +
                    " isbn NVARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            statement.executeUpdate(sql);

            // 연결 종료
            statement.close();
            connection.close();
        } catch (Exception e) {
            // error handling
            e.printStackTrace();
        } finally {
            // 자원 반환
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<T> read(Class<T> clss) {
        Field[] declaredFields = clss.getDeclaredFields();
        List<T> retList = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        try {
            // JDBC Driver 등록
            Class.forName(JDBC_DRIVER);

            // DB 연결 생성
            connection = DriverManager.getConnection(DB_URL, DB_ID, DB_PWD);

            // Query 실행
            statement = connection.createStatement();
            String sql = "SELECT * FROM " + clss.getSimpleName() + ";";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                // 입력받은 제너릭 클래스 생성
                T t = clss.getConstructor().newInstance();
                // Retrieve by column name
                Arrays.stream(declaredFields).forEach(field -> {
                            try {
                                if (field.isAnnotationPresent(CustomColumn.class)) {
                                    field.setAccessible(true);
                                    if (field.getType() == int.class) {
                                        field.set(t, rs.getInt(field.getName()));
                                    } else if (field.getType() == Long.class) {
                                        field.set(t, rs.getLong(field.getName()));
                                    } else if (field.getType() == String.class) {
                                        field.set(t, rs.getString(field.getName()));
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                );
                retList.add(t);
            }

            // 연결 종료
            statement.close();
            connection.close();
        } catch (Exception e) {
            // error handling
            e.printStackTrace();
        } finally {
            // 자원 반환
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return retList;
    }

    public boolean create(T t) {
        Connection connection = null;
        Statement statement = null;
        try {
            // 입력받은 제너릭 클래스 생성
            Class<? extends Object> clss = t.getClass();
            Field[] declaredFields = clss.getDeclaredFields();
            Field pk = null;

            StringBuffer sb = new StringBuffer();
            sb.append("INSERT INTO ").append(clss.getSimpleName()).append(" (");


            Arrays.stream(declaredFields).forEach(field -> {
                    if (field.isAnnotationPresent(CustomColumn.class)) {
                        field.setAccessible(true);
                        sb.append(field.getName()).append(",");
                    }
            });

            if (sb.charAt(sb.length() - 1) == ',') {
                sb.setLength(sb.length() - 1);
            }

            sb.append(")");

            sb.append(" VALUES (");

            Arrays.stream(declaredFields).forEach(field -> {
                try {
                    if (field.isAnnotationPresent(CustomId.class)) {
                        // PK는 넘김
                    }
                    else if (field.isAnnotationPresent(CustomColumn.class)) {
                        field.setAccessible(true);
                        sb.append("'").append(field.get(t)).append("'").append(",");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

            if (sb.charAt(sb.length() - 1) == ',') {
                sb.setLength(sb.length() - 1);
            }

            sb.append(");");


            System.out.println(sb);
            // JDBC Driver 등록
            Class.forName(JDBC_DRIVER);

            // DB 연결 생성
            connection = DriverManager.getConnection(DB_URL, DB_ID, DB_PWD);

            // Query 실행
            statement = connection.createStatement();
            statement.executeUpdate(sb.toString());

            // 연결 종료
            statement.close();
            connection.close();
        } catch (Exception e) {
            // error handling
            e.printStackTrace();
            return false;
        } finally {
            // 자원 반환
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
