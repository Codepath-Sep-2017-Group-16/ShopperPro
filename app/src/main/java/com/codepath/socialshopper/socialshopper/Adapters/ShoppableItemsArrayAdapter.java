package com.codepath.socialshopper.socialshopper.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Activities.MainActivity;
import com.codepath.socialshopper.socialshopper.Fragments.AddItemDetailsDialogFragment;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * Created by gumapathi on 10/11/2017.
 */

public class ShoppableItemsArrayAdapter extends RecyclerView.Adapter<ShoppableItemsArrayAdapter.ShoppableItemsViewHolder> {
    Context mContext;
    ArrayList<ShoppableItem> shoppableItems;
    public final String TAG = "SocShpAdap";
    private Activity activity;
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    public ShoppableItemsArrayAdapter(ArrayList<ShoppableItem> shoppableItems) {
        this.shoppableItems = shoppableItems;
    }

    public interface OnUpdateItemListener {
        void OnUpdateItem(ShoppableItem shoppableItem);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ShoppableItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_shoppable, parent, false);
        ShoppableItemsViewHolder viewHolder = new ShoppableItemsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShoppableItemsViewHolder holder, int position) {
        final ShoppableItem shoppableItem = shoppableItems.get(position);
        holder.Bind(shoppableItem);

        //holder.btnShoppableItem.setText(shoppableItem.getmItemName());
        Drawable top = ContextCompat.getDrawable(mContext, mContext.getResources().getIdentifier(shoppableItem.getmItemIconFileName(), "raw", mContext.getPackageName()));
        //holder.btnShoppableItem.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        holder.btnShoppableItem.setImageDrawable(top);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showAddOptions();
            }
        });
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showRemoveOptions();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.showOptions();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppableItems.size();
    }

    public class ShoppableItemsViewHolder extends RecyclerView.ViewHolder {
        ShoppableItem shoppableItem;
        ImageView btnShoppableItem;
        TextView tvAmount;
        TextView tvMeasure;
        Button btnAdd;
        Button btnRemove;
        TextView tvPlusOne;

        public ShoppableItemsViewHolder(View itemView) {
            super(itemView);
            btnShoppableItem = (ImageView) itemView.findViewById(R.id.btnShoppableItem);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvMeasure = (TextView) itemView.findViewById(R.id.tvMeasure);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            btnRemove = (Button) itemView.findViewById(R.id.btnRemove);
            tvPlusOne = (TextView) itemView.findViewById(R.id.tvPLusOne);
        }

        public void Bind(ShoppableItem shoppableItem) {
            this.shoppableItem = shoppableItem;
        }

        private void showOptions() {
            increaseAmount();
            final ObjectAnimator addAnim = ObjectAnimator.ofFloat(btnAdd, View.ALPHA, 0, 1);
            addAnim.setDuration(400);

            final ObjectAnimator amountAnim = ObjectAnimator.ofFloat(tvAmount, View.ALPHA, 0, 1);
            amountAnim.setDuration(800);

            final ObjectAnimator measureAnim = ObjectAnimator.ofFloat(tvMeasure, View.ALPHA, 0, 1);
            measureAnim.setDuration(800);

            final ObjectAnimator removeAnim = ObjectAnimator.ofFloat(btnRemove, View.ALPHA, 0, 1);
            removeAnim.setDuration(1600);

            AnimatorSet animatorSet = new AnimatorSet();
            if (btnAdd.getAlpha() > 0)
                animatorSet.playTogether(amountAnim, measureAnim);
            else
                animatorSet.playTogether(addAnim, amountAnim, measureAnim, removeAnim);
            animatorSet.start();
        }

        private void showAddOptions() {
            final ObjectAnimator addAnim = ObjectAnimator.ofFloat(btnAdd, View.ALPHA, 0, 1);
            addAnim.setDuration(400);

            addAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    increaseAmount();
                }
            });
            addAnim.start();
        }

        private void showRemoveOptions() {
            final ObjectAnimator removeAnim = ObjectAnimator.ofFloat(btnRemove, View.ALPHA, 1, 0);
            removeAnim.setDuration(200);
            removeAnim.setRepeatMode(ValueAnimator.REVERSE);
            removeAnim.setRepeatCount(1);

            removeAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    decreaseAmount();
                }
            });
            removeAnim.start();
        }

        private void increaseAmount() {
            int amount = tvAmount.getText().toString().isEmpty() ? 0 : Integer.valueOf(tvAmount.getText().toString());
            int newAmount = amount + 1;

            shoppableItem.setmItemQty(newAmount);
            updateUI(shoppableItem);
        }

        private void decreaseAmount() {
            int amount = Integer.valueOf(tvAmount.getText().toString());
            int newAmount = amount > 0 ? amount - 1 : amount;

            shoppableItem.setmItemQty(newAmount);
            updateUI(shoppableItem);
        }

        private void updateUI(ShoppableItem shoppableItem) {
            tvAmount.setText(Integer.toString(shoppableItem.getmItemQty()));
            tvMeasure.setText(shoppableItem.getItemMeasure());

            if (shoppableItem.getmItemQty() == 0) {
                hideOptions();
            }

            final OnUpdateItemListener updateItemListener = (OnUpdateItemListener) activity;
            updateItemListener.OnUpdateItem(shoppableItem);
        }

        private void hideOptions() {
            final ObjectAnimator removeAnim = ObjectAnimator.ofFloat(btnRemove, View.ALPHA, 1, 0);
            removeAnim.setDuration(300);

            final ObjectAnimator addAnim = ObjectAnimator.ofFloat(btnAdd, View.ALPHA, 1, 0);
            removeAnim.setDuration(300);

            final ObjectAnimator amountAnim = ObjectAnimator.ofFloat(tvAmount, View.ALPHA, 1, 0);
            amountAnim.setDuration(300);

            final ObjectAnimator measureAnim = ObjectAnimator.ofFloat(tvMeasure, View.ALPHA, 1, 0);
            measureAnim.setDuration(300);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(removeAnim, amountAnim, measureAnim, addAnim);
            animatorSet.start();
        }
    }
}
