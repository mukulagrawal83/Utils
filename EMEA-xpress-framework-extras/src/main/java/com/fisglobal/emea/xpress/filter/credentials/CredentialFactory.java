package com.fisglobal.emea.xpress.filter.credentials;

/**
 *
 * @author morel
 */
public interface CredentialFactory<T> {
   public T getCredential(String userName);
}
