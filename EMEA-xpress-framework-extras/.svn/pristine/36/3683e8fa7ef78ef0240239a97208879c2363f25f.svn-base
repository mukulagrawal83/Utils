package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import com.fnis.ifx.xbo.v1_1.*;
import com.fnis.xes.framework.ext.processxbo.cache.BeanMethodCache;
import com.fnis.xes.framework.ext.processxbo.exceptions.TransformException;
import com.fnis.xes.framework.ext.processxbo.exceptions.XBOInternalStructureException;
import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPath;
import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPathElement;
import com.fnis.xes.framework.ext.processxbo.subject.TransformSubjectTarget;
import com.fnis.xes.framework.ext.processxbo.subject.TransformSubjectWithMethodCache;
import com.fnis.xes.framework.ext.processxbo.subject.helper.TransformSubjectHelper;
import com.fnis.xes.framework.ext.processxbo.exceptions.ValidationException;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class XBOTransformSubject extends TransformSubjectWithMethodCache {
    private static final Logger logger = Logger.getLogger(XBOTransformSubject.class);

    private Payload payload;
    private Object context;
    private static MethodCacheBuilder methodCacheBuilder;

    static {
        methodCacheBuilder = new MethodCacheBuilder();
    }

    public static void validate(String key) {
        BeanEntityPath path = TransformSubjectHelper.createBeanEntityPath(key);

        try {
            methodCacheBuilder.validateAndCacheMessagePath(path, getBeanMethodCache());
        } catch (TransformException e) {
            String message = MessageFormat.format("Validation of key {0} failed...", key);
            logger.error(message);
            throw new ValidationException(message, e);
        }
    }

    public XBOTransformSubject(Payload payload) {
        this(payload, null);
    }

    public XBOTransformSubject(Payload payload, Object ctx) {
        this.payload = payload;
        this.context = ctx;
    }

    public Payload getPayload() {
        return payload;
    }

    public Object getContext() {
        return context;
    }

    @Override
    public List<TransformSubjectTarget> get(String path) {
        BeanEntityPath beanEntityPath = TransformSubjectHelper.createBeanEntityPath(path);
        return get(beanEntityPath);
    }

    @Override
    public List<TransformSubjectTarget> get(BeanEntityPath path) {
        methodCacheBuilder.assertMinimumPathLength(path);

        BeanEntityPathElement messageRootPathElement = path.get(0);
        BeanEntityPathElement firstAttributePathElement = path.get(1);
        String firstAttributeName = firstAttributePathElement.getName();

        IFX2MessageInfo ifx2MessageInfo = IFX2MessageInfoProvider.provide(messageRootPathElement.getName());

        // ensure that payload represents messageRootPathElement; if not - return empty collection
        Object messageObject = XBOHelper.getMessageObject(ifx2MessageInfo, payload);
        if(messageObject == null) {
            return Collections.emptyList();
        }

        List<Object> businessObjects = XBOHelper.extractBusinessObjectsFromMessage(messageObject);
        if(businessObjects.isEmpty()) {
            return Collections.emptyList();
        }
        Object firstBusinessObject = businessObjects.get(0);

        // TODO: re-migrate this code!
        if(!isIFXObjectImplementation(ifx2MessageInfo, firstBusinessObject)) {
            return Collections.emptyList();
        }

        // handle Service direct attributes
        if (XBOConstants.SERVICE_DIRECT_ATTRIBUTE_LIST.contains(firstAttributeName)) {
            firstBusinessObject = (Service) payload.getProcess();
        }

        // process specific message
        if(firstBusinessObject instanceof Service) {
            businessObjects = new LinkedList<Object>();
            businessObjects.add(firstBusinessObject);
        } else if (firstBusinessObject instanceof XBO) {
            // handle cases when shortcut is used (data are stored directly in XBO - no matter where they have been stored earlier)
            XBOHelper.adjustQueryAsXBO(ifx2MessageInfo, path);
        } else if (firstBusinessObject instanceof XQO) {
            // special case - XQO doesn't contains xxxKeys directly (xxxInq@XQO/xxxKeys - no way)!
            if (firstAttributePathElement.getName().equals(ifx2MessageInfo.getObjectName() + "Keys")) {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }

        // create list of results - this list is going to be filled during path dismantling
        List<TransformSubjectTarget> targetList = new LinkedList<TransformSubjectTarget>();

        int idx = 0;
        for(Object businessObject : businessObjects) {
            if(firstAttributePathElement.getName().equals(ifx2MessageInfo.getObjectName() + "Rec")) {
                // handle /yyy/xxxRec[x] reference
                BeanEntityPath processingPath = new BeanEntityPath();
                processingPath.add(messageRootPathElement.getName());
                processingPath.add(ifx2MessageInfo.getObjectName() + "Rec" + "[" + idx + "]");
                recursiveGet(businessObject, path, 1, processingPath, targetList);
            } else  {
                // handle regular object
                recursiveGet(businessObject, path, 0, null, targetList);
            }
            idx++;
        }

        return targetList;
    }

    private boolean isIFXObjectImplementation(IFX2MessageInfo ifx2MessageInfo, Object businessObject) {
        String className = businessObject.getClass().getSimpleName();
        String ifx2ObjectName = ifx2MessageInfo.getObjectName();

        if(className.equals(ifx2ObjectName + "XBOImpl")) {
            return true;
        } else if (className.equals(ifx2ObjectName + "XQOImpl")) {
            return true;
        } else {
            return false;
        }
    }

    // this code asks for de-duplication - maybe some day...
    private void recursiveGet(Object currentObject, BeanEntityPath path, int startIndex, BeanEntityPath currentPath, List<TransformSubjectTarget> resultList) {
        if (currentPath == null)
            currentPath = new BeanEntityPath();

        // iterate to 'before last' path element, last is the one we need to look for
        for (int idx = startIndex; idx < path.size() - 1; idx++) {
            boolean isLast = (idx == path.size() - 2);

            BeanEntityPathElement element = path.get(idx);
            BeanEntityPathElement nextElement = path.get(idx + 1);

            // add root path element right now
            if (idx == 0) {
                currentPath.add(element.getName());
            }

            // path is broken (subsequent element gave 'null' reference)
            if (currentObject == null) {
                return;
            }

            Class clazz = currentObject.getClass();
            Method method = IntrospectionHelper.findGetter(clazz, nextElement.getName());
            if (method == null) {
                /* Another dirty hack:
                 *  1. Assumption: all setters and getters where validated already, but:
                 *  2. Selector valid for XQO only can be tested against XBO due to XBO implementation (thanks Mr. Zugarek!)
                 *  3. Based on above: when we are in XBO and we can't find attribute, for getters, just assume we do not have it!
                 */
                if(currentObject instanceof XBO) {
                    return;
                }
                else {
                    String message = MessageFormat.format("Internal problem occurred, could not find getter for {0} attribute in class {1}",
                            nextElement.getName(), clazz.getName());
                    throw new XBOInternalStructureException(message);
                }
            }

            Object nextObject = IntrospectionHelper.invokeMethod(method, currentObject);
            if (nextObject instanceof List) {
                // nextObject is a sequence
                List nextObjectList = (List) nextObject;

                if (nextObjectList.isEmpty()) {
                    return;
                }

                if (isLast) {
                    if (nextElement.isIndexed()) {
                        // if requested element is indexed (sequenced), we need only to select requested index
                        int objListIdx = nextElement.getIndex();
                        nextObject = nextObjectList.get(objListIdx);
                        BeanEntityPath newCurrentPath = new BeanEntityPath(currentPath);
                        newCurrentPath.add(nextElement.getName() + "[" + objListIdx + "]");
                        TransformSubjectTarget target = new TransformSubjectTarget(newCurrentPath.asString(), nextObject, context);
                        resultList.add(target);
                    } else {
                        // in other case add all elements into result list
                        for (int objListIdx = 0; objListIdx < nextObjectList.size(); objListIdx++) {
                            nextObject = nextObjectList.get(objListIdx);
                            BeanEntityPath newCurrentPath = new BeanEntityPath(currentPath);
                            newCurrentPath.add(nextElement.getName() + "[" + objListIdx + "]");
                            TransformSubjectTarget target = new TransformSubjectTarget(newCurrentPath.asString(), nextObject, context);
                            resultList.add(target);
                        }
                    }
                    break;
                } else {
                    // case: /A/B[]/C -> { /A/B[0]/C, /A/B[1]/C }
                    // B[] is nextObject in this case, so we need branch out recursive search to all elements of the list
                    if (nextElement.isIndexed()) {
                        int objListIdx = nextElement.getIndex();
                        nextObject = nextObjectList.get(objListIdx);
                        BeanEntityPath newCurrentPath = new BeanEntityPath(currentPath);
                        newCurrentPath.add(nextElement.getName() + "[" + objListIdx + "]");
                        recursiveGet(nextObject, path, idx + 1, newCurrentPath, resultList);
                    } else {
                        for (int objListIdx = 0; objListIdx < nextObjectList.size(); objListIdx++) {
                            nextObject = nextObjectList.get(objListIdx);
                            BeanEntityPath newCurrentPath = new BeanEntityPath(currentPath);
                            newCurrentPath.add(nextElement.getName() + "[" + objListIdx + "]");
                            recursiveGet(nextObject, path, idx + 1, newCurrentPath, resultList);
                        }
                    }
                    break;
                }
            } else {
                // single element
                if (isLast) {
                    if (nextObject != null) {
                        currentPath.add(nextElement.getName());
                        TransformSubjectTarget target = new TransformSubjectTarget(currentPath.asString(), nextObject, context);
                        resultList.add(target);
                    }
                } else {
                    currentPath.add(nextElement.getName());
                }
            }
            currentObject = nextObject;
        }
    }

    @Override
    public void set(String path, Object value) {
        BeanEntityPath beanEntityPath = TransformSubjectHelper.createBeanEntityPath(path);
        set(beanEntityPath, value);
    }

    // this code asks for de-duplication as well ... sorry - it was incremental work!
    @Override
    public void set(BeanEntityPath path, Object value) {
        BeanMethodCache beanMethodCache = getBeanMethodCache();
        BeanEntityPathElement messageRootPathElement = path.get(0);
        BeanEntityPathElement firstAttributePathElement = path.get(1);
        String firstAttributeName = firstAttributePathElement.getName();
        IFX2MessageInfo ifx2MessageInfo = IFX2MessageInfoProvider.provide(messageRootPathElement.getName());
        Object messageObject = XBOHelper.getMessageObject(ifx2MessageInfo, payload);
        List<Object> businessObjects = XBOHelper.extractBusinessObjectsFromMessage(messageObject);

        Object businessObject;

        // if you ask what is it for - let me explain you - it is just to skip the object path element which refers to particular record (xxxRec)
        int skipN = 0;
        if (XBOConstants.SERVICE_DIRECT_ATTRIBUTE_LIST.contains(firstAttributeName)) {
            businessObject = (Service) payload.getProcess();
        } else {
            if (ifx2MessageInfo.isResponse() && firstAttributePathElement.isIndexed()) {
                businessObject = businessObjects.get(firstAttributePathElement.getIndex());
                skipN = 1;
            } else {
                if (businessObjects.size() != 1)
                    throw new UnsupportedOperationException("Can't handle the object: referred as not indexed, but provided object contains more than one records");
                else
                    businessObject = businessObjects.get(0);
            }
        }

        Object currentObject = businessObject;
        BeanEntityPathElement previousPathElement = messageRootPathElement;
        for (int i = 1 + skipN; i < path.size(); i++) {
            Boolean isLast = (i == path.size() - 1);

            BeanEntityPathElement pathElement = path.get(i);
            String entryName = pathElement.getName();

            if (isLast) {
                if (currentObject == null) {
                    throw new IllegalArgumentException("Provided path could not be resolved. Check if all values on the path exists!");
                }

                if (pathElement.isIndexed()) {
                    Method method = beanMethodCache.findGetter(currentObject.getClass().getName(), entryName);

                    if (method == null) {
                        String message = MessageFormat.format("Can't find getter in {0} for attribute {1}", currentObject.getClass().getName(), entryName);
                        throw new IllegalArgumentException(message);
                    }

                    try {
                        Object listObject = method.invoke(currentObject);

                        if (!(listObject instanceof List)) {
                            throw new IllegalArgumentException("Object referred as sequence while is just regular object: " + listObject.getClass().getName());
                        }
                        XBOListHelper.setDataInXBOList(currentObject, entryName, (List) listObject, pathElement.getIndex(), value);

                    } catch (Exception e) {
                        throw new TransformException("Transform exception occurred with " + path.asString(), e);
                    }
                } else {
                    Method method = beanMethodCache.findSetter(currentObject.getClass().getName(), entryName, value.getClass());
                    if (method == null) {
                        throw new IllegalArgumentException("Can't find setter in " + currentObject.getClass().getName() + " for attribute " + entryName + " and parameter " + value.getClass());
                    }

                    IntrospectionHelper.invokeMethod(method, currentObject, value);
                }
            } else {
                // handle 'on-path' element to access last element which is meant to be set
                Method method = beanMethodCache.findGetter(currentObject.getClass().getName(), entryName);
                if (method == null)
                    throw new IllegalArgumentException("Can't find getter in " + currentObject.getClass().getName() + " for attribute " + entryName);

                currentObject = IntrospectionHelper.invokeMethod(method, currentObject);

                if (currentObject == null) {
                    throw new IllegalArgumentException("Provided path is not accessible for provided object: " + path.toString());
                }

                if (pathElement.isIndexed()) {
                    if (currentObject instanceof List)
                        currentObject = ((List) currentObject).get(pathElement.getIndex());
                    else
                        throw new IllegalArgumentException("Referenced indexed element " + pathElement.toString() + " while obtained on-path element is not a List instance");
                }

                previousPathElement = pathElement;
            }
        }
    }
}
