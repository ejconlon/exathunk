package net.exathunk.base;

import net.exathunk.genthrift.RemoteExecutionService;
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
}
