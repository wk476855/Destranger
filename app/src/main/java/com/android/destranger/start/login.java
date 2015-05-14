package com.android.destranger.start;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.destranger.R;
import com.android.destranger.data.Protocol;
import com.android.destranger.data.UserInfo;
import com.android.destranger.network.Communication;
import com.android.destranger.network.MessageHandler;
import com.android.destranger.ui.ILogin;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends ActionBarActivity implements ILogin{

    private EditText userName = null;
    private EditText passWord = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) this.findViewById(R.id.username);
        passWord = (EditText) this.findViewById(R.id.password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        if(id == R.id.finish)
        {
            String un = userName.getText().toString();
            String pw = passWord.getText().toString();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username",un);
                jsonObject.put("password",pw);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Communication com = new Communication(this,new MessageHandler(this));
            com.setUrl(Protocol.LOGIN_URL);
            com.setParams(jsonObject);
            com.setCode(0x002);
            com.sendGetRequest();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void hint(String str) {
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }

    @Override
    public void jump(UserInfo userInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfo);
        Intent intent = new Intent(this,home.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
