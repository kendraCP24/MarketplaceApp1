package com.example.marketplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InicioSesionActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin, etNombreRegistro;
    private Button btnIniciarSesion, btnRegistrarse;
    private TextView tvIrARegistro;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        // 🔹 Inicializar Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // 🔹 Si ya hay sesión iniciada → ir a MainActivity
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(InicioSesionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // 🔹 Referencias UI
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        etNombreRegistro = findViewById(R.id.etNombreRegistro);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        tvIrARegistro = findViewById(R.id.tvIrARegistro);

        // 🔹 Mostrar campos de registro
        tvIrARegistro.setOnClickListener(v -> {
            etNombreRegistro.setVisibility(View.VISIBLE);
            btnRegistrarse.setVisibility(View.VISIBLE);
        });

        // 🔹 Iniciar sesión
        btnIniciarSesion.setOnClickListener(v -> {
            String email = etEmailLogin.getText().toString().trim();
            String password = etPasswordLogin.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Sesión iniciada correctamente ✅", Toast.LENGTH_SHORT).show();

                            // 🔹 Redirigir al menú principal
                            Intent intent = new Intent(InicioSesionActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 🔹 Registrar usuario
        btnRegistrarse.setOnClickListener(v -> {
            String nombre = etNombreRegistro.getText().toString().trim();
            String email = etEmailLogin.getText().toString().trim();
            String password = etPasswordLogin.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            Map<String, Object> usuario = new HashMap<>();
                            usuario.put("nombre", nombre);
                            usuario.put("email", email);

                            firestore.collection("usuarios").document(uid).set(usuario)
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(this, "Registro exitoso ✅", Toast.LENGTH_SHORT).show()
                                    )
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error al guardar en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                    );
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
