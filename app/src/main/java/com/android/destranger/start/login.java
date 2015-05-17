package com.android.destranger.start;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.destranger.R;
import com.android.destranger.data.Protocol;
import com.android.destranger.data.UserInfo;
import com.android.destranger.network.Communication;
import com.android.destranger.network.MessageHandler;
import com.android.destranger.ui.ILogin;
import com.android.destranger.view.MyEditText;

import java.util.HashMap;
import java.util.Map;

public class login extends ActionBarActivity implements ILogin,View.OnClickListener{

    private MyEditText userName = null;
    private MyEditText passWord = null;
    private Button login = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (MyEditText) this.findViewById(R.id.username);
        userName.setHint("用户名");
        passWord = (MyEditText) this.findViewById(R.id.password);
        passWord.setHint("密   码");
        login = (Button) this.findViewById(R.id.login);
        login.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        String un = userName.getText().toString();
        String pw = passWord.getText().toString();
        Map<String,String> params = new HashMap<>();
        params.put("username",un);
        params.put("password",pw);
        Communication com = new Communication(this,new MessageHandler(this));
        com.setUrl(Protocol.LOGIN_URL);
        com.setParams(params);
        com.setCode(0x002);
        com.sendPostRequest();
    }
}
