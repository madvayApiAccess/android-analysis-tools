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

package com.madvay.tools.android.perf.apat;

import com.madvay.tools.android.perf.allocs.AllocRow;
import com.madvay.tools.android.perf.allocs.AllocTable;
import com.madvay.tools.android.perf.allocs.AllocationsParserAdapter;
import com.madvay.tools.android.perf.allocs.PrettyAllocRowOutput;
import com.madvay.tools.android.perf.common.*;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Main {

    private static void outln(String s) {
        System.out.println(s);
    }

    private static void out(String s) {
        System.out.print(s);
    }

    private static void err(String s) {
        System.out.println("Error: " + s + "\n");
        System.out.flush();
    }

    private static void err(Exception e) {
        err(e.getMessage());
        e.printStackTrace();
    }

    private static void printUsage() {
        try {
            List<String> lines =
                    Resources.readLines(Resources.getResource("README.txt"), Charsets.UTF_8);
            boolean foundUsage = false, foundBeginHash = false;

            /*
             * We want to output the lines of usage in the README.md file:

            <other stuff>
            ## Usage
            ```
            <stuff to display>
            ```
            <other stuff>

             */
            lineLoop:
            for (String l : lines) {
                if (foundUsage) {
                    if (foundBeginHash) {
                        if (l.equals("```")) {
                            // end!
                            break lineLoop;
                        }
                        outln(l);
                    }
                    if (l.equals("```")) {
                        foundBeginHash = true;
                    }
                }
                if (l.equals("## Usage")) {
                    foundUsage = true;
                }
            }
        } catch (IOException err) {
            err(err);
        }
    }

    private static void printLicense() {
        try {
            List<String> lines =
                    Resources.readLines(Resources.getResource("LICENSE"), Charsets.UTF_8);
            for (String l : lines) {
                outln(l);
            }
        } catch (IOException err) {
            err(err);
        }
    }

    private static String getVersion() {
        return Package.getPackage("com.madvay.tools.android.perf.apat").getSpecificationVersion();
    }

    private static void printVersion() {
        outln("Version " + getVersion());
        outln("---------------------------------------------------------------");
        try {
            List<String> lines =
                    Resources.readLines(Resources.getResource("NOTICE"), Charsets.UTF_8);
            for (String l : lines) {
                outln(l);
            }
        } catch (IOException err) {
            err(err);
        }
    }

    public static void main(String[] argv) {
        CommandLine cmd = null;
        try {
            cmd = new CommandLine(argv);
        } catch (IllegalArgumentException e) {
            err(e.getMessage());
            printUsage();
            System.exit(-1);
            return;
        }

        try {
            switch (cmd.command) {
                case "help":
                    printUsage();
                    break;
                case "version":
                    printVersion();
                    break;
                case "license":
                    printLicense();
                    break;
                case "allocs":
                    runAllocs(cmd);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command: " + cmd.command);
            }
        } catch (Exception e) {
            err(e.getMessage());
            printUsage();
            System.exit(-1);
        }
        return;
    }

    private static <T extends Row> void processTable(CommandLine cmd, Table<T> table) {
        // Filter
        for (String key : table.getAdapter().columns) {
            for (FilterSpec spec : cmd.getFilterSpecsFlag(key)) {
                table.matching(spec);
            }
        }

        // Sort
        List<String> sort = cmd.getMultiFlag("sort");
        if (!sort.isEmpty()) {
            table.sortOn(sort);
        }
    }

    private static <T extends Row> TableFormatter<T>  //
    pickFormatter(CommandLine cmd, Map<String, Function<? super T, String>> formatters) {
        // Output
        String fmt = cmd.getUnaryFlagWithDefault("format", "pretty");
        return new TableFormatter<>(formatters.get(fmt));
    }

    private static void runAllocs(CommandLine cmd) {
        switch (cmd.args.get(0)) {
            case "parse": {
                AllocTable table = new AllocTable(AllocationsParserAdapter.parse(cmd.args.get(1)));
                processTable(cmd, table);
                TableFormatter<AllocRow> fmt = pickFormatter(cmd,
                        ImmutableMap.<String, Function<? super AllocRow, String>>of(  //
                                "csv",
                                new CsvOutput<>(table.getAdapter().columns, table.getAdapter()),  //
                                "pretty", new PrettyAllocRowOutput()));
                out(fmt.format(table));
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown allocs subcommand: " + cmd.args.get(0));
        }
    }
}
