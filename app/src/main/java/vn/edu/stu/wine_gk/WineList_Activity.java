package vn.edu.stu.wine_gk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WineList_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnNavigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_list);

        setupToolbar();
        addControls();
        addEvents();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.home_title));
        }
    }

    private void addControls() {
        btnNavigate = findViewById(R.id.btnNavigate);
    }

    private void addEvents() {
        btnNavigate.setOnClickListener(v -> showChoiceDialog());
    }

    private void showChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choice_dialog_title))
                .setMessage(getString(R.string.choice_dialog_message))
                .setPositiveButton(getString(R.string.choice_manufacturer), (dialog, which) -> {
                    Intent intent = new Intent(WineList_Activity.this, ManufacturerActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(getString(R.string.choice_wine_type), (dialog, which) -> {
                    Intent intent = new Intent(WineList_Activity.this, DB_MainActivity.class);
                    startActivity(intent);
                })
                .setNeutralButton(getString(R.string.choice_cancel), null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_wine_list) {
            startActivity(new Intent(this, WineList_Activity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, About_Activity.class));
        } else if (id == R.id.action_db_main) {
            startActivity(new Intent(this, DB_MainActivity.class));
        } else if (id == R.id.action_manufacturer) {
            startActivity(new Intent(this, ManufacturerActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
