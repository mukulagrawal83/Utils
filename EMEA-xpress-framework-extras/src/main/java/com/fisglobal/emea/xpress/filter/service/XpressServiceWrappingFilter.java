package com.fisglobal.emea.xpress.filter.service;

import com.fisglobal.emea.xpress.service.XpressService;
import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import java.util.Properties;

/**
 *
 * @author morel
 */
public class XpressServiceWrappingFilter implements RequestFilter {
   
   private XpressService service;

   public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {
      service.execute(requestMessage, responseMessage);
      requestLink.doFilter(requestMessage, responseMessage);
   }

   public void initializeFilter(Properties prprts) throws BocException {
      // ignore, left for legacy interface compat
   }

   /**
    * @return the service
    */
   public XpressService getService() {
      return service;
   }

   /**
    * @param service the service to set
    */
   public void setService(XpressService service) {
      this.service = service;
   }

   
}
