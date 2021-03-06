package com.example.isteappbackend;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.quickstart.fcm.R;
//import com.google.firebase.quickstart.fcm.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText email_textView,pwd_textView;
    static View view;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void subscribe() {
        FirebaseMessaging.getInstance().subscribeToTopic("notify")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {

//                        String msg = getString(R.string.msg_subscribed);
//                        if (!task.isSuccessful()) {
//                            msg = getString(R.string.msg_subscribe_failed);
//                        }
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_login, container, false);
        Button login_button;
        email_textView=view.findViewById(R.id.login_email_ip);
        pwd_textView=view.findViewById(R.id.login_pwd_ip);
        login_button=view.findViewById(R.id.loginButton);
        mAuth=FirebaseAuth.getInstance();
        MainActivity2.onLogin=true;
        Button signup=view.findViewById(R.id.signUpButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFrag);
            }
        });
        Button forgotPassword=view.findViewById(R.id.forgotPasswordButton);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ForgotPwdAct.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(this);

        return view;
    }
    public static void toHome(){
        Log.i("mine","Auth success");
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_navigation_home);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity2.onLogin=false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.loginButton):
                String email=email_textView.getText().toString().trim();
                String password=pwd_textView.getText().toString().trim();
                if(!email.equals("") && !password.equals("")) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("mine", "signInWithEmail:success");
                                        subscribe();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("mine", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }
        }
    }
}