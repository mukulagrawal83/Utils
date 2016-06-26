package com.fnis.xes.framework.ext.processxbo.path;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BeanEntityPath implements Iterable<BeanEntityPathElement> {
    private List<BeanEntityPathElement> beanEntityPathElements;

    public BeanEntityPath() {
        this.beanEntityPathElements = new LinkedList<BeanEntityPathElement>();
    }

    public BeanEntityPath(List<BeanEntityPathElement> beanEntityPathElements) {
        this.beanEntityPathElements = new LinkedList<BeanEntityPathElement>(beanEntityPathElements);
    }

    public BeanEntityPath(BeanEntityPath oldBeanEntityPath) {
        this.beanEntityPathElements = new LinkedList<BeanEntityPathElement>(oldBeanEntityPath.beanEntityPathElements);
    }

    public BeanEntityPath(BeanEntityPathElement[] entityPathElementsArray) {
        assertNotNullOrEmpty(entityPathElementsArray);
        this.beanEntityPathElements = new LinkedList<BeanEntityPathElement>();
    }

    public BeanEntityPathElement get(int index) {
        return beanEntityPathElements.get(index);
    }

    public void add(String s) {
        BeanEntityPathElement e = new BeanEntityPathElement(s);
        beanEntityPathElements.add(e);
    }

    public void remove(int index) {
        beanEntityPathElements.remove(index);
    }

    public int size() {
        return beanEntityPathElements.size();
    }

    public String asString() {
        if (beanEntityPathElements.size() == 0)
            return "";
        else {
            StringBuffer sb = new StringBuffer();

            for (BeanEntityPathElement element : this) {
                sb.append("/");
                sb.append(element.getName());
                if (element.isIndexed()) {
                    sb.append("[");
                    sb.append(element.getIndex());
                    sb.append("]");
                }
            }
            return sb.toString();
        }
    }

    public Iterator<BeanEntityPathElement> iterator() {
        return beanEntityPathElements.iterator();
    }

    public String toString() {
        return "BeanEntityPath{" +
                "beanEntityPathElements=" + beanEntityPathElements +
                '}';
    }

    public boolean containsPathElement(String s) {
        for (BeanEntityPathElement element : this) {
            if (s.equals(element.getName())) {
                return true;
            }
        }
        return false;
    }

    private void assertNotNullOrEmpty(BeanEntityPathElement[] entityPathElementsArray) {
        if (entityPathElementsArray == null || entityPathElementsArray.length == 0) {
            throw new IllegalArgumentException("provided argument is null or empty");
        }
    }
}
