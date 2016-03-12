package com.arctouch.easybus.data;

import android.content.Context;

import com.arctouch.easybus.R;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Helper responsible for building the compatible RestTemplate and HttpHeaders to be used with the AppGlu REST API.
 */
public class ServiceHelper {

    private static final String APP_GLU_CUSTOM_HEADER_NAME = "X-AppGlu-Environment";
    private static final String APP_GLU_CUSTOM_HEADER_VALUE = "staging";

    public static RestTemplate buildRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> converters = Arrays.<HttpMessageConverter<?>>asList(
                new StringHttpMessageConverter(Charset.forName("UTF-8")),
                new GsonHttpMessageConverter()
        );
        restTemplate.setMessageConverters(converters);

        return restTemplate;
    }

    public static HttpHeaders buildHttpHeaders(Context context) {
        HttpHeaders httpHeaders = new HttpHeaders();

        String username = context.getString(R.string.app_glu_username);
        String password = context.getString(R.string.app_glu_password);
        httpHeaders.setAuthorization(new HttpBasicAuthentication(username, password));

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(APP_GLU_CUSTOM_HEADER_NAME, APP_GLU_CUSTOM_HEADER_VALUE);

        return httpHeaders;
    }

}
