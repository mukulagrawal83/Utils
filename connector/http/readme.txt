This module is created to serve the common need of Httpclient with SSL support.
See @com.fisglobal.emea.common.http.HttpClientTemplateTest for common usage scenarios.

For HTTPS:

==>Generate a Java keystore and key pair:

keytool -genkey -alias <alias name> -keyalg RSA -keystore keystore.jks -keysize 2048

==> Import a root or intermediate CA certificate to an existing Java keystore
keytool -import -trustcacerts -alias root -file Thawte.crt -keystore keystore.jks

==> Import a signed primary certificate to an existing Java keystore
keytool -import -trustcacerts -alias mydomain -file mydomain.crt -keystore keystore.jks

===============OR==================================

If you are using keystore tool, import the certificate in the keystore created with first step and see

@ com.fisglobal.emea.common.http.HttpClientTemplateTest.should_return_valid_response_for_xng_healthcheck_with_SSL()

=============================================================


How To use CustomeWebServiceTemplate:

CustomWebServiceTemplate is just a wrapper around Spring WebServiceTemplate with additional constructor arguments for Apache http client.

Default Spring configuration{ifx2_spring_http.xml} provided with this artifact takes care of all necessary configuration and bean declaration like below in spring configuration should create desired web service template:

    <bean id="webServiceTemplate" class="com.fisglobal.emea.springframwork.ws.core.XpressWebServiceTemplate">
        <constructor-arg ref="additional constructor args goes here"/>
        <constructor-arg ref="xpressWebServiceTemplateHttpClient"/>
        <property name="property names" ref="values"/>
    </bean>

To make more changes in configuration configure httpConfig as done in ifx2_spring_http.xml  
    
    