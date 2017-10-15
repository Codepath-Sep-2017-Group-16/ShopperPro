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
    String mItemQty;
    String mItemBrand;

    public String getmItemId() {
        return mItemId;
    }

    public void setmItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    public String getmItemQty() {
        return mItemQty;
    }

    public void setmItemQty(String mItemQty) {
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

    public ShoppableItem() {
    }

    public ShoppableItem(String mItemId, String mItemName, String mItemIconFileName, String mItemQty, String mItemBrand) {
        this.mItemId = mItemId;
        this.mItemName = mItemName;
        this.mItemIconFileName = mItemIconFileName;
        this.mItemQty = mItemQty;
        this.mItemBrand = mItemBrand;
    }
}
