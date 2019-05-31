package com.example.onlinestore_4th;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import adapter.ItemAdapter;
import model.Item;
import onlineClothingAPI.ClothingAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDashboard;
    private FloatingActionButton floatingActionButton;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        floatingActionButton = findViewById(R.id.fabAddItem);

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AddItemActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerViewDashboard = findViewById(R.id.recyclerViewDashboard);
        ClothingAPI clothingAPI = Url.getInstance().create(ClothingAPI.class);

        Call<List<Item>> listCall = clothingAPI.getAllItems(Url.Cookie);
        listCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(DashboardActivity.this, "Code : "+ response.code() , Toast.LENGTH_SHORT).show();
                }
                List<Item> items =  response.body();
                ItemAdapter itemAdapter = new ItemAdapter(items,DashboardActivity.this);
                recyclerViewDashboard.setAdapter(itemAdapter);
                recyclerViewDashboard.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

                Toast.makeText(DashboardActivity.this, "Error : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void logout() {

        ClothingAPI clothingAPI = Url.getInstance().create(ClothingAPI.class);
        Call<Void> voidCall = clothingAPI.logout(Url.Cookie);
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(DashboardActivity.this, "Code : "+ response.code() , Toast.LENGTH_SHORT).show();
                }else {
                    if (response.isSuccessful()){
                        Toast.makeText(DashboardActivity.this, "Logged out ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashboardActivity.this,ViewPagerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
