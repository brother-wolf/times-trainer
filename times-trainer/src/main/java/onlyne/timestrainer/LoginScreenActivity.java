package onlyne.timestrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import onlyne.timestrainer.db.UserDataSource;

public class LoginScreenActivity extends AbstractTimesTrainerActivity {

    private UserDataSource userDataSource;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_frame);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        userDataSource = new UserDataSource(this);

        findViewById(R.id.submit).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                saveUsername();
            }
        });
    }

    private void saveUsername() {
        EditText usernameTV = (EditText) findViewById(R.id.username);
        String username = usernameTV.getText().toString();
        if (hasValidName(username)) {
            createOrUseProfile(username);
        } else {
            Toast.makeText(LoginScreenActivity.this, "Username invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private void createOrUseProfile(String username) {
        if (!userDataSource.isUsername(username)) {
            userDataSource.createUsername(username);
        } else {
            Toast.makeText(LoginScreenActivity.this, "Using existing user", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, MultiplicationActivity.class);
        startActivity(intent);
    }

    private boolean hasValidName(String username) {
        return ! (null == username ||  username.trim().isEmpty());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }
}
