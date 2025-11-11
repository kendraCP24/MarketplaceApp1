package com.example.marketplaceapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class MiCuentaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgPerfil;
    private TextView tvNombreUsuario, tvCorreoUsuario;
    private RatingBar ratingUsuario;
    private Button btnEditarPerfil, btnCerrarSesion;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private Uri imageUri; // Para guardar la URI de la imagen seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Referencias UI
        imgPerfil = findViewById(R.id.imgPerfil);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreoUsuario = findViewById(R.id.tvCorreoUsuario);
        ratingUsuario = findViewById(R.id.ratingUsuario);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Mostrar datos del usuario
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            tvCorreoUsuario.setText(user.getEmail());
            firestore.collection("usuarios").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            tvNombreUsuario.setText(nombre);
                        }
                    });
        }

        // 🟦 Seleccionar imagen desde galería
        imgPerfil.setOnClickListener(v -> openGallery());

        // 🔹 Botón Editar Perfil (aún sin guardar)
        btnEditarPerfil.setOnClickListener(v ->
                Toast.makeText(this, "Función en desarrollo 🚧", Toast.LENGTH_SHORT).show());

        // 🔹 Cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, InicioSesionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Método para abrir la galería
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // 🔹 Mostrar imagen seleccionada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPerfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

