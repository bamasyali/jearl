/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jearl.log.Logger;
import org.jearl.socket.model.JSocketPropertie;
import org.jearl.util.StreamUtils;

/**
 *
 * @author bamasyali
 */
public class JSocket {

    private final String host;
    private final Integer port;
    private static Map<String, Socket> socketMap;

    static {
        socketMap = new HashMap<String, Socket>();
    }

    public JSocket(JSocketPropertie propertie) {
        this.host = propertie.getHost();
        this.port = propertie.getPort();
    }

    protected final Socket getSocket() throws IOException {
        synchronized (this) {
            Socket socket = socketMap.get(host);
            if (socket == null) {
                socket = new Socket(host, port);
                socketMap.put(host, socket);
                if (!socket.isConnected()) {
                    throw new ConnectException();
                } else {
                    StreamUtils.convertInputStreamToString(socket.getInputStream());
                }
            } else if (!socket.isConnected()) {
                socket = new Socket(host, port);
                socketMap.put(host, socket);
                if (!socket.isConnected()) {
                    throw new ConnectException();
                } else {
                    StreamUtils.convertInputStreamToString(socket.getInputStream());
                }
            }
            return socket;
        }
    }

    public List<String> execute(String data) {
        synchronized (this) {
            List<String> lines = new ArrayList<String>();
            try {
                getSocket().setSoTimeout(1000);
                PrintStream os = null;
                os = new PrintStream(getSocket().getOutputStream(), true, "UTF-8");
                os.print(data + ((char) 13));
                os.flush();
                lines.add(StreamUtils.convertInputStreamToString(getSocket().getInputStream()));
                getSocket().setSoTimeout(0);
            } catch (Exception ex) {
                Logger.getLogger(JSocket.class).error(ex, ex);
                lines.add(ex.getMessage());
            }
            return lines;
        }
    }
}
