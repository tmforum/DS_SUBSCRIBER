package org.tmf.dsmapi.subscriber.resource;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.tmf.dsmapi.subscriber.Config;
import org.tmf.dsmapi.subscriber.Server;

public class History implements HttpHandler {

    public History() {
    }

    private static final FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(Config.EXTENSION);
        }
    };

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers requestHeaders = httpExchange.getRequestHeaders();
        Headers responseHeaders = httpExchange.getResponseHeaders();
        Server.setHeaders(requestHeaders, responseHeaders);

        String method = httpExchange.getRequestMethod();

        File directory = new File(Config.DIRECTORY);

        if (directory.exists()) {

            File[] files = directory.listFiles(filter);

            if ("GET".equalsIgnoreCase(method)) {
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream responseBody = httpExchange.getResponseBody();
                responseBody.write("[".getBytes());
                FileInputStream logFile = null;
                for (int i = 0; files.length > i; i++) {
                    File file = files[i];
                    logFile = new FileInputStream(file);
                    responseBody.write(IOUtils.toByteArray(logFile));
                    if (i + 1 < files.length) {
                        responseBody.write(",".getBytes());
                    }
                    logFile.close();
                }
                responseBody.write("]".getBytes());
                responseBody.close();
            }

            if ("DELETE".equalsIgnoreCase(method)) {
                synchronized (this) {
                    Current.setCurrent(null);
                    httpExchange.sendResponseHeaders(200, 0);
                    OutputStream responseBody = httpExchange.getResponseBody();
                    int cpt = 0;
                    for (int i = 0; files.length > i; i++) {
                        File file = files[i];
                        if (!file.delete()) {
                            httpExchange.sendResponseHeaders(401, 0);
                            String msg = "Cannot delete file " + file.getName();
                            responseBody.write(msg.getBytes());
                            responseBody.close();
                            i = files.length + 1;
                        } else {
                            cpt = i + 1;
                        }
                    }
                    String msg = "{\"deleted\":" + cpt + "}";
                    responseBody.write(msg.getBytes());
                    responseBody.close();
                    Logger.getLogger(Resource.class.getName())
                            .log(Level.INFO, msg);
                }
            }
        } else {
            Logger.getLogger(Resource.class.getName())
                    .log(Level.SEVERE, directory.getName() + " doesn't exist");
        }

        httpExchange.close();

    }
}
