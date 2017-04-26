package myideasforworld.toolbarteste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.PrivateKey;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import myideasforworld.toolbarteste.banco.pesquisaParse;


public class Cadastro extends AppCompatActivity {

    EditText email;
    EditText senha;
    EditText reSenha;
    Spinner estados;
    Spinner cidades;
    Button cadastrar;
    pesquisaParse whereParse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);



        whereParse = new pesquisaParse();


        email=(EditText)findViewById(R.id.cadEmail);
        senha = (EditText)findViewById(R.id.cadSenha);
        reSenha =(EditText)findViewById(R.id.cadReSenha);
        estados= (Spinner)findViewById(R.id.cadEstado);
        cidades = (Spinner)findViewById(R.id.cadCidade);
        cadastrar = (Button)findViewById(R.id.cadBtn);

        //preenche o spinner de estados
        confSpinner();

        //preeche o spinner cidades de acordo com o estado selecionado
        estados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                confSpinnerCidades(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        reSenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


        //listener do botão de cadastrar
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();

            }
        });
    }



    private void singUp() {

        email.setError(null);
        senha.setError(null);
        reSenha.setError(null);
        boolean cancel = false;
        View focusView = null;

        String vemail = email.getText().toString();
        String vsenha = senha.getText().toString();
        String vresenha = reSenha.getText().toString();

        if (TextUtils.isEmpty(vemail)) {         // Verifica email
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(vemail)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }

        // verifica a senha, se o usuário digitou uma
        if (vsenha.trim().equals("") || !isPasswordValid(vsenha)) {
            senha.setError(getString(R.string.error_invalid_password));
            focusView = senha;
            cancel = true;
        }else if (!TextUtils.equals(vsenha,vresenha)){
            reSenha.setError(getString(R.string.senhas_iguais));
            focusView = reSenha;
            cancel = true;
        }




        if (cancel) {
            // Se o cancel for true o programa dá "focus" no textview que precisa ser editado
            focusView.requestFocus();

        } else  {
            ParseUser user = new ParseUser();
            user.setUsername(vemail);
            user.setPassword(vsenha);
            user.setEmail(vemail);
            user.put("estado", estados.getSelectedItem().toString());
            user.put("municipio", cidades.getSelectedItem().toString());
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {

                        ParseUser.getCurrentUser();
                        final String title = "Conta criada com sucesso";
                        final String message = "Verificar email antes de entrar";
                        alertDisplayer(title, message);
                        finish();
                    } else {

                        final String title = "Erro ao criar conta !";
                        final String message = "A conta não pode ser criada: ";
                        alertDisplayer(title, message + " :" + e.getMessage());
                    }
                }

            });
        }

    }




    private boolean isEmailValid(String email) {
        return email.contains("@");

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }



    private void confSpinner() {
        //estados = (Spinner) findViewById(R.id.cadEstado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.estados, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estados.setAdapter(adapter);

    }

    private void confSpinnerCidades(String uf) {
        ArrayAdapter<CharSequence> adapter;
        switch (uf){
            case "AC":
                adapter= ArrayAdapter.createFromResource(this,
                R.array.AC, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;
            case "AL":
                adapter = ArrayAdapter.createFromResource(this,
                R.array.AL, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "AP":
                adapter = ArrayAdapter.createFromResource(this,
                R.array.AP, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "AM":
                adapter = ArrayAdapter.createFromResource(this,
                R.array.AM, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;
            case "BA":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.BA, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;
            case "CE":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.CE, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;
            case "DF":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.DF, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;
            case "ES":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.ES, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;
            case "GO":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.GO, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "MA":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.MA, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "MT":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.MT, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "MS":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.MS, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "MG":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.MG, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "PA":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.PA, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "PB":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.PB, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "PR":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.PR, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "PE":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.PE, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "PI":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.PI, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "RJ":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.RJ, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "RN":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.RN, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "RS":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.RS, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "RO":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.RO, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "RR":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.RR, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "SC":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.SC, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "SP":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.SP, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "SE":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.SE, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;

            case "TO":
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.TO, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cidades.setAdapter(adapter);
                break;
        }




    }

            void alertDisplayer(String title, String message) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Cadastro.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog ok = builder.create();
                ok.show();
            }

}