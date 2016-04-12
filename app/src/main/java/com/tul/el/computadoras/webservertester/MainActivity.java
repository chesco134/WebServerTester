package com.tul.el.computadoras.webservertester;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView helloTag = (TextView)findViewById(R.id.activity_main_hello_tag);
        helloTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Networking().start();
            }
        });
    }

    private class Networking extends Thread{

        @Override
        public void run(){
            try{
                Log.d("Connecting", "Conencting...");
                HttpURLConnection con = (HttpURLConnection) new URL("http://192.168.100.3:8080/HelloHTTP/Hello").openConnection();
                con.setDoOutput(true);
                JSONObject json = new JSONObject();
                json.put("json_var_1", "Hello!");
                json.put("json_var_2", "I'm old!! n.n'");
                DataOutputStream salida = new DataOutputStream(con.getOutputStream());
                salida.write(json.toString().getBytes());
                salida.flush();
                DataInputStream entrada = new DataInputStream(con.getInputStream());
                int length;
                byte[] chunk = new byte[64];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while((length = entrada.read(chunk)) != -1)
                    baos.write(chunk, 0, length);
                Log.d("Connecting", "Recibimos: " + baos.toString());
                baos.close();
                con.disconnect();
                Log.d("connecting", "Done");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(findViewById(R.id.activity_main_hello_tag), "Done :3", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }catch(JSONException | IOException e){
                e.printStackTrace();
            }
        }
    }
}
