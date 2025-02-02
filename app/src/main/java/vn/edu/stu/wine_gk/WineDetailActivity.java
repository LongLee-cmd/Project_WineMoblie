package vn.edu.stu.wine_gk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import vn.edu.stu.wine_gk.model.WineDB;

public class WineDetailActivity extends AppCompatActivity {

    public static final String DB_NAME = "sample_wine.db";

    private WineDB wine;
    private ImageView imgWineEdit;
    private EditText edtNameWine, edtTypeWine, edtDescriptionWine, edtIdHangSX;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_detail);

        addControls();
        loadWineDetails();
        addEvents();
    }

    private void addControls() {
        imgWineEdit = findViewById(R.id.imgWineEdit);
        edtNameWine = findViewById(R.id.edtNameWine);
        edtTypeWine = findViewById(R.id.edtTypeWine);
        edtDescriptionWine = findViewById(R.id.edtDescriptionWine);
        edtIdHangSX = findViewById(R.id.edtIdHangSX);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadWineDetails() {
        wine = (WineDB) getIntent().getSerializableExtra("wine_data");
        if (wine != null) {
            edtNameWine.setText(wine.getNameWine());
            edtTypeWine.setText(wine.getTypeWine());
            edtDescriptionWine.setText(wine.getDescription());
            edtIdHangSX.setText(String.valueOf(wine.getIdHangSX()));

            if (wine.getImgWine() != null) {
                try {
                    byte[] decodedString = Base64.decode(new String(wine.getImgWine()), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgWineEdit.setImageBitmap(decodedByte);
                } catch (Exception e) {
                    imgWineEdit.setImageResource(R.drawable.img);
                }
            } else {
                imgWineEdit.setImageResource(R.drawable.img);
            }
        }
    }

    private void addEvents() {
        btnSave.setOnClickListener(v -> saveWineDetails());
        btnCancel.setOnClickListener(v -> finish());
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void confirmDelete() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.delete_prompt))
                .setPositiveButton(getString(android.R.string.ok), (dialog, which) -> deleteWineDetails())
                .setNegativeButton(getString(android.R.string.cancel), null)
                .show();
    }

    private void saveWineDetails() {
        String name = edtNameWine.getText().toString().trim();
        String type = edtTypeWine.getText().toString().trim();
        String description = edtDescriptionWine.getText().toString().trim();
        String idHangSXStr = edtIdHangSX.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || description.isEmpty() || idHangSXStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        int idHangSX;
        try {
            idHangSX = Integer.parseInt(idHangSXStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.id_must_be_integer), Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

            ContentValues values = new ContentValues();
            values.put("NameWine", name);
            values.put("TypeWine", type);
            values.put("Description", description);
            values.put("IDHangSX", idHangSX);

            if (imgWineEdit.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) imgWineEdit.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                String base64Image = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                values.put("ImgWine", base64Image.getBytes());
            }

            int rows = database.update("Wine", values, "ID = ?", new String[]{String.valueOf(wine.getIdWine())});
            if (rows > 0) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updated_wine", wine); // Gửi dữ liệu đã cập nhật
                setResult(RESULT_OK, resultIntent); // Trả về kết quả OK
                Toast.makeText(this, getString(R.string.wine_update_success), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.wine_update_failure), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.wine_update_error, e.getMessage()), Toast.LENGTH_SHORT).show();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    private void deleteWineDetails() {
        SQLiteDatabase database = null;
        try {
            database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

            int rows = database.delete("Wine", "ID = ?", new String[]{String.valueOf(wine.getIdWine())});
            if (rows > 0) {
                // Gửi kết quả xóa về WineListActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deleted_wine_id", wine.getIdWine()); // Gửi ID của loại rượu đã xóa
                setResult(RESULT_OK, resultIntent); // Trả về kết quả OK
                Toast.makeText(this, getString(R.string.wine_delete_success), Toast.LENGTH_SHORT).show();
                finish(); // Quay lại trang trước mà không phải thoát ứng dụng
            } else {
                Toast.makeText(this, getString(R.string.wine_delete_failure), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.wine_delete_error, e.getMessage()), Toast.LENGTH_SHORT).show();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }
}
