/*
 * Copyright 2017 Nervousync Studio
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nervousync.commons.core.zip;

import org.nervousync.commons.core.Globals;

/**
 * ZIP Constants value define
 *
 * @author Steven Wee <a href="mailto:wmkm0113@Hotmail.com">wmkm0113@Hotmail.com</a>
 * @version $Revision : 1.0 $ $Date: Nov 28, 2017 5:17:09 PM $
 */
public final class ZipConstants {

	/**
	 * The constant LOCSIG.
	 */
	/*
	 * Header signatures
	 */
	public static final long LOCSIG = 0x04034b50L; // "PK\003\004"
	/**
	 * The constant EXTSIG.
	 */
	public static final long EXTSIG = 0x08074b50L; // "PK\007\008"
	/**
	 * The constant CENSIG.
	 */
	public static final long CENSIG = 0x02014b50L; // "PK\001\002"
	/**
	 * The constant ENDSIG.
	 */
	public static final long ENDSIG = 0x06054b50L; // "PK\005\006"
	/**
	 * The constant DIGSIG.
	 */
	public static final long DIGSIG = 0x05054b50L;
	/**
	 * The constant ARCEXTDATREC.
	 */
	public static final long ARCEXTDATREC = 0x08064b50L;
	/**
	 * The constant SPLITSIG.
	 */
	public static final long SPLITSIG = 0x08074b50L;
	/**
	 * The constant ZIP64ENDCENDIRLOC.
	 */
	public static final long ZIP64ENDCENDIRLOC = 0x07064b50L;
	/**
	 * The constant ZIP64ENDCENDIRREC.
	 */
	public static final long ZIP64ENDCENDIRREC = 0x06064b50;
	/**
	 * The constant EXTRAFIELDZIP64LENGTH.
	 */
	public static final int EXTRAFIELDZIP64LENGTH = 0x0001;
	/**
	 * The constant AESSIG.
	 */
	public static final int AESSIG = 0x9901;

	/**
	 * The constant MAX_ALLOWED_ZIP_COMMENT_LENGTH.
	 */
	public static final int MAX_ALLOWED_ZIP_COMMENT_LENGTH = 0xFFFF;

	/**
	 * Compression Type STORE.
	 */
	public static final int COMP_STORE = 0;
	/**
	 * TCompression Type DEFLATE.
	 */
	public static final int COMP_DEFLATE = 8;

	/**
	 * The constant AES_AUTH_LENGTH.
	 */
	public static final int AES_AUTH_LENGTH = 10;
	/**
	 * The constant AES_BLOCK_SIZE.
	 */
	public static final int AES_BLOCK_SIZE = 16;

	/**
	 * AES Key Strength
	 */
	public static final int AES_STRENGTH_128 = 0x01;
	/**
	 * The constant AES_STRENGTH_192.
	 */
	public static final int AES_STRENGTH_192 = 0x02;
	/**
	 * The constant AES_STRENGTH_256.
	 */
	public static final int AES_STRENGTH_256 = 0x03;

	/**
	 * The constant MIN_SPLIT_LENGTH.
	 */
	public static final int MIN_SPLIT_LENGTH = 65536;
	/**
	 * The constant ZIP_64_LIMIT.
	 */
	public static final long ZIP_64_LIMIT = 4294967295L;

	/**
	 * The constant UFT8_NAMES_FLAG.
	 */
	public static final int UFT8_NAMES_FLAG = 1 << 11;
	/**
	 * Encryption types
	 */
	public static final int ENC_NO_ENCRYPTION = -1;
	/**
	 * The constant ENC_METHOD_STANDARD.
	 */
	public static final int ENC_METHOD_STANDARD = 0;
	/**
	 * The constant ENC_METHOD_STRONG.
	 */
	public static final int ENC_METHOD_STRONG = 1;
	/**
	 * The constant ENC_METHOD_AES.
	 */
	public static final int ENC_METHOD_AES = 99;

	/**
	 * Compression level for deflate algorithm
	 */
	public static final int DEFLATE_LEVEL_FASTEST = 1;
	/**
	 * The constant DEFLATE_LEVEL_FAST.
	 */
	public static final int DEFLATE_LEVEL_FAST = 3;
	/**
	 * The constant DEFLATE_LEVEL_NORMAL.
	 */
	public static final int DEFLATE_LEVEL_NORMAL = 5;
	/**
	 * The constant DEFLATE_LEVEL_MAXIMUM.
	 */
	public static final int DEFLATE_LEVEL_MAXIMUM = 7;
	/**
	 * The constant DEFLATE_LEVEL_ULTRA.
	 */
	public static final int DEFLATE_LEVEL_ULTRA = 9;

	/**
	 * The constant PASSWORD_VERIFIER_LENGTH.
	 */
	public static final int PASSWORD_VERIFIER_LENGTH = 2;
	/**
	 * The constant STD_DEC_HDR_SIZE.
	 */
	public static final int STD_DEC_HDR_SIZE = 12;
	/**
	 * The constant ENDHDR.
	 */
	public static final int ENDHDR = 22; // END header size

	/**
	 * The constant BUFFER_SIZE.
	 */
	public static final int BUFFER_SIZE = 1024 * 4;
	/**
	 * The constant ZIP64_EXTRA_BUFFER_SIZE.
	 */
	public static final int ZIP64_EXTRA_BUFFER_SIZE = 50;

	/**
	 * The constant FILE_MODE_NONE.
	 */
	public static final int FILE_MODE_NONE = 0;
	/**
	 * The constant FILE_MODE_READ_ONLY.
	 */
	public static final int FILE_MODE_READ_ONLY = 1;
	/**
	 * The constant FOLDER_MODE_NONE.
	 */
	public static final int FOLDER_MODE_NONE = 16;

	/**
	 * The constant ZIP_FILE_SEPARATOR.
	 */
	public static final String ZIP_FILE_SEPARATOR = "/";
	/**
	 * The constant ZIP_ENTRY_SEPARATOR.
	 */
	public static final String ZIP_ENTRY_SEPARATOR = ":" + Globals.DEFAULT_PAGE_SEPARATOR;

	private ZipConstants() {
	}
}
