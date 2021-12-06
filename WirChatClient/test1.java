package WirChat.WirChatClient;

import WirChat.WirChatSever.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class test1 {
    public static void main(String[] args) {


        Connection connection = null;
        PreparedStatement prestatement = null;
        ResultSet resultSet = null;


        HashMap<String, String> map = null;
        try {
            connection = JdbcUtils.getConnection();
            String sql = "SELECT id,PASSWORD FROM ACCOUNT";
            prestatement = connection.prepareStatement(sql);
            resultSet = prestatement.executeQuery();
            map = new HashMap<>();
            while (resultSet.next()) {
                System.out.println("索引1：" + resultSet.getObject(1));
                System.out.println("索引2：" + resultSet.getObject(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                JdbcUtils.release(connection, prestatement, resultSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

