package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import java.util.regex.Matcher;

public class IFX2MessageInfo {
    private static Matcher ifxMessageMatcher;

    private boolean request;
    private boolean response;
    private boolean query;
    private boolean operation;

    private String objectName;
    private String actionId;
    private String messageType;

    public IFX2MessageInfo(String id) {
        parse(id);
    }

    public boolean isRequest() {
        return request;
    }

    public boolean isResponse() {
        return response;
    }

    public boolean isQuery() {
        return query;
    }

    public boolean isOperation() {
        return operation;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getActionId() {
        return actionId;
    }

    public String getMessageType() {
        return messageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IFX2MessageInfo)) return false;

        IFX2MessageInfo that = (IFX2MessageInfo) o;

        if (actionId != null ? !actionId.equals(that.actionId) : that.actionId != null) return false;
        if (messageType != null ? !messageType.equals(that.messageType) : that.messageType != null) return false;
        if (objectName != null ? !objectName.equals(that.objectName) : that.objectName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = objectName != null ? objectName.hashCode() : 0;
        result = 31 * result + (actionId != null ? actionId.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IFX2MessageInfo{" +
                "request=" + request +
                ", response=" + response +
                ", query=" + query +
                ", operation=" + operation +
                ", objectName='" + objectName + '\'' +
                ", actionId='" + actionId + '\'' +
                ", messageType='" + messageType + '\'' +
                '}';
    }

    private void parse(String id) {
        Matcher matcher = getMatcher(id);
        if (matcher.matches()) {
            parseGroups(matcher);
        } else {
            throw new IllegalArgumentException("Parsing error - provided string is not correct IFX 2.0 Message identifier");
        }
    }

    private void parseGroups(Matcher matcher) {
        objectName = matcher.group(1);
        actionId = matcher.group(2);
        messageType = matcher.group(3);

        if (actionId.equals("Inq") || actionId.equals("AuthInq") || actionId.equals("StatusInq")) {
            query = true;
        }

        if (messageType.equals("Rs")) {
            response = true;
        }

        if (messageType.equals("Rq")) {
            request = true;
        }

        if (actionId.equals("Oper")) {
            operation = true;
        }

    }

    private Matcher getMatcher(String id) {
        IFX2MessageTypeParser parser = IFX2MessageTypeParser.getInstance();
        return parser.getMatcher(id);
    }



}
