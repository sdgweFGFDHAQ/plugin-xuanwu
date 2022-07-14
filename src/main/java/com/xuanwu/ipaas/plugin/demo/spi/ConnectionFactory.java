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

import java.util.Map;

@Slf4j
public class ConnectionFactory implements ConnectSPI {

     /**
     *
     * @param input 编排管理系统->应用账号管理 创建的账号所包含的字段，会封装为map传入该方法
     * @return 一个Connection对象 有三个属性：
     *          1. 连接对象
     *          2. 过期时间  到期毫秒数，设置为null代表永不过期(不建议)
     *          3. 连接类型，有两种：
     *                        ConnectType.CONNECT - 连接（如数据库连接）
     *                        ConnectType.SESSION - 会话（如接口鉴权token）
     */
    @Override
    public Connection getConnection(Map<String, Object> input) {
        try {
            log.info("参数： " + JsonUtils.toJSONString(input));

            String server = String.valueOf(input.get("server"));
            String username = String.valueOf(input.get("username"));
            String password = String.valueOf(input.get("password"));

            String response = HttpClientUtils.httpPost(server + "/api/auth/login", input, null);

            log.info("getConnection response = " + response);

            // 解析token
            String token = JsonUtils.get(response, "data").get("token").asText(); // token
            // 解析过期时间
            Long expired = JsonUtils.get(response, "data").get("tokenExpires").asLong(); // 过期时间
            // 自己生成一个

            // 返回会话连接
            return new Connection(token, expired, ConnectType.SESSION);
        } catch (Throwable e) {
            throw PluginException.asPluginException(PluginErrorCode.CONNECT_INIT_ERROR, "获取token失败 : ", e);
        }
    }

}
