/*
 *	$Header: /view/system_view/vobstore/xes/CVS/xesFramework2/CVSROOT/src/prod/com/fnf/xes/tplegacy/component/framework/XMLMessageSDB.java,v 1.2 2005/11/03 16:28:37 mfwang Exp $
 *	TouchPoint Java Enterprise Services Library
 *	Copyright 2003-2004, TouchPoint Solutions a division of Fidelity Information Services
 *  All rights reserved.
 *
 * 	WARNING: This file contains CONFIDENTIAL and PROPRIETARY information and
 * 	INTELLECTUAL DATA of TouchPoint Solutions, and is protected by copyright
 * 	law and international treaties.  Unauthorized reproduction or distribution
 * 	may result in severe civil and criminal penalties, and will be prosecuted
 * 	to the maximum extent possible under the law.
 */
package com.fnf.xes.tplegacy.component.framework;

//import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Added for XES-5889
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.apache.log4j.Level;
import com.fnf.xes.framework.util.FastInfoSetUtility;
import com.fnf.xes.tplegacy.internal.eo.EOBaseException;
import com.fnf.xes.tplegacy.internal.eo.EOBuilderFactory;
import com.fnf.xes.tplegacy.internal.eo.ESLObject;
import com.fnf.xes.tplegacy.internal.eo.XMLUtil;
import com.fnf.xes.tplegacy.internal.eoeslimpl.EOEslImpl;


/**
 *	This class implements Self Defined Buffer Interface (ISelfDefinedBuffer) and contains
 *  reference to the TouchPoint XML message in JSON object hierarchy.  
 *  
 *
 *
 *	@tp.creationdate 2003-05-05
 *	@author TP ESL DEV
 *	@version 0.1.0
 *	@tp.revision $Revision: 1.2 $
 *	@tp.modified $Date: 2005/11/03 16:28:37 $
 *	@tp.copyright 2003, TouchPoint Solutions, all rights reserved.
 */
public class XMLMessageSDB implements ISelfDefinedBuffer {

	/**
	 * Date/Time formats used by SimpleDateFormat to format and parse
	 * date objects and strings.
	 * Not sure why these are public but we can leave them this
	 * way for now.
	 */
	public final static String ISO_DATE_FORMAT = "yyyy-MM-dd";
	public final static String ISO_TIME_FORMAT = "HH:mm:ss";
	public final static String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String US_DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public final static String US_DATE_FORMAT = "MM/dd/yyyy";
	// Following constants added for XES-5889
	public static final String IFX_DATETIME_TZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";
	public static final String IFX_DATETIME_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.000-0000";
	
	/**
	 * DateFormat objects are expensive to create so we create them once
	 * and synchronize their usage.  So don't go thinking that you can
	 * use these outside of a synchronize block or you will find yourself
	 * in hot water.
	 */
	private final static SimpleDateFormat isoDateFormatter = new SimpleDateFormat(ISO_DATE_FORMAT);
	private final static SimpleDateFormat isoDateTimeFormatter = new SimpleDateFormat(ISO_DATE_TIME_FORMAT);
	private final static SimpleDateFormat usDateFormatter = new SimpleDateFormat(US_DATE_FORMAT);
	private final static SimpleDateFormat usDateTimeFormatter = new SimpleDateFormat(US_DATE_TIME_FORMAT);
	// Added for XES-5889
	private final static SimpleDateFormat ifxDateTimeFormatter = new SimpleDateFormat(IFX_DATETIME_FORMAT);


	
	/**
	 * Pattern objects are also expensive to create and compile.  They are not 
	 * mutable and are thus thread safe.
	 * To perform a match you must first call the matcher method and then then matches.
	 * <code>
	 * boolean isMatch = ISO_DATE_TIME_PATTERN.matcher(someDateString).matches();
	 * </code>
	 */
	private final static Pattern ISO_DATE_PATTERN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
	private final static Pattern ISO_DATE_TIME_PATTERN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
	private final static Pattern US_DATE_PATTERN = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
	private final static Pattern US_DATE_TIME_PATTERN = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4}) (\\d{2}):(\\d{2}):(\\d{2})");
	// Added for XES-5889
	public static final Pattern IFX_DATETIME_TZ_PATTERN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})(T(\\d{2}):(\\d{2}):(\\d{2}).*[a-zA-Z0-9])?+");

	private final static List<String> ignoreTagList = Arrays.asList(new String[]{"SERVICE_ERROR","SERVICE_ERROR_MSG"});

	private final static Logger logger = Logger.getLogger(XMLMessageSDB.class);
	// Added for XES-5889
	private EOBuilderFactory jsonBuilderFactory = EOBuilderFactory.createEOBuilderFactory();
	private static final String TOUCHPOINT = "TouchPoint";
	private static final String ACTION = "action";
	private static final String SERVICE_NAME = "name";

//	private boolean sdbTransactionState = false;
	private Document sdbDocument = null;
//	private Node rootNode = null;
//	private Element actionNode = null;
//	private HashMap map = null; //CVNS
//	private boolean sdbDocumentState = false; //CVNS
//	private HashMap<String, NodeList> iterationMap = null;
//	private static DocumentBuilder docb = null;
	
	// Added for XES-5889
	String serviceName;
	String version = "1.0";
	boolean hierarchical = false;
	ESLObject root;
	ESLObject action;
	
	XMLUtil xmlUtil = new XMLUtil();
	
	/**
	 * Getter method for root
	 * @return ESLObject
	 */
	public ESLObject getRoot() {
		return root;
	}
	
	/**
	 * Getter method for action
	 * @return ESLObject
	 */
	public ESLObject getAction() {
		return action;
	}
	
	/**
	 * This default constructor internally calls init() method to 
	 * initialize root and action ESLObject objects.
	 * @throws ParserConfigurationException
	 */
	public XMLMessageSDB()throws ParserConfigurationException {
		init();
	}
	
	/**
	 * This method uses JSONBuilderFactory to create base JSON object hierarchy for
	 * <TouchPoint> and <action> elements.   
	 * @throws ParserConfigurationException
	 */
	private void init() throws ParserConfigurationException {
		try {
			ESLObject[] eoBaseHierarchy = jsonBuilderFactory.createESLObjectBaseHierarchy();
			root = eoBaseHierarchy[0];
			action = eoBaseHierarchy[1];
		} catch (EOBaseException e) {
			// Hack to make backward compactible and avoid changing client interface.
			throw new ParserConfigurationException( e.getMessage());
		}
		
	}
	
	/**
	 * This method returns String representation of the Document object
	 * @param doc
	 * @return String 
	 * @throws TransformerConfigurationException
	 */
	private String getString( Document doc ) throws TransformerConfigurationException {
		return xmlUtil.getString(doc);
	}
	
	/**
	 * Overloaded constructor initializes action member variable from a given ESLObject
	 * @param rootObj
	 */
	public XMLMessageSDB(ESLObject rootObj ) throws DOMException {
		root = rootObj;
		try {
			ESLObject tp = root.getESLObject( TOUCHPOINT );
			action = tp.getESLObject(ACTION);
		} catch (EOBaseException e) {
			throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "Invalid Object");
		}
	}
	
//	public XMLMessageSDB()
//		throws ParserConfigurationException, FactoryConfigurationError, DOMException {
//
//		if(docb == null)
//			docb = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		sdbDocument = docb.newDocument();
//		Element tpElement = sdbDocument.createElement("TouchPoint");
//
//		Element anel = sdbDocument.createElement("action");
//		tpElement.appendChild(anel);
//
//		sdbDocument.appendChild(tpElement);
//
//		rootNode = tpElement;
//		actionNode = anel;
//
//	}
	/**
	 * Overloaded constructor initializes root and action member variables 
	 * from a given Document object
	 * @param adoc
	 */
	public XMLMessageSDB(Document adoc) throws DOMException {

//		sdbDocument = adoc;
//		if (sdbDocument.hasChildNodes()) {
//			rootNode = sdbDocument.getDocumentElement();
//			//actionNode = (Element)rootNode.getFirstChild();
//			NodeList list = rootNode.getChildNodes();
//			Node tmp;
//			for (int i=0; i<list.getLength(); i++) {
//				tmp = list.item(i);
//				if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("action"))) {
//					actionNode = (Element)tmp;
//					break;
//				}
//			}
//		}
		try {
			String msg = getString(adoc);
			root = jsonBuilderFactory.rootESLObject(msg);
			ESLObject tp = root.getESLObject( TOUCHPOINT );
			action = tp.getESLObject(ACTION);
		} catch (EOBaseException e) {
			throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "Invalid Object : "+ e.getMessage());
		} catch(Throwable ex) {
			if ( logger.isEnabledFor(Level.FATAL))
				logger.fatal("XMLMessageSDB: Error transforming DOM to JSONObject",ex);
			throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, "Invalid Object");
		}
	}
	
	/**
	 * 
	 * @param adoc
	 * @throws UnsupportedException
	 */
	public void setDom(Document adoc)
	{
//		sdbDocument = adoc;
//		if (sdbDocument.hasChildNodes()) {
//			rootNode = sdbDocument.getDocumentElement();
//			NodeList list = rootNode.getChildNodes();
//			Node tmp;
//			for (int i=0; i<list.getLength(); i++) {
//				tmp = list.item(i);
//				if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("action"))) {
//					actionNode = (Element)tmp;
//					break;
//				}
//			}
//		}
		throw new UnsupportedOperationException("setDom(Document adoc)");
	}

	/**
	 * Builds Document object
	 * @return Document
	 */
	public Document document() {
//		return sdbDocument;
		return jsonBuilderFactory.document(root);
	}
	
	/**
	 * 
	 * @param path
	 * @param occ
	 * @return
	 */
	private Object find(String path, int occ) {
		
		return action.find(path, occ);
	}
	
	/**
	 * 
	 * @param path
	 * @param occ
	 * @return
	 */
	private Node find(String path, long occ) {
		Node anode = null;

		//		FIXME: There must be a safety check for this.
		//		if(occ < 0) throw CoreException();
		NodeList alist = sdbDocument.getElementsByTagName(path);
		for (int i = 0; i < alist.getLength(); ++i) {
			if (occ == 0) {
				anode = alist.item(i);
				break;
			}
			occ--;
		}

		return anode;
	}
	
	/**
	 * @param path
	 * @param value
	 */
	public boolean add(String path, long value) {
		try {
			return add(path, String.valueOf(value));
		} catch (java.lang.Exception ex) {
			logger.warn("java.lang.Exception adding an element " + ex.getMessage(),ex);
			return false;
		}
	}
	
	/**
	 * @param path
	 * @param value
	 */
	public boolean add(String path, int value) {
		try {
			return add(path, String.valueOf(value));
		} catch (java.lang.Exception ex) {
			logger.warn("java.lang.Exception adding an element " + ex.getMessage(),ex);
			return false;
		}
	}
	
	/**
	 * @param path
	 * @param value
	 */
	public boolean add(String path, double value) {
		try {
			return add(path, String.valueOf(value));
		} catch (java.lang.Exception ex) {
			logger.warn("java.lang.Exception adding an element " + ex.getMessage(),ex);
			return false;
		}
	}

	/**
	 * CVNS - Modified to flush the data to XML document based on the state
	 * @param path
	 * @param value
	 */
	 public boolean add(String path, String value) {
//		try {
//			/*
//			 * This really isn't needed any more.
//			 */
//			if (sdbDocumentState == true) {
//				if (map == null) {
//					map = new HashMap();
//				}
//				map.put(path, value);
//			} else {
//				add(path, value, true);
///*				if(ignoreTagList.contains(path))
//					return true;				
//				//if(actionNode.getNodeType()==Node.ELEMENT_NODE) {
//					Element element = sdbDocument.createElement(path);
//					//Node test = sdbDocument.getElementsByTagName("action").item(0);
//					//((Element)actionNode).appendChild(element);
//				actionNode.appendChild(element);
//					//test.appendChild(element);
//					if (value != null)
//						element.appendChild(sdbDocument.createTextNode(value));
//				//}
//*/			}
//		} catch (java.lang.Exception ex) {
//			System.out.println("java.lang.Exception adding an element "
//					+ ex.getMessage());
//			ex.printStackTrace();
//			return false;
//		}
		
		 try {
			 if( value == null ) value = "";
				action.accumulate(path, value);
		} catch (EOBaseException e) {
			return false;
		}
		return true;
	 }
	 /**
	  * 
	  * @param path
	  * @param value
	  * @param flag
	  * @return boolean
	  */
	 public boolean add(String path, String value, boolean flag) {
/*			if(flag && ignoreTagList.contains(path))
				return true;*/
//			Element element = sdbDocument.createElement(path);
//			actionNode.appendChild(element);
//			if (value != null)
//				element.appendChild(sdbDocument.createTextNode(value));			
//		 return true;
		 return add(path, value);
	 }
	 /**
	  * Adds a date to the internal DOM.  The Date object is 
	  * serialized to a string using a SimpleDateFormat object.
	  * The SimpleDateFormat object is static for this class and
	  * its usage is synchronized.  It is faster to synchronize than
	  * it is to create a new format object each time.
	  * 
	  * @param path - String value of the tag name to add the data to.
	  * @param value - Date value to be added to the DOM
	  * @param bandDate - boolean indicator if value is date or datetime.
	  * If bandDate is true then the value should be both date and time.
	  */
	public boolean add(String path, Date value, boolean bandDate) {
		
		if( value == null ) return false;
	
		String dateString = null;
	
		if (bandDate == true) {
			synchronized(isoDateTimeFormatter) {
				dateString = isoDateTimeFormatter.format(value);
			}
		} else {
			synchronized(isoDateFormatter) {
				dateString = isoDateFormatter.format(value);
			}
		}
		return add(path, dateString);
	}

	 /**
	  * Modifies and existing item in the internal DOM with a date
	  * value. The Date object is serialized to a string using 
	  * a SimpleDateFormat object. SimpleDateFormat object is 
	  * static for this class and its usage is synchronized.  
	  * It is faster to synchronize than it is to create 
	  * a new format object each time.
	  * 
	  * @param path - String value of the tag name to add the data to.
	  * @param value - Date value to be added to the DOM
	  * @param bandDate - boolean indicator if value is date or datetime.
	  * If bandDate is true then the value should be both date and time.
	  */
	public boolean chg(String path, Date value, int occ, boolean bandDate) {

		if( value == null ) return false;

		String dateString = null;

		if (bandDate == true) {
			synchronized(isoDateTimeFormatter) {
				dateString = isoDateTimeFormatter.format(value);
			}
		} else {
			synchronized(isoDateFormatter) {
				dateString = isoDateFormatter.format(value);
			}
		}
		return chg(path, dateString, occ);
	}

	/**
	 * 
	 */
	public boolean chg(String path, Date value, boolean bandDate) {

		return chg(path, value, 0, bandDate);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, long value, int occ) {
		return chg(path, String.valueOf(value), occ);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, int value, int occ) {
		return chg(path, String.valueOf(value), occ);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, double value, int occ) {
		return chg(path, String.valueOf(value), occ);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, String value, int occ) {
//		NodeList elements = sdbDocument.getElementsByTagName(path);
//		if ((elements != null && elements.getLength() >= occ)) {
//
//			Node node = elements.item(occ);
//			if (node instanceof Element) {
//				NodeList children = node.getChildNodes();
//				if (children.getLength() == 1)
//					children.item(0).setNodeValue(value);
//				return true;
//			}
//		}
//		return true;
		return action.chg(path, value, occ);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, long value) {
		return chg(path, String.valueOf(value), 0);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, int value) {
		return chg(path, String.valueOf(value), 0);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, double value) {
		return chg(path, String.valueOf(value), 0);
	}
	
	/**
	 * 
	 */
	public boolean chg(String path, String value) {
		return chg(path, value, 0);
	}
	
	/**
	 * 
	 */
	public boolean get(long[] value, String path, int occ) {
		return true;
	}
	
	/**
	 * 
	 */
	public boolean get(int[] value, String path, int occ) {
		return true;
	}
	
	/**
	 * 
	 */
	public boolean get(String[] value, String path, int occ) {
		return true;
	}
	
	/**
	 * 
	 */
	public boolean get(double[] value, String path, int occ) {
		return true;
	}
	
	/**
	 * 
	 */
	public boolean get(long[] value, String path) {
		return true;
	}
	
	/**
	 * 
	 */
	public boolean get(int[] value, String path) {
		return true;
	}
	
	/**
	 * 
	 */
	public boolean get(String[] value, String path) {
		return true;
	}
	
	/**
	 * 
	 */
	public boolean get(double[] value, String path) {
		return true;
	}
	
	/**
	 * 
	 */
	public String get(String path) {
		return get(path, 0);
	}
	
	/**
	 * 
	 */
	public String get(String path, int occ) {
		
//		String value = "";
//		if (occ >= 0) {		
////			NodeList elements = sdbDocument.getElementsByTagName(path);
//			NodeList elements = null;
//			if(iterationMap != null){
//				if(!iterationMap.containsKey(path))
//					iterationMap.put(path, sdbDocument.getElementsByTagName(path));
//				elements = iterationMap.get(path);
//			}else{
//				elements = sdbDocument.getElementsByTagName(path);
//			}			
//			if (null != elements) {
//				Node node = elements.item(occ);
//				if ((node != null) && (node instanceof Element)) {
//					NodeList children = node.getChildNodes();
//					if (children != null) {
//						Node nValue = children.item(0);
//						if (nValue != null) {
//							value = nValue.getNodeValue();
//							if (value == null) value = "";
//						}
//					}
//				}
//			}
//		}
//		return value;
		return action.get(path, occ);
	}
	
	/**
	 * 
	 * @param path
	 * @param value
	 * @return
	 */
	public boolean replaceAll(String path, String value) {
		try {
			return jsonBuilderFactory.replaceAdd( action,path,value);
		} catch (EOBaseException e) {
			// if there is an exception, ignore and return false;
		}
		return false;
	}
	
	/**
	 * //CVNS - Formatting to remove commas from input 
	 */
	public long getlong(String path, int occ) {
		String sValue = get(path, occ);

		try {
			NumberFormat formatter = NumberFormat.getNumberInstance();
			Number number = formatter.parse(sValue);
			return number.longValue();
		} catch (ParseException pe) {
			return 0;
		}
	}
	
	/**
	 * 
	 */
	public double getdouble(String path, int occ) {
		String sValue = get(path, occ);
		return Double.parseDouble(sValue);
	}
	
	/**
	 * 
	 */
	public long getlong(String path) {
		return getlong(path, 0);
	}
	
	/**
	 * 
	 */
	public double getdouble(String path) {
		return getdouble(path, 0);
	}
	
	/**
	 * 
	 */
	public Long getLong(String path) {
		return getLong(path, 0);
	}
	
	/**
	 * 
	 */
	public Double getDouble(String path) {
		return getDouble(path, 0);
	}
	
	/**
	 * 
	 */
	public Long getLong(String path, int occ) {
		String sValue = get(path, occ);
		return Long.valueOf(sValue);
	}
	
	/**
	 * 
	 */
	public Double getDouble(String path, int occ) {
		String sValue = get(path, occ);
		return Double.valueOf(sValue);
	}
	
	/**
	 * 
	 */
	public Date getDate(String path) {
		return getDate(path, 0);
	}

	/**
	 * CVNS - Modified getDate(path, occ) to support multiple date formats.
	 *
	 */
	public Date getDate(String path, int occ) {
		return getDateTime(path, occ);
	}
	
	/**
	 * 
	 */
	public Date getDateTime(String path) {
		return getDateTime(path, 0);
	}

	/**
	 * Looks up an elmeent using the specified path value.
	 * The value looked up will be a String and is converted to
	 * a Date object using SimpleDateFormat.
	 * This lookup can support both ISO Date and ISO Date/Time so
	 * a pattern match is applied to the string to determine what the
	 * appropriate format should be.
	 * If the ISO Date pattern matches then that is the formatter used
	 * otherwise it is assumed that the format should be Date/Time.
	 * 
	 * @param path - String path name
	 * @param occ - Which value occurance of the tag to return.
	 * @return Date - will be null if ParseException occurs.
	 */
	public Date getDateTime(String path, int occ) {

		Date date = null;
		String sValue = null;
		try {
			sValue = get(path, occ);
			if ((null != sValue) && (!sValue.equals(""))) {
				if (ISO_DATE_PATTERN.matcher(sValue).matches()) {
					synchronized (isoDateFormatter) {
						isoDateFormatter.setLenient(false);
						date = isoDateFormatter.parse(sValue);
					}
				} else if (ISO_DATE_TIME_PATTERN.matcher(sValue).matches()) {
					synchronized (isoDateTimeFormatter) {
						isoDateTimeFormatter.setLenient(false);
						date = isoDateTimeFormatter.parse(sValue);
					}
				} else if (US_DATE_PATTERN.matcher(sValue).matches()) {
					synchronized (usDateFormatter) {
						usDateFormatter.setLenient(false);
						date = usDateFormatter.parse(sValue);
					}
				} else if (US_DATE_TIME_PATTERN.matcher(sValue).matches()) {
					synchronized (usDateTimeFormatter) {
						usDateTimeFormatter.setLenient(false);
						date = usDateTimeFormatter.parse(sValue);
					}
				} else if (IFX_DATETIME_TZ_PATTERN.matcher(sValue).matches()){
					sValue = fixIfxDateTimeTZone(sValue);
					synchronized (ifxDateTimeFormatter) {
						ifxDateTimeFormatter.setLenient(false);
						date = ifxDateTimeFormatter.parse(sValue);
					}
				} else {
					logger.warn("Unrecognized date format: ["+sValue+"] Will return null.");
				}
			}
		} catch (ParseException e) {
			logger.warn("Cannot parse date string: ["+sValue+"] Will return null.");
		}
		return date;
	}
	
	/**
	 * 
	 * @param strDateTime
	 * @return
	 */
	public static String fixIfxDateTimeTZone(String strDateTime) {
		strDateTime = strDateTime.trim();
		int i = strDateTime.length();
		String str;
		if (i == 10) { // yyyy-MM-dd
			str = "T00:00:00.000" + getTimeZone();
			strDateTime = strDateTime + str;
		} else if (i == 16) { // yyyy-MM-ddTHH:mm
			str = ":00.000" + getTimeZone();
			strDateTime = strDateTime + str;
		} else if (i == 19) { // yyyy-MM-ddTHH:mm:ss
			str = ".000" + getTimeZone();
			strDateTime = strDateTime + str;
		} else if (i == 26) { // yyyy-MM-ddTHH:mm:ss.SSSSSS
			// remove the last 3 digits of fraction of second
			strDateTime = strDateTime.substring(0, 23);
			strDateTime = strDateTime + getTimeZone();
		} else if (i == 32) {// yyyy-MM-ddTHH:mm:ss.SSSSSSZ
			// remove the last 3 digits of fraction of second
			strDateTime = strDateTime.substring(0, 23)
					+ strDateTime.substring(26);
			// remove the colon separating hour and minute figures in the time
			// zone
			i = strDateTime.lastIndexOf(':');
			strDateTime = strDateTime.substring(0, i)
					+ strDateTime.substring(i + 1);
		} else if (i == 22) { // yyyy-MM-ddTHH:mmZ
			strDateTime = strDateTime.substring(0, 16) + ":00.000"
					+ strDateTime.substring(16);
			// remove the colon separating hour and minute figures in the time
			// zone
			i = strDateTime.lastIndexOf(':');
			strDateTime = strDateTime.substring(0, i)
					+ strDateTime.substring(i + 1);
		} else if (i == 25) { // yyyy-MM-ddTHH:mm:ssZ
			strDateTime = strDateTime.substring(0, 19) + ".000"
					+ strDateTime.substring(19);
			// remove the colon separating hour and minute figures in the time
			// zone
			i = strDateTime.lastIndexOf(':');
			strDateTime = strDateTime.substring(0, i)
					+ strDateTime.substring(i + 1);
		}
		return strDateTime;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private static String getTimeZone() {
		SimpleDateFormat sdf = new SimpleDateFormat("Z");
		Date dt = new Date();
		return sdf.format(dt);
	}
	
	//	transaction processing methods
	/**
	 * 
	 */
	public boolean isInTran() {
		return true;
	}
	/**
	 *	Checks the transcation status. A buffer can be in a load transaction which disables internal index. This
	 *	implementation does not contain seperate indexes so that this method returns true.
	 *	@return		Answers a Boolean true to indicate a successful start of a buffer transaction.
	 */
	public boolean tbegin() {
//		sdbTransactionState = true;
		return true;
	}

	/**
	 *
	 */
	public boolean tend() {
//		sdbTransactionState = false;
		return true;
	}

	public boolean valid() {
		return true;
	}
	/**
	 *	@param path	A String containing the XML field name.
	 */
	public boolean exist(String path) {
		
		Object obj = find(path, 0);
		return (obj != null);
//		Node anode = find(path, 0);
//
//		return (anode != null);

		/*
			if (count(path) > 0) {
			  return true;
			}
			else {
			  return false;
			}
		 */
	}

	/**
	 *  <B>CVNS</B>
	 *	@param path	A String containing the XML field name.
	 * @param occ
	 */
	public boolean exist(String path, int occ) {
		Object obj = find(path, occ);
		return (obj != null);
	}
	
	/**
	 * 
	 */
	public long length(String path, long record) {
		String sValue = get(path, 0);

		return sValue.length();
	}
	
	/**
	 * 
	 */
	public long count(String path) {
//		NodeList elements = sdbDocument.getElementsByTagName(path);
//		if (elements == null) {
//			return 0;
//		} else {
//			return elements.getLength();
//		}
		return action.count(path);
	}
	
	/**
	 * 
	 */
	public long count() {
		return 0;
	}
	
	/**
	 * 
	 */
	public boolean del(String path) {
		return del(path, 0);
	}

	/**
	 * Delete specified node or multiple occurrences of the specified node
	 * To delete multiple occurrences of a node, pass -1 as argument for occ
	 */
	public boolean del(String path, int occ) {
		
		if( occ == -1 ){
			return (action.remove(path)!=null);
		}
		
		return action.del(path, occ);
		
//		//Changed by Venu
//		NodeList elements = null;
//		if(iterationMap != null){
//			if(!iterationMap.containsKey(path))
//				iterationMap.put(path, sdbDocument.getElementsByTagName(path));
//			elements = iterationMap.get(path);
//		}else{
//			elements = sdbDocument.getElementsByTagName(path);
//		}		
////		NodeList elements = sdbDocument.getElementsByTagName(path);
//		if(elements != null){
//			int nbrOfChildTags = elements.getLength();
//			if (nbrOfChildTags >= occ) {
//				if (occ == -1) {
//					for (int i = (nbrOfChildTags - 1); i >= 0; i--) {
//						Node node = elements.item(i);
//						if (node instanceof Element)
//							actionNode.removeChild(node);
//					}
//					return true;
//				} else {
//					Node node = elements.item(occ);
//					if (node instanceof Element)
//						actionNode.removeChild(node);
//					return true;
//				}
//			}
//		}
//		return false;
	}

	/**
	 * Delete all child nodes of 'action'
	 */
	public boolean deleteAll() {
		
		try {
			jsonBuilderFactory.deleteAll( root);
		} catch (EOBaseException e) {
			logger.error("deleteAll has failed.");
			return false;
		}
		return true;
		
//		if (actionNode != null) {
//			if (actionNode.hasChildNodes()) {
//				NodeList list = actionNode.getChildNodes();
//				int nbrOfChildTags = list.getLength();
//				for (int i = (nbrOfChildTags - 1); i >= 0; i--) {
//					Node node = list.item(i);
//					if (node instanceof Element)
//						actionNode.removeChild(node);
//				}
//				return true;
//			}
//		}
//		return false;
	}

	//Use this only for debugging.
	public String asString() {
		return jsonBuilderFactory.toXML(root,serviceName,version,hierarchical);
//		try {
////			Date date = new Date();
////			double startTime = date.getTime();
//
//			//return m_xmlString.toString();
//			//Serialize DOM
//			OutputFormat format = new OutputFormat(sdbDocument);
//			format.setIndenting(true); //CVNS Added Venu.
//			// as a String
//			StringWriter stringOut = new StringWriter();
//			XMLSerializer serial = new XMLSerializer(stringOut, format);
//			serial.serialize(sdbDocument);
//			// Display the XML
//			//System.out.println(stringOut.toString());
//
////			Date date2 = new Date();
////			double endTime = date2.getTime();
////			System.out.println("Time it took to serialize is " + (endTime - startTime));
//
//			String xmlString = stringOut.toString();
//
//			//Work around TouchPoint C++ bug.
//			String hackedString = xmlString.replaceAll("encoding=\"UTF-8\"", "");
//			return hackedString;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
	}

	/**
	 * CVNS
	 * @return
	 */
	public boolean begin() {
//		sdbDocumentState = true;
		return true;
	}
	/**
	 * CVNS - To write the data from HashMap into XML Document
	 */
	public boolean end() {
//		try {
//			if (map != null && !map.isEmpty()) {
//				Iterator itr = map.keySet().iterator();
//				String key = null;
//				String value = null;				
//				while (itr.hasNext()) {
//					 key = ((String) itr.next());
//					 value = (String) map.get(key);
//
//					Element element = sdbDocument.createElement(key);
//					//Get the action element from Document and append element to action tag
//					actionNode.appendChild(element);
//					if (value != null)
//						element.appendChild(sdbDocument.createTextNode(value)); 
//				}
//				map.clear();
//			}
//		} catch (java.lang.Exception ex) {
//			System.out.println("java.lang.Exception in end() " + ex.getMessage());
//			ex.printStackTrace();
//		}
//
//		sdbDocumentState = false;
		return true;
	}

	/**
	 * CVNS
	 * The Add method adds an XML stream to the buffer, excluding
	 * the root element 'TouchPoint' and action element 'action'.
	 * @param in XMLMessageSDB
	 * @return boolean
	 */
	public boolean add(XMLMessageSDB in) {
		
		try
		{
			jsonBuilderFactory.add( in.action,this.action);
		}catch( EOBaseException ex){
			logger.error("Error in add(XMLMessageSDB) : ", ex);
			return false;
		}
		return true;
		
//		Document document = in.document(); commented for XES-5889
//		if (document.hasChildNodes()) {
//			//Changed by Venu
//			//Get the action element from Document 
//			Node copyActionNode = document.getFirstChild().getFirstChild();
//			if (copyActionNode != null) {
//				//Node actionNode = (Node) nodeList.item(0);
//				if (copyActionNode.hasChildNodes()) {
//					NodeList actionList = copyActionNode.getChildNodes();
//					long actionLength = actionList.getLength();
//					if (actionLength != 0) {
//						for (int i = 0; i < actionLength; i++) {
//							Node node = actionList.item(i);
//							if (node instanceof Element) {
//								NodeList children = node.getChildNodes();
//								String nodeName = node.getNodeName();
//								String nodeValue =
//									((children.getLength() == 1)
//										? children.item(0).getNodeValue()
//										: "");
////								this.add(nodeName, nodeValue);
//								add(nodeName, nodeValue, false);
//							}
//						}
//					}
//				}
//			}
//		}
//		return true;
	} //end of add

	/**
	 * CVNS
	 * To add a node with value, attribute and attribute value
	 * @param path
	 * @param value
	 * @param attr Name of the XML Attribute
	 * @param attrValue Attribute Value
	 * @return boolean
	 */
	public boolean add(String path, String value, String attr, String attrValue) {

		add("action", path, value, attr, attrValue);

		return true;
	}

	/**
	 * CVNS
	 * To add a node with value, attribute and attribute value to the specified parent node
	 * @param parentNode
	 * @param path
	 * @param value
	 * @param attr
	 * @param attrValue
	 * @return
	 * @throws UnsupportedOperationException
	 */
	public boolean add(
		String parentNode,
		String path,
		String value,
		String attr,
		String attrValue) {

//		try { commented for XES-5889
//			NodeList elements = sdbDocument.getElementsByTagName(parentNode);
//			//int elementCount = elements.getLength();
//			Element element = sdbDocument.createElement(path);
//			elements.item(0).appendChild(element);
//			if (value != null)
//				element.appendChild(sdbDocument.createTextNode(value));
//			if (attr != null)
//				element.setAttribute(attr, attrValue);
//		} catch (java.lang.Exception ex) {
//			System.out.println(
//				"java.lang.Exception adding an element with parent node " + ex.getMessage());
//			ex.printStackTrace();
//			return false;
//		}
//
//		return true;
		throw new UnsupportedOperationException("public boolean add( parentNode,path,value,attr,attrValue) not supported");
	}

		/**
	 * CVNS
	 * The add method adds an XML stream to the buffer.
	 * For the same path name, the list of values are appended.
	 * Encountered this scenario in - CGTestSvc service that is resulting in performance issue
	 * @param path
	 * @param list
	 * @return boolean
	 */
	public boolean add(String path, ArrayList list) {
		String value = null;
		try {
			//Changed by Venu
			//NodeList elements = sdbDocument.getElementsByTagName("action");
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					value = (String) list.get(i);
					add(path, (String) list.get(i));
/*					Element element = sdbDocument.createElement(path);
					actionNode.appendChild(element);
					value = (String) list.get(i);
					if (value != null)
						element.appendChild(sdbDocument.createTextNode(value));*/ 
				}
			}
		} catch (java.lang.Exception ex) {
			System.out.println(
				"java.lang.Exception adding an element with ArrayList " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * This method will remove node from DOM
	 * @param path
	 * @param occ
	 * @return
	 */
	public boolean removeNode(String path, int occ) {

//		NodeList elements = sdbDocument.getElementsByTagName(path); commented for XES-5889
//		int nbrOfChildTags = elements.getLength();
//
//		if ((elements != null && nbrOfChildTags >= occ)) {
//			Node node = elements.item(occ);
//			if (node instanceof Element) {
//				NodeList list = sdbDocument.getElementsByTagName(node.getParentNode().getNodeName());
//				list.item(0).removeChild(node);
//				return true;
//			}
//		}
//		return false;
		
		return del(path, occ);
	}
	/**
	 * This method caches the nodelist till the iteration completes.
	 * @return
	 */
	public boolean beginIteration() {
//		iterationMap = new HashMap<String, NodeList>(); commented for XES-5889
		return true;
	}
	
	/**
	 * This method clears the cached nodelist.
	 * @return
	 */
	public boolean endIteration() {
//		if(iterationMap != null) commented for XES-5889
//			iterationMap.clear();
//		iterationMap = null;
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getServiceName() {
		return serviceName;
	}
	
	/**
	 * 
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isHierarchical() {
		return hierarchical;
	}
	
	/**
	 * 
	 * @param hierarchical
	 */
	public void setHierarchical(boolean hierarchical) {
		this.hierarchical = hierarchical;
	}
	
	/**
	 * This method builds the JSON object hierarchy from the input TouchPoint message
	 * in string format.
	 * @param body
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws MessageFormatError
	 */
	public boolean fromStringFI(String body)
	throws IOException, SAXException, MessageFormatError
	{
	    boolean isFastInfosetDocument = FastInfoSetUtility.isFastInfosetDocument(body);
	    Document dom;
	       
	    if (isFastInfosetDocument) {
	            try {
	            	byte abyte[] = FastInfoSetUtility.stringToByte(body);
	            	String fiBody = new String( abyte );
	            	root = jsonBuilderFactory.rootESLObject(body);
					initFromString();
	            	
	            } catch (Exception e) {
					throw new IOException(e.getMessage());
				}
	    } else {
	    	try {
				root = jsonBuilderFactory.rootESLObject(body);
				initFromString();
				
			} catch (Exception e) {
				logger.error("fromStringFI : ",e);
				throw new MessageFormatError("Incorrect format : fromStringFI(String body) "+body);
			}
	    }
		return isFastInfosetDocument;
	}
	
	/**
	 * Internally calls fromStringFI method to build JSON object hierarchy 
	 * from the input TouchPoint message 
	 * @param body
	 * @throws IOException
	 * @throws SAXException
	 * @throws MessageFormatError
	 */
	public void fromString(String body)
	throws IOException, SAXException, MessageFormatError
	{
		fromStringFI(body);
	}
	
	/**
	 * Initializes root action and serviceName member variables
	 * @throws Exception
	 */
	private void initFromString() throws Exception {
		
		ESLObject touchpoint = root.getESLObject(TOUCHPOINT);
		action = touchpoint.getESLObject(ACTION);
		// Start: Modified for fixing XES-6426
		String sName = "";
		
		// If action implementation is of type EOEslImpl, prepend attrib. to the SERVICE_NAME  
		if(action instanceof EOEslImpl){
			sName = action.getString("attrib."+SERVICE_NAME);
		} else {
			sName = action.getString(SERVICE_NAME);
		}
		// End: Modified for fixing XES-6426
		setServiceName(sName);
	}
	
	/**
	 * Returns the String representation of the TouchPoint message 
	 * represented by an instance of XMLMessageSDB object.
	 */
	public String toString()
	{
		if(root == null)
			return "(null)";
		return jsonBuilderFactory.toXML(root,serviceName,getVersion(),hierarchical);
	}
	
	/**
	 * Returns the FastInfoset String representation of the TouchPoint message 
	 * represented by this instance of XMLMessageSDB object.
	 * @param encoding
	 * @param fi
	 * @return String
	 */
	public String toString(String encoding, boolean fi)
	{
		if(root == null)
			return "(null)";
		
		String str = jsonBuilderFactory.toXML(root,serviceName,getVersion(),hierarchical);
		if( fi ){
			return FastInfoSetUtility.byteToString(str.getBytes());
		}
		return str;
		
		/*try
		{
			DOMSource source = new DOMSource(atr_internal);
			StreamResult result = new StreamResult(writer);
			Properties prop = transformer.getOutputProperties();
			
			if ( (encoding == utf16be) || (encoding == utf16le) )
			{
			   transformer.setOutputProperty("encoding","UTF-16");
			}
			else
			{
				transformer.setOutputProperty("encoding","UTF-8");
			}
			
			if(fi)
			{
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				transformer.transform(source, new FastInfosetResult(output));
				return FastInfoSetUtility.byteToString(output.toByteArray());
			}
			else{
				transformer.transform(source, result);
				return writer.toString();
			}
			//transformer.setOutputProperties(prop);
		}
		catch(Throwable ex)
		{
			if ( logger.isEnabledFor(Level.FATAL))
				logger.fatal("TouchPointMessage: Error transforming DOM to String",ex);
			return "("+ex.toString()+")";	
		}*/
	}
}