package net.exathunk.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClientTest {

    @Test
    public void testInitialize() {
        Client client = new Client("myid");
        assertEquals("myid", client.getId());
    }

}