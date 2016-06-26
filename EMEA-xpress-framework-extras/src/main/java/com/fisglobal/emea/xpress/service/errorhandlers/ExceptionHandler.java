package com.fisglobal.emea.xpress.service.errorhandlers;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import java.util.List;
import java.util.Map;

/**
 *
 * @author morel
 */
public interface ExceptionHandler {
   
   public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage responseMessage, List<ServiceContext> serviceContextList);
   
}
