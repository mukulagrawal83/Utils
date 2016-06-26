package com.fnis.xes.ifx.interfacepoint.qas;

/**
 * This program contains trade secrets that belong to Fidelity Information Services, Inc. and is licensed by an
 * agreement. Any unauthorized access, use, duplication, or disclosure is unlawful.
 *
 * Copyright (c) Fidelity Information Services, Inc. 2006, All right reserved. XProfileAdapter_Party.java
 *
 */
import com.fisglobal.emea.xpress.service.errorhandlers.exceptions.GenericMessageErrorException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.ws.soap.client.SoapFaultClientException;

/**
 * @author :	e3003319 (Basil George)
 * @description : This QASAbilityImpl implements QASAbility - used for communicating with QAS.
 * @implements : QASAbility,Ability
 * @extends	:
 * @date : June 19 2013
 */
@SuppressWarnings("serial")
public class QASAbilityImpl implements QASAbility {

   private Logger log = Logger.getLogger(this.getClass().getName());
   private WebServiceClient webServiceClient;
   
   public Object submitQASInq(Object qasObj) throws Exception {
      Object obj = null;
      try {
         obj = webServiceClient.sendAndReceiveQARequest(qasObj);
      } catch (SoapFaultClientException e) {
         log.error("SoapFault=" + e.getFaultCode());
         throw new GenericMessageErrorException(e.getFaultCode() != null ? e.getFaultCode().toString() : "Unknown SOAP error", e);
      } catch (Exception e) {
         log.error("General error occured", e);
         throw new GenericMessageErrorException(e.getMessage(), e);
      }
      return obj;
   }

   /**
    *
    * @param properties
    */
   public void initializeAbility(Properties properties) {
      
   }

   /**
    *
    * @param webServiceClient
    */
   public void setWebServiceClient(WebServiceClient webServiceClient) {
      this.webServiceClient = webServiceClient;
   }
}
