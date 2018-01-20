package ru.mail.polis.kvsimplemet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class MyKVservice implements KVService {
    private static final String PREF = "id=";
    @NotNull
    private final HttpServer myServer;

    private final InetSocketAddress isa;
    private final int delay = 0;
    private int operationId = 0;

    public static String getID(@NotNull final String query){
        if (!query.startsWith(PREF)) {
            throw new IllegalArgumentException("Bad ID");
        }

        final String id = query.substring(PREF.length());
        if (id.isEmpty()){
            throw new IllegalArgumentException();
        }

        return id;
    }
    public MyKVservice(@NotNull final MyDao dao, int port) throws IOException{
        this.isa = new InetSocketAddress(port);
        this.myServer =
                HttpServer.create(isa,0);

        this.myServer.createContext(
                "/v0/status",
                http -> {
                    operationId = 200;
                    final String res = "ONLINE";
                    http.sendResponseHeaders(operationId, res.length());
                    http.getResponseBody().write(res.getBytes());
                    http.close();
                });

        this.myServer.createContext(
                "/v0/entity",
                new ErrorHandler(
                    http -> {
                        final String id = getID(http.getRequestURI().getQuery());
                        switch (http.getRequestMethod()){
                            case "GET":
                                operationId = 200;
                                final byte[] getValue = dao.get(id);
                                http.sendResponseHeaders(operationId, getValue.length);
                                http.getResponseBody().write(getValue);
                                break;
                            case "DELETE":
                                operationId = 202;
                                dao.delete(id);
                                http.sendResponseHeaders(operationId, 0);
                                break;
                            case "PUT":
                                operationId = 201;

                                final int contentLength = Integer.valueOf(http.getRequestHeaders().getFirst("Content-Length"));
                                final byte[] putValue = new byte[contentLength];
                                if (http.getRequestBody().read(putValue) != putValue.length){
                                    throw new IOException("Can't read file");
                                }

                                dao.upsert(id, putValue);
                                http.sendResponseHeaders(operationId, putValue.length);
                                http.getResponseBody().write(putValue);
                                break;
                            default:
                                operationId = 405;
                                http.sendResponseHeaders(operationId,0);
                                break;
                        }

                        http.close();

                }));

        this.myServer.createContext(
                "/v0/status",
                http -> {
                    final String res = "ONLINE";
                    final int time = 200;
                    http.sendResponseHeaders(time, res.length());
                    http.getResponseBody().write(res.getBytes());
                    http.close();
                });
    }

    @Override
    public void start() {
        this.myServer.start();
    }

    @Override
    public void stop() {
        this.myServer.stop(delay);
    }

    private static class ErrorHandler implements HttpHandler{
        private final HttpHandler delegate;

        private ErrorHandler(HttpHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try{
                delegate.handle(httpExchange);
            } catch (NoSuchElementException e) {
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.close();
            }
            catch (IllegalArgumentException e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }
}
