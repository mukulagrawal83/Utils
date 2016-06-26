package com.fisglobal.emea.xpress.router;

import com.fnf.jef.boc.RequestMessage;

/**
 *
 * @author morel
 */
public interface IFXOperationAcceptor {
   
   public boolean accepts(RequestMessage requestMessage);
   
}
