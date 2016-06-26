package com.fisglobal.emea.xpress.filter.credentials;

import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.security.trust.SubjectContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author morel
 */
public class SSOSubjectContext implements SubjectContext {

   private static Logger log = Logger.getLogger(SSOSubjectContext.class);
   private CredentialRegistry credentialRegistry;
   private JdbcTemplate jdbcTemplate;

   private static ThreadLocal<Map<Object, Object>> credentials = new ThreadLocal<Map<Object, Object>>() {
      @Override
      protected Map<Object, Object> initialValue() {
         if (log.isDebugEnabled()) {
            log.debug(Thread.currentThread().getId() + ", Creating credentials map");
         }
         return new HashMap<Object, Object>();
      }
   };

   private static ThreadLocal<Map<Object, Object>> attributes = new ThreadLocal<Map<Object, Object>>() {
      @Override
      protected Map<Object, Object> initialValue() {
         if (log.isDebugEnabled()) {
            log.debug(Thread.currentThread().getId() + ", Creating attributes map");
         }
         return new HashMap<Object, Object>();
      }
   };

   public SSOSubjectContext() {
   }

   public SSOSubjectContext(CredentialRegistry credentialRegistry) {
      this.credentialRegistry = credentialRegistry;
   }

   public void cleanAttributes() {
      if (log.isDebugEnabled()) {
         log.debug(Thread.currentThread().getId() + ", about to clean attributes, attributes.size=" + attributes.get().size());
      }
      attributes.get().clear();
   }

   public void cleanCredentials() {
      if (log.isDebugEnabled()) {
         log.debug(Thread.currentThread().getId() + ", about to clean credentials, credentials.size=" + credentials.get().size());
      }
      credentials.get().clear();
   }

   public void putCredential(Object o) {
      if (o == null) {
         return;
      }
      putCredential(o.getClass(), o);
   }

   public void putCredential(Class type, Object o) {
      if (log.isDebugEnabled()) {
         log.debug(Thread.currentThread().getId() + ", saving type=" + type + ", value=" + o);
      }
      credentials.get().put(type, o);
   }

   public Object getCredential(Class type) throws ServiceException {
      Object savedCredential = credentials.get().get(type);
      if (savedCredential != null) {
         if (log.isDebugEnabled()) {
            log.debug(Thread.currentThread().getId() + ", Got saved credential type=" + type + ", " + savedCredential.toString());
         }
         return savedCredential;
      }

      SSOCredential ssoCredential = (SSOCredential) credentials.get().get(SSOCredential.class);
      if (ssoCredential == null) {
         if (log.isDebugEnabled()) {
            log.debug(Thread.currentThread().getId() + ", SSOCredential not found in SubjectContext, did you set up SSOSubjectContextProvider?");
         }
         return null;
      }

      CredentialRegistryItem registryItem = credentialRegistry.getCredentialRegistry().get(type);
      if (registryItem == null) {
         log.warn("Credential type " + type.getName() + " not configured, lookup in registry skipped");
         return null;
      }
      if (registryItem.getCredentialFactory() == null) {
         log.warn("Credential type " + type.getName() + " has no credential factory configured, lookup in registry skipped");
         return null;
      }
      if (StringUtils.isBlank(registryItem.getSystemName())) {
         log.warn("Credential type " + type.getName() + " has no system name configured, lookup in registry skipped");
         return null;
      }

      // do lookup in the mapping database
      List<String> result = jdbcTemplate.queryForList("SELECT MAPPED_USER_ID FROM SSO_USER_MAPPINGS WHERE USER_NAME = ? AND APPLICATION_ID = ?",
         new Object[]{ssoCredential.getUserName(), registryItem.getSystemName()}, String.class);
      if (result.isEmpty()) {
         log.debug("Mapping NOT FOUND for user=" + ssoCredential.getUserName() + ",class=" + type.getName() + ",system=" + registryItem.getSystemName());
         return null;
      }
      String mappedUserName = result.get(0);
      log.debug("Mapping found, " + ssoCredential.getUserName() + "=>" + mappedUserName + ",class=" + type.getName() + ",system=" + registryItem.getSystemName());

      // save credential in a thread local map
      Object targetCredential = registryItem.getCredentialFactory().getCredential(mappedUserName);
      credentials.get().put(targetCredential.getClass(), targetCredential);
      return targetCredential;
   }

   public void setAttribute(Object key, Object value) {
      if (log.isDebugEnabled()) {
         log.debug(Thread.currentThread().getId() + ", saving key=" + key + ", value=" + value);
      }
      attributes.get().put(key, value);
   }

   public Object getAttribute(Object o) {
      if (log.isDebugEnabled()) {
         log.debug(Thread.currentThread().getId() + ", attributes(" + o + ")=" + attributes.get().get(o));
      }
      return attributes.get().get(o);
   }

   public Map getAttributes() {
      return attributes.get();
   }

   public JdbcTemplate getJdbcTemplate() {
      return jdbcTemplate;
   }

   public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public CredentialRegistry getCredentialRegistry() {
      return credentialRegistry;
   }

   public void setCredentialRegistry(CredentialRegistry credentialRegistry) {
      this.credentialRegistry = credentialRegistry;
   }

   @PostConstruct
   public void checkConfig() {
      if (credentialRegistry == null) {
         log.warn("credentialRegistry is not set");
      }
      if (jdbcTemplate == null) {
         log.warn("jdbcTemplate not set");
      }
   }
}
