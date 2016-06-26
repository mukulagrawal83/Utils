package com.fisglobal.emea.xpress.service.errorhandlers.exceptions;

/**
 *
 * @author morel
 */
public class GenericMessageErrorException extends RuntimeException{

   private String errorCode;
   
   public GenericMessageErrorException() {
   }

   public GenericMessageErrorException(String message) {
      super(message);
   }

   public GenericMessageErrorException(Throwable cause) {
      super(cause);
   }

   public GenericMessageErrorException(String message, Throwable cause) {
      super(message, cause);
   }
   
   public GenericMessageErrorException(String message, String errorCode) {
      super(message);
      this.errorCode = errorCode;
   }

   public GenericMessageErrorException(String message, Throwable cause, String errorCode) {
      super(message, cause);
      this.errorCode = errorCode;      
   }   

   /**
    * @return the errorCode
    */
   public String getErrorCode() {
      return errorCode;
   }

   /**
    * @param errorCode the errorCode to set
    */
   public void setErrorCode(String errorCode) {
      this.errorCode = errorCode;
   }
   
}
