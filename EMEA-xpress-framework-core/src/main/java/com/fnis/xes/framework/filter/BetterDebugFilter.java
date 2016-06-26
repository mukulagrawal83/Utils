package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

public class BetterDebugFilter implements RequestFilter {

   private static final Logger log = Logger.getLogger(BetterDebugFilter.class);

   public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws java.lang.Throwable {
       log.info("Request message dump");
       if (log.isInfoEnabled() && requestMessage.getObject() instanceof String) {
           log.info(getPayloadContent(requestMessage.getObject()));
       }
       requestLink.doFilter(requestMessage, responseMessage);
       log.info("Response message dump");
       if (log.isInfoEnabled() &&  responseMessage.getObject() instanceof String) {
           log.info(getPayloadContent(responseMessage.getObject()));
       }
   }

   private String getPayloadContent(Object payload) throws Exception {
      if (payload == null) {
         return null;
      }

      // xerces load and save api
      DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
      DOMImplementationLS ls = (DOMImplementationLS) registry.getDOMImplementation("LS");
      LSSerializer serializer = ls.createLSSerializer();
      serializer.getDomConfig().setParameter("format-pretty-print", true);

      if (payload instanceof Document) {
         // pretty print serializer         
         return serializer.writeToString((Document) payload);
      }
        
      // bit oakward for pretty print
      LSInput dump = ls.createLSInput();
      dump.setStringData(payload.toString());
      LSParser parser = ls.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, "http://www.w3.org/2001/XMLSchema");
      
      // pretty print serializer and a check just in case that's an invalid xml
      try {
         return serializer.writeToString(parser.parse(dump));
      } catch (Exception e) {
         log.error("Could not parse payload:" + payload.toString(), e);
         return null;
      }
   }

   public void initializeFilter(Properties properties) throws BocException {
   }
}
