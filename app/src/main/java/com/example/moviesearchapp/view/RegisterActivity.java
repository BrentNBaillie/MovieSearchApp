package com.example.moviesearchapp.view;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moviesearchapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity
{
    private TextInputEditText userName;
    private TextInputEditText userPassword;
    private Button btnRegister;
    private FirebaseFirestore firebase;
    private String inputUsername;
    private String inputPassword;
    private Map<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        FirebaseApp.initializeApp(this);
        firebase = FirebaseFirestore.getInstance();

        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v ->
        {
            inputUsername = userName.getText().toString().trim();
            inputPassword = userPassword.getText().toString().trim();

            if (TextUtils.isEmpty(inputUsername) || TextUtils.isEmpty(inputPassword))
            {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            firebase.collection("users").whereEqualTo("userName", inputUsername).get()
                    .addOnSuccessListener(querySnapshot ->
                    {
                        if (!querySnapshot.isEmpty())
                        {
                            Toast.makeText(this, "Username taken", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user = new HashMap<>();
                            user.put("userName", inputUsername);
                            user.put("userPassword", inputPassword);
                            user.put("movies", new ArrayList<>());

                            firebase.collection("users").add(user)
                                    .addOnSuccessListener(documentReference ->
                                    {
                                        Toast.makeText(this, "User Registered! Please log in.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                    {
                                        Toast.makeText(this, "Failed to Register: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e ->
                    {
                        Toast.makeText(this, "Error finding username: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
