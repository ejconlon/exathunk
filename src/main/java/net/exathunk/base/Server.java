package net.exathunk.base;

import net.exathunk.genthrift.RemoteExecutionService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class Server implements Runnable {

    private final RemoteExecutionService.Iface handler;
    private final int port;
    
    public Server(RemoteExecutionService.Iface handler, int port) {
        this.handler = handler;
        this.port = port;
    }
    
    public void run() {
        TServerTransport serverTransport;
        try {
            serverTransport = new TServerSocket(port);
        } catch (TTransportException e) {
            e.printStackTrace();
            return;
        }
        RemoteExecutionService.Processor processor = new RemoteExecutionService.Processor(handler);
        TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
        server.serve();
    }
}
