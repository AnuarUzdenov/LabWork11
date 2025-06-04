import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView imgView;
    private ProgressBar progressBar;
    private TextView resultTextView;
    private double[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.imgView);
        progressBar = findViewById(R.id.progressBar);
        resultTextView = findViewById(R.id.resultTextView);

        generateArray();
    }

    private void generateArray() {
        Random random = new Random();
        array = new double[20];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextDouble() * 100 - 50;
        }
    }

    public void btnDownloadClick(View view) {
        DownloadTask task = new DownloadTask();
        task.execute("http://www.ncfu.ru/templates/current/images/logotype.png");
    }

    public void btnCalculateClick(View view) {
        CalculateTask task = new CalculateTask();
        task.execute();
    }

    private Bitmap downloadImage(String url) {
        try {
            return BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
        }
    }

    private class CalculateTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            double sumNegative = 0;
            double productBetween = 1;
            
            int minIndex = 0;
            int maxIndex = 0;
            
            for (int i = 0; i < array.length; i++) {
                if (array[i] < 0) {
                    sumNegative += array[i];
                }
                
                if (array[i] < array[minIndex]) {
                    minIndex = i;
                }
                
                if (array[i] > array[maxIndex]) {
                    maxIndex = i;
                }
            }
            
            int start = Math.min(minIndex, maxIndex) + 1;
            int end = Math.max(minIndex, maxIndex);
            
            for (int i = start; i < end; i++) {
                productBetween *= array[i];
            }
            
            return "Сумма отрицательных: " + sumNegative + 
                   "\nПроизведение между min и max: " + productBetween;
        }

        @Override
        protected void onPostExecute(String result) {
            resultTextView.setText(result);
            progressBar.setVisibility(View.GONE);
        }
    }
}
