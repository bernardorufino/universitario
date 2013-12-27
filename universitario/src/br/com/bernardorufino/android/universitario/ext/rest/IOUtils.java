package br.com.bernardorufino.android.universitario.ext.rest;


import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class IOUtils {

    public static String toString(final InputStream inputStream, Charset charset) throws IOException {
        InputSupplier<? extends InputStream> supplier = new InputSupplier<InputStream>() {
            @Override
            public InputStream getInput() throws IOException {
                return inputStream;
            }
        };
        return CharStreams.toString(CharStreams.newReaderSupplier(supplier, charset));
    }

    public static String toString(final InputStream inputStream) throws IOException {
        return toString(inputStream, Charsets.UTF_8);
    }

    // Prevents instantiation
    private IOUtils() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
