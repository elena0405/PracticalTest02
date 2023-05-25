package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {
    private Socket socket;
    private ServerThread thread;


    public CommunicationThread(Socket socket, ServerThread serverThread) {
        this.socket = socket;
        thread = serverThread;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            BufferedReader reader = Utils.getReader(socket);
            PrintWriter writer = Utils.getWriter(socket);

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");

            String post_key = reader.readLine();
            String post_value = reader.readLine();
            String get_key = reader.readLine();

            if ((post_key == null || post_key.isEmpty()) &&
                    (get_key == null || get_key.isEmpty()) &&
                    (post_value == null || post_value.isEmpty())) {
                Log.e(Constants.TAG, "1 [COMMUNICATION THREAD] Error receiving parameters from client!");
                return;
            }

            if ((post_key == null || post_key.isEmpty()) &&
                    (post_value != null && !post_value.isEmpty())) {
                Log.e(Constants.TAG, "2 [COMMUNICATION THREAD] Error receiving parameters from client!");
                return;
            }

            if ((post_key != null && !post_key.isEmpty()) &&
                    (post_value == null || post_value.isEmpty())) {
                Log.e(Constants.TAG, "3 [COMMUNICATION THREAD] Error receiving parameters from client!");
                return;
            }

            if (thread.getMap().containsKey(post_key)) {
                Log.i(Constants.TAG, "4 [COMMUNICATION THREAD] We have a POST request value " + thread.getMap().get(post_key));
                return;
            }

            if ((post_key != null && !post_key.isEmpty()) &&
                    (post_value != null && !post_value.isEmpty()) &&
                    (get_key == null || get_key.isEmpty())) {
                Log.i(Constants.TAG, "4 [COMMUNICATION THREAD] We have a POST request!");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";
        //        HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
//                HttpResponse httpGetResponse = httpClient.execute(httpGet);
//                HttpEntity httpGetEntity = httpGetResponse.getEntity();
//                if (httpGetEntity != null) {
//                    pageSourceCode = EntityUtils.toString(httpGetEntity);
//                }
//                if (pageSourceCode == null) {
//                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
//                    return;
//                } else Log.i(Constants.TAG, pageSourceCode);
//
//                JSONObject content = new JSONObject(pageSourceCode);
//                JSONObject main = content.getJSONObject(Constants.MAIN);

                // make the HTTP request to the web service

                thread.getMap().put(post_key, post_value);
                return;

            }

            if (get_key != null && !get_key.isEmpty()) {
                String result = thread.getMap().get(get_key);

                writer.println(result);
                writer.flush();
            }


        } catch (Exception e) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + e.getMessage());
            if (Constants.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
