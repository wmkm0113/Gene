/*
 * Copyright 2022 Nervousync Studio
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
package org.nervousync.restful.converter.core;

import org.nervousync.restful.converter.ParameterConverter;
import org.nervousync.utils.StringUtils;

/**
 * The type Double converter.
 */
public final class DoubleConverter implements ParameterConverter {

    @Override
    public boolean match(Class<?> targetClass) {
        return Double.class.equals(targetClass);
    }

    @Override
    public String toString(Object object, String[] mediaTypes) {
        return (object instanceof Double) ? object.toString() : null;
    }

    @Override
    public Object fromString(Class<?> clazz, String value) {
        if (!Double.class.equals(clazz)) {
            return null;
        }
        return StringUtils.notBlank(value) ? Double.valueOf(value) : null;
    }
}
