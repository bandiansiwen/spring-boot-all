package com.imp.all.download.builder;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Longlin
 * @date 2023/11/29 15:48
 * @description
 */
public class RestTemplateBuilder {

    private int connectTimeout = 10000;
    private int readTimeout = 10000;
    private boolean enableSslCheck = false;

    public RestTemplateBuilder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public RestTemplateBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RestTemplateBuilder enableSslCheck(boolean enableSslCheck) {
        this.enableSslCheck = enableSslCheck;
        return this;
    }

    public static RestTemplateBuilder builder() {
        return new RestTemplateBuilder();
    }

    public RestTemplate build() {
        final RestTemplate restTemplate = new RestTemplate();

        // sslIgnore
        SimpleClientHttpRequestFactory requestFactory;
        if (!this.enableSslCheck) {
            requestFactory = getUnsafeClientHttpRequestFactory();
        }
        else {
            requestFactory = new SimpleClientHttpRequestFactory();
        }

        // timeout
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private SimpleClientHttpRequestFactory getUnsafeClientHttpRequestFactory() {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                }
            }
        };
    }
}
