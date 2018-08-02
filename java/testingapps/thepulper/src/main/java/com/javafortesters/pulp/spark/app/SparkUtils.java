package com.javafortesters.pulp.spark.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class SparkUtils {

    public static String getMyStackTrace(final Exception e) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        return result.toString();
    }
}
