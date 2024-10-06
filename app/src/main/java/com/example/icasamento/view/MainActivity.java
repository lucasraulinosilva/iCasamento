package com.example.icasamento.view;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.icasamento.R;
import com.example.icasamento.controller.AdministradorController;
import com.example.icasamento.controller.ConvidadoController;
import com.example.icasamento.mascara.Mask;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    TextInputEditText nome;
    TextInputEditText cpf;
    ConvidadoController convidadoC = new ConvidadoController();
    AdministradorController administradorC = new AdministradorController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.nomeLogin);
        cpf = findViewById(R.id.cpfLogin);

        TextWatcher mask = Mask.mask(cpf, "###.###.###-##");
        cpf.addTextChangedListener(mask);
    }

    public void loginConvidado(View view) {
        convidadoC.autenticarLogin(String.valueOf(nome.getText()), String.valueOf(cpf.getText()), MainActivity.this);
    }

    public void loginAdministrador(View view) {
        administradorC.autenticarLogin(String.valueOf(nome.getText()), String.valueOf(cpf.getText()));
    }

    public void sair(View view) {
        finish();
    }
}