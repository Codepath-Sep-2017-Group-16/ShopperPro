package com.codepath.socialshopper.socialshopper.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppersListArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.Status;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PickUpListActivity extends AppCompatActivity implements DatabaseUtils.OnListFetchListener, DatabaseUtils.OnActiveListsFetchListener{

    private DatabaseUtils dbUtils;
    private ShoppersListArrayAdapter shoppersListArrayAdapter;
    private ArrayList<ShoppableItem> list;
    private RecyclerView rvItems;
    private String savedPhotoPath;
    private static final int REQUEST_CODE_TAKE_PICTURE = 1000;
    private static final String TAG = "SocShpPkUpAct";
    private static String listId;
    private TextView tvItemsCount;
    private SwipeButton swipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppers_list);
        list = new ArrayList<>();
        shoppersListArrayAdapter = new ShoppersListArrayAdapter(this, list);
        rvItems = (RecyclerView) findViewById(R.id.rvShoppingListItems);
        rvItems.setAdapter(shoppersListArrayAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        rvItems.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        tvItemsCount = (TextView) findViewById(R.id.tvItemsCount);
        swipeButton = (SwipeButton) findViewById(R.id.swipe_btn);

        swipeButton.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                takePhoto();
            }
        });

        dbUtils = new DatabaseUtils();
        processIntentAction(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntentAction(intent);
        super.onNewIntent(intent);
    }

    private void processIntentAction(Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();

        String listIds = intent.getStringExtra("list_id");
        Log.d(TAG, listIds);
        List<String> lists = Arrays.asList(listIds.split("\\s*,\\s*"));
        Log.d(TAG, lists.get(0));
        // TODO: Retrieve individual shopping list for all the list IDs passed in the intent. Need to see how to display that
        // Currently getting the first list in item to avoid ezception
        dbUtils.getShoppingListByListId(this,lists.get(0));
        DatabaseUtils.saveNameOfShopperAfterPickUp(lists.get(0));
        DatabaseUtils.saveFBIdOfShopperAfterPickUp(lists.get(0));
        DatabaseUtils.updateListStatus(lists.get(0), Status.PICKED_UP);
    }

    @Override
    public void OnListFetchListener(ShoppingList shoppingList) {
        listId = shoppingList.getListId();
        list.addAll(shoppingList.getItems());
        tvItemsCount.setText(""+ list.size());
        updateAdapter();
    }

    @Override
    public void OnListsFetchListener(ArrayList<ShoppingList> shoppingLists) {
        //TODO
    }

    private void updateAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shoppersListArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Tell the Intent where to save our image.
        File photoFile = null;
        try {
            photoFile = createFileToStorePhoto();
        }catch (IOException e){
            Log.d(TAG, e.getLocalizedMessage());
        }
        savedPhotoPath = photoFile.getAbsolutePath();
        Uri photoURI = FileProvider.getUriForFile(this,
                "com.codepath.socialshopper.socialshopper.fileprovider",
                photoFile);
        Log.d(TAG, "Saving to: " + savedPhotoPath);
        Log.d(TAG, "FileProvider URI: " + photoURI);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        // If this device has an app available that takes pictures, launch it.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "Launching Intent for camera");
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
        } else {
            Log.w(TAG, "Could not launch Intent for camera because this device does not support it.");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (REQUEST_CODE_TAKE_PICTURE == requestCode) {
            Log.d(TAG, "Got result from picture app.");
            if (RESULT_OK == resultCode) {
                Log.d(TAG, "Picture taking was successful.");
                    //ImageView ivPreview = (ImageView) findViewById(R.id.ivReceipt);
                    Log.d(TAG, "Photo path: " + savedPhotoPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(savedPhotoPath, null);
                    //ivPreview.setImageBitmap(bitmap);
                    Log.d(TAG, "Bitmap: " + bitmap);
                    DatabaseUtils.saveImage(listId, savedPhotoPath);

                    DatabaseUtils.updateListStatus(listId, Status.COMPLETED);
                    Intent intent = new Intent(this, ShareLocationActivity.class);
                    intent.putExtra("list_id", listId);
                    startActivity(intent);

            } else {
                Log.d(TAG, "Failed to take picture.");
            }
        }

    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private File createFileToStorePhoto() throws IOException {
        // Create an image file name
        String imageFileName = "photo_from_intent";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }


    public void notifyRequestor(View view) {
        //TODO do a post to server with image of receipt and this should trigger a push notification for requestor.
        DatabaseUtils.updateListStatus(listId, Status.COMPLETED);
        Intent intent = new Intent(this, ShareLocationActivity.class);
        intent.putExtra("list_id", listId);
        startActivity(intent);
    }


    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
