package com.fisglobal.emea.xpress.filter.credentials;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author morel
 */
public class CredentialRegistry {
   
   private Map<Class, CredentialRegistryItem> credentialRegistry = new HashMap();
   
   public void addRegistryItem(Class credentialClass, CredentialRegistryItem credentialItem) {
      credentialRegistry.put(credentialClass, credentialItem);
   }
   
   public CredentialRegistryItem removeRegistryItem(Class credentialClass) {
      return credentialRegistry.remove(credentialClass);
   }
   
   public Map<Class, CredentialRegistryItem> getCredentialRegistry() {
      return Collections.unmodifiableMap(credentialRegistry);
   }

   public void setCredentialRegistry(Map<Class, CredentialRegistryItem> credentialRegistry) {
      this.credentialRegistry = credentialRegistry;
   }
}
