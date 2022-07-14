package connectionSql;


import com.xuanwu.ipaas.plugin.sdk.util.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

public class SqlConnection {
public static Map<String, Object> input = new HashMap<>();

    public void responTest() {
        String respon = HttpClientUtils.httpPost("https://www.baidu.com", input, null);
    }

    public void mysqlConnect() {


    }
}
