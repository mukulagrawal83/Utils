package com.fisglobal.emea.xpress.service.test;

import com.fisglobal.emea.xpress.service.AbstractXpressServiceBase;
import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fisglobal.emea.xpress.service.XpressServiceInvokingWrapper;
import com.fnis.ifx.xbo.v1_1.XBO;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author trojanbug
 */
@Test
public class XpressServiceInvokingWrapperTest {

    @Test
    public void testXpressServiceInvokingWrapper() throws Exception {
        
        FakeService fs = new FakeService();
        XpressServiceInvokingWrapper wrapper = new XpressServiceInvokingWrapper(fs);
        
        List<ServiceContext> scl = new ArrayList<ServiceContext>();
        scl.add(new ServiceContext(new FakeXBO("RequestXBO")));
        
        wrapper.invokeExternally(scl);
        
        for (ServiceContext sc  : scl) {
            for (XBO xbo : sc.getResponseXboList()) {
                Assert.assertEquals("XBO not as expected", "ResponseXBO", xbo.getName());
            }
        }
    }

    public static class FakeService extends AbstractXpressServiceBase {

        @Override
        protected void executeInternal(List<ServiceContext> serviceContextList) throws Exception {
            for (ServiceContext sc : serviceContextList) {
                sc.addResponseXbo(new FakeXBO("ResponseXBO"));
            }
        }
    }

    static class FakeXBO implements XBO {

        String name = "FakeXBO";

        FakeXBO() {
        }

        FakeXBO(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<String> getCTUList() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public String getCTUValue(String path) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public List<String> getDelElements() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public List<String> getUpdElements() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public List<String> getNewElements() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
