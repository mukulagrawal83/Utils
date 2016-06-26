package com.fisglobal.emea.xpress.service;

import java.util.List;

/**
 *
 * @author morel
 */
public abstract class XpressSingleServiceBase extends AbstractXpressServiceBase {

   @Override
   protected void executeInternal(List<ServiceContext> serviceContextList) throws Exception {
      executeInternal(serviceContextList.get(0));
   }

   protected abstract void executeInternal(ServiceContext serviceContext) throws Exception;
}
