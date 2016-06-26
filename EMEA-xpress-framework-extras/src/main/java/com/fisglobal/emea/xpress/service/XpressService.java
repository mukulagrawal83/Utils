package com.fisglobal.emea.xpress.service;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;

/**
 *
 * @author morel
 */
public interface XpressService {
   
   public void execute(RequestMessage requestMessage, ResponseMessage responseMessage) throws Exception;
   
}
