package vn.edu.stu.wine_gk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AddWineActivity extends AppCompatActivity {

    private static final String DB_NAME = "sample_wine.db";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText edtName, edtType, edtDescription;
    private Spinner spinnerManufacturer;
    private Button btnAddWine, btnCancel, btnChooseImage;
    private ImageView imgWinePreview;
    private byte[] imageBytes;

    private HashMap<String, Integer> manufacturerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wine);

        addControls();
        loadManufacturers();
        addEvents();
    }

    private void addControls() {
        edtName = findViewById(R.id.edtName);
        edtType = findViewById(R.id.edtType);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerManufacturer = findViewById(R.id.spinnerManufacturer);
        btnAddWine = findViewById(R.id.btnAddWine);
        btnCancel = findViewById(R.id.btnCancel);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imgWinePreview = findViewById(R.id.imgWinePreview);
        manufacturerMap = new HashMap<>();
    }

    private void loadManufacturers() {
        ArrayList<String> manufacturerNames = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT IDHangSX, NameHangSX FROM HangSX", null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);

                manufacturerMap.put(name, id);
                manufacturerNames.add(name);
            }
            cursor.close();

            if (!manufacturerNames.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, manufacturerNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerManufacturer.setAdapter(adapter);
            } else {
                Toast.makeText(this, getString(R.string.manufacturer_not_found), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.manufacturer_load_error, e.getMessage()), Toast.LENGTH_SHORT).show();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    private void addEvents() {
        btnChooseImage.setOnClickListener(v -> chooseImageFromGallery());
        btnAddWine.setOnClickListener(v -> addWineToDatabase());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgWinePreview.setImageBitmap(bitmap);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                imageBytes = outputStream.toByteArray();
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.image_select_error, e.getMessage()), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addWineToDatabase() {
        String name = edtName.getText().toString().trim();
        String type = edtType.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String selectedManufacturer = (String) spinnerManufacturer.getSelectedItem();

        if (name.isEmpty() || type.isEmpty() || description.isEmpty() || selectedManufacturer == null) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        int idHangSX = manufacturerMap.get(selectedManufacturer);

        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("NameWine", name);
            values.put("TypeWine", type);
            values.put("Description", description);
            values.put("IDHangSX", idHangSX);

            if (imageBytes != null) {
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                values.put("ImgWine", base64Image);
            } else {
                values.put("ImgWine", "");
            }

            long result = database.insert("Wine", null, values);

            if (result != -1) {
                Toast.makeText(this, getString(R.string.wine_add_success), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, getString(R.string.wine_add_failure), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.wine_add_error, e.getMessage()), Toast.LENGTH_SHORT).show();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }
}
