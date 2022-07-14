package com.xuanwu.ipaas.plugin.demo.spi;


import com.xuanwu.ipaas.plugin.sdk._enum.PluginErrorCode;
import com.xuanwu.ipaas.plugin.sdk.domain.Connection;
import com.xuanwu.ipaas.plugin.sdk.domain.ResultMap;
import com.xuanwu.ipaas.plugin.sdk.exception.PluginException;
import com.xuanwu.ipaas.plugin.sdk.spi.ActionSPI;
import com.xuanwu.ipaas.plugin.sdk.util.HttpClientUtils;
import com.xuanwu.ipaas.plugin.sdk.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DemoAction implements ActionSPI {

    /**
     * @param connection 连接：实现ConnectSPI接口中生成的连接，这里作为参数传入
     * @param input      行为入参：执行当前行为需要的参数，封装在一个map中传入，对应plugin.json中的input{}配置；
     *                   这里只管定义和使用，实际怎么传入在使用编排管理系统编排集成流时再考虑
     * @return 通过ResultMap.success()返回数据，对应plugin.json中的output{}配置
     */
    @Override
    public Map<String, Object> action(Connection connection, Map<String, Object> input) {
        try {
            log.info("接口入参： " + JsonUtils.toJSONString(input));
            Map numbers = (Map) input.get("numbers");
            log.info(String.valueOf(input.get("numbers").getClass()));
//            log.info("接口入参： " + JsonUtils.toJSONString(numbers.get("first")));
            int first = (int) numbers.get("first");
//            System.out.println(first);
            int second = (int) numbers.get("second");
            int res = first + second;
            HashMap<String, Integer> data = new HashMap<>();
            data.put("add", res);
//            data.put("add", 5);
//            System.out.println(data);
            return ResultMap.success(data);

        } catch (Throwable e) {
            return ResultMap.failed(PluginErrorCode.RESULT_EXCEPTION.getCode(), e.getMessage());
        }
    }

    public static void main(String[] args) {
        DemoAction dA = new DemoAction();
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> num = new HashMap<>();
        num.put("first", 2);
        num.put("second", 3);
        String s = JsonUtils.toJSONString(num);
        input.put("numbers", s);
        Connection con = null;
        Map<String, Object> action = dA.action(con, input);
        System.out.printf(String.valueOf(action));


    }
}

