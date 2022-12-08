package org.pp.http.client;

public class HttpClientException extends RuntimeException {

    public HttpClientException() {
        super();
    }

    public HttpClientException(final String s) {
        super(s);
    }

    public HttpClientException(final Throwable cause) {
        initCause(cause);
    }

    public HttpClientException(final String message, final Throwable cause) {
        super(message);
        initCause(cause);
    }
}
