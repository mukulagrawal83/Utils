package com.fnis.xes.framework.util;

import com.fnf.xes.util.xmlhelpers.sax.NamespaceAwareGenericHandler;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author trojanbug
 */
public class ReplacingContentHandler extends NamespaceAwareGenericHandler {

    private Set<String> seenElements;
    private Set<String> elementsToReplace;
    private List<String> requiredParentElements;
    private Mapper mapper;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        getSeenElements().add(localName);
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {


        if (getElementsToReplace().contains(localName)) {
            boolean inRequiredElement = false;
            for (String requiredParent : getRequiredParentElements()) {
                if (getSeenElements().contains(requiredParent)) {
                    inRequiredElement = true;
                    break;
                }
            }
            if (inRequiredElement) {
                String curString = currentTagContent.toString();
                currentTagContent.setLength(0);
                String mappingResult = getMapper().map(curString);
                if (mappingResult==null)
                    throw new RuntimeException("Mapping of value "+curString+" failed. No matching value found!");
                currentTagContent.append(mappingResult);
            }
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public void reset() {
        seenElements = new HashSet();
        super.reset();
    }

    public Set<String> getElementsToReplace() {
        return elementsToReplace;
    }

    public void setElementsToReplace(Set<String> elementsToReplace) {
        this.elementsToReplace = elementsToReplace;
    }

    public List<String> getRequiredParentElements() {
        return requiredParentElements;
    }

    public void setRequiredParentElements(List<String> requiredParentElements) {
        this.requiredParentElements = requiredParentElements;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @return the seenElements
     */
    public Set<String> getSeenElements() {
        return seenElements;
    }
}
