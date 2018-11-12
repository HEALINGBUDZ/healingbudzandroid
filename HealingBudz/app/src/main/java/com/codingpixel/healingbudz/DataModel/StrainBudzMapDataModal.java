package com.codingpixel.healingbudz.DataModel;

import java.util.ArrayList;

/**
 * Created by jawadali on 11/17/17.
 */

public class StrainBudzMapDataModal {
    int id;
    int user_id;
    int business_type_id;
    int is_organic;
    int is_delivery;
    double lat;
    double lng;
    String title;
    String logo;
    String banner;
    String description;
    String location;
    String phone;
    String web;
    String facebook;
    String twitter;
    String instagram;
    String created_at;
    String updated_at;
    String stripe_id;
    String card_brand;
    String card_last_four;
    String trial_ends_at;
    String distance;

    ArrayList<BudzMapProductDataModal.Priceing> Priceing;

    public ArrayList<BudzMapProductDataModal.Priceing> getPriceing() {
        return Priceing;
    }

    public void setPriceing(ArrayList<BudzMapProductDataModal.Priceing> priceing) {
        Priceing = priceing;
    }

    ArrayList<Products> products;
    String Strain_type;

    public String getStrain_type() {
        return Strain_type;
    }

    public void setStrain_type(String strain_type) {
        Strain_type = strain_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBusiness_type_id() {
        return business_type_id;
    }

    public void setBusiness_type_id(int business_type_id) {
        this.business_type_id = business_type_id;
    }

    public int getIs_organic() {
        return is_organic;
    }

    public void setIs_organic(int is_organic) {
        this.is_organic = is_organic;
    }

    public int getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(int is_delivery) {
        this.is_delivery = is_delivery;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
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

    public String getStripe_id() {
        return stripe_id;
    }

    public void setStripe_id(String stripe_id) {
        this.stripe_id = stripe_id;
    }

    public String getCard_brand() {
        return card_brand;
    }

    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }

    public String getCard_last_four() {
        return card_last_four;
    }

    public void setCard_last_four(String card_last_four) {
        this.card_last_four = card_last_four;
    }

    public String getTrial_ends_at() {
        return trial_ends_at;
    }

    public void setTrial_ends_at(String trial_ends_at) {
        this.trial_ends_at = trial_ends_at;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    public static class Products {
        int id;
        int sub_user_id;
        int strain_id;
        int type_id;
        String name;
        String thc;
        String cbd;

        ArrayList<BudzMapProductDataModal.Priceing> Priceing;

        public ArrayList<BudzMapProductDataModal.Priceing> getPriceing() {
            return Priceing;
        }

        public void setPriceing(ArrayList<BudzMapProductDataModal.Priceing> priceing) {
            Priceing = priceing;
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
            return thc;
        }

        public void setThc(String thc) {
            this.thc = thc;
        }

        public String getCbd() {
            return cbd;
        }

        public void setCbd(String cbd) {
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


        public int getImages_count() {
            return images_count;
        }

        public void setImages_count(int images_count) {
            this.images_count = images_count;
        }

        String created_at;
        String updated_at;
        int images_count;
    }
}
