package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppersListArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PickUpList extends AppCompatActivity implements DatabaseUtils.OnListFetchListener, DatabaseUtils.OnActiveListsFetchListener{

    private DatabaseUtils dbUtils;
    private ShoppersListArrayAdapter shoppersListArrayAdapter;
    private ArrayList<ShoppableItem> list;
    private RecyclerView rvItems;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";
    private String GALLERY_LOCATION = "image gallery";
    private File mGalleryFolder;
    private static LruCache<String,Bitmap> mMemoryCache;

    public final String APP_TAG = "SocialShopper";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";


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

        String id = intent.getStringExtra("list_id");

        dbUtils.getShoppingListByListId(id);

//        if (intent.getAction() != null) {
//            switch (intent.getAction()) {
//                case "YES_ACTION":
//                    Toast.makeText(this, "Yes :)", Toast.LENGTH_SHORT).show();
//                    break;
//                case "NO_ACTION":
//                    Toast.makeText(this, "No :(", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivReceipt);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            return FileProvider.getUriForFile(PickUpList.this, "com.codepath.socialshopper.socialshopper.fileprovider", file);
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

}
