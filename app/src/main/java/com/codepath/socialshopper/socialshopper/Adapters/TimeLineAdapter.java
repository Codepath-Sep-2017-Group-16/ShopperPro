package com.codepath.socialshopper.socialshopper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Activities.PaymentActivity;
import com.codepath.socialshopper.socialshopper.Activities.TrackStatusActivity;
import com.codepath.socialshopper.socialshopper.Models.TimeLineModel;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DateTimeUtils;
import com.codepath.socialshopper.socialshopper.Utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.wallet.Cart;
import com.stripe.wrap.pay.activity.StripeAndroidPayActivity;
import com.stripe.wrap.pay.utils.CartContentException;
import com.stripe.wrap.pay.utils.CartManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saripirala on 10/28/17.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;
    private Context mContext;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<TimeLineModel> feedList, boolean withLinePadding) {
        mFeedList = feedList;
        mWithLinePadding = withLinePadding;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = mFeedList.get(position);

        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, android.R.color.holo_green_dark));

        if(!timeLineModel.getDate().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText(DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy"));
        }
        else
            holder.mDate.setVisibility(View.GONE);

        holder.mMessage.setText(timeLineModel.getMessage());

        if (timeLineModel.getmStatus() == "COMPLETED") {
            holder.btnPay.setVisibility(View.VISIBLE);
            String paymentText = timeLineModel.getPaymentStatus() != null &&  timeLineModel.getPaymentStatus() == "PAID" ? "Paid" :  "Pay " + timeLineModel.getMessage().split(" ")[0];
            holder.btnPay.setText(paymentText);

            Boolean payDisabled = timeLineModel.getPaymentStatus() != null &&  timeLineModel.getPaymentStatus() == "PAID";
            holder.btnPay.setEnabled(!payDisabled);

            holder.btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeCart();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    private void initializeCart() {
        CartManager cartManager = new CartManager();
        cartManager.setTotalPrice(1L);
        try {
            Cart cart = cartManager.buildCart();
            Intent intent = new Intent(mContext, PaymentActivity.class)
                    .putExtra(StripeAndroidPayActivity.EXTRA_CART, cart);
            ((TrackStatusActivity) mContext).startActivityForResult(intent, 1);
        } catch (CartContentException e) {
            Toast.makeText(mContext, "error preparing cart", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_timeline_date)
        TextView mDate;
        @BindView(R.id.text_timeline_title)
        TextView mMessage;
        @BindView(R.id.time_marker)
        TimelineView mTimelineView;
        @BindView(R.id.btnPay)
        Button btnPay;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);
        }
    }
}
