package vn.edu.stu.wine_gk.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.stu.wine_gk.R;
import vn.edu.stu.wine_gk.WineDetailActivity;
import vn.edu.stu.wine_gk.model.WineDB;

public class WineAdapterDB extends ArrayAdapter<WineDB> {

    private Activity context;
    private int resource;
    private List<WineDB> wineList;

    public WineAdapterDB(@NonNull Activity context, int resource, @NonNull List<WineDB> wineList) {
        super(context, resource, wineList);
        this.context = context;
        this.resource = resource;
        this.wineList = wineList;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            item = context.getLayoutInflater().inflate(resource, null);
        }

        TextView tvNameWine = item.findViewById(R.id.tvNameWine);
        TextView tvTypeWine = item.findViewById(R.id.tvTypeWine);
        TextView tvDescriptionWine = item.findViewById(R.id.tvDescriptionWine);
        ImageView imgWine = item.findViewById(R.id.imgWine);
        TextView tvHangSX = item.findViewById(R.id.tvIDHangSX);

        // Bind data to views
        WineDB wine = wineList.get(position);
        tvNameWine.setText(wine.getNameWine());
        tvTypeWine.setText(wine.getTypeWine());
        tvDescriptionWine.setText(wine.getDescription());
        tvHangSX.setText("Nhà SX: " + wine.getIdHangSX());

        // Decode Base64 image string
        String base64Image = new String(wine.getImgWine());
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgWine.setImageBitmap(decodedByte);
            } catch (Exception e) {
                Log.e("Image Decode Error", "Failed to decode image: " + e.getMessage());
                imgWine.setImageResource(R.drawable.img); // Default image in case of error
            }
        } else {
            imgWine.setImageResource(R.drawable.img); // Default image if no Base64 string
        }

        // Handle item click
        item.setOnClickListener(v -> {
            Intent intent = new Intent(context, WineDetailActivity.class);
            intent.putExtra("wine_data", wine); // Pass the WineDB object
            context.startActivity(intent);
        });

        return item;
    }
}
