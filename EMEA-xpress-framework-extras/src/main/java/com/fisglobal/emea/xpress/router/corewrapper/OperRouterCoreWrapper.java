package com.fisglobal.emea.xpress.router.corewrapper;

import com.fnf.xes.framework.event.EventBroker;
import com.fnis.xes.framework.util.ErrWarnInfoMessage;
import com.fnis.xes.services.aggregate.AgrregateMsgConfigAbility;
import com.fnis.xes.services.errormapping.ErrorMappingAbility;
import com.fnis.xes.services.filter.router.OperRouter;

/**
 *
 * @author morel
 */
public class OperRouterCoreWrapper extends OperRouter {

   public OperRouterCoreWrapper() {
      super(null, null, null, null);
   }
   
   public OperRouterCoreWrapper(AgrregateMsgConfigAbility msgConfigAbility, EventBroker eventBroker, ErrWarnInfoMessage ewiMessage, ErrorMappingAbility errMappingAbility) {
      super(msgConfigAbility, eventBroker, ewiMessage, errMappingAbility);
   }

   /**
    * @return the errorMappingAbility
    */
   public ErrorMappingAbility getErrorMappingAbility() {
      return errorMappingAbility;
   }

   /**
    * @param errorMappingAbility the errorMappingAbility to set
    */
   public void setErrorMappingAbility(ErrorMappingAbility errorMappingAbility) {
      this.errorMappingAbility = errorMappingAbility;
   }

   /**
    * @return the errWarnInfoMessage
    */
   public ErrWarnInfoMessage getErrWarnInfoMessage() {
      return errWarnInfoMessage;
   }

   /**
    * @param errWarnInfoMessage the errWarnInfoMessage to set
    */
   public void setErrWarnInfoMessage(ErrWarnInfoMessage errWarnInfoMessage) {
      this.errWarnInfoMessage = errWarnInfoMessage;
   }   

   /**
    * @return the eventBroker
    */
   public EventBroker getAggregateEventBroker() {
      return aggregateEventBroker;
   }

   /**
    * @param eventBroker the eventBroker to set
    */
   public void setAggregateEventBroker(EventBroker eventBroker) {
      this.aggregateEventBroker = eventBroker;
   }

   /**
    * @return the msgConfigAbility
    */
   public AgrregateMsgConfigAbility getAggregateMsgConfigAbility() {
      return aggregateMsgConfigAbility;
   }

   /**
    * @param msgConfigAbility the msgConfigAbility to set
    */
   public void setAggregateMsgConfigAbility(AgrregateMsgConfigAbility msgConfigAbility) {
      this.aggregateMsgConfigAbility = msgConfigAbility;
   }
   
}
