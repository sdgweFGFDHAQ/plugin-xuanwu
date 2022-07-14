package com.xuanwu.ipaas.plugin.demo.spi;


import com.xuanwu.ipaas.plugin.sdk._enum.PluginErrorCode;
import com.xuanwu.ipaas.plugin.sdk.domain.Connection;
import com.xuanwu.ipaas.plugin.sdk.domain.ResultMap;
import com.xuanwu.ipaas.plugin.sdk.exception.PluginException;
import com.xuanwu.ipaas.plugin.sdk.spi.ActionSPI;
import com.xuanwu.ipaas.plugin.sdk.util.HttpClientUtils;
import com.xuanwu.ipaas.plugin.sdk.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
            log.info(String.valueOf(numbers));
            Map first = (Map) numbers.get("first");
            log.info("第一个矩阵 " + JsonUtils.toJSONString(first));
            Map second = (Map) numbers.get("second");
            ArrayList a = (ArrayList) first.get("a");
            log.info("第一行数据 " + JsonUtils.toJSONString(a));
            ArrayList b = (ArrayList) first.get("b");
            log.info("第二行数据 " + JsonUtils.toJSONString(b));
            ArrayList c = (ArrayList) second.get("c");
            ArrayList d = (ArrayList) second.get("d");
            log.info("第er...二行数据 " + JsonUtils.toJSONString(c) + JsonUtils.toJSONString(d));
            int res = 0;
            for (int i = 0; i < 2; i++) {
                System.out.println(a.get(i));
                res += (int) a.get(i) * (int) c.get(i) + (int) b.get(i) * (int) d.get(i);
            }
            log.info("结果： " + JsonUtils.toJSONString(res));
            HashMap<String, Integer> data = new HashMap<>();
            data.put("add", res);
//            data.put("add", 5);
//            System.out.println(data);
            return ResultMap.success(data);

        } catch (Throwable e) {
            return ResultMap.failed(PluginErrorCode.RESULT_EXCEPTION.getCode(), e.getMessage());
        }
    }

//    public int[][] mapToArray(Map map) {
//        int[][] arr = new int[2][2];
//        //略
//        return arr;
//    }

    public static void main(String[] args) {
        DemoAction dA = new DemoAction();
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> num = new HashMap<>();
        Map<String, Object> ab = new HashMap<>();
        ArrayList a = new ArrayList();
        a.add(1);
        a.add(0);
        ArrayList b = new ArrayList();
        b.add(0);
        b.add(1);
        ab.put("a", a);
        ab.put("b", b);
        Map<String, Object> cd = new HashMap<>();
        ArrayList c = new ArrayList();
        c.add(1);
        c.add(0);
        ArrayList d = new ArrayList();
        d.add(0);
        d.add(1);
        cd.put("c", c);
        cd.put("d", d);
        num.put("first", ab);
        num.put("second", cd);
        input.put("numbers", num);
        Connection con = null;
        Map<String, Object> action = dA.action(con, input);
        System.out.printf(String.valueOf(action));
    }
}

