package com.fisglobal.emea.common.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@Test
public class HttpClientTemplateTest {

    HttpClientConfig config = new HttpClientConfig();
    HttpClientTemplate template;

    @BeforeTest
    public void setup() {
//        throw new SkipException("system properties are not set");
    }

    @Test
    public void should_return_valid_response_for_valid_custom_xml_grammar() throws Exception {
        template = new HttpClientTemplate(config);
        HttpEntity entity = createHttpRequest("custom_inqBookBalance.xml");

        HttpClientResponse response = template.submit(URI.create("http://amsxpress01.fnfis.com:9081/xpress/services/IFXService/ifsa"), entity);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        Assert.assertTrue(StringUtils.isNotBlank(response.getResponseBody()));

    }

    @Test
    public void should_return_valid_response_for_valid_ifx20() throws Exception {
        template = new HttpClientTemplate(config);

        HttpEntity entity = createHttpRequest("ifx20_corrs.xml");
        Header header = new BasicHeader("SOAPAction", "urn:IFX");

        HttpClientResponse response = template.submit(URI.create("http://ubddevrabxpap01.fisclient.local:9081/xpress/services/IFXService/ifx20-1"), entity, header);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        Assert.assertTrue(StringUtils.isNotBlank(response.getResponseBody()));

    }

    @Test
    public void should_return_valid_response_for_xng_healthcheck_with_SSL() throws Exception {
        HttpClientConfig httpsConfig = new HttpClientConfig();
        httpsConfig.setTrustStore("src/test/resources/keystore.jks");
        httpsConfig.setTrustStorePswd("password");
        httpsConfig.setTrustStoreCertSelfSigned(true);
        
        template = new HttpClientTemplate(httpsConfig);
        HttpEntity entity = createHttpRequest("xng_healthcheck.xml");

        HttpClientResponse response = template.submit(URI.create("https://amsxpress01.fnfis.com:9449/xpressNG/pox/v2rqst"), entity);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        Assert.assertTrue(StringUtils.isNotBlank(response.getResponseBody()));

    }
    
    @Test
    public void should_return_valid_response_for_xpress_ifx20_with_SSL() throws Exception {
        HttpClientConfig httpsConfig = new HttpClientConfig();

        httpsConfig.setTrustStore("src/test/resources/utility.jks");
        httpsConfig.setTrustStorePswd("password");
        httpsConfig.setTrustStoreCertSelfSigned(true);
        
        Header header = new BasicHeader("SOAPAction", "urn:IFX");
        HttpEntity entity = createHttpRequest("ifx20_partyInq.xml");
        
        template = new HttpClientTemplate(httpsConfig);
        HttpClientResponse response = template.submit(URI.create("https://ubdst1rabxpap01.fisclient.local:9443/xpress/services/IFXService/ifx20-1"), entity, header);

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        Assert.assertTrue(StringUtils.isNotBlank(response.getResponseBody()));
    }

    @Test(expectedExceptions = HttpCustomClientException.class)
    public void should_throw_exception_for_wrong_truststore_password() throws Exception {
        HttpClientConfig httpsConfig = new HttpClientConfig();

        httpsConfig.setTrustStore("src/test/resources/keystore.jks");
        httpsConfig.setTrustStorePswd("password123");
        httpsConfig.setTrustStoreCertSelfSigned(true);
        template = new HttpClientTemplate(httpsConfig);
        HttpEntity entity = createHttpRequest("xng_healthcheck.xml");

        template.submit(URI.create("https://amsxpress01.fnfis.com:9449/xpressNG/pox/v2rqst"), entity);
    }

    @Test(expectedExceptions = HttpCustomClientException.class)
    public void should_throw_exception_for_invalid_target_url() throws Exception {
        HttpEntity entity = createHttpRequest("custom_inqBookBalance.xml");
        template = new HttpClientTemplate(config);

        template.submit(URI.create("http://wrongurl.com:9081/xpress/services/IFXService/ifsa"), entity);
    }

    @Test
    public void should_return_404_for_incorrect_uri() throws Exception {
        HttpEntity entity = createHttpRequest("custom_inqBookBalance.xml");
        template = new HttpClientTemplate(config);

        HttpClientResponse response = template.submit(URI.create("http://amsxpress01.fnfis.com:9081/xpress/services/IFXService/404"), entity);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 404);

    }

    private String getResource(String resourceName) throws IOException {
        return IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName),
                "UTF-8");
    }

    private HttpEntity createHttpRequest(String fileName) throws IOException, UnsupportedEncodingException {
        String content = getResource(fileName);
        HttpEntity entity = new ByteArrayEntity(content.getBytes("utf-8"), ContentType.create("text/xml;charset=utf-8"));
        return entity;
    }
    
//    public class MyTransformer implements IAnnotationTransformer {
//
//        public boolean isTestDisabled(String testName){
//            return System.getProperty("atomXpressHost") != null;
//        }
//
//        @Override
//        public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
//                Method testMethod) {
//            if(isTestDisabled(testMethod.getName())){
//                annotation.setEnabled(false);
//            }
//            
//        }
//    }
}
