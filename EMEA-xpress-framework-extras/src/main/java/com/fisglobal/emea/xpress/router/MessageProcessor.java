package com.fisglobal.emea.xpress.router;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;

/**
 *
 * @author morel
 */
public interface MessageProcessor extends MessageAcceptor {
   public void process(RequestMessage req, ResponseMessage res) throws Throwable;
}
