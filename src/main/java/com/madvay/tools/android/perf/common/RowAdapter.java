/*
 * Copyright (c) 2015 by Advay Mengle.
 *
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
 *
 */

package com.madvay.tools.android.perf.common;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 *
 */
public abstract class RowAdapter<T extends Row> {
    public enum CoerceType {
        TEXT, NUMERIC
    }

    public final List<String> columns;
    public final List<CoerceType> types;

    private RowAdapter() {
        throw new UnsupportedOperationException();
    }

    protected RowAdapter(List<String> columns, List<CoerceType> types) {
        this.columns = ImmutableList.copyOf(columns);
        this.types = ImmutableList.copyOf(types);
    }

    public abstract Object get(T row, String column);
}
