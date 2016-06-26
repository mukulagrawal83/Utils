package com.fisglobal.emea.xpress.service;

import com.fisglobal.emea.xpress.service.errorhandlers.ExceptionHandler;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnis.xes.framework.component.ComponentContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author morel
 */
public abstract class AbstractXpressServiceBase implements XpressService {

   protected Logger log = Logger.getLogger(this.getClass().getName());
   private List<ExceptionHandler> exceptionHandlers = new ArrayList();

   public void execute(RequestMessage requestMessage, ResponseMessage responseMessage) throws Exception {

      Object payload = requestMessage.getObject();
      List<ServiceContext> input = new ArrayList<ServiceContext>();
      boolean operServiceCall = false;
      ServiceContext singleServiceCallWrapper = null;

      if (payload instanceof List) {
         Iterator inputListIterator = ((List) payload).iterator();
         while (inputListIterator.hasNext()) {
            Object payloadElement = inputListIterator.next();
            if (payloadElement instanceof ServiceContext) {
               input = Collections.unmodifiableList((List) payload);
               operServiceCall = true;
               break;
            } else {
               throw new IllegalArgumentException("Got unsupported payload type " + payloadElement.getClass().getName());
            }
         }
      } else {
         ComponentContext context = (ComponentContext) requestMessage.getProperty(ComponentContext.class.getName());
         singleServiceCallWrapper = new ServiceContext(payload, context);
         input.add(singleServiceCallWrapper);
         input = Collections.unmodifiableList(input);
      }

      try {
         executeInternal(input);
         if (operServiceCall) {
            responseMessage.setObject(input);
         } else {
            // special case - either single adapter call or an XBO with single element                        
            responseMessage.setObject(singleServiceCallWrapper.getResponseXboList());
         }
      } catch (Exception e) {
         if (log.isDebugEnabled()) {
            log.debug("Service threw an exception", e);
         }

         Map<String, Object> exceptionProcessingContext = new LinkedHashMap();
         Throwable current = e;
         for (ExceptionHandler handler : getExceptionHandlers()) {
            try {
               if (!handler.handle(current, exceptionProcessingContext, requestMessage, responseMessage, input)) {
                  break;
               }
            } catch (Exception innerException) {
               current = innerException;
            }
         }
      }
   }

   protected abstract void executeInternal(List<ServiceContext> serviceContextList) throws Exception;

   /**
    * @return the log
    */
   public Logger getLog() {
      return log;
   }

   /**
    * @param log the log to set
    */
   public void setLog(Logger log) {
      this.log = log;
   }

   /**
    * @return the exceptionHandlers
    */
   public List<ExceptionHandler> getExceptionHandlers() {
      return exceptionHandlers;
   }

   /**
    * @param exceptionHandlers the exceptionHandlers to set
    */
   public void setExceptionHandlers(List<ExceptionHandler> exceptionHandlers) {
      this.exceptionHandlers = exceptionHandlers;
   }
}
