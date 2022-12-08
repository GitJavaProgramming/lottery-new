package org.pp.http.client;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.protocol.HttpCoreContext;

/**
 * ************自强不息************
 * https://blog.csdn.net/zhongzh86/article/details/84070561?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-84070561-blog-103371615.pc_relevant_multi_platform_whitelistv3&spm=1001.2101.3001.4242.1&utm_relevant_index=3
 *
 * @author 鹏鹏
 * @date 2022/9/25 8:50
 * ************厚德载物************
 **/
public interface HttpClientEnvironment {
    default CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    default HttpContext httpContext() {
        return HttpCoreContext.create();
    }

    default RequestConfig requestConfig() {
        return RequestConfig.DEFAULT;
    }
}
