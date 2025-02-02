package vn.edu.stu.wine_gk;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import vn.edu.stu.wine_gk.adapter.ManufacturerAdapter;
import vn.edu.stu.wine_gk.model.Manufacturer;

public class ManufacturerActivity extends AppCompatActivity {

    public static final String DB_NAME = "sample_wine.db";

    private ListView lvManufacturer;
    private ArrayList<Manufacturer> manufacturerList;
    private ManufacturerAdapter adapter;
    private Button btnAddManufacturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer);

        setupToolbar();
        addControls();
        loadManufacturerData();
        addEvents();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.manufacturer_management));
        }
    }

    private void addControls() {
        lvManufacturer = findViewById(R.id.lvManufacturer);
        btnAddManufacturer = findViewById(R.id.btnAddManufacturer);

        manufacturerList = new ArrayList<>();
        adapter = new ManufacturerAdapter(this, R.layout.custom_item_manufacturer, manufacturerList);
        lvManufacturer.setAdapter(adapter);
    }

    private void addEvents() {
        btnAddManufacturer.setOnClickListener(v -> showAddDialog());

        lvManufacturer.setOnItemClickListener((parent, view, position, id) -> {
            Manufacturer manufacturer = manufacturerList.get(position);
            showEditDialog(manufacturer);
        });

        lvManufacturer.setOnItemLongClickListener((parent, view, position, id) -> {
            Manufacturer manufacturer = manufacturerList.get(position);
            showDeleteDialog(manufacturer);
            return true;
        });
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
    private void loadManufacturerData() {
        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM HangSX", null);

            manufacturerList.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                manufacturerList.add(new Manufacturer(id, name));
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("Database Error", getString(R.string.error_loading_manufacturers, e.getMessage()));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    private void showAddDialog() {
        EditText input = new EditText(this);
        input.setHint(R.string.enter_manufacturer_name);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_manufacturer)
                .setView(input)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        addManufacturer(name);
                    } else {
                        Toast.makeText(this, R.string.error_name_empty, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void addManufacturer(String name) {
        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("NameHangSX", name);

            long result = database.insert("HangSX", null, values);
            if (result != -1) {
                Toast.makeText(this, R.string.manufacturer_added_successfully, Toast.LENGTH_SHORT).show();
                loadManufacturerData();
            } else {
                Toast.makeText(this, R.string.error_adding_manufacturer, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Database Error", getString(R.string.error_adding_manufacturer_log, e.getMessage()));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    private void showEditDialog(Manufacturer manufacturer) {
        EditText input = new EditText(this);
        input.setText(manufacturer.getName());

        new AlertDialog.Builder(this)
                .setTitle(R.string.edit_manufacturer)
                .setView(input)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        updateManufacturer(manufacturer.getId(), newName);
                    } else {
                        Toast.makeText(this, R.string.error_name_empty, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void updateManufacturer(int id, String newName) {
        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("NameHangSX", newName);

            int rows = database.update("HangSX", values, "IDHangSX = ?", new String[]{String.valueOf(id)});
            if (rows > 0) {
                Toast.makeText(this, R.string.manufacturer_updated_successfully, Toast.LENGTH_SHORT).show();
                loadManufacturerData();
            } else {
                Toast.makeText(this, R.string.error_updating_manufacturer, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Database Error", getString(R.string.error_updating_manufacturer_log, e.getMessage()));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    private void showDeleteDialog(Manufacturer manufacturer) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_manufacturer)
                .setMessage(R.string.confirm_delete_manufacturer)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteManufacturer(manufacturer.getId()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteManufacturer(int id) {
        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

            Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM Wine WHERE IDHangSX = ?", new String[]{String.valueOf(id)});
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            if (count > 0) {
                Toast.makeText(this, R.string.error_manufacturer_linked_to_wines, Toast.LENGTH_SHORT).show();
                return;
            }

            int rows = database.delete("HangSX", "IDHangSX = ?", new String[]{String.valueOf(id)});
            if (rows > 0) {
                Toast.makeText(this, R.string.manufacturer_deleted_successfully, Toast.LENGTH_SHORT).show();
                loadManufacturerData();
            } else {
                Toast.makeText(this, R.string.error_deleting_manufacturer, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Database Error", getString(R.string.error_deleting_manufacturer_log, e.getMessage()));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }
}
