package com.fnis.xes.services.template;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.cache.ICache;
import com.fnf.xes.framework.logging.ManagedLogAbility;
import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.MalformedDataException;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.XBO;
import com.fnis.ifx.xbo.v1_1.XQO;
import com.fnis.ifx.xbo.v1_1.base.RecCtrlIn;
import com.fnis.ifx.xbo.v1_1.base.RecCtrlOut;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.framework.util.ErrWarnInfoMessage;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.XESServicesConstants;
import com.fnis.xes.services.adapter.HostAdapterFactoryAbility;
import com.fnis.xes.services.adapter.XSORAdapter;
import com.fnis.xes.services.aggregate.AggregateConstants;
import com.fnis.xes.services.errormapping.ErrorMappingAbility;
import com.fnis.xes.services.plugin.journal.DefaultJournalPlugin;
import com.fnis.xes.services.plugin.journal.JournalInfo;
import com.fnis.xes.services.util.ServiceUtil;
import com.fnis.xes.services.util.XESLogger;

public abstract class XCommonBusinessService {

    /*
      * Xpress Component Context
      */
    public ComponentContext _context;

    /*
      * Service Provider name to whom the message is directed to.
      */
    protected String _spName;

    /*
      * Map containing requested light object elements in the request message
      */
    public Map _rqInputElements;

    /*
      * Host AdapterFactory ability to route the request to appropriate host
      * adapter routine
      */
    public HostAdapterFactoryAbility _hafaAbility;

    /*
      * Error Mapping ability to map Host specific error messages to IFX message
      */
    public ErrorMappingAbility _errMapAbility;

    /*
      * Managed log ability for logging information in the log files
      */
    public ManagedLogAbility _logAbility;

    /*
      * To translate the host error messages into IFX messages (info, error,
      * warning)
      */
    protected ErrWarnInfoMessage _messageAbility;

    /* ----TODO--------remove-------------- */
    public DefaultJournalPlugin _journalPlugin;

    /*
      * Array of XBO Object
      */
    protected List objList = new ArrayList();

    /*
      * Holds the list of bean status object. Usually populated by the host
      * adapter routine which is then used to build the IFX Status Aggregate
      */
    protected List statusList = new ArrayList();

    /*
      * This variable to indicate the current position of the FIS XBO in the list
      */
    protected int _cursor;

    /*
      * Light object name
      */
    public String infoObjName;

    /*
      * Light object
      */
    public Object infoObj;

    /*
      * Log4j logger used for logging information in the log files
      */
    private Logger _logger = null;

    /*
      * Flag to indicate Single SOR request
      */
    protected boolean bSingleSORExist = false;

    /*
      * Additional service specific information will be stored in this variable
      * which will be appended while logging the information to the log files.
      * This will aid the service developer in resolving the issue.
      */
    private StringBuffer _businessInfo = new StringBuffer("BusinessInfo:");

    private ICache _cache;

    public XCommonBusinessService(HostAdapterFactoryAbility hafa,
                                  ErrorMappingAbility errMapAbility, ManagedLogAbility logAbility,ICache cache)
            throws ServiceException {
        this._hafaAbility = hafa;
        this._errMapAbility = errMapAbility;
        this._logAbility = logAbility;
        this._cache = cache;
    }

    public XCommonBusinessService(HostAdapterFactoryAbility hafa,
                                  ErrorMappingAbility errMapAbility, ManagedLogAbility logAbility)
            throws ServiceException {
        this._hafaAbility = hafa;
        this._errMapAbility = errMapAbility;
        this._logAbility = logAbility;
    }

    public DefaultJournalPlugin getJournalPlugin() {
        return _journalPlugin;
    }

    public void setJournalPlugin(DefaultJournalPlugin plugin) {
        _journalPlugin = plugin;
    }

    /**
     * This method provides the generic template for executing inquiry message
     * on the Host System.
     *
     * </p> As part of the template it does following .Validate the Inquiry
     * request .Place holder to Initalize the Inq message .Apply service rules
     * for request .Get the target SORs .Route to SORS .Inquiry Response
     * Validation Place Holder .Apply business rules for Response
     *
     * @param context
     *            TODO
     * @param xbo
     * @throws ServiceException
     */
    final public Object inq(XBO xbo, ComponentContext context) {

        try {
            // Initalize the Inq message
            init(xbo, context);

            // valid request message (should consider a validation interface
            // point)
            validateInqRequest(xbo);

            // Apply service rules for request
            applyInqRequestRules(xbo);

            // Get single or multiple Sors
            getTargetSORs(xbo);

            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                        getMsgId(xbo), _spName);

                if (sorAdapter != null) {
                    sorAdapter.doInquiry(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing

                mergeResponse(xbo);
            }
            validateInqResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyInqResponseRules(xbo);

        } catch (ValidationException ve) {
            XESLogger.error(_logger, context.getUserName(), "inq - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
            // In the case of validation errors we will have multiple
            // status beans set
            // String msg =
            // _messageAbility.format(ve.getErrorCode(),StatusHelper.IFX_SEV_ERROR,ve.getMessage());
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "inq - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "inq - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "inq - ", context);
        } finally {
            closeResources();
        }

        return xbo;
    }

    /**
     * This method provides the generic template for executing pre-processing
     * part of an inquiry message on the Host System.
     *
     * </p> As part of the template it does following .Validate the Inquiry
     * request .Place holder to Initalize the Inq message .Apply service rules
     * for request .Get the target SORs .Route to SORS
     *
     * @param xbo
     *            - XBO object to be processed
     * @param context
     *            - Service ComponentContext
     *
     * @throws ServiceException
     *             - Exception while processing a pre-processing of an inquiry
     *             message
     */
    final public Object inqPre(XBO xbo, ComponentContext context) {

        try {
            // Initalize the Inq message
            init(xbo, context);

            // valid request message (should consider a validation interface
            // point)
            validateInqRequest(xbo);

            // Apply service rules for request
            applyInqRequestRules(xbo);

            // Get single or multiple Sors
            getTargetSORs(xbo);

            // invoke SOR class associated with the service
            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (sorAdapter != null) {
                sorAdapter.doInquiryPre(xbo, _context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "inqPre - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "inqPre - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "inqPre - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "inqPre - ", context);
        }finally {
            closeResources();
        }

        return xbo;
    }

    /**
     * This method provides the generic template for executing post-processing
     * part of an inquiry message on the Host System.
     *
     * </p> As part of the template it does following .Route to SORS.Inquiry
     * Response Validation Place Holder .Apply business rules for Response
     *
     * @param xbo
     *            - XBO object to be processed
     * @param context
     *            - Service ComponentContext
     *
     * @throws ServiceException
     *             - Exception while processing a post-processing of an inquiry
     *             message
     */
    final public Object inqPost(XBO xbo, ComponentContext context) {

        try {
            init(xbo, context);
            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (sorAdapter != null) {
                sorAdapter.doInquiryPost(xbo, _context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
            validateInqResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyInqResponseRules(xbo);

        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "inqPost - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "inqPost - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "inqPost - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "inqPost - ", context);
        }finally {
            closeResources();
        }

        return xbo;
    }

    /**
     * This method provides the generic template for executing pre-processing
     * part of an inquiry message on the Host System.
     *
     * </p> As part of the template it does following .Validate the Inquiry
     * request .Place holder to Initalize the Inq message .Apply service rules
     * for request .Get the target SORs .Route to SORS
     *
     * @param xbo
     *            - XBO object to be processed
     * @param context
     *            - Service ComponentContext
     *
     * @throws ServiceException
     *             - Exception while processing a pre-processing of an inquiry
     *             message
     */
    final public Object addPre(XBO xbo, ComponentContext context) {

        try {
            // Initalize the Inq message
            init(xbo, context);

            // valid request message (should consider a validation interface
            // point)
            validateAddRequest(xbo);

            // Apply service rules for request
            applyAddRequestRules(xbo);

            // Get single or multiple Sors
            getTargetSORs(xbo);
            validateCPA(xbo);

            // invoke SOR class associated with the service
            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (sorAdapter != null) {
                sorAdapter.doAddPre(xbo, _context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "addPre - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "addPre - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "addPre - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "addPre - ", context);
        }finally {
            closeResources();
        }

        return xbo;
    }

    /**
     *
     * @param request
     * @throws ServiceException
     */
    protected void validateCPA(XBO request) throws ServiceException {

    }

    /**
     * This method provides the generic template for executing post-processing
     * part of an inquiry message on the Host System.
     *
     * </p> As part of the template it does following .Route to SORS.Inquiry
     * Response Validation Place Holder .Apply business rules for Response
     *
     * @param xbo
     *            - XBO object to be processed
     * @param context
     *            - Service ComponentContext
     *
     * @throws ServiceException
     *             - Exception while processing a post-processing of an inquiry
     *             message
     */
    final public Object addPost(XBO xbo, ComponentContext context) {

        try {
            init(xbo, context);
            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (sorAdapter != null) {
                sorAdapter.doAddPost(xbo, _context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
            validateAddResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyAddResponseRules(xbo);

        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "addPost - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "addPost - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "addPost - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "addPost - ", context);
        } finally {
            closeResources();
        }

        return xbo;
    }

    /**
     * This method provides the generic template for executing the update
     * message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the request on
     * the host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object mod(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);

            // valid request message (should consider a validation interface
            // point)
            validateModRequest(xbo);

            // supply inquiry rules (should consider a rule interface point
            applyUpdRequestRules(xbo);

            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter hostAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (hostAdapter != null) {
                hostAdapter.doUpdate(xbo, context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
            // Validate Response message
            validateInqResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyUpdResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "mod - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "mod - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "mod - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "mod - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * This method provides the generic template for executing the update
     * message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the request on
     * the host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object modPre(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);

            // valid request message (should consider a validation interface
            // point)
            validateModRequest(xbo);

            // supply inquiry rules (should consider a rule interface point
            applyUpdRequestRules(xbo);

            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter hostAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (hostAdapter != null) {
                hostAdapter.doUpdatePre(xbo, context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
            // Validate Response message
            validateInqResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyUpdResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "modPre - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "modPre - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "modPre - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "modPre - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * This method provides the generic template for executing the update
     * message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the request on
     * the host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object modPost(XBO xbo, ComponentContext context) {
        try {

            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter hostAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (hostAdapter != null) {
                hostAdapter.doUpdatePost(xbo, context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
            // Validate Response message
            validateInqResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyUpdResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "modPost - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "modPost - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "modPost - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "modPost - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * This method provides the generic template for executing the add message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the reques on the
     * host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object add(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);
            // valid request message (should consider a validation interface
            // point)
            validateAddRequest(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyAddRequestRules(xbo);
            // added for journal plugin
            invokeJournal(xbo);
            // Get single or multiple Sors
            getTargetSORs(xbo);
            validateCPA(xbo);
            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter hostAdapter = (XSORAdapter) _hafaAbility
                        .getAdapter(getMsgId(xbo), _spName);

                if (hostAdapter != null) {
                    hostAdapter.doAdd(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing
                mergeResponse(xbo);
            }
            // Validate Response message
            validateAddResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyAddResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "add - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, re
                    .getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "add - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "add - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "add - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * This method provides the generic template for executing the delete
     * message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the reques on the
     * host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object del(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);
            // valid request message (should consider a validation interface
            // point)
            validateDeleteRequest(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyDeleteRequestRules(xbo);
            // Get single or multiple Sors
            getTargetSORs(xbo);
            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                        getMsgId(xbo), _spName);

                if (sorAdapter != null) {
                    sorAdapter.doDelete(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing
                mergeResponse(xbo);
            }
            // Validate Response message
            validateDeleteResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyDeleteResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "del - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "del - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "del - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "del - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * This method provides the generic template for executing the delete
     * message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the reques on the
     * host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object delPre(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);
            // valid request message (should consider a validation interface
            // point)
            validateDeleteRequest(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyDeleteRequestRules(xbo);
            // Get single or multiple Sors
            getTargetSORs(xbo);
            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                        getMsgId(xbo), _spName);

                if (sorAdapter != null) {
                    sorAdapter.doDeletePre(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing
                mergeResponse(xbo);
            }
            // Validate Response message
            validateDeleteResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyDeleteResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "delPre - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "delPre - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "delPre - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "delPre - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * This method provides the generic template for executing the delete
     * message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the reques on the
     * host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object delPost(XBO xbo, ComponentContext context) {
        try {
            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                        getMsgId(xbo), _spName);

                if (sorAdapter != null) {
                    sorAdapter.doDeletePost(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing
                mergeResponse(xbo);
            }
            // Validate Response message
            validateDeleteResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyDeleteResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "delPost - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "delPost - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "delPost - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "delPost - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    protected void addBusinessObject(Object bo) {
        objList.add(bo);
    }

    protected void clearBusinessObjects() {
        objList.clear();
    }

    /**
     * Inq Request Validation Interface Point to be implemented
     *
     * @param xBO
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateInqRequest(XBO xBO) throws ServiceException {
        validateXSIL(xBO);
    }

    /**
     * Inq Response Validation Interface Point to be implemented
     *
     * @param xBO
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    protected void validateInqResponse(XBO xBO) throws ServiceException {
        validateXSIL(xBO);
    }

    /**
     * Request Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateAddRequest(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Response Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    final protected void validateAddResponse(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Request Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateDeleteRequest(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Response Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    final protected void validateDeleteResponse(XBO xbo)
            throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * validateXSIL :- To invoke the XSIL(Xpress Schema Instruction Language)
     * validation on XBO elements
     *
     * @param xBO
     *
     * @throws ServiceException
     */
    protected void validateXSIL(XBO xBO) throws ServiceException {
        // xBO.validation(_spName);
    }

    /**
     * validateXSIL :- To invoke the XSIL(Xpress Schema Instruction Language)
     * validation on XBO elements
     *
     * @param xqo
     *
     * @throws ServiceException
     */
    protected void validateXSIL(XQO xqo) throws ServiceException {
        // xqo.validation();
    }

    /**
     * Inq Request Rules Interface Point to be implemented
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void applyInqRequestRules(XBO xBO) throws ServiceException {
    }

    /**
     * Inq Request Rules Interface Point to be implemented
     *
     * @param xqo
     * @throws ServiceException
     */
    protected void applyInqRequestRules(XQO xqo) throws ServiceException {
    }

    /**
     * Inq Response Rules Interface Point to be implemented
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void applyInqResponseRules(XBO xBO) throws ServiceException {
    }

    /**
     * Inq Response Rules Interface Point to be implemented
     *
     * @param xqo
     * @throws ServiceException
     */
    protected void applyInqResponseRules(XQO xqo) throws ServiceException {
    }

    /**
     * Inq Request Rules Interface Point to be implemented
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void applyUpdRequestRules(XBO xBO) throws ServiceException {
    }

    /**
     * Update Response Rules Interface Point to be implemented
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void applyUpdResponseRules(XBO xBO) throws ServiceException {
    }

    /**
     * Add Request Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyAddRequestRules(XBO xbo) throws ServiceException {
    }

    /**
     * Status Request Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyStatusModRequestRules(XBO xbo) throws ServiceException {
    }

    /**
     * Add Response Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyAddResponseRules(XBO xbo) throws ServiceException {
    }

    /**
     * Add Request Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyDeleteRequestRules(XBO xbo) throws ServiceException {
    }

    /**
     * Add Response Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyDeleteResponseRules(XBO xbo) throws ServiceException {
    }

    /**
     * Journal Plugin being invoked
     *
     * @param xbo
     *
     * @throws ServiceException
     */
    final protected void invokeJournal(XBO xbo) throws ServiceException {

        if (this._journalPlugin != null) {
            journalBefore(xbo);
            journalCore(xbo);
            journalAfter(xbo, _journalPlugin.getJournalFields(), _journalPlugin
                    .getAdditionalJournalFields());
        }
    }

    /**
     * The Core default plugin functionality provided
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void journalCore(XBO xBO) throws ServiceException {
        _journalPlugin.doAdd();

    }

    /**
     * Method provided to set the journal Info
     *
     * @param xBO
     * @throws ServiceException
     */
    final protected void journalBefore(XBO xBO) throws ServiceException {
        JournalInfo journalInfo = getJournalInfoBefore(xBO);
        Map journalAddionalInfo = getJournalInfoAdditionalBefore(xBO);
        if (journalInfo != null)
            _journalPlugin.setJournalFields(journalInfo);
        if (journalAddionalInfo != null)
            _journalPlugin
                    .setAdditionalJournalFields((HashMap) journalAddionalInfo);

    }

    /**
     * Overiddable method provided to get the journal Info from the Calling
     * service
     *
     * @param xBO
     * @throws ServiceException
     */
    protected JournalInfo getJournalInfoBefore(XBO xBO) throws ServiceException {
        return null;

    }

    /**
     * Overiddable method provided to get the journal Info Additional Info from
     * the Calling service
     *
     * @param xBO
     * @throws ServiceException
     */
    protected Map getJournalInfoAdditionalBefore(XBO xBO)
            throws ServiceException {
        return null;

    }

    /**
     * Overidable method provided for the calling service to view the Journal
     * Info After update
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void journalAfter(XBO xBO, JournalInfo journalInfo,
                                Map journalAdditionInfoMap) throws ServiceException {
    }

    /**
     * Get Targeted SORs for routing request
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void getTargetSORs(XBO xBO) throws ServiceException {
        bSingleSORExist = true;
    }

    /**
     * Get the response for the single SOR
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void getResponse(XCommonBusinessService xBO)
            throws ServiceException {

    }

    /**
     * Merge the response for the multiple SOR
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void mergeResponse(XBO xbo) throws ServiceException {

    }

    /**
     * Placeholder to Initialize the Inq message
     *
     * @param xBO
     * @throws ServiceException
     */
    protected void init(XBO xBO, ComponentContext context)
            throws ServiceException {
        _context = context;
        _spName = (String) context.getAttribute(ComponentContext.SP_NAME);
        _logger = _logAbility.getLogger(this.getClass());
    }

    /**
     * Placeholder to Initialize the Inq message
     *
     * @param xQO
     * @throws ServiceException
     */
    protected void init(XQO xQO, ComponentContext context)
            throws ServiceException {
        _context = context;
        _spName = (String) context.getAttribute(ComponentContext.SP_NAME);
        _logger = _logAbility.getLogger(this.getClass());
    }

    public String getBusinessInfo() {
        return _businessInfo.toString();

    }

    public void setBusinessInfo(String info) {
        _businessInfo.append(" ").append(info);
    }

    /**
     * Used to set the bean Status with the pertinent error. In most cases
     * Exceptions are not planned to be thrown from the SOR adapter only the
     * bean will be set with the Error.
     *
     * @param status
     */
    public void setBeanStatus(XBOStatus status) {
        List<XBOStatus> beanStatusList = (List<XBOStatus>) _context
                .getAttribute("XBOStatusList");
        if (beanStatusList == null) {
            beanStatusList = new ArrayList<XBOStatus>();
            _context.setAttribute("XBOStatusList", beanStatusList);
        }
        if(status.getHostAppId() == null)
        	status.setHostAppId(_spName);
        beanStatusList.add(status);
    }

    /**
     * @param xboList
     * @param context
     * @return
     */
    final public Object inq(List xboList, ComponentContext context) {
        for (Iterator xboInterator = xboList.iterator(); xboInterator.hasNext();) {
            XBO xbo = (XBO) xboInterator.next();
            try {
                // Initalize the Inq message
                init(xbo, context);
                // valid request message (should consider a validation interface
                // point)
                validateInqRequest(xbo);
                // Apply service rules for request
                applyInqRequestRules(xbo);
                // Get single or multiple Sors
                getTargetSORs(xbo);
                // invoke SOR class associated with the service
                if (bSingleSORExist) {
                    _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                    XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility
                            .getAdapter(getMsgId(xbo), _spName);

                    if (sorAdapter != null) {
                        sorAdapter.doInquiry(xbo, _context);
                    } else {
                        // TODO: Need to pass ErrWarnInfoMessage ability to
                        // format
                        // the message
                        String msg = "Host adapter object not defined for service";
                        throw new ServiceException(
                                IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                    }
                } else {// Multiple SOR routing
                    mergeResponse(xbo);
                }
                validateInqResponse(xbo);
                // supply inquiry rules (should consider a rule interface point
                applyInqResponseRules(xbo);
            } catch (ValidationException ve) {
                // In the case of validation errors we will have multiple
                // status beans set
                setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                        .getErrorCode(), ve.getMessage(), ve));
                XESLogger.error(_logger, context.getUserName(), "inq - "
                        + this.getBusinessInfo(), ve.getErrorCode(),ve);
            } catch (RulesException re) {
                // In the case of validation errors we will have multiple
                // status beans set
                setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(),
                        re));
                XESLogger.error(_logger, context.getUserName(), "inq - "
                        + this.getBusinessInfo(), re.getErrorCode(), re);
            } catch (ServiceException se) {
                setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(),
                        se));
                XESLogger.error(_logger, context.getUserName(), "inq - "
                        + this.getBusinessInfo(), se.getErrorCode(), se);
            }  catch (Exception ex) {
                handleGenericException(ex, "inq - ", context);
            } finally {
                closeResources();
            }
        }
        return xboList;
    }

    /******
     * Xpress Query object Inquiry. To be used with the Selector object
     *
     * @param xqo
     * @param context
     * @return
     */
    final public Object inq(XQO xqo, ComponentContext context) {
        List xboList = null;
        try {
            // Initalize the Inq message
            init(xqo, context);

            // valid request message (should consider a validation interface
            // point)
            validateInqRequest(xqo);

            // Apply service rules for request
            applyInqRequestRules(xqo);

            // Get single or multiple Sors
            getTargetSORs(xqo);

            //call for Adapter and pagination
            xboList = exeAdapter(xqo,context);

            if (xboList != null) {
                for (Iterator xboInterator = xboList.iterator(); xboInterator
                        .hasNext();) {
                    XBO xbo = (XBO) xboInterator.next();

                    try {
                        validateInqResponse(xbo);
                        // supply inquiry rules (should consider a rule
                        // interface point
                        applyInqResponseRules(xbo);
                    } catch (ValidationException ve) {
                        // In the case of validation errors we will have
                        // multiple
                        // status beans set
                        setBeanStatus(new XBOStatus(
                                XESServicesConstants.XES_SPNAME, ve
                                .getErrorCode(), ve.getMessage(), ve));
                        XESLogger.error(_logger, context.getUserName(),
                                "inq - " + this.getBusinessInfo(), ve
                                .getErrorCode(),ve);
                    } catch (RulesException re) {
                        // In the case of validation errors we will have
                        // multiple
                        // status beans set
                        setBeanStatus(new XBOStatus(re.getErrorCode(), re
                                .getMessage(), re));

                        XESLogger.error(_logger, context.getUserName(),
                                "inq - " + this.getBusinessInfo(), re
                                .getErrorCode(), re);
                    } catch (ServiceException se) {

                        setBeanStatus(new XBOStatus(se.getErrorCode(), se
                                .getMessage(), se));

                        XESLogger.error(_logger, context.getUserName(),
                                "inq - " + this.getBusinessInfo(), se
                                .getErrorCode(), se);
                    }

                }
            }

        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            XESLogger.error(_logger, context.getUserName(), "inq - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
        } catch (RulesException re) {
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "inq - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {
            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));
            XESLogger.error(_logger, context.getUserName(), "inq - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "inq - ", context);
        } finally {
            closeResources();
        }

        return xboList;
    }

    /**
     * Method to validate RecCtrl for pagination and execute Adapter call.
     * 	This will Cache and Paging XBO data
     * @param xqo
     * @param context ComponentContext
     * @throws ServiceException
     */
    protected List exeAdapter(XQO xqo, ComponentContext context) throws ServiceException{
        List xboList = null;
        XBOStatus status = null;
        String cacheKey = null;

        // Service,RecCtrlIn and RecCtrlOut from ComponentContext
        Service svcMsg = (Service) _context.getAttribute(Service.class.getName());
        RecCtrlIn recCtrlIn = (RecCtrlIn)context.getAttribute(RecCtrlIn.class.getSimpleName());
        RecCtrlOut recCtrlOut = (RecCtrlOut)Factory.create(RecCtrlOut.class);
        ServiceUtil serviceUtil = new ServiceUtil(status,_cache,_logger,context.getUserName());
        Object rqUID = svcMsg.getRqUID();

        // Checks whether it is RecCtrl or not.
        if ( _cache != null
                && recCtrlIn != null
                && recCtrlIn.getRecCountLimit() != null
                && recCtrlIn.getRecCountLimit().getCurRecLimit() != null) {

            // Validate RecCtrlIn for standards
            status = serviceUtil.validateRecCtrlIn(recCtrlIn);
            if (serviceUtil.hasErrors()) {
                // The request is not correct.
                setBeanStatus(status);
                return xboList;
            }

            //Checks whether it is first request or not.
            if(recCtrlIn.getToken() == null || recCtrlIn.getToken().equals("")){
                //call Host adapter for XBO List
                xboList = getXBOList(xqo,context);
                //Checks data Cache is necessary or not.
                if(recCtrlIn.getRecCountLimit().getCurRecLimit() < xboList.size()){
                    cacheKey = (String)getCacheKey(xqo,rqUID);
                    serviceUtil.putInCache(cacheKey, xboList);
                }
            }else{
                cacheKey = (String)getCacheKey(xqo,rqUID);
                List cachedxboList = (List)serviceUtil.getFromCache(cacheKey);
                if(cachedxboList == null || cachedxboList.isEmpty()){
                    xboList = getXBOList(xqo,context);
                    // Checks data Cache is necessary or not.
                    if(recCtrlIn.getRecCountLimit().getCurRecLimit() < xboList.size()){
                        serviceUtil.putInCache(cacheKey, xboList);
                    }
                }else{
                    xboList = cachedxboList;
                }
            }
            //2013-05-30 INGNL-3644 If no records found, don't set RecCtrlOut
            if(!xboList.isEmpty()){
                status = serviceUtil.getPagedRecords(recCtrlIn, recCtrlOut, xboList);
                context.setAttribute(RecCtrlOut.class.getSimpleName(), recCtrlOut);
                setBeanStatus(status);
            }

        }else{
            //call Host adapter for XBO List
            xboList = getXBOList(xqo,context);
        }
        return xboList;
    }

    /**
     * Method to get the List of XBO from Host Adapter execution
     * @param xqo - XQO
     * @param context - ComponentContext
     * @return List of XBO
     * @throws ServiceException
     */
    protected List getXBOList(XQO xqo,ComponentContext context)throws ServiceException{
        List xboList = null;
        // invoke SOR class associated with the service
        if (bSingleSORExist) {
            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xqo), _spName);

            if (sorAdapter != null) {
                xboList = sorAdapter.doInquiry(xqo, _context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(
                        IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
            }
        } else {// Multiple SOR routing

            xboList = mergeResponse(xqo);
        }
        return xboList;
    }

    /**
     * Method to create Cache Key value from Service Input
     * @param obj
     * @param cacheKey
     * @return created Cache Key value
     * @throws ServiceException
     */
    protected Object getCacheKey(Object obj,Object cacheKey) throws ServiceException{
        try{
            Method[] method = obj.getClass().getDeclaredMethods();
            for(int i=0; i <method.length;i++){
                Method meth = method[i];
                if(meth.getName().startsWith("get")){
                    Object methVal = meth.invoke(obj, null);
                    if(methVal != null
                            && (methVal instanceof String
                            || methVal instanceof Integer
                            || methVal instanceof Long)){
                        cacheKey = cacheKey + "_" + methVal;
                    }else if(methVal != null
                            && !(methVal instanceof List)
                            && ((String.valueOf(methVal)).startsWith("com.fnis.ifx.xbo."))){
                        cacheKey = getCacheKey(methVal,cacheKey);
                    }
                }
            }
            return cacheKey;
        }catch(Exception e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    /**
     * Inq Request Validation Interface Point to be implemented
     *
     * @param xqo
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateInqRequest(XQO xqo) throws ServiceException {
        validateXSIL(xqo);
    }

    /**
     * Inq Response Validation Interface Point to be implemented
     *
     * @param xqo
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    protected void validateInqResponse(XQO xqo) throws ServiceException {
        validateXSIL(xqo);
    }

    /**
     * Get Targeted SORs for routing request
     *
     * @param xqo
     * @throws ServiceException
     */
    protected void getTargetSORs(XQO xqo) throws ServiceException {
        bSingleSORExist = true;
    }

    /**
     * Merge the response for the multiple SOR
     *
     * @param xqo
     * @throws ServiceException
     */
    protected List<XBO> mergeResponse(XQO xqo) throws ServiceException {
        List<XBO> xboList = new ArrayList();
        return xboList;
    }

    /**
     * StatusMod Request Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyStatusRequestRules(XBO xbo) throws ServiceException {
    }

    /**
     * StatusMod Response Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyStatusModResponseRules(XBO xbo) throws ServiceException {
    }

    /**
     * This method provides the generic template for executing the add message
     *
     * </p> a) validate the request b) Overridable method(hook) to execute
     * business rules prior to execute the request c) Executes the reques on the
     * host system d) Validate the response e) Overridable method (hook) to
     * execute business rules on the response
     *
     * @throws ServiceException
     */

    final public Object statusMod(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);
            // valid request message (should consider a validation interface
            // point)
            validateStatusModRequest(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyStatusModRequestRules(xbo);
            // added for journal plugin
            invokeJournal(xbo);
            // Get single or multiple Sors
            getTargetSORs(xbo);
            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter hostAdapter = (XSORAdapter) _hafaAbility
                        .getAdapter(getMsgId(xbo), _spName);

                if (hostAdapter != null) {
                    hostAdapter.doStatusMod(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing
                mergeResponse(xbo);
            }
            // Validate Response message
            validateStatusModResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyStatusModResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "statusMod - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "statusMod - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "statusMod - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "statusMod - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    final public Object statusModPre(XBO xbo, ComponentContext context) {

        try {
            // Initalize the Inq message
            init(xbo, context);

            // valid request message (should consider a validation interface
            // point)
            validateStatusModRequest(xbo);

            // Apply service rules for request
            applyStatusModRequestRules(xbo);

            // Get single or multiple Sors
            getTargetSORs(xbo);

            // invoke SOR class associated with the service
            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (sorAdapter != null) {
                sorAdapter.doStatusModPre(xbo, _context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "statusMod - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "statusMod - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "statusMod - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "statusMod - ", context);
        }finally {
            closeResources();
        }

        return xbo;
    }

    final public Object statusModPost(XBO xbo, ComponentContext context) {

        try {
            init(xbo, context);
            _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
            XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                    getMsgId(xbo), _spName);

            if (sorAdapter != null) {
                sorAdapter.doStatusModPost(xbo, _context);
            } else {
                // TODO: Need to pass ErrWarnInfoMessage ability to format
                // the message
                String msg = "Host adapter object not defined for service";
                throw new ServiceException(IdConstants.ERR_HOST_ADAPTER_OBJECT,
                        msg);
            }
            validateStatusModResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyStatusModResponseRules(xbo);

        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "statusModPost - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "statusModPost - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "statusModPost - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        }  catch (Exception ex) {
            handleGenericException(ex, "statusModPost - ", context);
        } finally {
            closeResources();
        }

        return xbo;
    }

    /**
     * Request Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateStatusModRequest(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Response Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    protected void validateStatusModResponse(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Inq Request Validation Interface Point to be implemented
     *
     * @param xBO
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateModRequest(XBO xBO) throws ServiceException {
        validateXSIL(xBO);
    }

    final public Object authMod(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);
            // valid request message (should consider a validation interface
            // point)
            validateAuthModRequest(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyAuthModRequestRules(xbo);
            // added for journal plugin
            invokeJournal(xbo);
            // Get single or multiple Sors
            getTargetSORs(xbo);
            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter hostAdapter = (XSORAdapter) _hafaAbility
                        .getAdapter(getMsgId(xbo), _spName);

                if (hostAdapter != null) {
                    hostAdapter.doAuthMod(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing
                mergeResponse(xbo);
            }
            // Validate Response message
            validateAuthModResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyAuthModResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "authMod - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "authMod - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "authMod - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "authMod - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * Request Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateAuthModRequest(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Response Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    protected void validateAuthModResponse(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Auth Request Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyAuthModRequestRules(XBO xbo) throws ServiceException {
    }

    /**
     * AuthMod Response Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyAuthModResponseRules(XBO xbo) throws ServiceException {
    }


    final public Object authInq(XBO xbo, ComponentContext context) {
        try {
            // Initalize the Inq message
            init(xbo, context);
            // valid request message (should consider a validation interface
            // point)
            validateAuthInqRequest(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyAuthInqRequestRules(xbo);
            // added for journal plugin
            invokeJournal(xbo);
            // Get single or multiple Sors
            getTargetSORs(xbo);
            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter hostAdapter = (XSORAdapter) _hafaAbility
                        .getAdapter(getMsgId(xbo), _spName);

                if (hostAdapter != null) {
                    hostAdapter.doAuthInq(xbo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing
                mergeResponse(xbo);
            }
            // Validate Response message
            validateAuthInqResponse(xbo);
            // supply inquiry rules (should consider a rule interface point
            applyAuthInqResponseRules(xbo);
        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
            XESLogger.error(_logger, context.getUserName(), "authInq - "
                    + this.getBusinessInfo(), ve.getErrorCode(), ve);
        } catch (RulesException re) {
            // In the case of validation errors we will have multiple
            // status beans set
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "authInq - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {

            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));

            XESLogger.error(_logger, context.getUserName(), "authInq - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "authInq - ", context);
        } finally {
            closeResources();
        }
        return xbo;
    }

    /**
     * Request Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateAuthInqRequest(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Response Validation Interface Point to be implemented
     *
     * @param xbo
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    protected void validateAuthInqResponse(XBO xbo) throws ServiceException {
        validateXSIL(xbo);
    }

    /**
     * Auth Request Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyAuthInqRequestRules(XBO xbo) throws ServiceException {
    }

    /**
     * AuthInq Response Rules Interface Point to be implemented
     *
     * @param xbo
     * @throws ServiceException
     */
    protected void applyAuthInqResponseRules(XBO xbo) throws ServiceException {
    }

    /******
     * Xpress Query object Inquiry. To be used with the Selector object
     *
     * @param xqo
     * @param context
     * @return
     */
    final public Object authInq(XQO xqo, ComponentContext context) {
        List xboList = null;
        try {
            // Initalize the Inq message
            init(xqo, context);

            // valid request message (should consider a validation interface
            // point)
            validateAuthInqRequest(xqo);

            // Apply service rules for request
            applyAuthInqRequestRules(xqo);

            // Get single or multiple Sors
            getTargetSORs(xqo);

            // invoke SOR class associated with the service
            if (bSingleSORExist) {
                _hafaAbility.setHostAdapterVersion((String)context.getAttribute("HostAdapterVersion"));
                XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
                        getMsgId(xqo), _spName);

                if (sorAdapter != null) {
                    xboList = sorAdapter.doAuthInq(xqo, _context);
                } else {
                    // TODO: Need to pass ErrWarnInfoMessage ability to format
                    // the message
                    String msg = "Host adapter object not defined for service";
                    throw new ServiceException(
                            IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
                }
            } else {// Multiple SOR routing

                xboList = mergeResponse(xqo);
            }

            if (xboList != null) {
                for (Iterator xboInterator = xboList.iterator(); xboInterator
                        .hasNext();) {
                    XBO xbo = (XBO) xboInterator.next();

                    try {
                        validateAuthInqResponse(xbo);
                        // supply inquiry rules (should consider a rule
                        // interface point
                        applyAuthInqResponseRules(xbo);
                    } catch (ValidationException ve) {
                        // In the case of validation errors we will have
                        // multiple
                        // status beans set
                        setBeanStatus(new XBOStatus(
                                XESServicesConstants.XES_SPNAME, ve
                                .getErrorCode(), ve.getMessage(), ve));
                        XESLogger.error(_logger, context.getUserName(),
                                "doAuthInquiry - " + this.getBusinessInfo(), ve
                                .getErrorCode(), ve);
                    } catch (RulesException re) {
                        // In the case of validation errors we will have
                        // multiple
                        // status beans set
                        setBeanStatus(new XBOStatus(re.getErrorCode(), re
                                .getMessage(), re));

                        XESLogger.error(_logger, context.getUserName(),
                                "doAuthInquiry - " + this.getBusinessInfo(), re
                                .getErrorCode(), re);
                    } catch (ServiceException se) {

                        setBeanStatus(new XBOStatus(se.getErrorCode(), se
                                .getMessage(), se));

                        XESLogger.error(_logger, context.getUserName(),
                                "doAuthInquiry - " + this.getBusinessInfo(), se
                                .getErrorCode(), se);
                    }

                }
            }

        } catch (ValidationException ve) {
            // In the case of validation errors we will have multiple
            // status beans set
            XESLogger.error(_logger, context.getUserName(), "authInq - "
                    + this.getBusinessInfo(), ve.getErrorCode(),ve);
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME, ve
                    .getErrorCode(), ve.getMessage(), ve));
        } catch (RulesException re) {
            setBeanStatus(new XBOStatus(re.getErrorCode(), re.getMessage(), re));

            XESLogger.error(_logger, context.getUserName(), "authInq - "
                    + this.getBusinessInfo(), re.getErrorCode(), re);
        } catch (ServiceException se) {
            setBeanStatus(new XBOStatus(se.getErrorCode(), se.getMessage(), se));
            XESLogger.error(_logger, context.getUserName(), "authInq - "
                    + this.getBusinessInfo(), se.getErrorCode(), se);
        } catch (Exception ex) {
            handleGenericException(ex, "authInq - ", context);
        } finally {
            closeResources();
        }

        return xboList;
    }

    /**
     * Request Validation Interface Point to be implemented
     *
     * @param xqo
     *
     *            Template defined a) validateXSIL
     *
     * @throws ServiceException
     */
    protected void validateAuthInqRequest(XQO xqo) throws ServiceException {
        validateXSIL(xqo);
    }

    /**
     * Response Validation Interface Point to be implemented
     *
     * @param xqo
     *
     *            Template defined a) validateXSIL
     *
     *
     * @throws ServiceException
     */
    protected void validateAuthInqResponse(XQO xqo) throws ServiceException {
        validateXSIL(xqo);
    }

    /**
     * Auth Request Rules Interface Point to be implemented
     *
     * @param xqo
     * @throws ServiceException
     */
    protected void applyAuthInqRequestRules(XQO xqo) throws ServiceException {
    }

    /**
     * AuthInq Response Rules Interface Point to be implemented
     *
     * @param xqo
     * @throws ServiceException
     */
    protected void applyAuthInqResponseRules(XQO xqo) throws ServiceException {
    }

    /**
     * Given a object derive its message id
     *
     * @param obj
     *            - Object
     * @return String - MessageId
     */
    protected String getMsgId(Object obj) {
        String msgId = obj.getClass().getName();
        if (msgId.endsWith("XBOImpl") || msgId.endsWith("XQOImpl")) {
            msgId = msgId.substring(0, msgId.length() - 7);
        }
        return msgId;
    }

    /**
     * Subclasses may implement any cleanup code in their implementations
     *
     * @throws ServiceException
     */
    public void closeResources() {

    }

    private void handleGenericException(Exception ex, String methodName,
                                        ComponentContext context) {
        if (ex.getCause() instanceof MalformedDataException) {
            MalformedDataException mex = (MalformedDataException) ex.getCause();
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME,
                    IdConstants.ERR_INPUT_INVALID, mex.getMessage(), mex));
            XESLogger.error(_logger, context.getUserName(), methodName
                    + this.getBusinessInfo(), IdConstants.ERR_INPUT_INVALID,
                    mex);
        } else {
            setBeanStatus(new XBOStatus(XESServicesConstants.XES_SPNAME,
                    IdConstants.IFX_ERR_SERVICE_ERRORS_OCCURRED, ex.getMessage(), ex));
            XESLogger.error(_logger, context.getUserName(), methodName
                    + this.getBusinessInfo(),
                    IdConstants.IFX_ERR_SERVICE_ERRORS_OCCURRED, ex);
        }
    }
}
