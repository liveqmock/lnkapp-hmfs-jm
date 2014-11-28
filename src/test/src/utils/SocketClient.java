package utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 同步Socket客户端
 */
public class SocketClient {


    public String onRequest(String reqmsg, int port) throws Exception {
        final String SERVER_IP = "127.0.0.1";
        Socket socket = new Socket(SERVER_IP, port);
        socket.setSoTimeout(60000);
        OutputStream os = socket.getOutputStream();
        byte[] reqBytes = reqmsg.getBytes("GBK");
        os.write(reqBytes);
        os.flush();
        InputStream is = socket.getInputStream();
        byte[] lengthBytes = new byte[8];
        is.read(lengthBytes);
        int toReadlength = Integer.parseInt(new String(lengthBytes).trim()) - 8;
        byte[] dataBytes = new byte[toReadlength];
        byte[] bytes = new byte[64];
        int index = 0;
        int curlen = 0;
        while ((curlen = is.read(bytes)) == 64) {
            System.arraycopy(bytes, 0, dataBytes, index, bytes.length);
            index += bytes.length;
        }
        if (curlen > 0) {
            System.arraycopy(bytes, 0, dataBytes, index, curlen);
        }
        return new String(dataBytes);
    }

    public String onRequest6(String reqmsg, int port) throws Exception {
        final String SERVER_IP = "127.0.0.1";
        Socket socket = new Socket(SERVER_IP, port);
        socket.setSoTimeout(60000);
        OutputStream os = socket.getOutputStream();
        byte[] reqBytes = reqmsg.getBytes("GBK");
        os.write(reqBytes);
        os.flush();
        InputStream is = socket.getInputStream();
        byte[] lengthBytes = new byte[6];
        is.read(lengthBytes);
        int toReadlength = Integer.parseInt(new String(lengthBytes).trim()) -  6;
        byte[] dataBytes = new byte[toReadlength];
        byte[] bytes = new byte[64];
        int index = 0;
        int curlen = 0;
        while ((curlen = is.read(bytes)) == 64) {
            System.arraycopy(bytes, 0, dataBytes, index, bytes.length);
            index += bytes.length;
        }
        if (curlen > 0) {
            System.arraycopy(bytes, 0, dataBytes, index, curlen);
        }
        return new String(dataBytes);
    }
}
