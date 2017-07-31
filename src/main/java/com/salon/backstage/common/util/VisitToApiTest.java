package com.salon.backstage.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class VisitToApiTest {

    public static void main(String[] args) {
        
        // "/address/eidAddressById.do?&userToken=123123&addressId=123456&nickName=杨帮东&city=北京市-朝阳区-立水桥&cityInfo=明天生活馆A座502&phone=13691528262&telephone=50052351&flag=0";
        //String a = "[{'commodityId' : '402880e855b4e05f0155b4e3331b0001','commodityName':'大扇贝','commodityStandard' : '100g','commodityPrice' : '9.9','number' : '1','storeId' : '2'},{'commodityId' :'402880e855b4c36c0155b4dd5f35000a','commodityName' : '小龙虾','commodityStandard' : '1000g','commodityPrice' : '499.9','number' : '3','storeId' : '2'}]";
        //String str = "/order/submitOrder.do?userToken=123123&addressId=123123&sendTime=2016-06-05 星期五&payType=1&sendWay=送货上门&couponId=123456&countText=这里是备注&commodityPrices=50&couponMoney=0&frightMoney=1.5&mustMoney=51.5&commodityInfos="+a;
      /*   String b = "[{'operatorName': '杨帮东','operatorPhone': '13691528262','IDcardNo': '231181199211283611','IDcardNoUrls': 'www.baidu.com/aa.png,www.bb.com/bb.png','headUrl':'www.baidu.com/touxiang.png','workingYears':'6','use':'0'},{'operatorName': '杨帮东','operatorPhone': '13691528262','IDcardNo': '231181199211283612','IDcardNoUrls': 'www.baidu.com/aa.png,www.bb.com/bb.png','headUrl':'www.baidu.com/touxiang.png','workingYears':'6','use':'1'},{'operatorName': '杨帮东','operatorPhone': '13691528262','IDcardNo': '231181199211283613','IDcardNoUrls': 'www.baidu.com/aa.png,www.bb.com/bb.png','headUrl':'www.baidu.com/touxiang.png','workingYears':'6','use':'1'}]";  
         String a = "[{'price':'5000','stand':'月'},{'price':'5000','stand':'台'},{'price':'5000','stand':'台班'}]";
        */ String str ="/recording/details.do?account=T972a96efe9b04b9082b205532e0499da&dataId=123";
         try {
            System.out.println(visitToApi(str));
        } catch (JSONException e) {

        }
    }

    /**
     * 
     * @return
     * @throws JSONException
     * @throws Exception
     */
    public static List<String> getField(String url) throws JSONException {
        List<String> keyListstr = new ArrayList<String>();
        JSONObject jsonObj = repPost(url);
        JSONArray jsonArry =JSONArray.fromObject(jsonObj);
        int iSize = jsonArry.size();
        if (iSize > 0) {
            jsonObj = jsonArry.getJSONObject(0);
            @SuppressWarnings("rawtypes")
            Iterator it = jsonObj.keys();
            while (it.hasNext()) {
                keyListstr.add(it.next().toString());
            }
        }
        return keyListstr;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public static JSONObject repPost(String url) {

        JSONObject jsonObj = new JSONObject();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpclient = httpClientBuilder.build();
        try {
            String str = String.format(url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(str));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                StringBuffer result = new StringBuffer();
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(entity.getContent()));
                    String text = "";
                    while ((text = bufferedReader.readLine()) != null) {
                        result.append(text);
                    }
                    jsonObj = JSONObject.fromObject(result);
                }
                EntityUtils.consume(entity);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            httpclient.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return jsonObj;
    }

    public static String visitToApi(String str) throws JSONException {

        String anchor = str.replaceAll(".*xianshenghui-api", "")
                .replaceAll("\\?.*", "");
        String test = str.replaceAll(".*\\?", "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("anchor", anchor);
        map.put("name", "接口标题");
        map.put("method", anchor);
        map.put("cid", 0);
        Map<String, Object> params = new HashMap<String, Object>();
        test = "&" + test;
        String[] tests = test.split("&");
        for (String string : tests) {
            if (string != null && string.trim().length() > 0) {
                params.put(string.replaceAll("=.*", ""),
                        "<require>[必选]</require>参数注释");
            }
        }
        map.put("params", JSONObject.fromObject(params));
        map.put("desc", "接口说明");
        map.put("test", test);

        Map<String, Object> returns = new HashMap<String, Object>();
        returns.put("code", "状态值");
        returns.put("errorMessage", "提示");
        returns.put("response", "返回的数据");
       // returns.put("result", "true-false");

        // List<String> list = getField(str);
        // for (String string : list) {
        // if(ParamUtils.chkString(string)){
        // returns.put(string, "<parent>[data]</parent>字段注释");}
        // }

        map.put("returns", JSONObject.fromObject(returns));
        JSONObject jSONObject = JSONObject.fromObject(map);
        return "bxsAPI.apis.push( "
                + jSONObject.toString().replaceAll("\\\\", "") + ");";

    }

}
