package com.fnis.xes.framework.ext.processxbo.path;

import java.util.regex.Matcher;

public class BeanEntityPathElement {
    private static PathElementMatcherFactory matcherFactory = new PathElementMatcherFactory();

    private String name;
    private boolean sequence;
    private int sequenceIndex;

    public BeanEntityPathElement(String definition, int index) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    public BeanEntityPathElement(String definition) {
        Matcher matcher = matcherFactory.getMatcher(definition);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Bean reference could not be parsed: " + definition);
        }

        this.name = matcher.group(1);

        if (matcher.group(2) != null) {
            String matchedIndexGroup = matcher.group(2);
            // omit first and last character - literally '[' and ']'
            String integerToParse = matchedIndexGroup.substring(1, matchedIndexGroup.length() - 1);
            this.sequence = true;
            this.sequenceIndex = Integer.parseInt(integerToParse);
        } else {
            this.sequence = false;
            this.sequenceIndex = -1;
        }
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return sequenceIndex;
    }

    public boolean isIndexed() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeanEntityPathElement)) return false;

        BeanEntityPathElement that = (BeanEntityPathElement) o;

        if (sequence != that.sequence) return false;
        if (sequenceIndex != that.sequenceIndex) return false;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (sequence ? 1 : 0);
        result = 31 * result + sequenceIndex;
        return result;
    }
}
