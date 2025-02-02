package vn.edu.stu.wine_gk.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.stu.wine_gk.R;
import vn.edu.stu.wine_gk.model.Manufacturer;

public class ManufacturerAdapter extends ArrayAdapter<Manufacturer> {
    private final Activity context;
    private final int resource;
    private final List<Manufacturer> manufacturerList;

    public ManufacturerAdapter(@NonNull Activity context, int resource, @NonNull List<Manufacturer> manufacturerList) {
        super(context, resource, manufacturerList);
        this.context = context;
        this.resource = resource;
        this.manufacturerList = manufacturerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Use ViewHolder pattern for better performance and reuse of views
        ViewHolder holder;
        if (convertView == null) {
            // Inflate the layout for the item
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            // Initialize the ViewHolder and save references to views
            holder = new ViewHolder();
            holder.tvIdManufacturer = convertView.findViewById(R.id.tvIdManufacturer);
            holder.tvNameManufacturer = convertView.findViewById(R.id.tvNameManufacturer);

            convertView.setTag(holder); // Save the holder in the convertView
        } else {
            // Reuse existing ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current manufacturer
        Manufacturer manufacturer = manufacturerList.get(position);

        // Bind data to views
        holder.tvIdManufacturer.setText(String.valueOf(manufacturer.getId())); // Updated to use `getId()`
        holder.tvNameManufacturer.setText(manufacturer.getName()); // Updated to use `getName()`

        return convertView;
    }

    // Static ViewHolder class to hold references to views
    private static class ViewHolder {
        TextView tvIdManufacturer;
        TextView tvNameManufacturer;
    }
}
