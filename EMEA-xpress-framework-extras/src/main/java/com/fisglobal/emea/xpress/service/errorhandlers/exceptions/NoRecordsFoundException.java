package com.fisglobal.emea.xpress.service.errorhandlers.exceptions;

/**
 *
 * @author morel
 */
public class NoRecordsFoundException extends RuntimeException {

   public NoRecordsFoundException() {
   }

   public NoRecordsFoundException(String message) {
      super(message);
   }

   public NoRecordsFoundException(Throwable cause) {
      super(cause);
   }

   public NoRecordsFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
