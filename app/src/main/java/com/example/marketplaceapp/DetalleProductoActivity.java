package com.example.marketplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetalleProductoActivity extends AppCompatActivity {

    private ImageView imgProductoDetalle;
    private TextView tvNombreProductoDetalle, tvPrecioProductoDetalle, tvDescripcionProductoDetalle;
    private Button btnComprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        imgProductoDetalle = findViewById(R.id.imgProductoDetalle);
        tvNombreProductoDetalle = findViewById(R.id.tvNombreProductoDetalle);
        tvPrecioProductoDetalle = findViewById(R.id.tvPrecioProductoDetalle);
        tvDescripcionProductoDetalle = findViewById(R.id.tvDescripcionProductoDetalle);
        btnComprar = findViewById(R.id.btnComprar);

        // Recibir datos del intent
        String nombre = getIntent().getStringExtra("nombre");
        String precio = getIntent().getStringExtra("precio");
        String descripcion = getIntent().getStringExtra("descripcion");
        String imagenUrl = getIntent().getStringExtra("imagenUrl");

        tvNombreProductoDetalle.setText(nombre);
        tvPrecioProductoDetalle.setText("₡" + precio);
        tvDescripcionProductoDetalle.setText(descripcion);

        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            Picasso.get().load(imagenUrl).into(imgProductoDetalle);
        }

        btnComprar.setOnClickListener(v -> {
            Intent intent = new Intent(this, PagoActivity.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("precio", precio);
            startActivity(intent);
        });
    }
}
