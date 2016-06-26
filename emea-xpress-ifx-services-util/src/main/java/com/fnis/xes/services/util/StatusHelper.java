/*
 * StatusHelper.java
 *
 * Created on October 26, 2004, 4:03 PM
 * Updated on February 8, 2013 by e1049528
 */

package com.fnis.xes.services.util;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.base.AdditionalStatus;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.XESServicesConstants;
import com.fnis.xes.services.errormapping.ErrorMappingAbility;
import com.fnis.xes.services.errormapping.HostMessageWrapper;
import com.fnis.xes.services.errormapping.XesHostMessageWrapper;
import com.fnis.xes.services.template.XBOStatus;

/**
 *
 * @author e0072970
 */
public class StatusHelper {
    private static final String CLASS_NAME = StatusHelper.class.getName();

    private static Logger log = Logger.getLogger(CLASS_NAME);

    private static final String IFX_SEV_DEFAULT_SUCCESS_MSG = "TRANSACTION PROCESSING COMPLETE.";
    private static final String IFX_SEV_DEFAULT_WARNING_MSG = "TRANSACTION PROCESSING COMPLETE WITH WARNINGS.";

    public static final String IFX_SEV_SUCCESS = "Info";

    public static final String IFX_SEV_INFO = "Info";

    public static final String IFX_SEV_WARN = "Warning";
    public static final String IFX_SEV_WARNSHORT = "Warn";    

    public static final String IFX_SEV_ERROR = "Error";

    public static final int SEVERITY_INFO = 0;

    public static final int SEVERITY_WARN = 1;

    public static final int SEVERITY_FATAL = 2;

    private StatusHelper() {
    }

    public static Status newInstance() {
        Status status = null;
        try {
            status = (Status) Factory.create(Status.class);
        } catch (Exception exp) {
            log.error("StatusHelper.newInstance errors: " + exp.getMessage(),
                    exp);
        }
        return status;
    }

    public static Status addStatus(long errorCode, String sev, String errdesc,
                                   Status status) {
        return addStatus(errorCode, null, sev, errdesc, status);
    }

    public static Status addStatus(long errorCode, String serverErrCode,
                                   String sev, String errdesc, Status status) {

        if (status == null) {
            status = newInstance();
        }
        if (null == status.getSeverity()) {
            status.setStatusCode(errorCode);
            status.setServerStatusCode(serverErrCode);
            status.setSeverity(sev);
            status.setStatusDesc(errdesc);
        } else {
            try {
                AdditionalStatus astatus = (AdditionalStatus) Factory.create(AdditionalStatus.class);
                astatus.setStatusCode(errorCode);
                astatus.setServerStatusCode(serverErrCode);

                if (sev == null) {
                    sev = IFX_SEV_ERROR;
                }

                astatus.setSeverity(sev);

                astatus.setStatusDesc(errdesc);
                status.getAdditionalStatus().add(astatus);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return status;
    }
    
   public static boolean hasWarnings(Status status) {
      if (status == null) {
         return false;
      }

      if (IFX_SEV_WARN.equals(status.getSeverity()) || IFX_SEV_WARNSHORT.equals(status.getSeverity())) {
         return true;
      }

      for (AdditionalStatus s : status.getAdditionalStatus()) {
         if (IFX_SEV_WARN.equals(s.getSeverity()) || IFX_SEV_WARNSHORT.equals(s.getSeverity())) {
            return true;
         }
      }
      return false;
   }

    public static boolean hasErrors(Status status) {
        boolean bErrors = false;

        if (null == status)
            return bErrors;

        if ((null != status.getSeverity()) || (null != status.getStatusDesc())
                || (null != status.getServerStatusCode())) {
            /* if the status code is not zero(Success) then errors have occured. */
            if ((status.getStatusCode() != 0)
                    && (null != status.getSeverity())
                    && !((status.getSeverity().equalsIgnoreCase(IFX_SEV_SUCCESS)) || 
                         (status.getSeverity().equalsIgnoreCase(IFX_SEV_WARN)) ||
                         (status.getSeverity().equalsIgnoreCase(IFX_SEV_WARNSHORT)))
                    ) {
                bErrors = true;
            }
            /* first status was a success, how about the additionals? */
            else {
                java.util.List list = status.getAdditionalStatus();
                AdditionalStatus astatus;
                for (int i = 0; i < list.size(); i++) {
                    astatus = (AdditionalStatus) list.get(i);
                    if ((astatus.getStatusCode() != 0)
                            && !((astatus.getSeverity().equalsIgnoreCase(IFX_SEV_SUCCESS)) || 
                            (astatus.getSeverity().equalsIgnoreCase(IFX_SEV_WARN)) ||
                            (astatus.getSeverity().equalsIgnoreCase(IFX_SEV_WARNSHORT)))
                            ) {
                        bErrors = true;
                        break;
                    }
                }
            }
        }
        return bErrors;
    }

    public static boolean hasStatus(int statusCode, Status status) {
        boolean bHasStatus = false;

        if (null == status)
            return false;

        if (status.getStatusCode() == statusCode)
            return true;

        java.util.List list = status.getAdditionalStatus();
        for (int i = 0; i < list.size(); i++) {
            if (((AdditionalStatus) list.get(i)).getStatusCode() == statusCode) {
                bHasStatus = true;
                break;
            }
        }
        return bHasStatus;
    }

    public static boolean hasSatus(String serverStatusCode, Status status) {
        boolean bHasStatus = false;

        if (null == status)
            return false;

        if (status.getServerStatusCode().equals(serverStatusCode))
            return true;

        java.util.List list = status.getAdditionalStatus();
        for (int i = 0; i < list.size(); i++) {
            if (((AdditionalStatus) list.get(i)).getServerStatusCode().equals(
                    serverStatusCode)) {
                bHasStatus = true;
                break;
            }
        }
        return bHasStatus;
    }

    /**
     * This method will be used to add info and warning status messages to
     * successful status message object, as additional status messages.
     *
     * @param status -
     *            Status Object with Successful message.
     * @param adtnalStatus -
     *            Status Object with info and warning messages
     */
    public static void addAdditionalStatus(Status status, Status adtnalStatus) {

        try {
            if (status != null && adtnalStatus != null) {

                AdditionalStatus astatus = (AdditionalStatus) Factory.create(AdditionalStatus.class);
                astatus.setStatusCode(adtnalStatus.getStatusCode());
                astatus.setServerStatusCode(adtnalStatus.getServerStatusCode());
                astatus.setSeverity(adtnalStatus.getSeverity());
                astatus.setStatusDesc(adtnalStatus.getStatusDesc());
                status.getAdditionalStatus().add(astatus);

                java.util.List statusList = adtnalStatus.getAdditionalStatus();
                for (int i = 0; i < statusList.size(); i++) {
                    AdditionalStatus additionalStatus = (AdditionalStatus) statusList
                            .get(i);
                    status.getAdditionalStatus().add(additionalStatus);
                }
            }
        } catch (Exception jbe) {
            log.error(IdConstants.ERR_JAXB_EXCEPTION
                    + "StatusHelper.addAdditionalStatus JAXB errors: "
                    + jbe.getMessage(), jbe);
        }
    }

    public static Status createStatus(int statusCode, String severity,
                                      String statusDesc) {
        try {
            // Create a Status object with a message namespace
            Status stat = (Status) Factory.create(Status.class);

            stat.setStatusCode(Long.valueOf(statusCode));
            stat.setStatusDesc(statusDesc);
            stat.setSeverity(severity);
            return stat;
        } catch (Exception exp) {
            log.error(IdConstants.ERR_UNKNOWN_EXCEPTION
                    + "StatusHelper.addAdditionalStatus JAXB errors: "
                    + exp.getMessage(), exp);
            return null;
        }
    }
    
    /**
     * Returns new instance of status object with success code
     *
     * @return status - Status object
     */
    public static Status createWarningStatus(long statusCode) {
        Status w  = StatusHelper.newInstance();
        w.setSeverity(StatusHelper.IFX_SEV_WARN);
        w.setStatusCode(statusCode); 
        w.setStatusDesc("Warning");
        return w;
    }    

    /**
     * Returns new instance of status object with success code
     *
     * @return status - Status object
     */
    public static Status createSuccessStatus() {
        Status successStatus = null;
        successStatus = StatusHelper.newInstance();
        successStatus.setSeverity(StatusHelper.IFX_SEV_INFO);
        successStatus.setStatusCode(Long.valueOf(0)); 
        successStatus.setStatusDesc(XESServicesConstants.SUCCESS);
        successStatus = StatusHelper.addStatus(Long.valueOf(0), "0", StatusHelper.IFX_SEV_INFO, IFX_SEV_DEFAULT_SUCCESS_MSG, successStatus);
        return successStatus;
    }

    /**
     * Returns new instance of status object with success code
     *
     * @return status - Status object
     */
    public static Status createNotProcessedStatus() {
        Status successStatus = null;
        successStatus = StatusHelper.newInstance();
        successStatus.setSeverity(StatusHelper.IFX_SEV_WARN);
        successStatus.setStatusCode(Long.valueOf(0));
        successStatus.setStatusDesc("Not Processed");
        return successStatus;
    }
    /**
     *
     * @param status
     * @param beanStatusList
     * @param errorMappingAbility
     * @param restStatus
     * @return
     */
    public static Status addStatus(Status status,
                                   List<XBOStatus> beanStatusList,
                                   ErrorMappingAbility errorMappingAbility, XBOStatus restStatus, String spName){
        try {
            Iterator<XBOStatus> iterator = beanStatusList.iterator();
            XBOStatus lastXboStatus = null;
            int maxSeverity = 0;
            while (iterator.hasNext()) {
                int internalSeverity = 3;
                lastXboStatus = iterator.next();
                if(lastXboStatus.getHostError() != null){
                    internalSeverity = lastXboStatus.getHostError().getMaxSeverity();
                    if(internalSeverity == 2)
                        internalSeverity = 3;
                    if(lastXboStatus.isOverride() || lastXboStatus.getHostError().getRestriction() != null){
                        internalSeverity = 2;
                    }
                    spName = StringUtils.isNotEmpty(lastXboStatus.getHostAppId())?lastXboStatus.getHostAppId():spName;
                    spName = StringUtils.replace(spName, "fnis", "fnf");
                    status = errorMappingAbility.mapStatusXBO(
                            spName, lastXboStatus.getHostError(), status);
                    if (lastXboStatus.getStatus() != null && lastXboStatus.getStatus().getAdditionalStatus() != null) {
                        if (lastXboStatus.getStatus().getAdditionalStatus().size()>0) {
                            if(lastXboStatus.getStatus().getAdditionalStatus().get(0).getOvrrideExceptn().size() > 0) {
                                status.getAdditionalStatus().get(0).getOvrrideExceptn().
                                        addAll(lastXboStatus.getStatus().getAdditionalStatus().get(0).getOvrrideExceptn());
                            } else {
                                status.getAdditionalStatus().addAll(lastXboStatus.getStatus().getAdditionalStatus());
                            }
                        }
                    }
                    if (internalSeverity > maxSeverity) {
                        maxSeverity = internalSeverity;
                    }
                }
            }
            if(maxSeverity == 2){
                restStatus.setOverride(true);
            }
        } catch (Exception exp) {
            log.error(IdConstants.ERR_UNKNOWN_EXCEPTION
                    + "StatusHelper.addAdditionalStatus errors: "
                    + exp.getMessage(), exp);
            return null;
        }
        return status;
    }

    public static Status addStatus(Status status,
                                   List<XBOStatus> beanStatusList,
                                   ErrorMappingAbility errorMappingAbility, boolean flag){
        try {
            Iterator<XBOStatus> iterator = beanStatusList.iterator();
            XBOStatus lastXboStatus = null;
            while (iterator.hasNext()) {
                lastXboStatus = iterator.next();
                status = errorMappingAbility.mapStatusXBO(
                        XESServicesConstants.XES_SPNAME, lastXboStatus.getHostError(), status);
            }
/*			if (hasErrors(status)) {
				addAdditionalStatus(status, lastXboStatus.getStatus(), factory);
			}*/
        } catch (Exception exp) {
            log.error(IdConstants.ERR_UNKNOWN_EXCEPTION
                    + "StatusHelper.addAdditionalStatus JAXB errors: "
                    + exp.getMessage(), exp);
            return null;
        }
        return status;
    }
    public static Status addStatus(Status status,
                                   List<XBOStatus> beanStatusList,
                                   ErrorMappingAbility errMappingAbility) {
        try {
            Iterator<XBOStatus> iterator = beanStatusList.iterator();
            XBOStatus lastXboStatus = null;
            while (iterator.hasNext()) {
                lastXboStatus = iterator.next();
                String spName = lastXboStatus.getHostAppId() != null ? lastXboStatus.getHostAppId() : XESServicesConstants.XES_SPNAME;
                if(lastXboStatus.getHostError() != null ){
                    status = addStatus(status, lastXboStatus.getHostError(),
                            lastXboStatus.getSeverity(), lastXboStatus
                            .getStatusDesc(), errMappingAbility, spName);
                }else{
                    status = addStatus(status, lastXboStatus.getStatusCode(),
                            lastXboStatus.getSeverity(), lastXboStatus
                            .getStatusDesc(), errMappingAbility, spName);
                }
            }
            if (hasErrors(status)) {
                addAdditionalStatus(status, lastXboStatus.getStatus());
            }
        } catch (Exception exp) {
            log.error(IdConstants.ERR_UNKNOWN_EXCEPTION
                    + "StatusHelper.addAdditionalStatus JAXB errors: "
                    + exp.getMessage(), exp);
            return null;
        }
        return status;
    }

    public static Status addStatus(Status rootStatus, HostMessageWrapper hostMessageWrapper,
                                   String severity, String desc,
                                   ErrorMappingAbility errorMappingAbility, String spName) throws Exception {
        rootStatus = errorMappingAbility.mapStatusXBO(
                spName, hostMessageWrapper, rootStatus);
        return rootStatus;
    }
    
    public static Status addStatus(Status rootStatus, HostMessageWrapper hostMessageWrapper,
                                   String severity, String desc,
                                   ErrorMappingAbility errorMappingAbility) throws Exception {
        rootStatus = errorMappingAbility.mapStatusXBO(
                XESServicesConstants.XES_SPNAME, hostMessageWrapper, rootStatus);
        return rootStatus;
    }    

    public static Status addStatus(Status rootStatus, long fnfErrorCode,
                                   String severity, String desc,
                                   ErrorMappingAbility errorMappingAbility, String spName) throws Exception {
        rootStatus = errorMappingAbility.mapStatusXBO(
                spName, new XesHostMessageWrapper(
                (int) fnfErrorCode, desc), rootStatus);
        return rootStatus;
    }

    
    public static Status addStatus(Status rootStatus, long fnfErrorCode,
                                   String severity, String desc,
                                   ErrorMappingAbility errorMappingAbility) throws Exception {
        rootStatus = errorMappingAbility.mapStatusXBO(
                XESServicesConstants.XES_SPNAME, new XesHostMessageWrapper((int) fnfErrorCode, desc), rootStatus);
        return rootStatus;
    }
    
    /**
     * Method to check the XBOStatus has errors
     * @param status
     * @return boolean true/false
     */
    public static boolean hasErrors(XBOStatus status) {
        boolean bErrors = false;

        if (null == status)
            return bErrors;

        if ((null != status.getSeverity()) || (null != status.getStatusDesc())
                || (null != status.getServerStatusCode())) {
            /* if the status code is not zero(Success) then errors have occured. */
            if ((status.getStatusCode() != 0)
                    && (null != status.getSeverity())
                    && !((status.getSeverity().equalsIgnoreCase(IFX_SEV_SUCCESS)) || 
                    (status.getSeverity().equalsIgnoreCase(IFX_SEV_WARN)) ||
                    (status.getSeverity().equalsIgnoreCase(IFX_SEV_WARNSHORT)))
                    ) {
                bErrors = true;
            }
        }
        return bErrors;
    }
}
