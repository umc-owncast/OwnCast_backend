package com.umc.owncast.domain.sentence.service;

import com.umc.owncast.domain.enums.Language;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//@Service
public class PapagoTranslationService implements TranslationService{
    @Value("${naver.cloud.id}")
    String id;
    @Value("${naver.cloud.secret}")
    String secret;

    @Override
    public String translateToKorean(String script) {
        String clientId = id;
        String clientSecret = secret;
        String text;
        try {
            text = URLEncoder.encode(script, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-NCP-APIGW-API-KEY-ID", clientId);
        requestHeaders.put("X-NCP-APIGW-API-KEY", clientSecret);

        long startTime = System.currentTimeMillis();
        String responseBody = post(requestHeaders, text);
        long endTime = System.currentTimeMillis();
        System.out.printf("PapagoTranslationService: Translation took %.2f seconds%n", (double)(endTime - startTime)/1000);
        JSONObject jsonObject = new JSONObject(responseBody);

        String translatedText = jsonObject.getJSONObject("message").getJSONObject("result").getString("translatedText");
        System.out.println(translatedText);
        return translatedText;
    }

    @Override
    public String translateToMemberLanguage(String script, Language language) {
        return null;
    }

    private static String post(Map<String, String> requestHeaders, String text) {
        HttpURLConnection con = connect("https://naveropenapi.apigw.ntruss.com/nmt/v1/translation");

        //todo user 완성후 언어 선택에서 끌어오기
        String postParams = "source=auto&target=ko&text=" + text;
        try {
            con.setRequestMethod("POST");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
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
