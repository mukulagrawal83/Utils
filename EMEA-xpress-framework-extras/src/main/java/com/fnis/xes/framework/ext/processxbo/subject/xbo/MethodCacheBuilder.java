package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import com.fnis.ifx.xbo.v1_1.ServiceImpl;
import com.fnis.xes.framework.ext.processxbo.cache.BeanMethodCache;
import com.fnis.xes.framework.ext.processxbo.exceptions.ValidationException;
import com.fnis.xes.framework.ext.processxbo.exceptions.XBOInternalStructureException;
import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPath;
import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPathElement;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

public class MethodCacheBuilder {
    private static final int MINIMUM_ACCEPTED_PATH_DEPTH = 2;

    public void validateAndCacheMessagePath(BeanEntityPath messagePath, BeanMethodCache beanMethodCache) {
        if (messagePath.size() < MINIMUM_ACCEPTED_PATH_DEPTH) {
            throw new ValidationException("Provided message path must contain at least from " + MINIMUM_ACCEPTED_PATH_DEPTH + " elements");
        }

        validateAndCacheRootElement(messagePath, beanMethodCache);
        validateAndCacheFromNthElement(messagePath, 1, beanMethodCache);
    }

    public void validateAndCacheRootElement(BeanEntityPath messagePath, BeanMethodCache beanMethodCache) {
        BeanEntityPathElement rootElement = messagePath.get(0);
        BeanEntityPathElement firstAttributeElement = messagePath.get(1);

        IFX2MessageInfo messageInfo = IFX2MessageInfoProvider.provide(rootElement.getName());

        String firstAttributeName = firstAttributeElement.getName();
        String messageObjectName = messageInfo.getObjectName();

        if (messageInfo.isOperation()) {
            throw new UnsupportedOperationException("Operation elements are not supported yet");
        }

        BOAggregateClassInfo boAggregateClassInfo = new BOAggregateClassInfo(messageObjectName);

        Boolean firstParsed = false;
        // test if first path attribute is one of the service attributes
        if (XBOConstants.SERVICE_DIRECT_ATTRIBUTE_LIST.contains(firstAttributeName)) {
            registerBeanMethodsWithConcreteSetter(ServiceImpl.class, firstAttributeName, beanMethodCache);
            firstParsed = true;
            // if it is a query it can be both XQO or XBO (depends on xxxSel contents)
        } else if (messageInfo.isQuery()) {
            if(boAggregateClassInfo.isValidAsQueryObject()) {
                if (firstAttributeName.equals(messageObjectName + XBOConstants.IFS_XXXSEL_AGGREGATE_SUFFIX)) {
                    registerBeanGetter(boAggregateClassInfo.getXqoImplementation(), messageObjectName + XBOConstants.IFS_XXXSEL_AGGREGATE_SUFFIX, beanMethodCache);
                    firstParsed = true;
                } else if (firstAttributeName.equals(XBOConstants.IFX_RECSELECT_RECORD)) {
                    registerBeanGetter(boAggregateClassInfo.getXqoImplementation(), XBOConstants.IFX_RECSELECT_RECORD, beanMethodCache);
                    firstParsed = true;
                }
            } else if (boAggregateClassInfo.isValidAsQueryObject() || boAggregateClassInfo.isValidAsBusinessObject()) {
                // np. /PartyInqRs/PartyRec/PartyKeys/PartyIdent/PartyIdentValue | xxxXBO extends already xxxRec !
                if (messageInfo.isRequest()) {
                    registerBeanMethodsWithConcreteSetter(boAggregateClassInfo.getXboImplementation(), firstAttributeName, beanMethodCache);
                    firstParsed = true;
                }
            } else {
                throw new ValidationException("Neither XQO nor XBO contains requested attribute: " + firstAttributeName);
            }

            for (String suffix : XBOConstants.XXXREC_DIRECT_ATTRIBUTE_LIST) {
                if (messagePath.containsPathElement(messageObjectName + suffix)) {
                    registerBeanMethodsWithConcreteSetter(boAggregateClassInfo.getXboImplementation(), messageObjectName + suffix, beanMethodCache);
                    firstParsed = true;
                }
            }

            if(messageInfo.isResponse() && firstAttributeName.equals(messageObjectName + "Rec")) {
                firstParsed = true;
            }

            // if first attribute name would not be validated here and path would be 2 elements long - path would pass
            if(!firstParsed) {
                throw new ValidationException("Neither XQO nor XBO contains requested attribute: " + firstAttributeName);
            }

        } else {
            // not inquiry, so must be XBO
            String xxxRecDirectAttribute = firstAttributeName.substring(messageObjectName.length());
            if (XBOConstants.XXXREC_DIRECT_ATTRIBUTE_LIST.contains(xxxRecDirectAttribute)) {
                registerBeanMethodsWithConcreteSetter(boAggregateClassInfo.getXboImplementation(), firstAttributeName, beanMethodCache);
            } else if (XBOConstants.xboDirectAttributes.contains(firstAttributeName)) {
                registerBeanGetter(boAggregateClassInfo.getXboImplementation(), firstAttributeName, beanMethodCache);
            } else {
                throw new ValidationException("Neither XQO nor XBO contains requested attribute: " + firstAttributeName);
            }
        }
    }

    public void validateAndCacheFromNthElement(BeanEntityPath path, int i, BeanMethodCache beanMethodCache) {
        for (int idx = i; idx < path.size() - 1; idx++) {
            Boolean isLast = false;

            if (idx == path.size() - 2)
                isLast = true;

            BeanEntityPathElement curElement = path.get(idx);
            BeanEntityPathElement nxtElement = path.get(idx + 1);

            Class clazz = XBOHelper.findXBOImplementation(curElement.getName());

            if (clazz == null) {
                String message = MessageFormat.format("No XBO interface implementation found: {0} - ensure it exists in the classpath", curElement.getName());
                throw new ValidationException(message);
            }

            Method getterMethod = registerBeanGetter(clazz, nxtElement.getName(), beanMethodCache);
            Class getterReturnType = getterMethod.getReturnType();

            if (nxtElement.isIndexed() && getterReturnType != List.class) {
                String message = MessageFormat.format("incompatibility of return type between declared type and returned: {0}", nxtElement.getName());
                throw new IllegalArgumentException(message);
            }

            if (isLast && !isAbstractListOrVector(getterReturnType)) {
                registerBeanMethodsWithConcreteSetter(clazz, nxtElement.getName(), beanMethodCache);
            }
        }
    }

    private void registerBeanMethodsWithConcreteSetter(Class clazz, String attribute, BeanMethodCache cache) {
        Method getterMethod = IntrospectionHelper.findGetter(clazz, attribute);
        if (getterMethod == null) {
            String message = MessageFormat.format("Referred class doesn't contain corresponding getter for: {0}", attribute);
            throw new ValidationException(message);
        }

        // find setter with getter's returned method
        Method setterMethod = IntrospectionHelper.findSetter(clazz, attribute, getterMethod.getReturnType());
        Class setterMethodAttribute = getterMethod.getReturnType();

        // if is interface or abstract class - provide concrete setter
        if (Modifier.isAbstract(setterMethodAttribute.getModifiers()) || setterMethodAttribute.isInterface()) {
            // HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK!
            if (setterMethodAttribute.equals(Calendar.class)) {
                setterMethodAttribute = GregorianCalendar.class;
            } else {
                setterMethodAttribute = XBOHelper.findXBOImplementation(setterMethodAttribute.getSimpleName());
            }
        }

        if (getterMethod != null) {
            // cache methods
            cache.put(clazz, getterMethod);
            cache.put(clazz, setterMethod, setterMethodAttribute);
        } else {
            String message = MessageFormat.format("Referred ''{0}'' object doesn''t contain corresponding getter for ''{1}'' attribute", clazz, attribute);
            throw new IllegalArgumentException(message);
        }
    }

    private Method registerBeanGetter(Class clazz, String attribute, BeanMethodCache cache) {
        Method getterMethod = IntrospectionHelper.findGetter(clazz, attribute);
        if (getterMethod == null) {
            String message = MessageFormat.format("Referred XBO class ''{0}'' doesn''t contain corresponding getter for ''{1}''", clazz.getName(), attribute);
            throw new IllegalArgumentException(message);
        }

        cache.put(clazz, getterMethod);
        return getterMethod;
    }

    public void assertMinimumPathLength(BeanEntityPath path) {
        if (path == null || path.size() < MINIMUM_ACCEPTED_PATH_DEPTH)
            throw new IllegalArgumentException();
    }

    private boolean isAbstractListOrVector(Class clazz) {
        return Vector.class.equals(clazz) ||
                List.class.equals(clazz);
    }

    private static class BOAggregateClassInfo {
        Class xqoInterface;
        Class xqoImplementation;
        Class xboInterface;
        Class xboImplementation;

        boolean validAsQueryObject;
        boolean validAsBusinessObject;

        private BOAggregateClassInfo(String name) {
            this.xboInterface = XBOHelper.findBusinessObjectInterface(name);
            this.xboImplementation = XBOHelper.findBusinessObjectImplementation(name);
            this.xqoInterface = XBOHelper.findQueryObjectInterface(name);
            this.xqoImplementation = XBOHelper.findQueryObjectImplementation(name);
            this.validAsBusinessObject = !(xboInterface != null ^ xboImplementation != null);
            this.validAsQueryObject = !(xqoInterface != null ^ xqoImplementation != null);

            if (!validAsBusinessObject) {
                throw new XBOInternalStructureException("Both XBO Interface and XBO Implementation must be either null or not null at the same time!");
            }

            if (!validAsQueryObject) {
                throw new XBOInternalStructureException("Both XQO Interface and XQO Implementation must be either null or not null at the same time!");
            }
        }

        public Class getXqoInterface() {
            return xqoInterface;
        }

        public Class getXqoImplementation() {
            return xqoImplementation;
        }

        public Class getXboInterface() {
            return xboInterface;
        }

        public Class getXboImplementation() {
            return xboImplementation;
        }

        public boolean isValidAsQueryObject() {
            return validAsQueryObject;
        }

        public boolean isValidAsBusinessObject() {
            return validAsBusinessObject;
        }
    }
}
