package org.pp.http.client;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.pp.util.JacksonUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * ************自强不息************
 * 参考： https://www.jianshu.com/p/71dffcc56d33
 * HttpClient
 * HttpContext
 *
 * @author 鹏鹏
 * @date 2022/9/16 23:02
 * ************厚德载物************
 **/
public final class HttpUtil {
    private static final byte[] buffer = new byte[4094];
    private static final HttpClientEnvironment httpClientEnvironment = DefaultHttpClientEnvironment.create();
    /**
     * 两种使用方法
     */
    private static final HttpClientResponseHandler<String> stringResponseHandler = response -> EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    private static final Function<HttpEntity, String> strFunction = entity -> {
        try {
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new HttpClientException(e);
        } catch (ParseException e) {
            throw new HttpClientException(e);
        }
    };

    private static String getWithFunc(String url) throws Exception {
        return get(url, strFunction);
    }

    public static String get(String url) {
        return get(url, new HashMap<>());
    }

    public static String get(String url, Map<String, String> param) {
        try {
            return request(HttpClients.createDefault(), HttpGet.METHOD_NAME, url, param);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    public static String post(String url) {
        return post(url, new HashMap<>());
    }

    public static String post(String url, Map<String, String> param) {
        return post(url, param, false);
    }

    public static String post(String url, Map<String, String> param, boolean paramIntoBody) {
        try {
            return request(HttpClients.createDefault(), HttpPost.METHOD_NAME, url, param, paramIntoBody);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    private static String request(HttpClient client, String method, String url, Map<String, String> param) throws Exception {
        return request(client, method, url, param, false);
    }

    private static String request(HttpClient client, String method, String url, Map<String, String> param, boolean paramIntoBody) throws Exception {
        return client.execute(buildHttpRequest(method, url, param, paramIntoBody), stringResponseHandler);
    }
    //================================================================================================

    public static <T> T get(String url, Class<T> clazz) throws Exception {
        return get(url, getResponseHandler(clazz));
    }

    public static <T> T get(String url, Map<String, String> param, Class<T> clazz) throws Exception {
        return get(url, param, getResponseHandler(clazz));
    }

    public static <T> T get(String url, HttpClientResponseHandler<T> handler) throws Exception {
        return get(url, new HashMap<>(), handler);
    }

    public static <T> T get(String url, Map<String, String> param, HttpClientResponseHandler<T> handler) throws Exception {
        return request(HttpGet.METHOD_NAME, url, param, false, handler);
    }

    public static <T> T post(String url, Class<T> clazz) throws Exception {
        return post(url, getResponseHandler(clazz));
    }

    public static <T> T post(String url, HttpClientResponseHandler<T> handler) throws Exception {
        return post(url, new HashMap<>(), handler);
    }

    public static <T> T post(String url, Map<String, String> param, HttpClientResponseHandler<T> handler) throws Exception {
        return request(HttpPost.METHOD_NAME, url, param, false, handler);
    }

    public static <T> T post(String url, Map<String, String> param, boolean paramIntoBody, Class<T> clazz) throws Exception {
        return request(HttpPost.METHOD_NAME, url, param, paramIntoBody, clazz);
    }

    public static <T> T request(String method, String url, Map<String, String> param, Class<T> clazz) throws Exception {
        return request(method, url, param, false, clazz);
    }

    public static <T> T request(String method, String url, Map<String, String> param, boolean paramIntoBody, Class<T> clazz) throws Exception {
        return request(method, url, param, paramIntoBody, getResponseHandler(clazz));
    }

    public static <T> T request(String method, String url, Map<String, String> param, boolean paramIntoBody, HttpClientResponseHandler<T> handler) throws Exception {
        return request(HttpClients.createDefault(), method, url, param, paramIntoBody, handler);
    }

    public static <T> T request(HttpClient client, String method, String url, Map<String, String> param, boolean paramIntoBody, HttpClientResponseHandler<T> handler) throws Exception {
        return client.execute(buildHttpRequest(method, url, param, paramIntoBody), handler);
    }

    public static <R> R get(String url, Function<HttpEntity, R> convert) throws Exception {
        return get(url, new HashMap<>(), convert);
    }

    public static <R> R get(String url, Map<String, String> param, Function<HttpEntity, R> convert) throws Exception {
        return request(HttpClients.createDefault(), HttpGet.METHOD_NAME, url, param, false, convert);
    }

    public static <R> R get(String url, Map<String, String> param, boolean paramIntoBody, Function<HttpEntity, R> convert) throws Exception {
        return request(httpClient(), HttpGet.METHOD_NAME, url, param, paramIntoBody, convert);
    }

    public static <R> R get(HttpClient client, String url, Map<String, String> param, boolean paramIntoBody, Function<HttpEntity, R> convert) throws Exception {
        return request(client, HttpGet.METHOD_NAME, url, param, paramIntoBody, convert);
    }

    public static <R> R request(HttpClient client, String method, String url, Map<String, String> param, boolean paramIntoBody, Function<HttpEntity, R> convert) throws Exception {
        HttpUriRequestBase requestBase = buildHttpRequest(method, url, param, paramIntoBody);
        return client.execute(requestBase, consume(convert));
    }

    private static final <R> HttpClientResponseHandler<R> consume(Function<HttpEntity, R> convert) {
        return response -> {
            if (HttpStatus.SC_SUCCESS == response.getCode()) {
                return convert.apply(response.getEntity());
            }
            return null;
        };
    }

    private static final <T> HttpClientResponseHandler<T> getResponseHandler(final Class<T> clazz) {
        return response -> {
            if (HttpStatus.SC_SUCCESS == response.getCode()) {
                InputStream stream = response.getEntity().getContent();
                EntityUtils.consume(response.getEntity());
                int read = stream.read(buffer, 0, buffer.length);
                try {
                    // 方法一
//                    T o = JacksonUtil.nativeRead(stream, clazz); // 可以直接从流中读取对象
                    // 方法二
                    byte[] bytes = Arrays.copyOf(buffer, read);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer, 0, read);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    T o = (T) objectInputStream.readObject();
                    return o;
                } catch (ClassNotFoundException e) {
                    throw new HttpClientException(e);
                } finally {
                    for (int i = 0; i < read; i++) {
                        buffer[i] = 0;
                    }
                    stream.close();
                }
            }
            return null;
        };
    }

    public static URI buildURI(String url, Map<String, String> param) throws Exception {
        return RequestURIBuilder.buildURI(url, param);
    }

    public static void abortRequest(HttpUriRequestBase httpUriRequestBase) {
        httpUriRequestBase.abort();
    }

    /**
     * 构造请求
     *
     * @param method        请求方法
     * @param url           请求地址
     * @param param         请求参数
     * @param paramIntoBody 请求参数是否放进RequestBody
     * @return 返回请求
     * @throws Exception 抛出异常
     */
    public static HttpUriRequestBase buildHttpRequest(String method, String url, Map<String, String> param, boolean paramIntoBody) throws Exception {
        URI uri = buildURI(url, param);
        HttpUriRequestBase requestBase = new HttpUriRequestBase(method, uri);
        if (HttpPost.METHOD_NAME.equals(method) && paramIntoBody) {
            HttpEntity httpEntity = new StringEntity(JacksonUtil.toJson(param), ContentType.APPLICATION_JSON);
            requestBase.setEntity(httpEntity);
        }
        requestBase.setConfig(buildRequestConfig());
        return requestBase;
    }

    public static HttpUriRequestBase buildHttpRequest(String method, String url, Map<String, String> param) throws Exception {
        return buildHttpRequest(method, url, param, false);
    }

    public static HttpUriRequestBase addHeaders(HttpUriRequestBase requestBase, Map<String, Object> headers) {
        headers.forEach(requestBase::addHeader);
        return requestBase;
    }

    private static RequestConfig buildRequestConfig() {
        return httpClientEnvironment.requestConfig();
    }

    /**
     * HttpClient实现是线程安全的。建议将此类的同一个实例重用于多个请求执行。
     * HttpClient资源释放
     * 当不再需要实例CloseableHttpClient并且即将超出范围时，必须通过调用CloseableHttpClient＃close（）方法关闭与其关联的连接管理器。
     */
    public static CloseableHttpClient httpClient() {
        return httpClientEnvironment.httpClient();
    }

}
