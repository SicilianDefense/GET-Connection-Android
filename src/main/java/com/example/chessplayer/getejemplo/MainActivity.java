package com.example.chessplayer.getejemplo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener{


            public static final int SHOW_RESPONSE =0;
            private Button sendRequest;
            private TextView responseText;

            private Handler handler = new Handler(){
                //主线程获取子线程传递回来的message,进行处理，显示在界面
                public void handleMessage(Message msg)
                {
                    switch (msg.what)
                    {
                        case SHOW_RESPONSE:
                            String response = (String) msg.obj;

                                    //shows it up the result.



                           responseText.setText(response);



                    }
                }
            };

            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                sendRequest=(Button)findViewById(R.id.send_request);
                responseText=(TextView)findViewById(R.id.response);

                sendRequest.setOnClickListener(this);


            }






        private void sendRequestWithHttpURLConnection()
        {
            //open subgroup to activate internet request
            new Thread(new Runnable()
            {

                @Override
                public void run()
                {
                    //use HttpURLConnection to  make a HTTP request to a website
                    HttpURLConnection connection=null;
                    try
                    {
                        //connect
                        URL url = new URL("http://www.mizuhobank.co.jp/takarakuji/loto/backnumber/loto70081.html");
                        connection=(HttpURLConnection)url.openConnection();
                        connection.setRequestMethod("GET");
                        //set the maximum connection time, if overtimed throw it away
                        connection.setConnectTimeout(8000);
                        //set the maximom input stream finish processing time , if overtimed throw it away
                        connection.setReadTimeout(8000);
                        //"in" is the input stream
                        InputStream in= connection.getInputStream();

                        //read stream that go back from the server , and put the result in  a message target
                        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        StringBuilder response =new StringBuilder();
                        String line;



                        while((line=reader.readLine())!=null)
                        {
                            //addding the lines to the response
                            response.append(line);


                        }

                        Message message = new Message();

                        message.what = SHOW_RESPONSE;
                        //put the results from the server to Message
                        message.obj = response.toString();
                        //handler sends message back to main thread
                        handler.sendMessage(message);

                    } catch (Exception e)//catch bug inside and manage
                    {
                        e.printStackTrace();
                    }finally{
                        //finally release connection
                        if (connection !=null)
                        {
                            connection.disconnect();
                        }
                    }
                }
            }).start();
        }















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.send_request)
        {
            sendRequestWithHttpURLConnection();


        }
    }
}





