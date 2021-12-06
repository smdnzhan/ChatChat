package WirChat.WirChatSever;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {
    private  static String driver = null;
    private  static String url = null;
    private  static String name = null;
    private static String password = null;
    static {
        try {
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties(); //加载
            properties.load(in);

            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            name = properties.getProperty("username");
            password = properties.getProperty("password");
            //驱动只用加载一次
            Class.forName(driver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        //获取连接
        public static Connection getConnection() throws SQLException{
                return DriverManager.getConnection(url,name,password);
        }

        public static void release(Connection connection, Statement statement, ResultSet resultSet) throws Exception{
        if (resultSet!=null){resultSet.close();}
        if (statement!=null){statement.close();}
        if (connection!=null){connection.close(); }
        }
}
