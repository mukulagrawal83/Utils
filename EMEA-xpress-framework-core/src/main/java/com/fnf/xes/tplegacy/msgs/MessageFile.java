/*      
 *	$Header: /view/system_view/vobstore/xes/CVS/xesFramework2/CVSROOT/src/prod/com/fnf/xes/tplegacy/msgs/MessageFile.java,v 1.1 2005/04/04 13:38:02 wsettle Exp $
 *  TouchPoint Java Enterprise Services Library
 *  Copyright 2004, TouchPoint Solutions a division of Fidelity Information Services.
 * 	All rights reserved.
 *
 *  WARNING: This file contains CONFIDENTIAL and PROPRIETARY information and
 *  INTELLECTUAL DATA of TouchPoint Solutions, and is protected by copyright
 *  law and international treaties.  Unauthorized reproduction or distribution
 *  may result in severe civil and criminal penalties, and will be prosecuted
 *  to the maximum extent possible under the law.
 *
 *	Created on: Jan 13, 2004
 *
 *	To change the template for this generated file go to
 * 	Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.fnf.xes.tplegacy.msgs;


import java.io.FileNotFoundException;
import java.io.IOException;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

import com.fnf.xes.tplegacy.util.PrintfFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MessageFile.java
 * 
 * @author XES DEV TEAM
 * @version 0.1.0
 * @tp.creationdate Jan 13, 2004
 * @tp.copyright Copyright 2004, TouchPoint Solutions a division of FIS.
 * @tp.revision $Revision: 1.1 $
 * @tp.modified $Date: 2005/04/04 13:38:02 $
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MessageFile 
	implements MessageResource 
	
{	
	/**
	 * 	Default constructor
	 *
	 */
	public MessageFile()
	{
	}
	
	/**
	 * 
	 * @param aclass
	 * @param alocale
	 * @throws MessageResourceException
	 */
	public MessageFile(String basepath, Class aclass, Locale alocale)
	throws MessageResourceException
	{
		load(basepath, aclass, alocale);
	}
		
	/**
	 * 
	 * @param aclass
	 * @throws MessageResourceException
	 */
	public MessageFile(String basepath, Class aclass)
	throws MessageResourceException
	{
		load(basepath, aclass, null);
	}
	/**
	 * 
	 * @param basename
	 * @param alocale
	 * @throws MessageResourceException
	 */	
	public MessageFile(String basepath, String basename, Locale alocale)
	throws MessageResourceException
	{
		load(basepath, basename, alocale);	
	}
	
	/**
	 * 
	 * @param basename
	 * @throws MessageResourceException
	 */
//	public MessageFile(String basepath, String basename)
//	throws MessageResourceException
//	{
//		load(basepath, basename, null);	
//	}

	//Resource Bundle like calls
	/**
	 *  @return Answers the Locale object for this resource
	 * 	@see java.util.ResourceBundle#getLocale()
	 */
	public Locale getLocale()
	{		
		return messagelocale;
	}

	/**
	 * 
	 *  @return Answers the Locale object for this resource
	 * 	@see java.util.ResourceBundle
	 */	
	public Enumeration getKeys()
	{
		return messageindex.keys();
	}
	
	/**
	 * 
	 * @param 	item	A string version of the message id key
	 * @return  Answers the object (String) contained 
	 * 			at the specified string key
	 * @see java.util.ResourceBundle#getObject(java.lang.String)
	 */
	public Object getObject(String item)
	{
		return messageindex.get(item);
	}
	
	
	//MessageResource Implementation
	/** 
	 * {@inheritDoc}
	 * @param messageid	{@inheritDoc}
	 * @see com.TouchPoint.msgs.MessageResource#find(int)
	 */
	public String find(int messagid) {
		String item = Integer.toString(messagid);
		String result = (String)messageindex.get(item);
		if(result == null) return "[" + item + "]";
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @param messageid	{@inheritDoc}
	 * @param args		{@inheritDoc}
	 * @see com.TouchPoint.msgs.MessageResource#format(int, java.lang.Object[])
	 */
	public String format(int messageid, Object[] args) {
		String result = null;
		String msg = find(messageid);
		
		//result = (new PrintfFormat(msg)).sprintf(args);
		result = "[" + messageid + "] " + (new PrintfFormat(msg)).sprintf(args); //CVNS
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @param messageid	{@inheritDoc}
	 * @param arg0		{@inheritDoc}
	 * @see com.TouchPoint.msgs.MessageResource#format(int, java.lang.Object)
	 */
	public String format(int messageid, Object arg0) {
		Object[] argv = new Object[1];
		argv[0] = arg0;
		return format(messageid, argv);
	}

	/**
	 * {@inheritDoc}
	 * @param messageid	{@inheritDoc}
	 * @param arg0		{@inheritDoc}
	 * @param arg1		{@inheritDoc}
	 * @see com.TouchPoint.msgs.MessageResource#format(int, java.lang.Object, java.lang.Object)
	 */
	public String format(int messageid, Object arg0, Object arg1) {
		Object[] argv = new Object[2];
		argv[0] = arg0;
		argv[1] = arg1;
		return format(messageid, argv);
	}
 
	/**
	 * {@inheritDoc}
	 * @param messageid	{@inheritDoc}
	 * @param arg0		{@inheritDoc}
	 * @param arg1		{@inheritDoc}
	 * @param arg2		{@inheritDoc}
	 * @see com.TouchPoint.msgs.MessageResource#format(int, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public String format(
		int messageid,
		Object arg0,
		Object arg1,
		Object arg2) {
		Object[] argv = new Object[3];
		argv[0] = arg0;
		argv[1] = arg1;
		argv[2] = arg2;
		return format(messageid, argv);
	}

	/**
	 * {@inheritDoc}
	 * @param messageid	{@inheritDoc}
	 * @param arg0		{@inheritDoc}
	 * @param arg1		{@inheritDoc}
	 * @param arg2		{@inheritDoc}
	 * @param arg3		{@inheritDoc}
	 * @see com.TouchPoint.msgs.MessageResource#format(int, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public String format(
		int messageid,
		Object arg0,
		Object arg1,
		Object arg2,
		Object arg3) {
		Object[] argv = new Object[4];
		argv[0] = arg0;
		argv[1] = arg1;
		argv[2] = arg2;
		argv[3] = arg3;
		return format(messageid, argv);
	}
	
	
	 
	
	///////////////////////////////////
	/**
	 * @param aclass
	 * @param alocale	A Locale object to use in identifying the
	 * 					proper message file, if <b>null</b> then the
	 * 					default Locale for the VM is used.
	 * @throws MessageResourceException
	 */
	public void load(String basepath, String aclass, Locale alocale)
	throws MessageResourceException
	{
		String lang = null;
		String country = null;
		if(alocale != null)
			messagelocale = alocale;

		if ((messagelocale.getISO3Language() != null) && (messagelocale.getISO3Language().length()>=3))
			lang = messagelocale.getISO3Language().substring(0,2);
		if ((messagelocale.getISO3Country() != null) && (messagelocale.getISO3Country().length()>=3))
			country	= messagelocale.getISO3Country().substring(0,2);
		
		String[] name     	= aclass.split("\\.");
		if(name.length < 1) 
			throw new MessageResourceException("Message base name " + aclass + " is invalid");
		//load(basepath + "\\" + name[name.length-1], lang, country);
		load(basepath + "/" + name[name.length-1], lang, country); //CVNS - Modified Path Separator
	}

	/**
	 * 
	 * @param aclass
	 * @param alocale	A Locale object to use in identifying the
	 * 					proper message file, if <b>null</b> then the
	 * 					default Locale for the VM is used.
	 * @throws MessageResourceException
	 */
	public void load(String basepath, Class aclass, Locale alocale)
	throws MessageResourceException
	{
		load(basepath, aclass.getName(), alocale);		
	}
 
	/**
	 * 
	 * @param name
	 * @param lang
	 * @param country
	 * @throws MessageResourceException
	 */
	protected void load(String name, String lang, String country) throws MessageResourceException {
		String base = null;
      MessageFileReader mfr_base = null;
		try
		{
			base = buildName(name, lang, null);
			mfr_base = new MessageFileReader(base);
			mfr_base.read(messageindex);
		}
		catch(IOException ex)
		{
         // here try the default 'en' locale
         base = buildName(name, "en", null);
         try {
            mfr_base = new MessageFileReader(base);
            mfr_base.read(messageindex);
         } catch (IOException ex1) {
            throw new MessageResourceException(ex1);
         }
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param lang
	 * @param country
	 * @return Answers a properly formated message file name string
	 */	
	private String buildName(String name, String lang, String country)
	{
		StringBuffer namebuild = new StringBuffer();

		namebuild.append(name);
		namebuild.append(".");
		namebuild.append(lang);
		if(country != null)
		{
			namebuild.append(".");
			namebuild.append(country);
		}
		namebuild.append(".");
		namebuild.append(defextension);
		
		return namebuild.toString();		
	}
	
	
	private static final String defextension = "msgs";
	private Locale	  messagelocale = Locale.getDefault(); 
	private Hashtable messageindex = new Hashtable();
}
