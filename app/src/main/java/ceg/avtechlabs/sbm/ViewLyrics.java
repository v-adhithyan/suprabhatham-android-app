package ceg.avtechlabs.sbm;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.InputStream;


public class ViewLyrics extends ActionBarActivity {
    TextView tv;
    InputStream inputStream;
    String entireLyrics = "";
    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        Intent intent =  getIntent();
        String fname = intent.getStringExtra(Lyrics.FILE_NAME);
        tv = (TextView)findViewById(R.id.textViewLyrics);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarColor)));
        assetManager = getAssets();
        if(fname.equals("english.txt"))
            setTitle("English Lyrics");
        else if(fname.equals("tamil.txt"))
            setTitle("சுப்ரபாதம் தமிழ் வரிகள்");
        else if(fname.equals("kannada.txt"))
            setTitle("ಕನ್ನಡ ಭಾವಗಿತೆಗಳ ಸುಪ್ರಭಾತಮ್");
        else if(fname.equals("telugu.txt"))
            setTitle("సుప్రభాతం తెలుగు పాట సాహిత్యం");
        try
        {
            inputStream = assetManager.open(fname);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            entireLyrics = new String(buffer);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //Toast.makeText(this, entireLyrics, Toast.LENGTH_LONG).show();
        tv.setText(entireLyrics);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_view_lyrics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
