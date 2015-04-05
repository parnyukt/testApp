package com.tanya.testapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by tparnyuk on 05.04.15.
 */
public class Streams {

    public static <T extends OutputStream> T copy(InputStream in, T out) throws IOException {
        byte[] buf = new byte[8192];

        for(int read = in.read(buf); read != -1; read = in.read(buf)) {
            out.write(buf, 0, read);
        }

        return out;
    }

    public static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
}
