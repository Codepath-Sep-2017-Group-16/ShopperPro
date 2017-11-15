package com.codepath.socialshopper.socialshopper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.socialshopper.socialshopper.Activities.PaymentActivity;
import com.codepath.socialshopper.socialshopper.Activities.TrackStatusActivity;
import com.codepath.socialshopper.socialshopper.Models.TimeLineModel;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.DateTimeUtils;
import com.codepath.socialshopper.socialshopper.Utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.wallet.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.wrap.pay.activity.StripeAndroidPayActivity;
import com.stripe.wrap.pay.utils.CartContentException;
import com.stripe.wrap.pay.utils.CartManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by saripirala on 10/28/17.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>   {

    private List<TimeLineModel> mFeedList;
    private Context mContext;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private DatabaseUtils dbUtils;
    private static String receiptURL;
    private static Long extractReceiptAmount;

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
    public void onBindViewHolder(final TimeLineViewHolder holder, int position)  {

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
            holder.iVReceiptImage.setVisibility(View.VISIBLE);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            DatabaseReference ref = mDatabase.child("lists").child(TimeLineModel.listID).child("receiptImageURL");
            ref.addListenerForSingleValueEvent(


                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String image;
                            image = (String) dataSnapshot.getValue();
                            receiptURL = image;
                            Glide.with(mContext)
                            .load(image)
                            .into(holder.iVReceiptImage);

                            String postBody = readStringFromJSON();
                            postBody = postBody.replace("$__RECEIPT_URL__", receiptURL);
                            try {
                                extractBillAmount(postBody);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
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
        cartManager.setTotalPrice(extractReceiptAmount);
        try {
            Cart cart = cartManager.buildCart();
            Intent intent = new Intent(mContext, PaymentActivity.class)
                    .putExtra(StripeAndroidPayActivity.EXTRA_CART, cart)
                    .putExtra("receiptURL", receiptURL)
                    .putExtra("amount", extractReceiptAmount);
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

        @BindView(R.id.receiptImage)
        ImageView iVReceiptImage;


        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);

            iVReceiptImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeCart();
                }
            });
        }
    }


    void extractBillAmount(String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url("https://vision.googleapis.com/v1/images:annotate?key=AIzaSyCbshrDoUFiWNVYMTqPeT9687NABAgFAjs")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                //Log.d("TAG",response.body().string());
                try {
                    JSONObject result = new JSONObject(jsonData);

                    JSONArray arr = result.getJSONArray("responses");
                    JSONObject responseObj = arr.getJSONObject(0);
                    JSONArray textAnnotationsArr = responseObj.getJSONArray("textAnnotations");
                    String extractedOutput = textAnnotationsArr.getJSONObject(0).getString("description");
                    String [] elements = extractedOutput.split("\n");

                    double maxValue = 0.0;

                    for(String element: elements){
                        try{
                            element = element.replace("$","");
                            if(isNumericAndHasDot(element)){
                                double value =	Double.parseDouble(element);
                                maxValue = value > maxValue ? value: maxValue;
                            }
                        }catch(NumberFormatException e){

                        }
                    }

                    extractReceiptAmount = Math.round(maxValue);

                }catch (JSONException e){
                    Log.d("Exception", e.getLocalizedMessage());
                }
            }
        });
    }

    public boolean isNumericAndHasDot(String inputData) {
        Scanner sc = new Scanner(inputData);
        return sc.hasNextDouble() && inputData.contains(".");
    }


    private String readStringFromJSON() {

        InputStream inputStream = mContext.getResources().openRawResource(R.raw.image_to_text);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            int i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

}


