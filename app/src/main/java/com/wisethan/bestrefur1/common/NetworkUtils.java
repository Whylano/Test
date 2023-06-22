package com.wisethan.bestrefur1.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    // API 호출 및 JSON 데이터 가져오기
    public static String fetchJSONData(String apiUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //매개변수로 주어진 API URL을 사용하여 연결을 설정
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            //GET 요청 메서드를 설정
            connection.setRequestMethod("GET");
            //데이터 읽어옴
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            //예외
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();

                    //예외
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //stringBuilder 문자열반환
        return stringBuilder.toString();
    }
}

