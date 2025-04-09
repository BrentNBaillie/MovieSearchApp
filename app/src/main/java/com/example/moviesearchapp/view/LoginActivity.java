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

public class LoginActivity extends AppCompatActivity
{
    private Button btnLogin;
    private Button btnRegister;
    private TextInputEditText userPassword;
    private TextInputEditText userName;
    private FirebaseFirestore firebase;
    private String username;
    private String password;
    private Intent intent;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FirebaseApp.initializeApp(this);
        firebase = FirebaseFirestore.getInstance();

        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v ->
        {
            username = userName.getText() != null ? userName.getText().toString() : "";
            password = userPassword.getText() != null ? userPassword.getText().toString() : "";

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            firebase.collection("users").whereEqualTo("userName", username).whereEqualTo("userPassword", password).get()
                    .addOnSuccessListener(querySnapshot ->
                    {
                        if (!querySnapshot.isEmpty())
                        {
                            userId = querySnapshot.getDocuments().get(0).getId();
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            intent = new Intent(LoginActivity.this, SearchActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        btnRegister.setOnClickListener(v ->
        {
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
