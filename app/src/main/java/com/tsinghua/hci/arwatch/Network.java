package com.tsinghua.hci.arwatch;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Network {
    MainActivity father;
    public final static int PORT = 8398;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    Thread threadReceive;

    public Network(MainActivity father) {
        this.father = father;
    }

    public void connect(String ip) {
        if (socket == null) {
            new NetworkAsyncTask().execute(ip);
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            Log.d("network", e.toString());
        }
        reader = null;
        writer = null;
        socket = null;
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            try {
                socket = new Socket(params[0], PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                threadReceive = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (reader != null) {
                                String s = reader.readLine();
                                Log.d("message", s);
                                if (s.equals("logon")) {
                                    father.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            father.changeLogStatus(true);
                                        }
                                    });
                                } else if (s.equals("logoff")) {
                                    father.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            father.changeLogStatus(false);
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            Log.d("network", e.toString());
                        }
                    }
                });
                threadReceive.start();
                return socket.toString();
            } catch (Exception e) {
                socket = null;
                reader = null;
                return e.toString();
            }
        }

        protected void onPostExecute(String string) {
            //father.uTextNetwork.setText(string);
        }
    }
}
