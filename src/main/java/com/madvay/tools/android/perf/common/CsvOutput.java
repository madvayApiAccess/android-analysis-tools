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

import com.google.common.base.Function;

import java.util.List;

/**
 *
 */
public class CsvOutput<T extends Row> implements Function<T, String> {

    private final List<String> cols;
    private final RowAdapter<T> adapter;

    public CsvOutput(List<String> cols, RowAdapter<T> adapter) {
        this.cols = cols;
        this.adapter = adapter;
    }

    @Override
    public String apply(T input) {
        StringBuilder sb = new StringBuilder();
        for (String col : cols) {
            String v = adapter.get(input, col).toString();
            v.replace("\"", "\"\"");
            if (v.contains("'") || v.contains(",") || v.contains("\n") || v.contains("\r")) {
                sb.append("\"");
                sb.append(v);
                sb.append("\"");
            } else {
                sb.append(v);
            }
            sb.append(",");
        }
        // remove last comma
        sb.setLength(sb.length() - 1);
        sb.append("\n");
        return sb.toString();
    }
}
