package com.fnis.xes.services.template;

import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.services.errormapping.HostMessageWrapper;
import com.fnis.xes.services.errormapping.XesHostMessageWrapper;
import com.fnis.xes.services.util.StatusHelper;

public class XBOStatus {
	private boolean override = false;
	protected long statusCode;

	protected String serverStatusCode;

	protected String severity;

	protected String statusDesc;

	protected String id;

	protected Throwable cause;
	
	protected String hostAppId = null;
	
	private Status status = null;
	private HostMessageWrapper hostError = null;
	
	public XBOStatus(HostMessageWrapper hostError) {
		severity = update(hostError.getMaxSeverity());
		this.hostError = hostError;
		statusDesc = hostError.getMessageText(0);
		try{
			statusCode = Long.parseLong(hostError.getMessageId(0));
		}catch (Exception ex) {
			// Do Nothing
		}
	}
	
	/**
	 * 
	 * @param sev
	 * @return
	 */
	private String update(int sev){
		if(HostMessageWrapper.SEVERITY_FATAL == sev){
			return StatusHelper.IFX_SEV_ERROR;
		}else if(HostMessageWrapper.SEVERITY_INFO == sev){
			return StatusHelper.IFX_SEV_INFO;
		}else if(HostMessageWrapper.SEVERITY_WARN == sev){
			return StatusHelper.IFX_SEV_WARN;
		}else{
			return StatusHelper.IFX_SEV_SUCCESS;
		}		
	}
   
   private int getHostSeverity(String severity) {
      if (StatusHelper.IFX_SEV_INFO.equals(severity)) {
         return HostMessageWrapper.SEVERITY_INFO;
      }
      if (StatusHelper.IFX_SEV_WARN.equals(severity)) {
         return HostMessageWrapper.SEVERITY_WARN;
      }
      return HostMessageWrapper.SEVERITY_FATAL;
   }
	
	public XBOStatus(Status status) {
		this.severity = status.getSeverity();
		this.serverStatusCode = status.getServerStatusCode();
		statusCode = status.getStatusCode();
		statusDesc = status.getStatusDesc();
		this.status = status;
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc, getHostSeverity(severity));
	}
	/**
	 * @param cause
	 * @param id
	 * @param severity
	 * @param code
	 * @param desc
	 */
	public XBOStatus(String id, String serverStatusCode, String severity,
			long code, String desc, Throwable cause) {
		this.cause = cause;
		this.id = id;
		this.severity = severity;
		this.serverStatusCode = serverStatusCode;
		statusCode = code;
		statusDesc = desc;
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc, getHostSeverity(this.severity));
	}

	public XBOStatus(long code, String desc) {
		statusCode = code;
		statusDesc = desc;
		severity = "Error";
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc);
	}

	public XBOStatus(long code, String desc, Throwable cause) {
		this.cause = cause;
		statusCode = code;
		statusDesc = desc;
		severity = "Error";
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc, getHostSeverity(severity));
	}
	
	public XBOStatus(String hostAppId, long code, String desc, Throwable cause) {
		this.cause = cause;
		statusCode = code;
		statusDesc = desc;
		severity = "Error";
		this.hostAppId = hostAppId;
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc, getHostSeverity(severity));
	}

    public XBOStatus(long code, String desc, String sev)
    {
        statusCode = code;
        statusDesc = desc;
        severity = sev;
        hostError = new XesHostMessageWrapper((int)statusCode, statusDesc, getHostSeverity(severity));
    }

	public XBOStatus() {
	}

	public XBOStatus(String severity, long statusCode, String statusDesc) {
		this.severity = severity;
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc, getHostSeverity(severity));
	}

	public XBOStatus(String severity, long statusCode, String statusDesc, int hostSeverity ) {
		this.severity = severity;
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc,hostSeverity);
	}
	
	public XBOStatus(String hostAppId, String severity, long statusCode, String statusDesc) {
		this.severity = severity;
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
		this.hostAppId = hostAppId;
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc, getHostSeverity(severity));
	}
	
	public long getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(long value) {
		statusCode = value;
	}

	public String getServerStatusCode() {
		return serverStatusCode;
	}

	public void setServerStatusCode(String value) {
		serverStatusCode = value;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String value) {
		severity = value;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String value) {
		statusDesc = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		id = value;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
		statusCode = status.getStatusCode();
		statusDesc = status.getStatusDesc();
		hostError = new XesHostMessageWrapper((int)statusCode, statusDesc);
	}
	public HostMessageWrapper getHostError(){
		return hostError;
	}
	/**
	 * @return the ignore
	 */	
	public boolean isOverride() {
		return override;
	}

	/**
	 * @param restrction the ignore to set
	 */
	public void setOverride(boolean override) {
		this.override = override;
	}

	public String getHostAppId() {
		return hostAppId;
	}

	public void setHostAppId(String hostAppId) {
		this.hostAppId = hostAppId;
	}	
}
