package com.illinodes.jouchats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    private MaterialEditText mUsername, mEmail, mPassword, mConfirmPassword;
    private Button btn_Register;
    private final int PASSWORD_LENGTH = 6;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Title for Toolbar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Assign Variables to XML variables
        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        btn_Register = findViewById(R.id.btn_register);
        mConfirmPassword = findViewById(R.id.confirm_password);

        auth = FirebaseAuth.getInstance();

        btn_Register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String txt_username = mUsername.getText().toString();
                String txt_email = mEmail.getText().toString();
                String txt_password = mPassword.getText().toString();
                String txt_confirm_password = mConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) ||
                        TextUtils.isEmpty(txt_password ) || TextUtils.isEmpty(txt_confirm_password))
                {
                    Toast.makeText(RegisterActivity.this,
                            "All fields are required",
                            Toast.LENGTH_SHORT).show();
                } else
                    if (!txt_password.matches(txt_confirm_password))
                    {
                        Toast.makeText(RegisterActivity.this, "Passwords don't match",
                                Toast.LENGTH_SHORT).show();
                    } else
                        if (txt_password.length() < PASSWORD_LENGTH)
                        {
                            Toast.makeText(RegisterActivity.this,
                                    "Password must be at least 6 characters",
                                    Toast.LENGTH_SHORT).show();
                        } else
                            {
                                register(txt_username, txt_email, txt_password);
                            }
            }
        });
    }

    //Add User to Firebase Database
    private void register(final String username, String email, String password)
    {
        //Create User
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance()
                                    .getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "You can't register with this email or password",
                                        Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}
