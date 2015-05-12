package com.android.destranger;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.destranger.com.android.destranger.push.Message;

import org.w3c.dom.Text;

import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
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
    ImageButton btn1, btn2;
    EditText editText;
    List<Message> data;
    MyAdapter myAdapter;
    SharedPreferences usp;
    String username;

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
        editText = (EditText) findViewById(R.id.editText);

        editText.addTextChangedListener(new MyTextChangedListener());

        //get Data
        data = new ArrayList<Message>();

        //set Adapter
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myAdapter = new MyAdapter(inflater);
        listView.setAdapter(myAdapter);



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
            boolean left = to.equals(username);
            switch (msg.getType()) {
                case 0:
                    if (left)   return VIEW_TEXT_LEFT;
                    else        return VIEW_TEXT_RIGHT;
                case 1:
                    if (left)   return VIEW_VOICE_LEFT;
                    else        return VIEW_VOICE_RIGHT;
                case 2:
                    if (left)   return VIEW_IMAGE_LEFT;
                    else        return VIEW_IMAGE_RIGHT;
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_COUNT;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Message msg = data.get(position);
            int type = getItemViewType(position);
            if(convertView == null)
                convertView = inflater.inflate(layouts[type], parent, false);
            switch (type) {
                case VIEW_TEXT_LEFT:

                    TextView textView = (TextView) findViewById(R.id.txt);
                    textView.setText(new String(msg.getContent()));
                    break;
                case VIEW_TEXT_RIGHT:
                case VIEW_VOICE_LEFT:
                case VIEW_VOICE_RIGHT:
                case VIEW_IMAGE_LEFT:
                case VIEW_IMAGE_RIGHT:
            }
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.listitem_chat_right, parent, false);
                holder = new Holder();
                holder.ibtn = (ImageButton) convertView.findViewById(R.id.ibtn);
                holder.txt = (TextView) convertView.findViewById(R.id.txt);
                holder.txt2 = (TextView)convertView.findViewById(R.id.txt2);
                convertView.setTag(holder);
            }else {
                holder = (Holder) convertView.getTag();
            }
            holder.txt = data.get(position).ge
            return convertView;
        }

    }

    class MyTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0)
                changeBtn2();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    private void changeBtn2() {

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
