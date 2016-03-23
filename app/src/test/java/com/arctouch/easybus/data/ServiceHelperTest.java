package com.arctouch.easybus.data;

import android.content.Context;

import com.arctouch.easybus.R;
import com.arctouch.easybus.data.ServiceHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;
import static org.mockito.Mockito.when;

/**
 * Tests for the HttpHeaders creation and REST consuming general settings.
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceHelperTest {

    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";

    @Mock
    private Context context;

//    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    @Before
    public void setup() {
        when(context.getString(R.string.app_glu_username)).thenReturn(TEST_USERNAME);
        when(context.getString(R.string.app_glu_password)).thenReturn(TEST_PASSWORD);
        httpHeaders = ServiceHelper.buildHttpHeaders(context);
//        restTemplate = ServiceHelper.buildRestTemplate();
    }

    @Test
    public void testAuthenticationHandlerValidity() {
        String authorization = httpHeaders.getAuthorization();

        String credentials = TEST_USERNAME + ":" + TEST_PASSWORD;
        String encodedCredentials = Base64Utils.encodeToString(credentials.getBytes());

        assertThat(authorization, equalTo("Basic " + encodedCredentials));
    }

    @Test
    public void contentTypeShouldBeJson() {
        assertThat(httpHeaders.getContentType(), equalTo(MediaType.APPLICATION_JSON));
    }

    @Test
    public void appGluCustomHeaderShouldBePresent() {
        List<String> customHeader = httpHeaders.get("X-AppGlu-Environment");
        assertThat(customHeader, hasSize(1));
        assertThat("staging", isIn(customHeader));
    }

    // Apparently RestTemplate needs the Android platform in its creation, so I can't test it here :(
//    @Test
//    public void stringAndGsonConvertersShouldBePresent() {
//        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
//        assertThat(converters, hasSize(2));
//        assertThat(new StringHttpMessageConverter(), isIn(converters));
//        assertThat(new GsonHttpMessageConverter(), isIn(converters));
//    }

}
