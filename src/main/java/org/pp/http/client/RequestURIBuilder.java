package org.pp.http.client;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * ************自强不息************
 *
 * @author 鹏鹏
 * @date 2022/9/25 16:13
 * ************厚德载物************
 **/
public interface RequestURIBuilder {

    /**
     * 通用钩爪Http请求URI的方法
     */
    static URI buildURI(String url, Map<String, String> param) throws HttpClientException, URISyntaxException, MalformedURLException {
        URIBuilder uriBuilder = new URIBuilder(new URI(url));

        List<NameValuePair> queryParams = uriBuilder.getQueryParams();
        param.forEach((k, v) -> queryParams.add(new BasicNameValuePair(k, v)));
        URI uri = uriBuilder.build();
        return uri;
    }
}
