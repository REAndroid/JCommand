/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.jcommand.exceptions;

public class CommandFormatException extends CommandException {

    private final Class<?> type;

    public CommandFormatException(Class<?> type, String message) {
        super(message);
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
    @Override
    public String getMessage() {
        return format("Invalid <%s> string: '%s'");
    }

    @Override
    public String format(String format) {
        return String.format(format, getType().getSimpleName(), getRawMessage());
    }
}
