package hr.etfos.mgrgic.bmxridesleep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class sign_up_screen2 extends AppCompatActivity {

    public String spinnerValue1;
    public String spinnerValue2;
    Spinner spinner;
    Spinner spinner2;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen2);
        Intent intent = getIntent();

        spinner = findViewById(R.id.spinner1);
        adapter = ArrayAdapter.createFromResource(this, R.array.rider, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i)+" selected", Toast.LENGTH_LONG).show();
                spinnerValue1 = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner2 = findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.riding, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i)+"", Toast.LENGTH_LONG).show();
                spinnerValue2 = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button button = findViewById(R.id.nextButton2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(sign_up_screen2.this, sign_up_screen3.class);
                startActivity(intent);*/

                Intent intent = new Intent(sign_up_screen2.this, profileActivity.class);
                intent.putExtra("spinnerValue1", spinnerValue1);
                Log.d("spinnerValue1", spinnerValue1);
                intent.putExtra("spinnerValue2", spinnerValue2);
                startActivity(intent);
                finish();
            }
        });

    }
}
