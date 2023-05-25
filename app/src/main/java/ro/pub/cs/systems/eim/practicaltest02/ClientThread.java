package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private String address;
    private int port;
    private String post_key;
    private String post_value;
    private String get_key;

    private Socket socket;

    public ClientThread(String address, int port, String post_key, String post_value, String get_key) {
        this.get_key = get_key;
        this.post_key = post_key;
        this.post_value = post_value;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] Client thread created successfully!");
        System.out.println("Client running!");
        try {
            // tries to establish a socket connection to the server
            socket = new Socket(address, port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);

            // Trimite inf catre CommunicationThread
            printWriter.println(post_key);
            printWriter.flush();
            printWriter.println(post_value);
            printWriter.flush();
            printWriter.println(get_key);
            printWriter.flush();

            String line = bufferedReader.readLine();
            String line2 = bufferedReader.readLine();

            System.out.println(line + " " + line2);

        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getPost_value() {
        return post_value;
    }

    public void setPost_value(String post_value) {
        this.post_value = post_value;
    }

    public String getGet_key() {
        return get_key;
    }

    public void setGet_key(String get_key) {
        this.get_key = get_key;
    }
}
