package com.android.destranger.start;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

public class register extends ActionBarActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, IRegister{

    private static final int CAMERA_REQUSET = 0X001;
    private ImageView head = null;
    private EditText userName = null;
    private EditText passWord = null;
    private RadioGroup gender = null;
    private UserInfo userInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userInfo = new UserInfo();
        head = (ImageView) this.findViewById(R.id.head);
        head.setOnClickListener(this);
        userName = (EditText) this.findViewById(R.id.username);
        passWord = (EditText) this.findViewById(R.id.password);
        gender = (RadioGroup) this.findViewById(R.id.gender);
        gender.setOnCheckedChangeListener(this);
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
            userInfo.setUsername(userName.getText().toString());
            userInfo.setPassword(passWord.getText().toString());
            Communication com = new Communication(this,new MessageHandler(this));
            com.setCode(0x001);
            com.setUrl(Protocol.REGISTER_URL);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", userInfo.getUsername());
                jsonObject.put("password", userInfo.getPassword());
                jsonObject.put("head", userInfo.getHead());
                jsonObject.put("gender", userInfo.getGender());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            com.setParams(jsonObject);
            com.sendPostRequest();
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.boy)
        {
            userInfo.setGender(0);
        }
        else
            userInfo.setGender(1);
    }

    @Override
    public void hint(String str) {
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }
}
