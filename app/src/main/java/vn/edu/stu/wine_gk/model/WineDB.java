package vn.edu.stu.wine_gk.model;

import java.io.Serializable;

public class WineDB implements Serializable {

    private int idWine;
    private String nameWine;
    private String typeWine;
    private String description;
    private byte[] imgWine;
    private int idHangSX;

    public WineDB() {
    }

    public WineDB(int idWine, String nameWine, String typeWine, String description, byte[] imgWine, int idHangSX) {
        this.idWine = idWine;
        this.nameWine = nameWine;
        this.typeWine = typeWine;
        this.description = description;
        this.imgWine = imgWine;
        this.idHangSX = idHangSX;
    }

    public int getIdWine() {
        return idWine;
    }

    public void setIdWine(int idWine) {
        this.idWine = idWine;
    }

    public String getNameWine() {
        return nameWine;
    }

    public void setNameWine(String nameWine) {
        this.nameWine = nameWine;
    }


    public String getTypeWine() {
        return typeWine;
    }

    public void setTypeWine(String typeWine) {
        this.typeWine = typeWine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImgWine() {
        return imgWine;
    }

    public void setImgWine(byte[] imgWine) {
        this.imgWine = imgWine;
    }

    public int getIdHangSX() {
        return idHangSX;
    }

    public void setIdHangSX(int idHangSX) {
        this.idHangSX = idHangSX;
    }
}
