package com.example.tuan7;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SanPhamAdapter extends ArrayAdapter<SanPham> {

    Activity context;
    int resource;

    public SanPhamAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }


    public View getView(int position, View convertView,  ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);
        ImageView imgSP = customView.findViewById(R.id.imgSP);
        ImageView imgEdit = customView.findViewById(R.id.imgEdit);
        ImageView imgDelete = customView.findViewById(R.id.imgDelete);
        TextView txtTenSP = customView.findViewById(R.id.txtTenSP);
        TextView txtGiaSP = customView.findViewById(R.id.txtGiaSP);
        SanPham sp = getItem(position);
        txtTenSP.setText(sp.getTenSP());
//        String gia = Integer.toString(sp.getGiaSP());
        txtGiaSP.setText(Integer.toString(sp.getGiaSP()));

        imgSP.setImageURI(Uri.parse(sp.getImagePath()));

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Edit san pham id = " + getItem(position).getIdSP(), Toast.LENGTH_SHORT).show();
                ((MainActivity) context).showSuaSPDialog(getItem(position));
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Delete san pham id = " + getItem(position).getIdSP(), Toast.LENGTH_SHORT).show();
                ((MainActivity) context).showXoaSPDialog(getItem(position));
            }
        });

        return customView;
    }
}
