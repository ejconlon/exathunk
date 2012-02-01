package net.exathunk.remote;

import net.exathunk.base.*;
import net.exathunk.functional.Unit;
import net.exathunk.genthrift.*;
import net.exathunk.schemey.SchemeyTypeChecker;
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

    /**
     * EXAMPLE:
     *
     * mvn exec:java -Dexec.mainClass="net.exathunk.remote.Client" -Dexec.args="localhost 5678 '(* (+ 2 6) 5)'"
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("USE: java net.exathunk.remote.Client [hostname] [port] [quoted expression]");
            System.exit(-1);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String expression = args[2];

        try (Client client = new Client(host, port)) {
            client.open();
            FuncDefLibrary library = new RemoteFuncDefLibrary(client.getStub());
            TypeChecker checker = new SchemeyTypeChecker(); // TODO not really sexp/scheme specific
            NTreeParser parser = new SexpParser();
            NTree<Unit, String, String> tree = parser.parse(expression);
            EvalRequest evalRequest = TypeCheckerUtils.makeEvalRequest(library, checker, tree);
            RemoteThunk thunk = client.getStub().submitEvalRequest(evalRequest);
            VarCont value = client.getStub().thunkGet(thunk);
            System.out.println(value);
        }
    }
}