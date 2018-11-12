package com.codingpixel.healingbudz.DataModel;

/**
 * Created by incubasyss on 30/03/2018.
 */

//public class Product_Redeem {
//}
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product_Redeem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_points")
    @Expose
    private Integer productPoints;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("get_product")
    @Expose
    private GetProduct getProduct;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product_Redeem withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Product_Redeem withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Product_Redeem withProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public Integer getProductPoints() {
        return productPoints;
    }

    public void setProductPoints(Integer productPoints) {
        this.productPoints = productPoints;
    }

    public Product_Redeem withProductPoints(Integer productPoints) {
        this.productPoints = productPoints;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Product_Redeem withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Product_Redeem withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Product_Redeem withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public GetProduct getGetProduct() {
        return getProduct;
    }

    public void setGetProduct(GetProduct getProduct) {
        this.getProduct = getProduct;
    }

    public Product_Redeem withGetProduct(GetProduct getProduct) {
        this.getProduct = getProduct;
        return this;
    }

}