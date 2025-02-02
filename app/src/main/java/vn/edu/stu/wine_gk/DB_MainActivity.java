package vn.edu.stu.wine_gk;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vn.edu.stu.wine_gk.adapter.WineAdapterDB;
import vn.edu.stu.wine_gk.model.WineDB;

public class DB_MainActivity extends AppCompatActivity {

    private static final String DB_NAME = "sample_wine.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    private ListView lvWine;
    private ArrayList<WineDB> wineList;
    private WineAdapterDB adapter;
    private Toolbar toolbar;
    private Button btnAddWine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_main);

        addControls();
        addEvents();
        copyDatabaseFromAssets();
        loadWineData();
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_title));
        }

        lvWine = findViewById(R.id.lvWine);
        btnAddWine = findViewById(R.id.btnAddWine);
        wineList = new ArrayList<>();
        adapter = new WineAdapterDB(this, R.layout.custom_item_winedb, wineList);
        lvWine.setAdapter(adapter);
    }

    private void addEvents() {
        btnAddWine.setOnClickListener(v -> {
            Intent intent = new Intent(DB_MainActivity.this, AddWineActivity.class);
            startActivityForResult(intent, 1);  // Gọi startActivityForResult với requestCode = 1
        });
    }

    private void copyDatabaseFromAssets() {
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            File dbDir = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!dbDir.exists()) {
                dbDir.mkdir();
            }
            try (InputStream is = getAssets().open(DB_NAME);
                 OutputStream os = new FileOutputStream(getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (Exception e) {
                Log.e("Database Error", getString(R.string.error_copy_db, e.getMessage()));
            }
        }
    }

    private void loadWineData() {
        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM Wine", null);

            wineList.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                String description = cursor.getString(3);
                byte[] img = cursor.getBlob(4);
                int idHangSX = cursor.getInt(5);

                wineList.add(new WineDB(id, name, type, description, img, idHangSX));
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("Database Error", getString(R.string.error_load_data, e.getMessage()));
        } finally {
            if (database != null) {
                database.close();
            }
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                loadWineData();
            }
            if (data != null) {

                if (data.hasExtra("updated_wine") || data.hasExtra("deleted_wine_id")) {
                    loadWineData();
                }
            }
        }
    }
}
