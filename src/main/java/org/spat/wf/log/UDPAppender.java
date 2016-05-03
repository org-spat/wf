package org.spat.wf.log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class UDPAppender extends AppenderSkeleton {
    static private int bufferSize = 8 * 1024;

    private byte data[];
    private String remoteHost = "localhost";
    private int port = 5000;

    private InetAddress address = null;
    private DatagramSocket dataSocket = null;
    private DatagramPacket dataPacket = null;

    public UDPAppender() {
        // LogLog.setInternalDebugging(true);

        // LogLog.setQuietMode(false);

        // LogLog.debug("default constructor.");

    }

    private void init() {
        try {
            dataSocket = new DatagramSocket(this.port + 1);
            address = InetAddress.getByName(remoteHost);
        } catch (SocketException e) {
            LogLog.debug(e.getMessage());
        } catch (UnknownHostException e) {
            LogLog.debug(e.getMessage());
        }

        data = new byte[bufferSize];

        if (this.layout == null) {
            LogLog.debug("The layout is not loaded... we set it.");
            String pattern = "%-4r %-5p %d{yyyy-MM-dd HH:mm:ss} %c %m%n";
            this.setLayout(new org.apache.log4j.PatternLayout(pattern));
        }
    }

    @Override
    protected void append(LoggingEvent event) {
        try {
            String msg =  this.getLayout().format(event);
            data = msg.getBytes();
            dataPacket = new DatagramPacket(data, data.length, address, port);
            dataSocket.send(dataPacket);
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Derived appenders should override this method if option structure
     * requires it.
     */
    public void activateOptions() {
        init();
    }

    @Override
    public void close() {
        if (closed)
            return;

        if (!dataSocket.isClosed()) {
            dataSocket.close();
        }
        closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    /**
     * The RemoteHost option takes a string value which should be the
     * host name of the server where a {@link SocketNode} is running.
     * */
    public void setRemoteHost(String host) {
        String val = host.trim();
        remoteHost = val;
    }

    /**
     * Returns value of the RemoteHost option.
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * The Port option takes a positive integer representing the port
     * where the server is waiting for connections.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Returns value of the Port option.
     */
    public int getPort() {
        return port;
    }
}