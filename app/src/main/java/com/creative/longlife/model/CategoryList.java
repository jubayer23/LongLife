package com.creative.longlife.model;

/**
 * Created by jubayer on 8/23/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryList implements Serializable
{

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    private final static long serialVersionUID = 8568429893725549267L;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}