package com.example.icasamento.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.icasamento.R;
import com.example.icasamento.controller.ConvidadoController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class LoginConvidado extends AppCompatActivity {
    Button clickBtn;
    ImageView qrCode;
    TextView nome;
    TextView cpf;
    String nomeLogin;
    String cpfLogin;
    DrawerLayout drawerLayout;
    ConvidadoController convidadoC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_convidado);
        qrCode = findViewById(R.id.qrCode);
        nome = findViewById(R.id.nomePosLogin);
        cpf = findViewById(R.id.cpfPosLogin);
        nomeLogin = getIntent().getStringExtra("nome");
        cpfLogin = getIntent().getStringExtra("cpf");
        nome.setText(nomeLogin);
        cpf.setText(cpfLogin);
        clickBtn = findViewById(R.id.menuConvidado);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        convidadoC = new ConvidadoController(drawerLayout, clickBtn, cpfLogin, nomeLogin, qrCode);
    }

    public void menuConvidado(View view) {
        convidadoC.menuConvidado();
    }

    public void gerarQrCode(MenuItem item) {
        convidadoC.gerarQrCode();
    }
}