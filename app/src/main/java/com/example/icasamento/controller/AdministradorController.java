package com.example.icasamento.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.icasamento.R;
import com.example.icasamento.model.Convidado;
import com.example.icasamento.view.LoginAdministrador;
import com.example.icasamento.view.LoginConvidado;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdministradorController extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://testando-firebase-android-default-rtdb.firebaseio.com/").getReference();
    private Activity activity;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<ScanOptions> qrCodeLauncher;
    DrawerLayout drawerLayout;
    TextView nomeText;
    TextView cpfText;
    TextView idText;
    ListView lista;

    public AdministradorController(Activity activity) {
        this.activity = activity;
    }

    public AdministradorController(Activity activity, ActivityResultLauncher<String> requestPermissionLauncher, ActivityResultLauncher<ScanOptions> qrCodeLauncher, DrawerLayout drawerLayout, TextView nomeText, TextView cpfText, ListView lista) {
        this.activity = activity;
        this.requestPermissionLauncher = requestPermissionLauncher;
        this.qrCodeLauncher = qrCodeLauncher;
        this.drawerLayout = drawerLayout;
        this.nomeText = nomeText;
        this.cpfText = cpfText;
        this.lista = lista;
    }

    public AdministradorController(Activity activity, TextView idText, TextView cpfText, TextView nomeText, ListView lista) {
        this.activity = activity;
        this.idText = idText;
        this.cpfText = cpfText;
        this.nomeText = nomeText;
        this.lista = lista;
    }

    public ActivityResultLauncher<ScanOptions> getQrCodeLauncher() {
        return qrCodeLauncher;
    }

    public void setQrCodeLauncher(ActivityResultLauncher<ScanOptions> qrCodeLauncher) {
        this.qrCodeLauncher = qrCodeLauncher;
    }

    public ActivityResultLauncher<String> getRequestPermissionLauncher() {
        return requestPermissionLauncher;
    }

    public void setRequestPermissionLauncher(ActivityResultLauncher<String> requestPermissionLauncher) {
        this.requestPermissionLauncher = requestPermissionLauncher;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public TextView getNomeText() {
        return nomeText;
    }

    public void setNomeText(TextView nome) {
        this.nomeText = nome;
    }

    public TextView getCpfText() {
        return cpfText;
    }

    public void setCpfText(TextView cpf) {
        this.cpfText = cpf;
    }

    public ListView getLista() {
        return lista;
    }

    public void setLista(ListView lista) {
        this.lista = lista;
    }

    public TextView getIdText() {
        return idText;
    }

    public void setIdText(TextView idText) {
        this.idText = idText;
    }

    public void autenticarLogin(String nome, String cpf) {
        mDatabase.child("Administradores").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Convidado[] convidados = gson.fromJson(json, Convidado[].class);
                    boolean logar = false;
                    String nomeConvidado = "";
                    String cpfConvidado = "";

                    for(int i = 0; i < convidados.length; i++) {
                        if(convidados[i] != null) {
                            if(convidados[i].getNome().equals(nome) && convidados[i].getCpf().equals(cpf)) {
                                logar = true;
                                nomeConvidado = convidados[i].getNome();
                                cpfConvidado = convidados[i].getCpf();
                                break;
                            }  else {
                                logar = false;
                            }
                        }
                    }

                    if(logar) {
                        Intent intent = new Intent(activity, LoginAdministrador.class);
                        /*intent.putExtra("nome", nomeConvidado);
                        intent.putExtra("cpf", cpfConvidado);*/
                        activity.startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(activity, "Nome ou CPF errado", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    public void menuAdministrador(Button clickBtn) {
        if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
            clickBtn.setGravity(Gravity.CENTER);
        }
        else {
            drawerLayout.closeDrawer(Gravity.LEFT);
            clickBtn.setGravity(Gravity.START);
        }
    }

    public void checarPermissaoELigarCamera() {
        if(ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            mostrarCamera();
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Permissão da câmera necessária", Toast.LENGTH_LONG).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    public void mostrarCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Escaneie o QR Code");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }

    public void setResult(String contents) {
        String[] resultado = contents.split(",");
        nomeText.setText(resultado[0]);
        cpfText.setText(resultado[1]);
        drawerLayout.close();
    }

    public void mostrarConvidados() {
        mDatabase.child("Convidados").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Convidado[] convidados = gson.fromJson(json, Convidado[].class);
                    ArrayList<String> nomes = new ArrayList<>();

                    for(int i = 0; i < convidados.length; i++) {
                        if(convidados[i] != null) {
                            nomes.add("Id: " + convidados[i].getId() + "Nome: " + convidados[i].getNome() + " CPF: " + convidados[i].getCpf());
                        }
                    }

                    listView(nomes);
                }
            }
        });
    }

    public void listView(ArrayList<String> nomes) {
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>( activity, android.R.layout.simple_list_item_1, nomes );
        lista.setAdapter(adaptador);
    }

    public void inserirConvidado() {
        pegarIdMaximo(String.valueOf(nomeText.getText()), String.valueOf(cpfText.getText()));
    }

    public void pegarIdMaximo(String nome, String cpf) {
        mDatabase.child("Convidados").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Convidado[] convidados = gson.fromJson(json, Convidado[].class);

                    novoConvidado(String.valueOf((convidados[convidados.length - 1].getId()) + 1), (convidados[convidados.length - 1].getId()) + 1, nome, cpf);

                    mostrarConvidados();
                }
            }
        });
    }
    public void novoConvidado(String userId, int id, String nome, String cpf) {
        Convidado convidado = new Convidado(id, nome, cpf);

        mDatabase.child("Convidados").child(userId).setValue(convidado);
    }

    public void deletarConvidado() {
        mDatabase.child("Convidados/" + Integer.parseInt(String.valueOf(idText.getText()))).removeValue();
        mostrarConvidados();
    }

    public void atualizarUsuario() {
        int idAtualizar = Integer.parseInt(String.valueOf(idText.getText()));
        String nomeAtualizar = String.valueOf(nomeText.getText());
        String cpfAtualizar = String.valueOf(cpfText.getText());
        mDatabase.child("Convidados").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Convidado[] convidados = gson.fromJson(json, Convidado[].class);

                    for (int i = 0; i < convidados.length; i++) {
                        if(convidados[i] != null) {
                            if(convidados[i].getId() == idAtualizar) {
                                atualizarConvidado(convidados[i].getId(),  nomeAtualizar, cpfAtualizar);
                            }
                        }
                    }
                }
            }
        });
    }

    private void atualizarConvidado(int userId, String nome, String cpf) {
        String key = mDatabase.child("Convidados").push().getKey();
        Convidado convidado = new Convidado(userId, nome, cpf);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Convidados/" + userId, convidado);

        mDatabase.updateChildren(childUpdates);
        mostrarConvidados();
    }

}
