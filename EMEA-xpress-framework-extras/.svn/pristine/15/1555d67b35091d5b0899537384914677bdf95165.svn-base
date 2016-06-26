package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import com.fnis.ifx.xbo.v1_1.Data;
import com.fnis.ifx.xbo.v1_1.ListImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class XBOListHelper {
    // HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK! HACK!
    // this hack overcomes limits of XBO Rec implementation in which ListImpl is created and returned on each get() call
    // ListImpl set() method just doesn't work
    public static void setDataInXBOList(Object currentObject, String entryName, List listObject, int index, Object value) {
        if(listObject instanceof Vector) {
            Vector vector = (Vector) listObject;
            vector.set(index, value);
        } else if(listObject instanceof ListImpl) {
            ListImpl list = (ListImpl) listObject;
            List mutableList = XBOHelper.createMutableList(list);
            mutableList.set(index, value);

            JSONObject jsonObject = XBOHelper.convertListToJSONObject(entryName, mutableList);
            Data data = (Data) currentObject;
            JSONObject currentObjectData = data.getData();
            try {
                JSONObject merged = new JSONObject(currentObjectData, JSONObject.getNames(currentObjectData));
                for(String key : JSONObject.getNames(jsonObject))
                {
                    merged.put(key, jsonObject.get(key));
                }
                data.setData(currentObjectData);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UnsupportedOperationException("Unsupported operation exception - set operation used on unsupported list type: " + listObject.getClass().getName());
        }
    }
}
