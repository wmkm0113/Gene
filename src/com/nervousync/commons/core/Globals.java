package com.nervousync.commons.core;

import java.io.File;
import java.util.Locale;

public final class Globals {

	public static final String CONTENT_TYPE_APPLICATION_XML = "application/xml";
	public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	
	public static final int DEFAULT_BUFFER_SIZE = 8192;
	
	public static final int DEFAULT_TIME_OUT = 0;
	public static final int DEFAULT_DOWNLOAD_SINGLE_THREAD = 1;
	public static final int DEFAULT_DOWNLOAD_THREAD_COUNT = 5;
	public static final int DEFAULT_DOWNLOAD_BLOCK_SIZE = 512;
	
	public static final int DEFAULT_DOWNLOAD_STATUS_WAITING = 0;
	public static final int DEFAULT_DOWNLOAD_STATUS_PROCESSING = 1;
	public static final int DEFAULT_DOWNLOAD_STATUS_PAUSE = 2;
	public static final int DEFAULT_DOWNLOAD_STATUS_FAILED = 3;
	public static final int DEFAULT_DOWNLOAD_STATUS_FINISHED = 4;
	public static final int DEFAULT_DOWNLOAD_STATUS_DELETE = 5;
	public static final int DEFAULT_DOWNLOAD_STATUS_VALIDATE_FAILED = 6;
	public static final int DEFAULT_DOWNLOAD_STATUS_CANCEL = 7;
	public static final int DEFAULT_DOWNLOAD_STATUS_RETRY = 8;

	public static final int DEFAULT_VALUE_INT = -1;
	public static final long DEFAULT_VALUE_LONG = -1L;
	public static final short DEFAULT_VALUE_SHORT = -1;
	public static final double DEFAULT_VALUE_DOUBLE = -1;
	public static final float DEFAULT_VALUE_FLOAT = -1;
	public static final boolean DEFAULT_VALUE_BOOLEAN = false;
	public static final String DEFAULT_VALUE_STRING = "";
	
	public static final String DEFAULT_EMAIL_FOLDER_INBOX = "INBOX";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final Locale DEFAULT_LOCALE = Locale.getDefault();
	public static final String DEFAULT_LANGUAGE = 
			System.getProperty("user.language")	+ "_" + System.getProperty("user.country");
	public static final String DEFAULT_SYSTEM_CHARSET = System.getProperty("file.encoding");
	public static final String DEFAULT_PAGE_SEPARATOR = File.separator;
	public static final String DEFAULT_URL_SEPARATOR = "/";
	public static final String DEFAULT_JAR_PAGE_SEPARATOR = "\\";
	public static final String DEFAULT_ZIP_PAGE_SEPARATOR = "/";

	public static final String DEFAULT_DOWNLOAD_CONFIGURE = "nervousync_download.conf";
	
	public static final String DEFAULT_EMAIL_CONTENT_TYPE_TEXT = "text/plain";
	public static final String DEFAULT_EMAIL_CONTENT_TYPE_HTML = "text/html";
	public static final String DEFAULT_EMAIL_CONTENT_TYPE_MULTIPART = "multipart/*";
	public static final String DEFAULT_EMAIL_CONTENT_TYPE_MESSAGE_RFC822 = "message/rfc822";

	public static final String DEFAULT_PROTOCOL_PREFIX_FTP = "http://";
	public static final String DEFAULT_PROTOCOL_PREFIX_HTTP = "http://";
	public static final String DEFAULT_PROTOCOL_PREFIX_HTTPS = "https://";
	
	public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";
	public static final String DEFAULT_DATETIME_PATTERN_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss";

	public static final int INITIALIZE_INT_VALUE = 0;
	
	public static final int SNMP_VERSION_1 = 0;
	public static final int SNMP_VERSION_2C = 1;
	public static final int SNMP_VERSION_3 = 2;
}
