package com.xuanwu.ipaas.plugin.demo.spi;


import com.xuanwu.ipaas.plugin.sdk._enum.ConnectType;
import com.xuanwu.ipaas.plugin.sdk._enum.PluginErrorCode;
import com.xuanwu.ipaas.plugin.sdk.domain.Connection;
import com.xuanwu.ipaas.plugin.sdk.domain.ResultMap;
import com.xuanwu.ipaas.plugin.sdk.exception.PluginException;
import com.xuanwu.ipaas.plugin.sdk.spi.ConnectSPI;
import com.xuanwu.ipaas.plugin.sdk.util.HttpClientUtils;
import com.xuanwu.ipaas.plugin.sdk.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConnectionFactory implements ConnectSPI {

    /**
     * @param input 编排管理系统->应用账号管理 创建的账号所包含的字段，会封装为map传入该方法
     * @return 一个Connection对象 有三个属性：
     * 1. 连接对象
     * 2. 过期时间  到期毫秒数，设置为null代表永不过期(不建议)
     * 3. 连接类型，有两种：
     * ConnectType.CONNECT - 连接（如数据库连接）
     * ConnectType.SESSION - 会话（如接口鉴权token）
     */
    @Override
    public Connection getConnection(Map<String, Object> input) {
        try {

            String ip = String.valueOf(input.get("address"));
            String port = String.valueOf(input.get("port"));
            String dataBaseName = String.valueOf(input.get("dataBaseName"));
            String username = String.valueOf(input.get("username"));
            String password = String.valueOf(input.get("password"));
            //String option = String.valueOf(input.get("option"));

            String url = "jdbc:postgresql://" + ip + ":" + port + "/" + dataBaseName; //+ "?" + option;
            log.info(url);
            java.sql.Connection conn = DriverManager.getConnection(url, username, password);
            return new Connection(conn, null, ConnectType.CONNECT);
        } catch (Throwable e) {
            throw PluginException.asPluginException(PluginErrorCode.CONNECT_INIT_ERROR, "连接失败失败 : ", e);
        }
    }

    public static void main(String[] args) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        map.put("address", "172.16.3.106");
        map.put("port", "15432");
        map.put("dataBaseName", "1");
        map.put("username", "postgres");
        map.put("password", "csb123456");
        //map.put("option", "");

        ConnectionFactory con = new ConnectionFactory();
        Connection c = con.getConnection(map);
        java.sql.Connection connection = (java.sql.Connection) c.getConnection();
        ResultSet rs = connection.createStatement().executeQuery("select * from t_taxi");
        while (rs.next()){
            System.out.println(rs.getInt(1));
        }
        System.out.println(rs);

    }
}
