package com.fisglobal.emea.xpress.service.errorhandlers.exceptions;

/**
 *
 * @author morel
 */
public class InputValidationException extends RuntimeException {

   public InputValidationException() {
   }

   public InputValidationException(String message) {
      super(message);
   }

   public InputValidationException(Throwable cause) {
      super(cause);
   }

   public InputValidationException(String message, Throwable cause) {
      super(message, cause);
   }
}
