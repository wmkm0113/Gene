/*
 * Copyright 2021 Nervousync Studio
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
package org.nervousync.annotations.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3 class="en">Annotation class for ID generator implement class</h3>
 * <h3 class="zh-CN">ID生成器实现类的标注</h3>
 *
 * @author Steven Wee	<a href="mailto:wmkm0113@Hotmail.com">wmkm0113@Hotmail.com</a>
 * @version $Revision : 1.0 $ $Date: 12/10/2021 3:25 PM $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GeneratorProvider {

    /**
     * <span class="en">ID generator implement name</span>
     * <span class="zh-CN">ID生成器实现名称</span>
     *
     * @return  <span class="en">Implement name</span>
     *          <span class="zh-CN">实现名称</span>
     */
    String value();
}
