package com.example.diario_iriarivas;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class editting extends AppCompatActivity {
    private EditText etTitle, etDate, etText;
    private ImageButton ib1, ib2, ib3, ib4, ib5;
    private ImageButton ibMic;
    private CheckBox ckbFav;
    private Button btnCancel, btnAccept;
    private int selectedMoodRes = -1;

    //pedir permiso para grabar audio
    private ActivityResultLauncher<String> permissionLauncher;

    //recibir resultado de la petición
    private ActivityResultLauncher<Intent> speechLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editting);

        etTitle = findViewById(R.id.add_etTitle);
        etDate  = findViewById(R.id.etDate);
        etText  = findViewById(R.id.edit_etText);

        ib1 = findViewById(R.id.ib_mood1);
        ib2 = findViewById(R.id.ib_mood2);
        ib3 = findViewById(R.id.ib_mood3);
        ib4 = findViewById(R.id.ib_mood4);
        ib5 = findViewById(R.id.ib_mood5);

        ibMic = findViewById(R.id.ib_mic);

        ckbFav = findViewById(R.id.edit_ckbFavourite);
        btnCancel = findViewById(R.id.edit_btnCancel);
        btnAccept = findViewById(R.id.edit_accept);

        //inicializar micrófono
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        startVoiceRecognition();
                    } else {
                        Toast.makeText(editting.this, "Permiso denegado.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //añadir texto reconocido
        speechLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            String recognized = matches.get(0);
                            String current = etText.getText().toString();
                        }
                    }
                }
        );

        //selección de ánimo
        View.OnClickListener moodClick = v -> {
            int id = v.getId();
            if (id == R.id.ib_mood1) {
                selectedMoodRes = R.drawable.mood_1;
            }
            else if (id == R.id.ib_mood2) {
                selectedMoodRes = R.drawable.mood_2;
            }
            else if (id == R.id.ib_mood3) {
                selectedMoodRes = R.drawable.mood_3;
            }
            else if (id == R.id.ib_mood4) {
                selectedMoodRes = R.drawable.mood_4;
            }
            else if (id == R.id.ib_mood5) {
                selectedMoodRes = R.drawable.mood_5;
            }

            ib1.setAlpha(id == R.id.ib_mood1 ? 1f : 0.5f);
            ib2.setAlpha(id == R.id.ib_mood2 ? 1f : 0.5f);
            ib3.setAlpha(id == R.id.ib_mood3 ? 1f : 0.5f);
            ib4.setAlpha(id == R.id.ib_mood4 ? 1f : 0.5f);
            ib5.setAlpha(id == R.id.ib_mood5 ? 1f : 0.5f);
        };

        ib1.setOnClickListener(moodClick);
        ib2.setOnClickListener(moodClick);
        ib3.setOnClickListener(moodClick);
        ib4.setOnClickListener(moodClick);
        ib5.setOnClickListener(moodClick);

        //listener botón micrófono
        ibMic.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(editting.this, android.Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognition();
            } else {
                permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO);
            }
        });

        //cancelar entrada
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        //aceptar cambios y enviar de vuelta
        btnAccept.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String date  = etDate.getText().toString();
            String text  = etText.getText().toString();
            boolean fav  = ckbFav.isChecked();

            Intent out = new Intent();
            out.putExtra("title", title);
            out.putExtra("date", date);
            out.putExtra("text", text);
            out.putExtra("mood", selectedMoodRes);
            out.putExtra("fav", fav);

            setResult(Activity.RESULT_OK, out);
            finish();
        });
    }

    //reconicimiento de voz
    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora");
        try {
            speechLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo iniciar el reconocimiento de voz", Toast.LENGTH_SHORT).show();
        }
    }
}
