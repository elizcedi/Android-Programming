

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.RestrictionsManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class Main extends AppCompatActivity {

    String welcomeMessage= "Welcome to the cat bonanza!";
    boolean isImageModeEnabled = true;

    Button imageModeButton;

    Intent startImageActivityIntent;

    RestrictionsManager myRestrictionsManager;

    BroadcastReceiver restrictionsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startImageActivityIntent = new Intent(this,Image.class);

        Button welcomeButton = (Button) findViewById(R.id.welcomeButton);
        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, welcomeMessage, Snackbar.LENGTH_LONG).show();
            }
        });

        imageModeButton = (Button) findViewById(R.id.startImageModeButton);
        imageModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(startImageActivityIntent);
            }
        });

        myRestrictionsManager = (RestrictionsManager) getSystemService(Context.RESTRICTIONS_SERVICE);

        IntentFilter restrictionsFilter = new IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED);

        restrictionsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle applicationRestrictions = myRestrictionsManager.getApplicationRestrictions();

                if(applicationRestrictions.size() > 0) {
                    welcomeMessage = applicationRestrictions.getString("welcomeButtonMessage");
                    isImageModeEnabled = applicationRestrictions.getBoolean("isImageModeEnabled");
                }

                imageModeButton.post(new Runnable() {
                    @Override
                    public void run() {
                        imageModeButton.setEnabled(isImageModeEnabled);
                    }
                });
            }
        };

        registerReceiver(restrictionsReceiver,restrictionsFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle applicationRestrictions = myRestrictionsManager.getApplicationRestrictions();

        if(applicationRestrictions.size() > 0) {
            welcomeMessage = applicationRestrictions.getString("welcomeButtonMessage");
            isImageModeEnabled = applicationRestrictions.getBoolean("isImageModeEnabled");
        }

        imageModeButton.setEnabled(isImageModeEnabled);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(restrictionsReceiver);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        int id = item.getItemId();

      
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
