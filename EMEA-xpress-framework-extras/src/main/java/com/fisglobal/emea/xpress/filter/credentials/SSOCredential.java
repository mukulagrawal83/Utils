package com.fisglobal.emea.xpress.filter.credentials;

/**
 *
 * @author morel
 */
public class SSOCredential {
   
   private String userName = "uninitialized";

   public SSOCredential() {
   }
   
   public SSOCredential(String userName) {
      this.userName = userName;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }
   
}
