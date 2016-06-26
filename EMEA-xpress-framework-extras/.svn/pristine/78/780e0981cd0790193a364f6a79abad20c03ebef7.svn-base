package com.fisglobal.emea.xpress.filter.credentials;

import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author morel
 */
public class CredentialRegistryItem implements InitializingBean {

   private Class credentialClass;
   private String systemName;
   private CredentialFactory credentialFactory;
   private CredentialRegistry credentialRegistry;

   public CredentialRegistryItem() {
   }
   
   public CredentialRegistryItem(CredentialRegistry credentialRegistry) {
      this.credentialRegistry = credentialRegistry;
   }
   
   public Class getCredentialClass() {
      return credentialClass;
   }

   public void setCredentialClass(Class credentialClass) {
      this.credentialClass = credentialClass;
   }

   public String getSystemName() {
      return systemName;
   }

   public void setSystemName(String systemName) {
      this.systemName = systemName;
   }

   public CredentialFactory getCredentialFactory() {
      return credentialFactory;
   }

   public void setCredentialFactory(CredentialFactory credentialFactory) {
      this.credentialFactory = credentialFactory;
   }

   public CredentialRegistry getCredentialRegistry() {
      return credentialRegistry;
   }

   public void setCredentialRegistry(CredentialRegistry credentialRegistry) {
      this.credentialRegistry = credentialRegistry;
   }
   
   public void addToRegistry() {
      credentialRegistry.addRegistryItem(credentialClass, this);
   }

   public void afterPropertiesSet() throws Exception {
      addToRegistry();
   }
}
