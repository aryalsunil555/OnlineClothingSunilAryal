package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinestore_4th.DashboardActivity;
import com.example.onlinestore_4th.MainActivity;
import com.example.onlinestore_4th.R;

import model.LoginSignUpResponse;
import onlineClothingAPI.ClothingAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText etUserName, etPassword;
    private Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        etUserName = view.findViewById(R.id.etUserNameLogin);
        etPassword = view.findViewById(R.id.etPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

        return view;
    }

    private void checkUser() {

        String email = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        ClothingAPI clothingAPI = Url.getInstance().create(ClothingAPI.class);
        Call<LoginSignUpResponse> loginSignUpResponseCall = clothingAPI.checkUser(email,password);
        loginSignUpResponseCall.enqueue(new Callback<LoginSignUpResponse>() {
            @Override
            public void onResponse(Call<LoginSignUpResponse> call, Response<LoginSignUpResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Either username or password is not correct" , Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (response.isSuccessful()){
                        Url.Cookie = response.headers().get("Set-Cookie");
                        Intent intent = new Intent(getActivity(), DashboardActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginSignUpResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error :" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
