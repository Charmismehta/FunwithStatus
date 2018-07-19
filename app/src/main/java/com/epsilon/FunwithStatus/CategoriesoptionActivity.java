package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.CategoryAdapter;
import com.epsilon.FunwithStatus.adapter.RecycleImageAdapter;
import com.epsilon.FunwithStatus.fragment.HomeFragment;
import com.epsilon.FunwithStatus.fragment.VideoFragment;
import com.epsilon.FunwithStatus.jsonpojo.addimage.AddImage;
import com.epsilon.FunwithStatus.jsonpojo.addstatus.AddStatus;
import com.epsilon.FunwithStatus.jsonpojo.categories.Categories;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.epsilon.FunwithStatus.utills.Utils;
import com.epsilon.FunwithStatus.utills.UtilsConstant;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesoptionActivity extends BaseActivity implements SingleUploadBroadcastReceiver.Delegate {
    Activity activity;
    TextView title;
    ImageView ileft, iright;
    TextView hashtag;
    GridView recycler_view;
    SwipeRefreshLayout swipelayout;
    APIInterface apiInterface;
    Uri uri;
    String category_id, type,file,text,token,imagefile;
    RequestBody requestFile;
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoriesoption);
        activity = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);

        type = getIntent().getStringExtra("TYPE");
        title = (TextView) findViewById(R.id.title);
        ileft = (ImageView) findViewById(R.id.ileft);
        iright = (ImageView) findViewById(R.id.iright);
        hashtag = (TextView) findViewById(R.id.hashtag);

        title.setText("Categories");
        ileft.setVisibility(View.GONE);
        iright.setImageResource(R.drawable.vc_done);

        Categories();
        recycler_view = (GridView) findViewById(R.id.recycler_view);
        swipelayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Categories();
                swipelayout.setRefreshing(false);
            }
        });
        token = Utils.getPref(getActivity(), UtilsConstant.TOKEN, "");
        Log.e("Token-->>", token);

        iright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("IMAGE")) {
                    file = getIntent().getStringExtra("imageUri");
                    uploadMultipart();
//                    File file = new File(myUri.getPath());
//                    CirclePostAdd(category_id,name,file,token);
                }
                else if(type.equalsIgnoreCase("VIDEO"))
                {
                    file = getIntent().getStringExtra("selectpath");
                    uploadvideo(v);
                }
                else
                {
                    text = getIntent().getStringExtra("STATUS");
                    int cat = Integer.valueOf(category_id);
                    addstatus(cat,hashtag.getText().toString(),text,token);

                }
            }
        });

        recycler_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = Constants.categoriesData.get(position).getCategoryName();
                category_id = String.valueOf(Constants.categoriesData.get(position).getId());
                hashtag.setText("#"+category);
            }
        });
    }

    // TODO : ADD STATUS

    public void addstatus(int category_id,String name, final String text,String token) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        Call<AddStatus> logincall = apiInterface.addstatuspojo(category_id,name, text,token);
        logincall.enqueue(new Callback<AddStatus>() {
            @Override
            public void onResponse(Call<AddStatus> call, Response<AddStatus> response) {
                dialog.dismiss();
                if (response.body().status == 1) {
                    Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddStatus> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(activity,"No Internet Connection");
            }
        });
    }

    // TODO : END

    // TODO : SHOW CATEGORIES

    public void Categories() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        Call<Categories> logincall = apiInterface.categoriespojo();
        logincall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                dialog.dismiss();
                if (response.body().getStatus() == 1) {
                    if (Constants.categoriesData != null) {
                        Constants.categoriesData.clear();
                    }
                    if (!Constants.categoriesData.equals("") && Constants.categoriesData != null) {
                        Constants.categoriesData.addAll(response.body().getData());
                        CategoryAdapter adapter = new CategoryAdapter(getActivity());
                        recycler_view.setAdapter(adapter);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(activity, "No Internet Connection");
            }
        });
    }

    // TODO : END


    // TODO : UPLOAD IMAGE

    public void CirclePostAdd(String name, final String category_id, File file,String token) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        RequestBody loginid = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody circleid = RequestBody.create(MediaType.parse("text/plain"), category_id);
        requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part userProfile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Call<AddImage> circlePostAddPCall = apiInterface.circlepostaddpojocall(loginid, circleid, token,userProfile);
            circlePostAddPCall.enqueue(new Callback<AddImage>() {
                @Override
                public void onResponse(Call<AddImage> uploadImageCall, Response<AddImage> response)
                {
                    if (response.body().status == 1) {
                        Toast.makeText(activity, response.body().msg, Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(activity, response.body().msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AddImage> call, Throwable t) {
                    dialog.dismiss();
                    Helper.showToastMessage(getApplication(),t.getMessage());
                }
            });
        }

// TODO : END


    public void onBackPressed() {
        Intent it = new Intent(getActivity(), Dashboard.class);
        startActivity(it);
        finish();
    }



// TODO : ADD VIDEO
    public void uploadvideo(View view) {
        Helper.hideSoftKeyboard(getActivity());
        //getting name for the image
        String name = hashtag.getText().toString().trim();

        //getting the actual path of the image
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_VIDEO)
                    .addFileToUpload(file, "file") //Adding file
                    .addParameter("category_id", category_id) //Adding text parameter to the request//Adding text parameter to the request
                    .addParameter("name", name) //Adding text parameter to the request
                    .addHeader("Authorization", token)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            showToast(view);
        } catch (Exception exc) {

        }
    }

    // TODO : END


    public void uploadMultipart() {

       String name = hashtag.getText().toString().trim();
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(file, "file") //Adding file
                    .addParameter("category_id", category_id) //Adding text parameter to the request
                    .addParameter("name", name) //Adding text parameter to the request
                    .addHeader("Authorization", token) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            Toast.makeText(getActivity(), "Add Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception exc) {
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProviderif (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{split[1]};

            return getDataColumn(context, contentUri, selection,
                    selectionArgs);
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    public void onProgress(int progress) {
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
    }

    @Override
    public void onError(Exception exception) {
        Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        Intent it = new Intent(getActivity(), Dashboard.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onCancelled() {

    }

    public void showToast(View view) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        // Set the toast and duration
        int toastDurationInMilliSeconds = 20000;
        dialog.show();
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                dialog.show();

            }

            public void onFinish() {
                dialog.dismiss();
//                custom_layout.setVisibility(View.GONE);

//                mToastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        dialog.show();
        toastCountDown.start();
    }

}
