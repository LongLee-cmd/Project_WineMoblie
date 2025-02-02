package vn.edu.stu.wine_gk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        SetName();
        addEvents();
    }

    private void addControls() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
    }

    private void SetName() {
        username.setText("admin");
        password.setText("123");
    }

    private void  addEvents() {
        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (isValidCredentials(user, pass)) {
            navigateToWineList();
        } else {
            showInvalidCredentialsMessage();
        }
    }

    private boolean isValidCredentials(String user, String pass) {
        return "admin".equals(user) && "123".equals(pass);
    }

    private void navigateToWineList() {
        Intent intent = new Intent(MainActivity.this, WineList_Activity.class);
        startActivity(intent);
    }

    private void showInvalidCredentialsMessage() {
        String message = getString(R.string.invalid_credentials);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
