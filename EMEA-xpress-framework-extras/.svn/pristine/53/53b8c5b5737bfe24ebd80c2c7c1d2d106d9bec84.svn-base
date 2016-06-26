package com.fisglobal.emea.xpress.filter.dispatcher;

import com.fisglobal.emea.xpress.router.MessageProcessor;
import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author morel
 */
public class DispatcherFilter implements RequestFilter {
   
   private List<MessageProcessor> messageProcessors = new ArrayList();

   public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {
      for (MessageProcessor processor : messageProcessors) {
         if (processor.accepts(requestMessage)) {
            processor.process(requestMessage, responseMessage);
            return;
         }
      }
      requestLink.doFilter(requestMessage, responseMessage);
   }

   public void initializeFilter(Properties prprts) throws BocException {
      // ignore, left for legacy interface compat
   }

   /**
    * @return the messageProcessors
    */
   public List<MessageProcessor> getMessageProcessors() {
      return messageProcessors;
   }

   /**
    * @param messageProcessors the messageProcessors to set
    */
   public void setMessageProcessors(List<MessageProcessor> messageProcessors) {
      this.messageProcessors = messageProcessors;
   }
   
}
