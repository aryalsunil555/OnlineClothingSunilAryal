package com.example.onlinestore_4th;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import model.ImageResponse;
import model.Item;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import onlineClothingAPI.ClothingAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import url.Url;

public class AddItemActivity extends AppCompatActivity {

    private EditText etName,etPrice,etDescription;
    private Button btnAddItem,btnBackItemAdd;
    private ImageView imgItem;
    String imagePath;
    String imageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        imgItem = findViewById(R.id.imgItem);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnBackItemAdd= findViewById(R.id.btnBackAddItem);
        btnBackItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }



    private void loadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (data ==null){
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }

        Uri uri = data.getData();
        imagePath = getRealPathFromUri(uri);
        previewImage(imagePath);
    }

    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private void previewImage(String imagePath){
        File imgFile = new File(imagePath);
        if (imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgItem.setImageBitmap(bitmap);
        }
    }


    private void strictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void saveImageOnly(){

        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);

        ClothingAPI clothingAPI = Url.getInstance().create(ClothingAPI.class);
        Call<ImageResponse> imageResponseCall = clothingAPI.uploadImage(Url.Cookie, body);

        strictMode();

        try {
            Response<ImageResponse> imageResponseResponse = imageResponseCall.execute();
//            after saving the current image, retrieve the current image
            imageName = imageResponseResponse.body().getFilename();
        }catch (Exception e){

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void save() {

        saveImageOnly();
        String itemName = etName.getText().toString().trim();
        String itemDescription = etDescription.getText().toString().trim();
        float itemPrice = Float.parseFloat(etPrice.getText().toString());

        Item item= new Item(itemName,imageName,itemDescription);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClothingAPI clothingAPI = retrofit.create(ClothingAPI.class);
        Call<Void> voidCall = clothingAPI.addItems(Url.Cookie, itemName,imageName,itemDescription);
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(AddItemActivity.this, "Code :" + response.code(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddItemActivity.this, "Successfully added..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddItemActivity.this, "Error :" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


//    check main dashboard for all the files

}
