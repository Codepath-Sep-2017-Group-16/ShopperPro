package com.codepath.socialshopper.socialshopper.Adapters;

/**
 * Created by gumapathi on 10/22/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.socialshopper.socialshopper.Activities.ShoppingListActivity;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;

import java.util.HashMap;
import java.util.List;

import static com.codepath.socialshopper.socialshopper.Activities.MainActivity.shoppingList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ShoppableItem>> listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<ShoppableItem>> listChildData) {
        this._context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    public Object getChildren(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ShoppableItem childText = (ShoppableItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_list, null);
        }

        ImageView ivItemImage = (ImageView) convertView.findViewById(R.id.ivItemImage);

        TextView tvItemQtyList = (TextView) convertView
                .findViewById(R.id.tvItemQuantity);

        TextView tvItemName = (TextView) convertView
                .findViewById(R.id.tvItemName);


        TextView tvItemBrand = (TextView) convertView
                .findViewById(R.id.tvItemBrand);

        String qty = childText.getmItemQty().isEmpty() ? "-" : childText.getmItemQty();
        String name = childText.getmItemName().isEmpty() ? "-" : childText.getmItemName();
        String brand = childText.getmItemBrand().isEmpty() ? "-" : childText.getmItemBrand();
        String icon = childText.getmItemIconFileName().isEmpty() ? "" : childText.getmItemIconFileName();
        int id = _context.getResources().getIdentifier(icon, "drawable", _context.getPackageName());
        Drawable drawable = _context.getResources().getDrawable(id);

        tvItemQtyList.setText(qty);
        tvItemName.setText(name);
        tvItemBrand.setText(brand);
        ivItemImage.setImageDrawable(drawable);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null ? 0 : this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        Button btnReorder = (Button) convertView.findViewById(R.id.btnReorder);
        btnReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ShoppableItem> reOrderItems = (List<ShoppableItem>) getChildren(groupPosition);
                shoppingList.setStore(headerTitle);
                for (ShoppableItem item :
                        reOrderItems) {
                    shoppingList.addItems(item);
                }

                Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}