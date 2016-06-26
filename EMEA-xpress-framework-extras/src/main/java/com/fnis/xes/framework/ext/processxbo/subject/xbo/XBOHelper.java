package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import com.fnis.ifx.xbo.v1_1.*;
import com.fnis.xes.framework.ext.processxbo.exceptions.XBOInternalStructureException;
import com.fnis.xes.framework.ext.processxbo.helper.ClassValidatorHelper;
import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPath;
import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPathElement;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class XBOHelper {
    private static final Logger logger = Logger.getLogger(XBOHelper.class);

    private static final String DEFAULT_XBO_PREFIX = "com.fnis.ifx.xbo.v1_1";
    private static final String DEFAULT_XBO_IMPLEMENTATION_PACKAGE = DEFAULT_XBO_PREFIX + ".base";
    private static final String BUSINESS_OBJECT_INTERFACE_POSTFIX = "XBO";
    private static final String QUERY_OBJECT_INTERFACE_POSTFIX = "XQO";
    private static final String BUSINESS_OBJECT_IMPLEMENTATION_POSTFIX = "XBOImpl";
    private static final String QUERY_OBJECT_IMPLEMENTATION_POSTFIX = "XQOImpl";

    public static Class findXBOInterface(String name) {
        String packageName = DEFAULT_XBO_IMPLEMENTATION_PACKAGE;
        return ClassValidatorHelper.findClass(packageName, name);
    }

    public static Class findXBOImplementation(String name) {
        String className = name + "Impl";
        String packageName = DEFAULT_XBO_IMPLEMENTATION_PACKAGE;
        return ClassValidatorHelper.findClass(packageName, className);
    }

    public static JSONObject convertListToJSONObject(String objectName, List list) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            Object data = list.get(i);
            try {
                if (data instanceof Data)
                    jsonArray.put(i, ((Data) data).getData());
                else if (data instanceof String)
                    // TODO: limit this to basic types... Currently this is to override indexed String type.
                    jsonArray.put(i, data);
                else
                    throw new UnsupportedOperationException("Unsupported conversion to JSONObject: " + data.getClass().getName());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            jsonObject.put(objectName, jsonArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }


    public static Class findBusinessObjectInterface(String ifxObjectName) {
        String xboObjectPackageName = DEFAULT_XBO_PREFIX + "." + ifxObjectName.toLowerCase();
        String xboObjectNameInterface = ifxObjectName + BUSINESS_OBJECT_INTERFACE_POSTFIX;

        return ClassValidatorHelper.findClass(xboObjectPackageName, xboObjectNameInterface);
    }

    public static Class findQueryObjectInterface(String ifxObjectName) {
        String xboObjectPackageName = DEFAULT_XBO_PREFIX + "." + ifxObjectName.toLowerCase();
        String xboObjectNameInterface = ifxObjectName + QUERY_OBJECT_INTERFACE_POSTFIX;

        return ClassValidatorHelper.findClass(xboObjectPackageName, xboObjectNameInterface);
    }

    public static Class findBusinessObjectImplementation(String ifxObjectName) {
        String xboObjectPackageName = DEFAULT_XBO_PREFIX + "." + ifxObjectName.toLowerCase();
        String xboObjectNameInterface = ifxObjectName + BUSINESS_OBJECT_IMPLEMENTATION_POSTFIX;

        return ClassValidatorHelper.findClass(xboObjectPackageName, xboObjectNameInterface);
    }

    public static Class findQueryObjectImplementation(String ifxObjectName) {
        String xboObjectPackageName = DEFAULT_XBO_PREFIX + "." + ifxObjectName.toLowerCase();
        String xboObjectNameInterface = ifxObjectName + QUERY_OBJECT_IMPLEMENTATION_POSTFIX;

        return ClassValidatorHelper.findClass(xboObjectPackageName, xboObjectNameInterface);
    }

    public static List createMutableList(ListImpl list) {
        return new LinkedList<Data>(list);
    }

    public static Class findClassForXBOProcess(com.fnis.ifx.xbo.v1_1.Process process) {
        if (process instanceof Service) {
            return findXBOClassForService((Service) process);
        } else {
            throw new UnsupportedOperationException("Processing payload other than Service is not supported yet");
        }
    }

    public static Class findXBOClassForService(Service service) {
        XQO xqo = service.getRequest().getXQO();
        String servicePackageName = DEFAULT_XBO_PREFIX + service.getName().toLowerCase();
        String serviceXQOInterfaceName = service.getName() + "XQO";
        String fullClassName = servicePackageName + "." + serviceXQOInterfaceName;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            return Class.forName(fullClassName, false, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static List<Object> extractBusinessObjectsFromMessage(Object obj) {
        List<Object> results = new ArrayList<Object>();

        if (obj instanceof Request) {
            Request request = (Request) obj;
            XBO xbo = request.getXBO();
            XQO xqo = request.getXQO();

            if (xqo != null) {
                results.add(xqo);
            } else if (xbo != null) {
                results.add(xbo);
            } else {
                throw new UnsupportedOperationException("Neither XQO nor XBO message was provided...");
            }
        } else if (obj instanceof Response) {
            Response response = (Response) obj;
            results.addAll(response.getXBO());
        } else {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
        return results;
    }

    public static Object extractServiceObjectFromMessage(Object obj) {
        if (!(obj instanceof Service)) {
            throw new IllegalArgumentException("Provided reference does not refer to Service instance");
        }
        return obj;
    }

    // extracts message object from Process object: either Request or Response
    public static Object getMessageObject(IFX2MessageInfo ifx2MessageInfo, Payload xboPayload) {
        Service service = (Service) xboPayload.getProcess();
        Object messageObject;

        if (ifx2MessageInfo.isRequest()) {
            Request request = service.getRequest();

            // if(!ifx2MessageInfo.getObjectName().equals(request.getXQO().getName()))
            // either XQO or XBO names must be equal to ifx2MessageInfo.getObjectName() || service must contain request

            if(request == null) {
//                dumpServiceData(service);
//                throw new XBOInternalStructureException("Filter refers to Request, but provided Service instance doesn't contain one...");
                return null;
            }

            // assert object name equality
            if(request.getXQO() != null) {
                if(!ifx2MessageInfo.getObjectName().equals(request.getXQO().getName())) {
                    return null;
                } else if (!ifx2MessageInfo.getActionId().equals("Inq")){
                    // if it is XQO so it must be also "Inq" service!
                    return null;
                }
            } else if(request.getXBO() != null) {
                if(!ifx2MessageInfo.getObjectName().equals(request.getXBO().getName()) || !ifx2MessageInfo.getActionId().equals(service.getAction())) {
                    return null;
                }
            }

            messageObject = request;
        } else if (ifx2MessageInfo.isResponse()) {
            Response response = service.getResponse();
            if(response == null) {
//                dumpServiceData(service);
//                throw new XBOInternalStructureException("Filter refers to Response, while provided Service instance doesn't contain one...");
                return null;
            }

            // assert object name equality
            if(response.getXBO() != null && !response.getXBO().isEmpty()) {
                if(!ifx2MessageInfo.getObjectName().equals(response.getXBO().get(0).getName()) || !ifx2MessageInfo.getActionId().equals(service.getAction())) {
                    return null;
                }
            }

            messageObject = response;
        } else {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        return messageObject;
    }

    public static void adjustQueryAsXBO(IFX2MessageInfo ifx2MessageInfo, BeanEntityPath path) {
        BeanEntityPathElement firstAttribute = path.get(1);

        if (ifx2MessageInfo.isQuery()) {
            // this means xxx Inq message refers only to ONE xxxKeys object
            if (firstAttribute.getName().equals(ifx2MessageInfo.getObjectName() + "Sel")) {
                path.remove(1);
                return;
            }
        }
    }

    public static void dumpServiceData(Service service) {
        StringBuffer sb = new StringBuffer();
        sb.append("RecAction:  ").append(service.getAction()).append("\n");
        sb.append("Request:    ").append(service.getRequest()).append("\n");
        sb.append("Response:   ").append(service.getResponse()).append("\n");
        sb.append("RecCtrlIn:  ").append(service.getRecCtrlIn()).append("\n");
        sb.append("RecCtrlOut: ").append(service.getRecCtrlIn()).append("\n");

        logger.debug(sb.toString());

    }

}
