package com.example.marketplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnInicioSesion, btnRegistro, btnPostearProducto, btnMiCuenta;
    private FirebaseAuth auth; // Para saber si hay usuario logueado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // 🔹 Referencias a botones
        btnInicioSesion = findViewById(R.id.btnInicioSesion);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnPostearProducto = findViewById(R.id.btnPostearProducto);
        btnMiCuenta = findViewById(R.id.btnMiCuenta);

        // Botón: Iniciar Sesión
        btnInicioSesion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InicioSesionActivity.class);
            startActivity(intent);
        });

        //  Botón: Registrar (va al mismo login pero mostrando la parte de registro)
        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InicioSesionActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Desliza abajo para registrarte", Toast.LENGTH_SHORT).show();
        });

        //  Botón: Postear Producto
        btnPostearProducto.setOnClickListener(v -> {
            if (auth.getCurrentUser() != null) {
                // Si hay usuario logueado, ir a postear producto
                Intent intent = new Intent(MainActivity.this, PostearProductoActivity.class);
                startActivity(intent);
            } else {
                // Si no hay usuario, pedir inicio de sesión
                Toast.makeText(this, "Debes iniciar sesión para publicar un producto", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, InicioSesionActivity.class);
                startActivity(intent);
            }
        });

        //  Botón: Mi Cuenta
        btnMiCuenta.setOnClickListener(v -> {
            if (auth.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, MiCuentaActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Inicia sesión para ver tu cuenta", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, InicioSesionActivity.class);
                startActivity(intent);
            }
        });
    }
}
