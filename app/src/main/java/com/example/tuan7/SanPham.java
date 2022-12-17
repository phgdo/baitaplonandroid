package com.example.tuan7;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int idSP;
    private String tenSP;
    private int giaSP;
    private String imagePath;
    private String filePath;

    public SanPham(){}

    public SanPham(int idSP, String tenSP, int giaSP, String imagePath) {
        this.idSP = idSP;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.imagePath = imagePath;
    }

    public int getIdSP() {
        return idSP;
    }

    public void setIdSP(int idSP) {
        this.idSP = idSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(int giaSP) {
        this.giaSP = giaSP;
    }

    public String getImagePath(){return imagePath;}
    public void setImagePath(){this.imagePath=imagePath;}

    @Override
    public String toString(){
        return "SanPham{"+
                "idSP="+ idSP +
                ", tenSP=" + tenSP +
                ", giaSP" + giaSP +
                ", filePath"+ filePath +
                '\'' +
                '}';
    }
}
