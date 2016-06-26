package com.fisglobal.emea.xpress.filter.credentials.legacy;

import com.fisglobal.emea.xpress.filter.credentials.SSOCredential;
import com.fisglobal.emea.xpress.filter.credentials.SSOSubjectContext;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.component.ComponentContext;
import com.fnf.xes.framework.security.trust.LoginManager;
import com.fnf.xes.framework.security.trust.SubjectContext;
import com.fnf.xes.framework.security.trust.SubjectContextBuilder;
import com.fnf.xes.framework.security.trust.SubjectContextProvider;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;

/**
 *
 * @author morel
 */
public class SSOSubjectContextProvider implements SubjectContextBuilder, SubjectContextProvider {
   
   private static Logger log = Logger.getLogger(SSOSubjectContextProvider.class);
   private SSOSubjectContext subjectContext;
   private LoginManager loginManager;
   
   public SubjectContext createSubjectContext() {
      if (subjectContext == null) {
         log.warn("No subjectContext provided");
      }
      return subjectContext;
   }

   public SubjectContext assertSubjectContext(SubjectContext sc) throws ServiceException {
      return assertSubjectContext(sc, null);
   }
   
   public SubjectContext assertSubjectContext(SubjectContext sc, String loginModuleName) throws ServiceException {
      if (loginManager == null) {
         return subjectContext;
      }
      loginManager.login(subjectContext);
      loginManager.commit(subjectContext);
      
      ComponentContext c = (ComponentContext) subjectContext.getCredential(ComponentContext.class);
      if (c == null) {
         log.debug("No login module commited componentContext in subjectContext - this is an authentication failure");
         return subjectContext;
      }
      if (c.getAttribute(ComponentContext.USER_NAME) == null) {
         log.warn("ComponentContext.USER_NAME is null");
         return subjectContext;
      }
      if (log.isDebugEnabled()) {
         log.debug("Saving SSOCredential(" + c.getAttribute(ComponentContext.USER_NAME).toString() + ") in subjectContext");
      }
      subjectContext.putCredential(new SSOCredential(c.getAttribute(ComponentContext.USER_NAME).toString()));
      
      return subjectContext;
   }
   
   public void revokeSubjectContext() {
      // clean threadlocal map
      subjectContext.cleanCredentials();
      subjectContext.cleanAttributes();
   }
   
   public LoginManager getLoginManager() {
      return loginManager;
   }
   
   public void setLoginManager(LoginManager loginManager) {
      this.loginManager = loginManager;
   }
   
   public SSOSubjectContext getSubjectContext() {
      return subjectContext;
   }
   
   public void setSubjectContext(SSOSubjectContext subjectContext) {
      this.subjectContext = subjectContext;
   }
   
   @PostConstruct
   public void checkConfig() {
      if (subjectContext == null) {
         log.warn("No subjectContext set");
      }
      if (loginManager == null) {
         log.warn("No loginManager set");
      }
   }


}
