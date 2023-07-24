/*
 * Licensed to the Nervousync Studio (NSYC) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nervousync.exceptions.builder;

import org.nervousync.exceptions.AbstractException;

/**
 * <h2 class="en">Builder Exception</h2>
 * <h2 class="zh-CN">构建器异常</h2>
 *
 * @author Steven Wee	<a href="mailto:wmkm0113@Hotmail.com">wmkm0113@Hotmail.com</a>
 * @version $Revision : 1.0 $ $Date: Apr 25, 2017 18:30:42 $
 */
public final class BuilderException extends AbstractException {
	/**
	 * <span class="en">Serial version UID</span>
	 * <span class="zh-CN">序列化UID</span>
	 */
    private static final long serialVersionUID = -156775157749202954L;
	/**
	 * <h3 class="en">Constructor method for BuilderException</h3>
	 * <span class="en">Create a new BuilderException with the specified message.</span>
	 * <h3 class="zh-CN">BuilderException构造方法</h3>
	 * <span class="zh-CN">使用特定的信息创建BuilderException实例对象。</span>
	 *
	 * @param errorCode 	<span class="en">Error identified code</span>
     *                      <span class="zh-CN">错误识别代码</span>
     * @param bundle        <span class="en">Resource bundle name</span>
     *                      <span class="zh-CN">资源包名</span>
     * @param messageKey    <span class="en">Message identify key</span>
     *                      <span class="zh-CN">信息识别键值</span>
     * @param collections   <span class="en">given parameters of information formatter</span>
     *                      <span class="zh-CN">用于资源信息格式化的参数</span>
	 */
    public BuilderException(final long errorCode, final String bundle, final String messageKey,
							final Object... collections) {
        super(errorCode, bundle, messageKey, collections);
    }
	/**
	 * <h3 class="en">Constructor method for BuilderException</h3>
	 * <span class="en">Create a new BuilderException with the specified message and root cause.</span>
	 * <h3 class="zh-CN">BuilderException构造方法</h3>
	 * <span class="zh-CN">使用特定的信息以及异常信息对象实例创建BuilderException实例对象。</span>
	 *
	 * @param errorCode 	<span class="en">Error identified code</span>
     *                      <span class="zh-CN">错误识别代码</span>
     * @param bundle        <span class="en">Resource bundle name</span>
     *                      <span class="zh-CN">资源包名</span>
     * @param messageKey    <span class="en">Message identify key</span>
     *                      <span class="zh-CN">信息识别键值</span>
	 * @param cause 		<span class="en">The root cause</span>
	 *              		<span class="zh-CN">异常信息对象实例</span>
     * @param collections   <span class="en">given parameters of information formatter</span>
     *                      <span class="zh-CN">用于资源信息格式化的参数</span>
	 */
	public BuilderException(final long errorCode, final String bundle, final String messageKey,
							final Throwable cause, final Object... collections) {
		super(errorCode, bundle, messageKey, cause, collections);
	}
}
