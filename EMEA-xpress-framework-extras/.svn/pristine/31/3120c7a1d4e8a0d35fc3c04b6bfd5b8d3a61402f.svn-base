package com.fnis.xes.framework.ext.processxbo.subject.helper;

import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPath;
import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPathElement;

import java.util.LinkedList;
import java.util.List;

public class TransformSubjectHelper {
    public static BeanEntityPath createBeanEntityPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid path provided.");
        }

        if (path.charAt(0) != '/') {
            throw new IllegalArgumentException("Invalid path provided - missing root separator.");
        }

        return createEntryPath(path.substring(1).split("/"));
    }

    private static BeanEntityPath createEntryPath(String[] entryPathArray) {
        List<BeanEntityPathElement> beanEntityPathElementList = new LinkedList<BeanEntityPathElement>();

        for (String pathElementName : entryPathArray) {
            BeanEntityPathElement element = new BeanEntityPathElement(pathElementName);
            beanEntityPathElementList.add(element);
        }

        return new BeanEntityPath(beanEntityPathElementList);
    }
}
