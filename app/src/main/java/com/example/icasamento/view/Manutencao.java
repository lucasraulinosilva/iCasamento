package com.example.icasamento.view;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.icasamento.R;
import com.example.icasamento.controller.AdministradorController;
import com.example.icasamento.mascara.Mask;
import com.google.android.material.textfield.TextInputEditText;

public class Manutencao extends AppCompatActivity {
    AdministradorController administradorC;
    TextInputEditText id;
    TextInputEditText nome;
    TextInputEditText cpf;
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manutencao);

        id = findViewById(R.id.idManutencao);
        nome = findViewById(R.id.nomeManutencao);
        cpf = findViewById(R.id.cpfManutencao);
        lista = findViewById(R.id.listaConvidadosManutencao);

        TextWatcher mask = Mask.mask(cpf, "###.###.###-##");
        cpf.addTextChangedListener(mask);

        administradorC = new AdministradorController(this, id, cpf, nome, lista);
        administradorC.mostrarConvidados();
    }

    public void voltar(View view) {
        finish();
    }

    public void inserirConvidado(View view) {
        administradorC.inserirConvidado();
    }

    public void deletarConvidado(View view) {
        administradorC.deletarConvidado();
    }

    public void atualizarConvidado(View view) {
        administradorC.atualizarUsuario();
    }
}