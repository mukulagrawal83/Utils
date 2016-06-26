package com.fisglobal.emea.xpress.service.errorhandlers.exceptions;

/**
 *
 * @author morel
 */
public class GenericMessageInfoException extends RuntimeException{

   private String errorCode;
   
   public GenericMessageInfoException() {
   }

   public GenericMessageInfoException(String message) {
      super(message);
   }

   public GenericMessageInfoException(Throwable cause) {
      super(cause);
   }

   public GenericMessageInfoException(String message, Throwable cause) {
      super(message, cause);
   }
   
   public GenericMessageInfoException(String message, String errorCode) {
      super(message);
      this.errorCode = errorCode;
   }

   public GenericMessageInfoException(String message, Throwable cause, String errorCode) {
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
