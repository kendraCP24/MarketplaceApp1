package com.example.marketplaceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostearProductoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNombre, etDescripcion, etPrecio;
    private Spinner spCategoria;
    private ImageView imgProducto;
    private Button btnPublicar;

    private Uri imageUri;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postear_producto);

        etNombre = findViewById(R.id.etNombreProducto);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        spCategoria = findViewById(R.id.spCategoria);
        imgProducto = findViewById(R.id.imgProducto);
        btnPublicar = findViewById(R.id.btnPublicar);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Publicando producto...");
        progressDialog.setCancelable(false);

        // 🔹 Seleccionar imagen de galería
        imgProducto.setOnClickListener(v -> openGallery());

        // 🔹 Publicar producto
        btnPublicar.setOnClickListener(v -> publicarProducto());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgProducto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void publicarProducto() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String categoria = spCategoria.getSelectedItem() != null ? spCategoria.getSelectedItem().toString() : "";

        if (nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Completa todos los campos e incluye una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // Subir imagen a Firebase Storage
        String imageId = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("productos/" + imageId + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            guardarProductoFirestore(nombre, descripcion, precio, categoria, uri.toString());
                        }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error al subir imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void guardarProductoFirestore(String nombre, String descripcion, String precio, String categoria, String imagenUrl) {
        String usuarioId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "desconocido";
        String id = UUID.randomUUID().toString();

        Map<String, Object> producto = new HashMap<>();
        producto.put("id", id);
        producto.put("nombre", nombre);
        producto.put("descripcion", descripcion);
        producto.put("categoria", categoria);
        producto.put("precio", precio);
        producto.put("imagenUrl", imagenUrl);
        producto.put("usuarioId", usuarioId);
        producto.put("fechaPublicacion", System.currentTimeMillis());

        firestore.collection("productos").document(id)
                .set(producto)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Producto publicado exitosamente ✅", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error al guardar producto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
