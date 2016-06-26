package com.fisglobal.emea.common.http;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {

    private final static HttpClientFactory INSTANCE = new HttpClientFactory();

    public static HttpClientFactory getInstance() {
        return INSTANCE;
    }

    public CloseableHttpClient buildHttpClient(HttpClientConfig config) throws HttpCustomClientException {

        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create()
                .register(Protocol.HTTP.name(), PlainConnectionSocketFactory.INSTANCE)
                .register(Protocol.HTTPS.name(), buildSSLConnectionSocketFactory(config));

        Registry<ConnectionSocketFactory> socketFactoryRegistry = registryBuilder.build();
        
        
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        HttpClientBuilder httpBuilder = HttpClients.custom().disableAuthCaching()
                .disableAutomaticRetries()
                .disableCookieManagement()
                .disableContentCompression()
                .setKeepAliveStrategy(buildKeepAlipveStrategy(config))
                .setDefaultCredentialsProvider(credentialsProvider);

        configureConnectionManager(config, socketFactoryRegistry, httpBuilder);
        
        configureDefaultRequestConfig(config, httpBuilder);

        configureProxy(config, credentialsProvider, httpBuilder);

        return httpBuilder.build();
    }

    private void configureDefaultRequestConfig(HttpClientConfig config, HttpClientBuilder httpBuilder) {
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(config.getReadTimeout())
                .setStaleConnectionCheckEnabled(config.isUseStaleConnectionCheck())
                .setConnectTimeout((int) config.getConnectionTimeout())
                .setConnectionRequestTimeout((int) config.getReadTimeout())
                .build();
        httpBuilder.setDefaultRequestConfig(defaultRequestConfig);
    }

    private void configureConnectionManager(HttpClientConfig config, Registry<ConnectionSocketFactory> socketFactoryRegistry,
            HttpClientBuilder httpBuilder) {
        
        
        PoolingHttpClientConnectionManager connManager = null;
        if (config.getMaxConnectionAge() > 0) {
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, null, null, null,
                    config.getMaxConnectionAge(), TimeUnit.MILLISECONDS);
        } else {
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        }
        connManager.setMaxTotal(config.getMaxConnections());
        connManager.setDefaultMaxPerRoute(config.getMaxConnections());
        
        httpBuilder.setConnectionManager(connManager);
    }

    private void configureProxy(final HttpClientConfig config, CredentialsProvider credentialsProvider,
            HttpClientBuilder httpBuilder) throws HttpCustomClientException {
        
        try {
            if (isNotBlank(config.getProxy()) && !config.isBypassProxy()) {
                URL proxyUrl = new URL(config.getProxy());
                HttpHost proxyHost = new HttpHost(proxyUrl.getHost(), proxyUrl.getPort(), proxyUrl.getProtocol());
                httpBuilder.setProxy(proxyHost);
                
                if(isNotBlank(config.getProxyUser()) && isNotBlank(config.getProxyPassword())){
                    credentialsProvider.setCredentials(new AuthScope(proxyHost), new UsernamePasswordCredentials(config.getProxyUser(), config.getProxyPassword()));
                }
                
                if(isNotBlank(config.getBypassProxyPatterns())){
                    HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHost) {
                        @Override
                        public HttpRoute determineRoute(
                                final HttpHost host,
                                final HttpRequest request,
                                final HttpContext context) throws HttpException {
                            String hostname = host.getHostName();
                            String[] patterns = config.getBypassProxyPatterns().split(",");
                            for(String pattern : patterns){
                                if (hostname.matches(pattern)) {
                                    // Return direct route
                                    return new HttpRoute(host);
                                }
                            }

                            return super.determineRoute(host, request, context);
                        }
                    };
                    
                    httpBuilder.setRoutePlanner(routePlanner);
                }
                
            }
        } catch (MalformedURLException mue) {
            throw new HttpCustomClientException("HttpConnector malformed proxy URL " + config.getProxy(), mue);
        }
        
        
    }

    private ConnectionKeepAliveStrategy buildKeepAlipveStrategy(final HttpClientConfig config) {
        return new ConnectionKeepAliveStrategy() {
            
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                if (config.getMaxConnectionIdleTime() == -1) {
                    return DefaultConnectionKeepAliveStrategy.INSTANCE.getKeepAliveDuration(response, context);
                }
                return config.getMaxConnectionIdleTime();
            }
        };
    }

    private ConnectionSocketFactory buildSSLConnectionSocketFactory(HttpClientConfig config)
            throws HttpCustomClientException {
        if (isBlank(config.getTrustStore()) && isBlank(config.getKeyStore())) {
            return SSLConnectionSocketFactory.getSystemSocketFactory();
        }
        
        SSLContextBuilder sslContextBuilder = SSLContexts.custom().useTLS();

        configureTrustStore(sslContextBuilder, config);

        configureKeyStore(sslContextBuilder, config);
        
        try {
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                    SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
            return sslsf;
        } catch (Exception ex) {
            throw new HttpCustomClientException(String.format(
                    "HttpConnector Error Building SSLContext, TrustStore %s,  KeyStore %s",
                    config.getTrustStore(), config.getKeyStore()), ex);

        }
    }

    private void configureKeyStore(SSLContextBuilder sslContextBuilder, HttpClientConfig config)
            throws HttpCustomClientException {
        if (isNotBlank(config.getKeyStore())) {
            if (isBlank(config.getKeyStorePswd())) {
                throw new HttpCustomClientException("HttpConnector KeyStore password is not configured");
            }

            FileInputStream ksStream = null;
            try {
                KeyStore privateKs = KeyStore.getInstance("jks");
                ksStream = new FileInputStream(new File(config.getKeyStore()));
                privateKs.load(ksStream, config.getKeyStorePswd().toCharArray());
                sslContextBuilder.loadKeyMaterial(privateKs, config.getKeyStoreCertPswd().toCharArray());
            } catch (FileNotFoundException fne) {
                throw new HttpCustomClientException(String.format("HttpConnector KeyStore %s not found",
                        config.getKeyStore()), fne);
            } catch (Exception ex) {
                throw new HttpCustomClientException(String.format(
                        "HttpConnector KeyStore Error, KeyStore %s", config.getKeyStore()), ex);
            } finally {
                IOUtils.closeQuietly(ksStream);
            }
        }
    }

    private void configureTrustStore(SSLContextBuilder sslContextBuilder, HttpClientConfig config)
            throws HttpCustomClientException {
        if (isNotBlank(config.getTrustStore())) {

            if (isBlank(config.getTrustStorePswd())) {
                throw new HttpCustomClientException("HttpConnector TrustStore password is not configured");
            }

            FileInputStream ksStream = null;
            try {
                KeyStore trustStore = KeyStore.getInstance("jks");
                ksStream = new FileInputStream(new File(config.getTrustStore()));
                trustStore.load(ksStream, config.getTrustStorePswd().toCharArray());
//                sslContextBuilder = sslContextBuilder.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());
                sslContextBuilder.loadTrustMaterial(trustStore);
            } catch (FileNotFoundException fne) {
                throw new HttpCustomClientException(String.format("HttpConnector TrustStore %s not found",
                                                                                config.getTrustStore()), fne);
            } catch (Exception ex) {
                throw new HttpCustomClientException(String.format("HttpConnector KeyStore Error, TrustStore %s", 
                                                                                config.getTrustStore()), ex);
            } finally {
                IOUtils.closeQuietly(ksStream);
            }

        }
    }

}
