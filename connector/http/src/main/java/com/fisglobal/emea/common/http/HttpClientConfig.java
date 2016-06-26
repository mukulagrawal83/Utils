package com.fisglobal.emea.common.http;


public class HttpClientConfig {

    private String contentType = "text/xml;charset=utf-8";
    private int maxConnections = 100;
    private String acceptEncoding;
    private int connectionTimeout = 5000;
    private int readTimeout = 10000;
    private int maxConnectionIdleTime = -1; // values: -1 - use HttpClient
                                            // default keep-alive policy, 0 - no
                                            // keepAlive, > 0 keepAlive
    private int maxConnectionAge = 30000; // value: 0 = unlimited age.
    private boolean useStaleConnectionCheck = true;

    private boolean bypassProxy = false;
    private String proxy;
    private String proxyUser;
    private String proxyPassword;
    private String bypassProxyPatterns;
    
    private String keyStore;
    private String keyStorePswd;
    private String keyStoreCertPswd;

    private String trustStore;
    private String trustStorePswd;
    private boolean trustStoreCertSelfSigned = false;


    public String getContentType() {
        return contentType;
    }

    /**
     * will be used for addHeader(HttpHeaders.CONTENT_TYPE, config.getContentType()
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    /**
     * will be used for addHeader(HttpHeaders.ACCEPT_ENCODING, config.getAcceptEncoding())
     * @param acceptEncoding
     */
    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public boolean isBypassProxy() {
        return bypassProxy;
    }

    public void setBypassProxy(boolean bypassProxy) {
        this.bypassProxy = bypassProxy;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxConnectionIdleTime() {
        return maxConnectionIdleTime;
    }
    
    
    /**
     * will be called addHeader(HttpHeaders.CONNECTION, "close") for 0
     * @param maxConnectionIdleTime
     */
    public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    public int getMaxConnectionAge() {
        return maxConnectionAge;
    }

    public void setMaxConnectionAge(int maxConnectionAge) {
        this.maxConnectionAge = maxConnectionAge;
    }

    public boolean isUseStaleConnectionCheck() {
        return useStaleConnectionCheck;
    }

    public void setUseStaleConnectionCheck(boolean useStaleConnectionCheck) {
        this.useStaleConnectionCheck = useStaleConnectionCheck;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePswd() {
        return keyStorePswd;
    }

    public void setKeyStorePswd(String keyStorePswd) {
        this.keyStorePswd = keyStorePswd;
    }

    public String getKeyStoreCertPswd() {
        return keyStoreCertPswd;
    }

    public void setKeyStoreCertPswd(String keyStoreCertPswd) {
        this.keyStoreCertPswd = keyStoreCertPswd;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePswd() {
        return trustStorePswd;
    }

    public void setTrustStorePswd(String trustStorePswd) {
        this.trustStorePswd = trustStorePswd;
    }

    public boolean isTrustStoreCertSelfSigned() {
        return trustStoreCertSelfSigned;
    }

    public void setTrustStoreCertSelfSigned(boolean trustStoreCertSelfSigned) {
        this.trustStoreCertSelfSigned = trustStoreCertSelfSigned;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getBypassProxyPatterns() {
        return bypassProxyPatterns;
    }

    public void setBypassProxyPatterns(String bypassProxyPatterns) {
        this.bypassProxyPatterns = bypassProxyPatterns;
    }

}
