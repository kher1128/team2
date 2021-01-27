package com.youngwoo.teamtwo;

import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CrawlingController {
    @RequestMapping(value = "/title", method= RequestMethod.GET)
    @ResponseBody() // JSON
    public List getTitle(@RequestParam(value="company_name") String company_name) {
        String clientId = "rhncl5RCIR2VT3kXEejE"; //애플리케이션 클라이언트 아이디값"
        String clientSecret = "WBmajfWSrI"; //애플리케이션 클라이언트 시크릿값"

        String text = null;
        try {
            text = URLEncoder.encode(company_name, "UTF-8"); // company_name에 회사이름 넣으면 utf-8변환되서 검색에 사용
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + text + "&sort=sim&display=5"; // 5개만 추출, 연관성 위주로 정렬
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret); // 헤더 인증id 추가
        String responseBody = get(apiURL,requestHeaders);    // API 호출

        JSONObject Obj = new JSONObject(responseBody);
        JSONArray Arr = Obj.getJSONArray("items");
        List list = new ArrayList();

        for(int i=0;i<Arr.length();i++){     // List에 기사 제목 추가
            Map result = new HashMap<String, String>();
            JSONObject targetObj = Arr.getJSONObject(i);
            String title = targetObj.get("title").toString();
            title = title.replace("<b>", "");
            title = title.replace("</b>", "");
            result.put("title", title);
            list.add(result);
        }
        return list;
    }
    private static String get(String apiUrl, Map<String, String> requestHeaders){ // API 호출해서 데이터 가져오기
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){ // HTTP 연결
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){ // API 결과로부터 데이터 읽어오기
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
