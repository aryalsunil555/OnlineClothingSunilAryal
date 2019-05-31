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

import com.example.onlinestore_4th.AddItemActivity;
import com.example.onlinestore_4th.MainActivity;
import com.example.onlinestore_4th.R;

import model.LoginSignUpResponse;
import model.User;
import onlineClothingAPI.ClothingAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private EditText etFirstName,etLastName, etUserName, etPassword,etConfirmPassword;
    private Button btnRegister;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration,container,false);

        etFirstName= view.findViewById(R.id.etFirstName);
        etLastName= view.findViewById(R.id.etLastName);
        etUserName= view.findViewById(R.id.etUserNameRegister);
        etPassword = view.findViewById(R.id.etPasswordRegister);
        etConfirmPassword = view.findViewById(R.id.etPasswordConfirm);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });



        return view;

    }

    private void register() {
        String firstName= etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        User user = new User(userName,password);


        ClothingAPI clothingAPI = Url.getInstance().create(ClothingAPI.class);
        Call<LoginSignUpResponse> loginSignUpResponseCall = clothingAPI.registerUser(userName,password);
        loginSignUpResponseCall.enqueue(new Callback<LoginSignUpResponse>() {
            @Override
            public void onResponse(Call<LoginSignUpResponse> call, Response<LoginSignUpResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Code :" + response.code(), Toast.LENGTH_SHORT).show();
                }else{
                    if (response.isSuccessful()){
                        Toast.makeText(getActivity(), "Successfully registered..", Toast.LENGTH_SHORT).show();
                        etFirstName.setText("");
                        etLastName.setText("");
                        etUserName.setText("");
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginSignUpResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error : "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}
