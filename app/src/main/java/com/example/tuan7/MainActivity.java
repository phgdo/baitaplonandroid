package com.example.tuan7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<SanPham> listSP;
    ListView lvSP;
    SanPhamAdapter spAdapter;
    String DB_NAME = "databasesp.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    Database database;
    ImageView imgSP;
    Uri imgUrl;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvSP = findViewById(R.id.lvSP);
        listSP = new ArrayList<>();
        spAdapter = new SanPhamAdapter(MainActivity.this, R.layout.san_pham_item);
        lvSP.setAdapter(spAdapter);


        //Tao CSDL
        database = new Database(this, DB_NAME, null, 2);
        //Tao bang san pham
        database.queryData("CREATE TABLE IF NOT EXISTS " +
                "SanPham2("+
                "id integer primary key autoincrement, " +
                "tenSP varchar(255),"+
                "giaSP integer, "+
                "filePath varchar(500)" +
                ")");

        //Insert du lieu
//        database.queryData("INSERT INTO SanPham VALUES(null, 'San pham A', '20000')");
        //Doc du lieu trong CSDL va them vao ListView
        loadDataSanPham();
    }

    //Tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.san_pham_mnu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAddSP:
                showThemSPDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    // Dialog them san pham
    public void showThemSPDialog() {
        Dialog dialogThemSP = new Dialog(this);
        dialogThemSP.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogThemSP.setContentView(R.layout.dialog_them_san_pham);
        dialogThemSP.setCanceledOnTouchOutside(false);
        Button btnThemSP = dialogThemSP.findViewById(R.id.btnThemSP);
        Button btnHuyThemSP = dialogThemSP.findViewById(R.id.btnHuyThemSP);

        EditText edtTenSP = dialogThemSP.findViewById(R.id.edtTenSP);
        EditText edtGiaSP = dialogThemSP.findViewById(R.id.edtGiaSP);

        Button btnChonAnh = dialogThemSP.findViewById(R.id.btnChonAnh);
        imgSP = dialogThemSP.findViewById(R.id.imgSP);
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addControls();
                addEvents();
            }
        });

        btnThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenSP = edtTenSP.getText().toString().trim();
                int giaSP = Integer.parseInt(edtGiaSP.getText().toString().trim());
                if(tenSP.length()>0 && giaSP>=0){
                    database.queryData("INSERT INTO SanPham2 VALUES(null, '" + tenSP + "'," + giaSP + ", '" + imgUri+ "')");
                    Toast.makeText(MainActivity.this, "Them SP + '" + tenSP + "' thanh cong!", Toast.LENGTH_SHORT).show();
                    dialogThemSP.dismiss();
                    loadDataSanPham();
                }
                else if(giaSP < 0){
                    Toast.makeText(MainActivity.this, "Vui long nhap dung so tien.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Vui long nhap ten san pham", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHuyThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogThemSP.dismiss();
            }
        });
        dialogThemSP.show();
    }


    //Dialog sua san pham
    public void showSuaSPDialog(SanPham sp){
        //Tao cua so sua san pham
        Dialog suaSPDialog = new Dialog(this);
        suaSPDialog.setContentView(R.layout.dialog_sua_cong_viec);
        suaSPDialog.setCanceledOnTouchOutside(false);

        //Ket noi cac view
        TextView txtIDSP = suaSPDialog.findViewById(R.id.txtIdSP);
        EditText edtTenSP = suaSPDialog.findViewById(R.id.edtTenSP);
        EditText edtGiaSP = suaSPDialog.findViewById(R.id.edtGiaSP);

        Button btnSuaSP = suaSPDialog.findViewById(R.id.btnSuaSP);
        Button btnHuySuaSP = suaSPDialog.findViewById(R.id.btnHuySuaSP);
        Button btnSuaAnh = suaSPDialog.findViewById(R.id.btnSuaAnh);
        txtIDSP.setText("ID: " + sp.getIdSP());
        edtTenSP.setText(sp.getTenSP());

        imgSP = suaSPDialog.findViewById(R.id.imgSP);

        String gia = Integer.toString(sp.getGiaSP());
        edtGiaSP.setText(gia);

        btnSuaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addControls();
                addEvents();
            }
        });

        btnSuaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temSPMoi = edtTenSP.getText().toString().trim();
                int giaSPMoi = Integer.parseInt(edtGiaSP.getText().toString().trim());
                if(temSPMoi.length()>0 && giaSPMoi>=0){
                    database.queryData("UPDATE SanPham2 SET tenSP= '" + temSPMoi +"', giaSP = " + giaSPMoi+ ", " + "filePath= '" + imgUri + "' WHERE id = " + sp.getIdSP());
                    Toast.makeText(MainActivity.this, "Sua SP + '" + temSPMoi + "' thanh cong!", Toast.LENGTH_SHORT).show();
                    suaSPDialog.dismiss();
                    loadDataSanPham();
                }
                else if(giaSPMoi < 0){
                    Toast.makeText(MainActivity.this, "Vui long nhap dung so tien.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Vui long nhap ten san pham", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHuySuaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaSPDialog.dismiss();
            }
        });
        suaSPDialog.show();
    }

    //Xoa san pham
    public void showXoaSPDialog(SanPham sp){
        AlertDialog.Builder dialogXoaSP = new AlertDialog.Builder(this);
        dialogXoaSP.setMessage("Ban co muon xoa " + sp.toString() + "?");
        dialogXoaSP.setCancelable(false);
        dialogXoaSP.setPositiveButton("Co", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.queryData("DELETE FROM SanPham2 "+
                        "WHERE id = '" + sp.getIdSP()+"'");
                Toast.makeText(MainActivity.this, "Xoa " + sp.toString() + " thanh cong", Toast.LENGTH_SHORT).show();
                loadDataSanPham();
            }
        });
        dialogXoaSP.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogXoaSP.show();
    }

    private void loadDataSanPham() {
        Cursor dataSanPham = database.getData("SELECT * FROM SanPham2");
        spAdapter.clear();
        SanPham sp;
        while(dataSanPham.moveToNext()){
            int idSP = dataSanPham.getInt(0);
            String tenSP = dataSanPham.getString(1);
            int giaSP = dataSanPham.getInt(2);
            String filePath = dataSanPham.getString(3);
            sp = new SanPham(idSP, tenSP, giaSP, filePath);
            spAdapter.add(sp);
        }
        spAdapter.notifyDataSetChanged();
    }

    //Chon anh
    public void xuLyLayAnh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Chọn ảnh"), 113);
    }

    private void addControls(){
        Button btnChonAnh = findViewById(R.id.btnChonAnh);
        ImageView imgSp = findViewById(R.id.imgSP);
    }

    private void addEvents(){
        xuLyLayAnh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==113 && resultCode==RESULT_OK){
            try {
                imgUri = data.getData();
                Bitmap hinh = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                imgSP.setImageBitmap(hinh);
                Toast.makeText(MainActivity.this, "path: " + imgUri, Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex){
                Log.e("Khong the hien thi anh", ex.toString());
            }
        }
    }
}