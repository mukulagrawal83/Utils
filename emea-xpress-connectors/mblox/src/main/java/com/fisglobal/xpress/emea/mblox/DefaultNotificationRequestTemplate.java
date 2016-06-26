package com.fisglobal.xpress.emea.mblox;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;

public class DefaultNotificationRequestTemplate implements NotificationRequestTemplate {
    public static final String DEFAULT_CLASSPATH_TEMPLATE_FILE_LOCATION = "NotificationRequestTemplate.xml";

    private final String template;

    public DefaultNotificationRequestTemplate() {
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_CLASSPATH_TEMPLATE_FILE_LOCATION);

        try {
            template = IOUtils.toString(inStream);
            IOUtils.closeQuietly(inStream);
        } catch (IOException e) {
            String msg = "Could not read Notification Request Template file from classpath: " + DEFAULT_CLASSPATH_TEMPLATE_FILE_LOCATION;
            throw new RuntimeException(msg, e);
        }
    }

    public DefaultNotificationRequestTemplate(String classpathTemplateFileLocation) {
        if (StringUtils.isBlank(classpathTemplateFileLocation))
            throw new IllegalArgumentException("classpathTemplateFileLocation is blank");

        InputStream inStream = getClass().getClassLoader().getResourceAsStream(classpathTemplateFileLocation);
        try {
            template = IOUtils.toString(inStream);
            IOUtils.closeQuietly(inStream);
        } catch (IOException e) {
            String msg = "Could not read Notification Request Template file from classpath: " + DEFAULT_CLASSPATH_TEMPLATE_FILE_LOCATION;
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getTemplate() {
        return template;
    }
}
