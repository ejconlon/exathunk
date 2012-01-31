package net.exathunk.remote;

import net.exathunk.base.*;
import net.exathunk.schemey.*;
import net.exathunk.genthrift.*;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class Server implements Runnable, AutoCloseable {

    private final RemoteExecutionService.Iface handler;
    private final int port;
    private TServer server;

    public Server(RemoteExecutionService.Iface handler, int port) {
        this.handler = handler;
        this.port = port;
    }

    public void open() {
        TServerTransport serverTransport;
        try {
            serverTransport = new TServerSocket(port);
        } catch (TTransportException e) {
            e.printStackTrace();
            return;
        }
        RemoteExecutionService.Processor processor = new RemoteExecutionService.Processor(handler);
        server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
    }

    @Override
    public void run() {
        if (server == null) open();
        server.serve();
    }

    @Override
    public void close() throws Exception {
        if (server != null) server.stop();
        server = null;
    }

    private static RemoteExecutionServiceHandler makeHandler() {
        NFuncLibrary library = new SchemeyNFuncLibrary();
        TypeChecker checker = new SchemeyTypeChecker();
        ThunkExecutor executor = new DefaultThunkExecutor(library);
        return new RemoteExecutionServiceHandler(library, checker, executor);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1 || "help".equals(args[0])) {
            System.err.println("USE: java net.exathunk.remote.Server [port]");
        }
        int port = Integer.parseInt(args[0]);
        try (Server server = new Server(makeHandler(), port)) {
            server.open();
            server.run();
        }
    }
}
