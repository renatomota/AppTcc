package myideasforworld.toolbarteste;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class AddPonto extends AppCompatActivity {
    TextView local;
    TextView titulo;
    EditText addQtd;
    ToggleButton acces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ponto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addQtd = (EditText)findViewById(R.id.edtQtd);
        acces = (ToggleButton)findViewById(R.id.toggleButton);

        local = (TextView)findViewById(R.id.addLocal);
        titulo = (TextView)findViewById(R.id.addTitulo);


        Intent intent = getIntent();
        local.setText(intent.getStringExtra("local"));
        titulo.setText(intent.getStringExtra("titulo"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();

                try {

                    ParseObject query = new ParseObject("Destinos");
                    query.put("criadoPor", ParseUser.getCurrentUser());
                    query.put("nome", titulo.getText().toString());
                    query.saveInBackground();
                }catch (Exception e){

                }
            }
        });
    }

}
