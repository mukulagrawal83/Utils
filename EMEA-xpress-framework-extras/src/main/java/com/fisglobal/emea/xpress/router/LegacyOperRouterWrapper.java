package com.fisglobal.emea.xpress.router;

import com.fnf.jef.boc.RequestMessage;
import com.fnis.ifx.xbo.v1_1.Operation;
import com.fnis.ifx.xbo.v1_1.Payload;

/**
 *
 * @author morel
 */
public class LegacyOperRouterWrapper extends LegacyRouterWrapper {

   public boolean accepts(RequestMessage requestMessage) {
      return ((requestMessage.getObject() instanceof Payload)
              && (((Payload) requestMessage.getObject()).getProcess() instanceof Operation));
   }
}
