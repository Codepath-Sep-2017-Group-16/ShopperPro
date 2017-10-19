package com.codepath.socialshopper.socialshopper.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppersListArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PickUpListActivity extends AppCompatActivity implements DatabaseUtils.OnListFetchListener, DatabaseUtils.OnActiveListsFetchListener{

    private DatabaseUtils dbUtils;
    private ShoppersListArrayAdapter shoppersListArrayAdapter;
    private ArrayList<ShoppableItem> list;
    private RecyclerView rvItems;
    private String savedPhotoPath;
    private static final int REQUEST_CODE_TAKE_PICTURE = 1000;
    private static final String TAG = "PickUpListActivity";

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

        dbUtils = new DatabaseUtils(this);
        processIntentAction(getIntent());
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

        String id = intent.getStringExtra("list_id");
        dbUtils.getShoppingListByListId(id);
    }

    @Override
    public void OnListFetchListener(ShoppingList shoppingList) {
        list.addAll(shoppingList.getItems());
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

    public void takePhoto(View view) {
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
                    ImageView ivPreview = (ImageView) findViewById(R.id.ivReceipt);
                    Log.d(TAG, "Photo path: " + savedPhotoPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(savedPhotoPath, null);
                    ivPreview.setImageBitmap(bitmap);
                    Log.d(TAG, "Bitmap: " + bitmap);

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



}
