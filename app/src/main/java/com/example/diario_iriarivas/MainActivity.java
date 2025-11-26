package com.example.diario_iriarivas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image;
    private TextView info;
    private EditText user, password;
    private Button accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        image = findViewById(R.id.iv_login);
        info = findViewById(R.id.tv_info);
        user = findViewById(R.id.et_user);
        password = findViewById(R.id.et_password);
        accept = findViewById(R.id.btn_login);

        accept.setOnClickListener(this);
    }

    //inicio de sesion con estos datos y enviar a siguiente ventana
    @Override
    public void onClick(View v) {
        if (!"user".equals(user.getText().toString()) || !"pass".equals(password.getText().toString())) {
            info.setText(R.string.wrong_login);
        } else {
            Intent intent = new Intent(MainActivity.this, viewing.class);
            startActivity(intent);
        }
    }
}







