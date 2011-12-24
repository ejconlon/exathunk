package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.fuelsyourcyb.exathunk.Client;

public class ClientTest {

    @Test
    public void testInitialize() {
	Client client = new Client("myid");
	assertEquals("myid", client.getId());
    }

}