package com.fnis.xes.framework.ext.processxbo.subject.map;

import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPath;
import com.fnis.xes.framework.ext.processxbo.subject.TransformSubject;
import com.fnis.xes.framework.ext.processxbo.subject.TransformSubjectTarget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapTransformSubject extends TransformSubject {

    Map<String, Object> valueMap;

    public static void validate(String key) {
    }

    public MapTransformSubject() {
        this.valueMap = new HashMap();
    }

    @Override
    public List<TransformSubjectTarget> get(String key) {
        List<TransformSubjectTarget> list = new LinkedList<TransformSubjectTarget>();
        TransformSubjectTarget target = new TransformSubjectTarget(key, valueMap.get(key));
        list.add(target);
        return list;
    }

    @Override
    public List<TransformSubjectTarget> get(BeanEntityPath path) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public void set(String key, Object value) {
        valueMap.put(key, value);
    }

    @Override
    public void set(BeanEntityPath path, Object value) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public String dump() {
        StringBuffer sb = new StringBuffer();

        sb.append("MapTransformSubject(valueMap=[");
        int keySetSize = valueMap.keySet().size();
        int keyIndex = 0;
        for (String key : valueMap.keySet()) {
            keyIndex++;
            sb.append("'").append(key).append("'").append(" : ").append("'").append(valueMap.get(key)).append("'");
            if (keyIndex != keySetSize)
                sb.append(", ");
        }
        sb.append("])");

        return sb.toString();
    }

    @Override
    public String toString() {
        return "MapTransformSubject{" +
                "valueMap=" + valueMap +
                '}';
    }
}
