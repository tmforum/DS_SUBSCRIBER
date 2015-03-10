package org.tmf.dsmapi.subscriber.resource;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.tmf.dsmapi.subscriber.Config;
import org.tmf.dsmapi.subscriber.Server;

public class Current implements HttpHandler {

    private static String filename = null;
    private static final File DIRECTORY = new File(Config.DIRECTORY);
    private static final FilenameFilter filenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(Config.EXTENSION);
        }
    };

    public Current() {
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers requestHeaders = httpExchange.getRequestHeaders();
        Headers responseHeaders = httpExchange.getResponseHeaders();
        Server.setHeaders(requestHeaders, responseHeaders);

        String method = httpExchange.getRequestMethod();

        File last = null;

        if (filename != null) {
            last = new File(filename);
        } else {
            File[] files = DIRECTORY.listFiles(filenameFilter);
            if (files.length > 0) {
                last = files[0];
                for (int i = 0; files.length > i; i++) {
                    File file = files[i];
                    if (file.lastModified() > last.lastModified()) {
                        last = file;
                    }
                }
            }
        }

        if ("GET".equalsIgnoreCase(method)) {
            OutputStream responseBody = httpExchange.getResponseBody();
            FileInputStream inputStream = null;
            if (last != null && last.exists()) {
                httpExchange.sendResponseHeaders(200, 0);
                inputStream = new FileInputStream(last);
                responseBody.write(IOUtils.toByteArray(inputStream));
            } else {
                httpExchange.sendResponseHeaders(204, 0);
            }
            responseBody.close();
            inputStream.close();
        }

    }

    public static synchronized void setCurrent(String current) {
        Current.filename = current;
    }
}
