package org.pp.http.client;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.RedirectStrategy;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;

import java.io.IOException;
import java.net.URI;

/**
 * ************自强不息************
 *
 * @author 鹏鹏
 * @date 2022/9/25 8:59
 * ************厚德载物************
 **/
public class DefaultHttpClientEnvironment implements org.pp.http.client.HttpClientEnvironment {
    private HttpClient httpClient = httpClient();
    private HttpContext httpContext = httpContext();
    private RequestConfig requestConfig = requestConfig();

    public static org.pp.http.client.HttpClientEnvironment create() {
        return new DefaultHttpClientEnvironment();
    }

    @Override
    public CloseableHttpClient httpClient() {
        // http协议拦截器
        HttpRequestInterceptor httpRequestInterceptor = new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {

            }
        };
        // 长短连接
        ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public TimeValue getKeepAliveDuration(
                    HttpResponse response,
                    HttpContext context) {
                TimeValue keepAlive = super.getKeepAliveDuration(response, context);
                if (!TimeValue.isNonNegative(keepAlive)) {
                    // Keep connections alive 5 seconds if a keep-alive value
                    // has not be explicitly set by the server
                    return TimeValue.ofSeconds(5);
                }
                return keepAlive;
            }

        };
        // 请求重试
        HttpRequestRetryStrategy retryStrategy = new DefaultHttpRequestRetryStrategy() {
            @Override
            public boolean retryRequest(HttpRequest request, IOException exception, int execCount, HttpContext context) {
                return super.retryRequest(request, exception, execCount, context);
            }

            @Override
            public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
                return super.retryRequest(response, execCount, context);
            }
        };
        // 重定向策略
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return super.isRedirected(request, response, context);
            }

            @Override
            public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException {
                return super.getLocationURI(request, response, context);
            }

            @Override
            protected URI createLocationURI(String location) throws ProtocolException {
                return super.createLocationURI(location);
            }
        };
        CloseableHttpClient httpclient = HttpClients.custom()
                .setKeepAliveStrategy(keepAliveStrat)
                .setRetryStrategy(retryStrategy)
                .setRedirectStrategy(redirectStrategy)
                .addRequestInterceptorLast(httpRequestInterceptor)
                .setDefaultCookieStore(new BasicCookieStore())
                .build();
        return httpclient;
    }

    public HttpContext httpContext() {
        return httpContext;
    }

    public RequestConfig requestConfig() {
        return requestConfig;
    }
}
