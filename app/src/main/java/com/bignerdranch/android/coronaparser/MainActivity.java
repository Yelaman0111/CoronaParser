package com.bignerdranch.android.coronaparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;



public class MainActivity extends AppCompatActivity {
    private Document doc;
    private Thread secThread;
    private Runnable runnable;

    TextView infected_count;
    TextView cured_count;
    TextView deaths_count;
    TextView infected_today;
    TextView cured_today;
    TextView day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        init();

    }

    private void init()
    {
        infected_count = findViewById( R.id.tv_infected_count );
        cured_count = findViewById( R.id.tv_cured_count );
        deaths_count = findViewById( R.id.tv_deaths_count );
        infected_today = findViewById( R.id.tv_infected_today );
        cured_today = findViewById( R.id.tv_cured_today );
        day = findViewById( R.id.tv_day );

        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread( runnable );
        secThread.start();
    }

    private void getWeb()
    {
        try {
            doc = Jsoup.connect( "https://coronavirus-monitor.info/country/kazakhstan/"  ).get();
            Elements divs = doc.getElementsByTag( "div" );

            final Elements data = divs.get( 15).getElementsByTag( "h1" );

            final Elements infected_count_h2 = divs.get( 19 ).getElementsByTag( "h2" );
            final Elements infected_count_today = divs.get( 19 ).getElementsByTag( "sup" );

            final Elements cured_count_h2 = divs.get( 21 ).getElementsByTag( "h2" );
            final Elements cured_count_today = divs.get( 21 ).getElementsByTag( "sup" );

            final Element deaths = divs.get(23);

            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                   infected_count.setText( infected_count_h2.text().substring( 8,16 ) );
                   infected_today.setText( infected_count_today.text() );
                   cured_count.setText( cured_count_h2.text().substring( 8,15 ) );
                   cured_today.setText( cured_count_today.text() );
                   deaths_count.setText( deaths.text().substring( 8 ) );
                   day.setText( data.text().substring( 65 ) );
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
