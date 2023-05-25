package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private EditText server_port = null;
    private EditText client_port = null;
    private EditText client_ip = null;
    private EditText value = null;
    private EditText key = null;
    private TextView get_value = null;

    private ServerThread serverThread;

    private ServerConnectListener server_button_listener = new ServerConnectListener();
    private ClientButtonListener client_button_listener = new ClientButtonListener();

    private class ClientButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = client_ip.getText().toString();
            String clientPort = client_port.getText().toString();
            if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String post_value = value.getText().toString();
            String post_key = key.getText().toString();
            String get_key = get_value.getText().toString();

            ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), post_key, post_value, get_key);
            clientThread.start();

        }
    }

    private class ServerConnectListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String port = server_port.getText().toString();
            if (port.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(port));
            if (serverThread.getSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }

            Log.i(Constants.TAG, "[MAIN ACTIVITY] Server thread created successfully!");
            serverThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        server_port = findViewById(R.id.server_port_id);
        client_port = findViewById(R.id.client_port_id);
        client_ip = findViewById(R.id.client_address_id);

        Button client_button = findViewById(R.id.connect_client);
        client_button.setOnClickListener(client_button_listener);

        Button server_button = findViewById(R.id.connect_server);
        server_button.setOnClickListener(server_button_listener);

        value = findViewById(R.id.client_post_value);
        key = findViewById(R.id.client_key);
        get_value = findViewById(R.id.client_value);
    }
}