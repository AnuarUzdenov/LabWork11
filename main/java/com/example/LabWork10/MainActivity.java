import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etArray;
    private Button btnCalculate;
    private TextView tvZeroCount, tvSumAfterMin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etArray = findViewById(R.id.etArray);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvZeroCount = findViewById(R.id.tvZeroCount);
        tvSumAfterMin = findViewById(R.id.tvSumAfterMin);
        progressBar = findViewById(R.id.progressBar);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etArray.getText().toString();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите массив", Toast.LENGTH_SHORT).show();
                    return;
                }
                new CalculateTask().execute(input);
            }
        });
    }

    private class CalculateTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {
            String input = strings[0];
            String[] parts = input.split(",");
            double[] array = new double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                try {
                    array[i] = Double.parseDouble(parts[i].trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            }

            int zeroCount = 0;
            for (double num : array) {
                if (num == 0) zeroCount++;
            }

            double min = array[0];
            int minIndex = 0;
            for (int i = 1; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                    minIndex = i;
                }
            }

            double sumAfterMin = 0;
            for (int i = minIndex + 1; i < array.length; i++) {
                sumAfterMin += array[i];
            }

            return new String[]{
                    String.valueOf(zeroCount),
                    String.valueOf(sumAfterMin)
            };
        }

        @Override
        protected void onPostExecute(String[] result) {
            progressBar.setVisibility(View.GONE);
            if (result == null) {
                Toast.makeText(MainActivity.this, "Ошибка ввода данных", Toast.LENGTH_SHORT).show();
                return;
            }
            tvZeroCount.setText("Количество нулевых элементов: " + result[0]);
            tvSumAfterMin.setText("Сумма после минимального элемента: " + result[1]);
        }
    }
}