package com.android.destranger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.destranger.com.android.destranger.push.ConcurrentQueue;
import com.android.destranger.com.android.destranger.push.CurrentChatUser;
import com.android.destranger.com.android.destranger.push.Message;
import com.android.destranger.com.android.destranger.push.Protocol;
import com.android.destranger.com.android.destranger.push.ProtocolPair;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatActivity extends ActionBarActivity {

    final int VIEW_COUNT = 6;
    final int VIEW_TEXT_LEFT = 0;
    final int VIEW_TEXT_RIGHT = 1;
    final int VIEW_VOICE_LEFT = 2;
    final int VIEW_VOICE_RIGHT = 3;
    final int VIEW_IMAGE_LEFT = 4;
    final int VIEW_IMAGE_RIGHT = 5;
    final int[] layouts = new int[]{R.layout.listitem_text_left, R.layout.listitem_text_right, R.layout.listitem_voice_left,
                                    R.layout.listitem_voice_right, R.layout.listitem_image_left, R.layout.listitem_image_right};

    ListView listView;
    ImageButton btn1, btn2, btn3;
    EditText editText;
    List<Message> data;
    MyAdapter myAdapter;
    SharedPreferences usp;
    String username;
    String friendname = "A";
    IntentFilter intentFilter;
    MessageReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        if(usp.contains("username"))
            username = usp.getString("username", "");

        listView = (ListView) findViewById(R.id.listView);
        btn1 = (ImageButton) findViewById(R.id.ibtn1);
        btn2 = (ImageButton) findViewById(R.id.ibtn2);
        btn3 = (ImageButton) findViewById(R.id.ibtn3);
        editText = (EditText) findViewById(R.id.editText);

        btn1.setOnClickListener(new MyOnClickListener());
        btn2.setOnClickListener(new MyOnClickListener());
        btn3.setOnClickListener(new MyOnClickListener());
        editText.addTextChangedListener(new MyTextChangedListener());

        //get Data
        data = new ArrayList<Message>();

        //set Adapter
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myAdapter = new MyAdapter(inflater);
        listView.setAdapter(myAdapter);

        //TODO
        username = "A";
        friendname = "A";
        CurrentChatUser.username = "A";


        //set intent filter
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.destranger.push");
        mReceiver = new MessageReceiver();
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(btn1 == v) {}
            if(btn2 == v) {}
            if(btn3== v) {
                if(editText.getText() == null || editText.getText().toString() == null ||  editText.getText().toString().length() == 0)
                    return;
                Message msg = new Message();
                msg.setTime(new Date());
                msg.setType(0);
                msg.setFrom(username);
                msg.setTo(friendname);
                msg.setContent(editText.getText().toString().getBytes());
                editText.setText("");
                try {
                    JSONObject json = new JSONObject();
                    json.put("session", "123");
                    json.put("message", new Gson().toJson(msg));
                    ConcurrentQueue.Wait_Queue.put(new ProtocolPair(Protocol.MESSAGE_SEND, json.toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                data.add(msg);
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater inflater;
        public MyAdapter(LayoutInflater inflater) {
            super();
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            Message msg = data.get(position);
            String to = msg.getTo();
            String from = msg.getFrom();
            boolean right = from.equals(username);
            switch (msg.getType()) {
                case 0:
                    if (right)   return VIEW_TEXT_RIGHT;
                    else        return VIEW_TEXT_LEFT;
                case 1:
                    if (right)   return VIEW_VOICE_RIGHT;
                    else        return VIEW_VOICE_LEFT;
                case 2:
                    if (right)   return VIEW_IMAGE_RIGHT;
                    else        return VIEW_IMAGE_LEFT;
                default: return 0;
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_COUNT;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Message msg = data.get(position);
            byte[] content = data.get(position).getContent();
            int type = getItemViewType(position);
            if(convertView == null)
                convertView = inflater.inflate(layouts[type], parent, false);
            ImageButton ibtn = (ImageButton) convertView.findViewById(R.id.ibtn);
            TextView textView;

            switch (type) {
                case VIEW_TEXT_LEFT:
                case VIEW_TEXT_RIGHT:
                    textView = (TextView) convertView.findViewById(R.id.txt);
                    textView.setText(new String(msg.getContent()));
                    break;
                case VIEW_VOICE_LEFT:
                case VIEW_VOICE_RIGHT:
                    ImageButton ibtn2 = (ImageButton) convertView.findViewById(R.id.ibtn2);
                    ibtn2.setOnClickListener(null);
                    textView = (TextView) convertView.findViewById(R.id.txt);
                    textView.setText("5");
                    break;
                case VIEW_IMAGE_LEFT:
                case VIEW_IMAGE_RIGHT:
                    ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(content, 0, content.length));
                    break;
                default:;
            }
            return convertView;
        }

    }


    class MyTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0 && btn2.getVisibility() == View.VISIBLE) {
                btn2.animate().alpha(0f).setDuration(500);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.VISIBLE);
                btn3.setAlpha(0f);
                btn3.animate().alpha(1.0f).setDuration(500);
            }
            if(s.length() == 0) {
                btn3.animate().alpha(0f).setDuration(500);
                btn3.setVisibility(View.GONE);
                btn2.setVisibility(View.VISIBLE);
                btn2.setAlpha(0f);
                btn2.animate().alpha(1.0f).setDuration(500);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.android.destranger.push")) {
                Message msg = (Message) intent.getSerializableExtra("message");
                data.add(msg);
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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


}
