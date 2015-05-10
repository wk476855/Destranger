package com.android.destranger.start;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.destranger.R;


public class main extends ActionBarActivity {

    private Button register = null;
    private Button login = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (Button) this.findViewById(R.id.register);
        login = (Button) this.findViewById(R.id.login);
        register.setOnClickListener(new OnClickListener());
        login.setOnClickListener(new OnClickListener());
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

    public class OnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            Intent intent = null;
            if(id == R.id.register)
            {
                intent = new Intent(main.this,register.class);
            }
            else if(id == R.id.login)
            {
                intent = new Intent(main.this,login.class);
            }
            startActivity(intent);
        }
    }
}
