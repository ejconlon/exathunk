package net.exathunk.base;

import net.exathunk.genthrift.RemoteExecutionService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class Client implements AutoCloseable {

    private final String host;
    private final int port;
    private TTransport transport;
    private RemoteExecutionService.Client stub;
    
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void open() throws TTransportException {
        transport = new TSocket(host, port);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        stub = new RemoteExecutionService.Client(protocol);
    } 
    
    public RemoteExecutionService.Client getStub() {
        return stub;
    }
    
    public void close() {
        if (transport != null) transport.close();
        stub = null;
        transport = null;
    }
}