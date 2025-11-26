package com.example.diario_iriarivas;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class viewing extends AppCompatActivity {
    private ActivityResultLauncher<Intent> editting;

    private RecyclerView recyclerView;
    private entryAdapt adapter;

    private List<entry> all;
    private List<entry> shownEntries;

    private RadioGroup rgViewMode;
    private EditText etSearch;
    private Button btnAccept;
    private Button btnNew;
    private Spinner spFilter;

    //filtro spinner
    private static final int f_all = 0;
    private static final int f_fav = 1;
    private static final int f_not_fav = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewing);

        recyclerView = findViewById(R.id.rView);
        rgViewMode = findViewById(R.id.rgViewMode);
        etSearch = findViewById(R.id.etSearch);
        btnAccept = findViewById(R.id.view_btnAccept);
        spFilter = findViewById(R.id.spFilter);
        btnNew = findViewById(R.id.view_btnNew);

        //union con ventana de edicion
        editting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String title = data.getStringExtra("title");
                        String date  = data.getStringExtra("date");
                        String text  = data.getStringExtra("text");
                        int mood     = data.getIntExtra("mood", -1);
                        boolean fav  = data.getBooleanExtra("fav", false);

                        //crear y añadir la entrada
                        entry newEntry = new entry(title, date, mood, text, fav);
                        all.add(0, newEntry);

                        //mostrar lista actualizada
                        shownEntries = new ArrayList<>(all);
                        adapter.setList(shownEntries);
                        recyclerView.scrollToPosition(0);
                    }
                }
        );

        Button btnNew = findViewById(R.id.view_btnNew);
        btnNew.setOnClickListener(v -> {
            Intent i = new Intent(viewing.this, editting.class);
            editting.launch(i);
        });

        //datos de ejemplo
        all = new ArrayList<>();
        all.add(new entry("Día malo", "2024-02-01", R.drawable.mood_1, "Hoy fue un mal día", true));
        all.add(new entry("Cansado", "2024-02-02", R.drawable.mood_2, "Me siento cansado", false));
        all.add(new entry("Normal", "2024-02-03", R.drawable.mood_3, "Nada especial", false));
        all.add(new entry("Guay", "2024-03-10", R.drawable.mood_4, "Hoy estuvo bien", true));
        all.add(new entry("Fiesta", "2024-04-05", R.drawable.mood_5, "Lo pasé genial", false));

        //mostrar tabla
        shownEntries = new ArrayList<>(all);

        //adapter
        adapter = new entryAdapt(shownEntries);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // vista por defecto: lista
        recyclerView.setAdapter(adapter);

        //filto spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Todos", "Favoritos", "No favoritos"}
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(spinnerAdapter);
        spFilter.setSelection(f_all);

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFavFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }


        });

        //buscar
        btnAccept.setOnClickListener(v -> {
            String q = etSearch.getText().toString();
            applyTextFilter(q);
        });

        //cambio lista y grid
        rgViewMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbList) {
                recyclerView.setLayoutManager(new LinearLayoutManager(viewing.this));
                adapter.showListLayout();
            } else if (checkedId == R.id.rbGrid) {
                recyclerView.setLayoutManager(new GridLayoutManager(viewing.this, 3));
                adapter.showGridLayout();
            }
        });
    }

    //filtro de búsqueda
    private void applyTextFilter(String q) {
        if (q == null || q.isEmpty()) {
            shownEntries = new ArrayList<>(all);
            adapter.setList(shownEntries);
            return;
        }

        //lista filtrada
        List<entry> filtered = new ArrayList<>();
        for (entry e : all) {
            String title = e.getTitle();
            if (title != null && title.toLowerCase().contains(q)) {
                filtered.add(e);
            }
        }
        shownEntries = filtered;
        adapter.setList(shownEntries);

    }

    //filtro favoritos
    private void applyFavFilter(int mode) {
        List<entry> filtered = new ArrayList<>();
        for (entry e : all) {
            //seleccoinar según necesario
            if (mode == f_all) {
                filtered.add(e);
            } else if (mode == f_fav && e.isFavourite()) {
                filtered.add(e);
            } else if (mode == f_not_fav && !e.isFavourite()) {
                filtered.add(e);
            }
        }
        shownEntries = filtered;
        adapter.setList(shownEntries);
    }
}




