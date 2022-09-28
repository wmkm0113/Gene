/*
 * Copyright 2018 Nervousync Studio
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
package org.nervousync.commons.adapter.xml;

import org.nervousync.utils.DateTimeUtils;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * @author Steven Wee	<a href="mailto:wmkm0113@Hotmail.com">wmkm0113@Hotmail.com</a>
 * @version $Revision: 1.0 $Date: 2018-10-15 14:31
 */
public final class DateTimeAdapter extends XmlAdapter<String, Date> {
	/**
	 * Convert a value type to a bound type.
	 *
	 * @param v The value to be converted. Can be null.
	 * @throws Exception if there's an error during the conversion. The caller is responsible for
	 *                   reporting the error to the user through.
	 */
	@Override
	public Date unmarshal(String v) throws Exception {
		return DateTimeUtils.parseSiteMapDate(v);
	}
	
	/**
	 * Convert a bound type to a value type.
	 *
	 * @param v The value to be converted. Can be null.
	 */
	@Override
	public String marshal(Date v) {
		return DateTimeUtils.formatDateForSiteMap(v);
	}
}
