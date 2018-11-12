package com.codingpixel.healingbudz.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jawadali on 12/20/17.
 */

public class BudzMapProductDataModal implements Serializable {
    private int id;
    private int sub_user_id;
    private int strain_id;
    private int type_id;
    private String name;
    private boolean isAlsoStrain;
    private Double thc;
    private boolean isIndica = false;
    private boolean isStive = false;
    private boolean isHybrid = false;
    private boolean isOther = false;
    private String alsoStrain = "";
    private Double cbd;
    private String created_at;
    private String updated_at;
    private String strian_type_title;
    private Integer idType;
    private ArrayList<String> image;
    private ArrayList<Priceing> Priceing;
    /////Strain Things
    private String charges;
    private String image_path;
    private boolean isProduct;
    private boolean isTitle = false;
    private GenaricModel strainModel;
    private GenaricModel categoryModel;

    public GenaricModel getStrainModel() {
        return strainModel;
    }

    public void setStrainModel(GenaricModel strainModel) {
        this.strainModel = strainModel;
    }

    public GenaricModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(GenaricModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public String getAlsoStrain() {
        return alsoStrain;
    }

    public void setAlsoStrain(String alsoStrain) {
        this.alsoStrain = alsoStrain;
    }

    public boolean isAlsoStrain() {
        return isAlsoStrain;
    }

    public void setAlsoStrain(boolean alsoStrain) {
        isAlsoStrain = alsoStrain;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }

    public void setHybrid(boolean hybrid) {
        isHybrid = hybrid;
    }

    public void setIndica(boolean indica) {
        isIndica = indica;
    }

    public void setStive(boolean stive) {
        isStive = stive;
    }

    public boolean isHybrid() {
        return isHybrid;
    }

    public boolean isIndica() {
        return isIndica;
    }

    public boolean isStive() {
        return isStive;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public void setProduct(boolean product) {
        isProduct = product;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSub_user_id() {
        return sub_user_id;
    }

    public void setSub_user_id(int sub_user_id) {
        this.sub_user_id = sub_user_id;
    }

    public int getStrain_id() {
        return strain_id;
    }

    public void setStrain_id(int strain_id) {
        this.strain_id = strain_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThc() {
        if (thc == null || thc.isNaN()) {
            return String.valueOf("");
        }
        return String.valueOf(thc);
    }

    public void setThc(Double thc) {

        this.thc = thc;
    }

    public String getCbd() {
        if (cbd == null || cbd.isNaN()) {
            return String.valueOf("");
        }
        return String.valueOf(cbd);
    }

    public void setCbd(Double cbd) {
        this.cbd = cbd;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStrian_type_title() {
        return strian_type_title;
    }

    public void setStrian_type_title(String strian_type_title) {
        this.strian_type_title = strian_type_title;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<BudzMapProductDataModal.Priceing> getPriceing() {
        return Priceing;
    }

    public void setPriceing(ArrayList<BudzMapProductDataModal.Priceing> priceing) {
        Priceing = priceing;
    }

    public static class Priceing implements Serializable {
        String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        String weight;
    }

    public static class GenaricModel implements Serializable {
        int id;
        String title;

        public GenaricModel(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
