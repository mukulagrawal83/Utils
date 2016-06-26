package com.fnis.xes.tests;

import com.fnis.xes.framework.filter.BetterIFXExceptionFilterGT;
import junit.framework.Assert;
import org.testng.annotations.Test;

@Test
public class IFXExceptionFilterTest {

    public void testIFXExceptionFilter() throws Exception {
        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fnis=\"http://www.fnf.com/xes\"  xmlns=\"urn:ifxforum-org:XSD:1\">     <soapenv:Header/>     <soapenv:Body>        <fnis:AcctInqRq>           <RqUID xmlns=\"urn:ifxforum-org:XSD:1\">0b5bcd23-fede-447c-8b40-b0cd2a6d597b</RqUID>           <ns1:MsgRqHdr xmlns:ns1=\"urn:ifxforum-org:XSD:1\">              <ns1:CredentialsRqHdr>                 <ns1:SubjectRole>System</ns1:SubjectRole>                 <ns1:SecTokenLogin>                    <ns1:LoginName>samm</ns1:LoginName>                    <ns1:SubjectPswd>                       <ns1:CryptType>none</ns1:CryptType>                       <ns1:Pswd>samm</ns1:Pswd>                    </ns1:SubjectPswd>                 </ns1:SecTokenLogin>              </ns1:CredentialsRqHdr>              <ns1:ContextRqHdr>                 <ns1:ClientDt>0000-00-00T00:00:00.000000-00:00</ns1:ClientDt>                 <ns1:CustLangPref>en_US</ns1:CustLangPref>                 <ns1:ClientApp>                    <ns1:Org>ING RBB</ns1:Org>                    <ns1:Name>ING Nederland Retail Bank</ns1:Name>                    <ns1:Version>2</ns1:Version>                 </ns1:ClientApp>                 <ns1:SPName>com.fnis.xes.PRF</ns1:SPName>              </ns1:ContextRqHdr>           </ns1:MsgRqHdr>           <AcctSel>              <AcctKeys>                 <AcctId>112131313</AcctId>              </AcctKeys>           </AcctSel>        </fnis:AcctInqRq>     </soapenv:Body>  </soapenv:Envelope>";

        BetterIFXExceptionFilterGT filter = new BetterIFXExceptionFilterGT(null);
        
        Assert.assertEquals("0b5bcd23-fede-447c-8b40-b0cd2a6d597b",filter.getRqUID(request));
    }
}
