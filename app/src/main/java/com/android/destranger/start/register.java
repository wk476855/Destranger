package com.android.destranger.start;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.destranger.data.Protocol;
import com.android.destranger.data.ToolKit;
import com.android.destranger.data.UserInfo;
import com.android.destranger.R;
import com.android.destranger.network.Communication;
import com.android.destranger.network.MessageHandler;
import com.android.destranger.ui.IRegister;
import com.android.destranger.view.MyEditText;

import java.util.HashMap;
import java.util.Map;

public class register extends ActionBarActivity implements View.OnClickListener, IRegister{

    private static final int CAMERA_REQUSET = 0X001;
    private ImageView head = null;
    private MyEditText myusername = null;
    private MyEditText mypassword = null;
    private MyEditText sex = null;
    private Button register = null;
    private UserInfo userInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userInfo = new UserInfo();
        head = (ImageView) this.findViewById(R.id.head);
        head.setOnClickListener(this);
        myusername = (MyEditText)this.findViewById(R.id.username);
        myusername.setHint("用户名");
        mypassword = (MyEditText) this.findViewById(R.id.password);
        mypassword.setHint("密   码");
        sex = (MyEditText) this.findViewById(R.id.sex);
        sex.setHint("性   别");
        sex.setOnClickListener(this);
        this.registerForContextMenu(sex);
        register = (Button) this.findViewById(R.id.register);
        register.setOnClickListener(this);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.finish)
        {
            userInfo.setUsername(myusername.getText().toString());
            userInfo.setPassword(mypassword.getText().toString());
            Communication com = new Communication(this,new MessageHandler(this));
            com.setCode(0x001);
            com.setUrl(Protocol.REGISTER_URL);
            Map<String,String> params = new HashMap<>();
            params.put("username",userInfo.getUsername());
            params.put("password",userInfo.getPassword());
            params.put("head",userInfo.getHead());
            params.put("gender",String.valueOf(userInfo.getGender()));
            com.setParams(params);
            com.sendPostRequest();
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("请选择您的性别");
        menu.add(0, 0, 1, "男");
        menu.add(0, 1, 2, "女");
        menu.add(0,2,3,"性别不明");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case 0:
                sex.setText("男");
                userInfo.setGender(0);
                break;
            case 1:
                sex.setText("女");
                userInfo.setGender(1);
                break;
            case 2:
                sex.setText("性别不详");
                userInfo.setGender(2);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUSET)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle bundle = data.getExtras();
                Bitmap photo = (Bitmap) bundle.get("data");
                head.setImageBitmap(photo);
                userInfo.setHead(ToolKit.BitmapToString(photo));
                /*userInfo.setHead("photo");*/
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.head)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_REQUSET);
        }
        if(id == R.id.sex)
        {
            v.showContextMenu();
        }
        if(id == R.id.register)
        {
            userInfo.setUsername(myusername.getText().toString());
            userInfo.setPassword(mypassword.getText().toString());
            Communication com = new Communication(this,new MessageHandler(this));
            com.setCode(0x001);
            com.setUrl(Protocol.REGISTER_URL);
            Map<String,String> params = new HashMap<>();
            params.put("username",userInfo.getUsername());
            params.put("password",userInfo.getPassword());
            params.put("head",userInfo.getHead());
            params.put("gender",String.valueOf(userInfo.getGender()));
            com.setParams(params);
            com.sendPostRequest();
        }
    }

    @Override
    public void hint(String str) {
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }
}
