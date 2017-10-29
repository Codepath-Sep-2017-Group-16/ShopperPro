package com.codepath.socialshopper.socialshopper.Models;

import org.parceler.Parcel;

/**
 * Created by gumapathi on 10/11/2017.
 */
@Parcel(analyze={ShoppableItem.class})
public class ShoppableItem {

    String mItemId;
    String mItemName;
    String mItemIconFileName;
    String mItemMeasure;
    Integer mItemQty;
    String mItemBrand;

    public String getmItemId() {
        return mItemId;
    }

    public void setmItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    public Integer getmItemQty() {
        return mItemQty;
    }

    public void setmItemQty(Integer mItemQty) {
        this.mItemQty = mItemQty;
    }

    public String getmItemBrand() {
        return mItemBrand;
    }

    public void setmItemBrand(String mItemBrand) {
        this.mItemBrand = mItemBrand;
    }

    public String getmItemName() {
        return mItemName;
    }

    public void setmItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getmItemIconFileName() {
        return mItemIconFileName;
    }

    public void setmItemIconFileName(String mItemIconFileName) {
        this.mItemIconFileName = mItemIconFileName;
    }

    public String getItemMeasure() {
        return mItemMeasure;
    }

    public void setItemMeasure(String mItemMeasure) {
        this.mItemMeasure = mItemMeasure;
    }

    public ShoppableItem() {
    }

    public ShoppableItem(String mItemId, String mItemName, String mItemIconFileName, Integer mItemQty, String mItemBrand, String itemMeasure) {
        this.mItemId = mItemId;
        this.mItemName = mItemName;
        this.mItemIconFileName = mItemIconFileName;
        this.mItemQty = mItemQty;
        this.mItemBrand = mItemBrand;
        this.mItemMeasure = itemMeasure;
    }

    @Override
    public String toString() {
        return "ShoppableItem{" +
                "mItemId='" + mItemId + '\'' +
                ", mItemName='" + mItemName + '\'' +
                ", mItemIconFileName='" + mItemIconFileName + '\'' +
                ", mItemQty='" + mItemQty + '\'' +
                ", mItemBrand='" + mItemBrand + '\'' +
                '}';
    }
}
