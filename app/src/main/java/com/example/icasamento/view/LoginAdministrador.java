package com.example.icasamento.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.icasamento.R;
import com.example.icasamento.controller.AdministradorController;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class LoginAdministrador extends AppCompatActivity {
    Button clickBtn;
    TextView nome;
    TextView cpf;
    ListView lista;
    AdministradorController administradorC;
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission()
            , isGranted -> {
                if(isGranted){
                    administradorC.mostrarCamera();
                } else {

                }
            });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null){
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
        } else {
            administradorC.setResult(result.getContents());
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_administrador);
        clickBtn = findViewById(R.id.menuAdmin);
        nome = findViewById(R.id.nomeAdmin);
        cpf = findViewById(R.id.cpfAdmin);
        DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout2);
        lista = findViewById(R.id.convidados);

        administradorC = new AdministradorController(this, requestPermissionLauncher, qrCodeLauncher, drawerLayout, nome, cpf, lista);

        administradorC.mostrarConvidados();
    }

    public void menuAdministrador(View view) {
        administradorC.menuAdministrador(clickBtn);
    }

    public void checarPermissaoELigarCamera(MenuItem item) {
        Context context = this;
        administradorC.checarPermissaoELigarCamera();
    }

    public void manutencao(MenuItem item) {
        Intent intent = new Intent(this, Manutencao.class);
        startActivity(intent);
    }
}