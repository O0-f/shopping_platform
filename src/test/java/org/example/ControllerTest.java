package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.utils.EncryptUtils;
import org.junit.Test;

import java.security.Key;

public class ControllerTest {

    @Test
    public void signIn() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8080/shopping_platform_war_exploded/getPublicKey");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String json = EntityUtils.toString(entity);
        JSONObject resultMap = JSON.parseObject(json);
        String keyString = resultMap.getString("data");
        Key publicKey = EncryptUtils.getPublicKey(keyString);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("id", "user_id_test");
        jsonObject.put("password", "111");
        jsonObject.put("device_id", "device");
        jsonObject.put("device_type", "PC");
        String request = jsonObject.toJSONString();
        String result = EncryptUtils.RSAEncrypt(request, publicKey);
        System.out.println(result);
    }
}
