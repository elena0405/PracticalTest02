package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    private ServerSocket socket;
    private HashMap<String, String> map;

    public ServerThread(int port) {
        System.out.println("port = " + port);
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }

        map = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("Thread-ul pt server ruleaza!!!");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket1 = socket.accept();
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());

                CommunicationThread communicationThread = new CommunicationThread(socket1, this);
                communicationThread.start();
            }


        } catch (IOException e) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + e.getMessage());
            if (Constants.DEBUG) {
                e.printStackTrace();
            }
        }

    }

    public ServerSocket getSocket() {
        return socket;
    }

    public void stopThread() {
        interrupt();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map, String key, String value) {
        map.put(key, value);
    }
}
