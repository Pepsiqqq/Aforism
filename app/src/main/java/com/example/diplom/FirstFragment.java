package com.example.diplom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static java.lang.Math.abs;

public class FirstFragment extends Fragment {
    List<Integer> like = new ArrayList<Integer>();
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            new Thread(new Runnable() {
                public void run() {
                    // a potentially time consuming task
                    mtextview.post(new Runnable() {
                        @Override
                        public void run() {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                    "HH:mm:ss", Locale.getDefault());
                            String strDate = simpleDateFormat.format(calendar.getTime());
                            mtextview.setText(strDate);
                            mtextview.postDelayed(this,1000);
                        }
                    });
                }
            }).start();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void writeToFile(List<Integer> data, Context context){
        try {
            context.getApplicationContext().deleteFile("Like.txt");
            // File fil = new File(context.getApplicationContext().getFilesDir(),"Like.txt");
            OutputStream stream = context.getApplicationContext().openFileOutput("Like.txt",Context.MODE_PRIVATE);
            BufferedWriter br = new BufferedWriter (new OutputStreamWriter(stream));
            data.sort(Comparator.naturalOrder());
            for(int i =0;i<data.size();i++) {
                br.write(data.get(i) + "\n");
            }

            br.close();
            stream.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private List<String> readLike(Context context)
    {
        List<String> myData = new ArrayList<String>();
        try {
            // File fil = new File(context.getApplicationContext().getFilesDir(),"Like.txt");
            InputStream fis= context.getApplicationContext().openFileInput("Like.txt");
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData.add(strLine);
            }
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }
    private List<String> readFile()
    {
        List<String> myData = new ArrayList<String>();
        try {
            java.io.InputStream fis = getResources().openRawResource(R.raw.aforism);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData.add(strLine);
            }
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }
    Timer timer;
    MyTimerTask timerTask;
    TextView mtextview;
    TextView showCountTextView;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @SuppressLint("ResourceAsColor")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> str = new ArrayList<String>();
        List <String> p = new ArrayList<String>();
        like.clear();
        p = readLike(this.getContext());
        for(String s : p ) like.add(Integer.valueOf(s));
        str = readFile();
        int day,month;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month= Calendar.getInstance().get(Calendar.MONTH);
        Random rand = new Random();
        int aforism_day = day*month;
        rand.setSeed(aforism_day);
        int aforism = abs(rand.nextInt()%str.size()+1);
        //String temp = Integer.toString(aforism);
        if(aforism%2==0){
        }
        else{
        aforism-=1;
        }
        ((TextView)view.findViewById(R.id.Aforism_day)).setText(str.get(aforism) + "\n\n" + str.get(aforism+1));
        mtextview = view.findViewById(R.id.textView22);
        timer =new Timer();
        timerTask = new MyTimerTask();
        timer.schedule(timerTask, 1, 100000);
        for(int i =0;i<like.size();i++){
            if(aforism/2 == like.get(i)) {
                ((Button) view.findViewById(R.id.button6)).setText("Unlike");
                ((Button) view.findViewById(R.id.button6)).setBackgroundColor(RED);
            }
        }
        int finalAforism = aforism;
        view.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(((Button)view.findViewById(R.id.button6)).getText().equals("Unlike")){
                    ((Button)view.findViewById(R.id.button6)).setText("Like");
                    ((Button)view.findViewById(R.id.button6)).setBackgroundColor(WHITE);
                    int o = like.indexOf(finalAforism /2);
                    like.remove(o);
                    writeToFile(like,FirstFragment.this.getContext());
                }
                else {
                    ((Button)view.findViewById(R.id.button6)).setText("Unlike");
                    ((Button)view.findViewById(R.id.button6)).setBackgroundColor(RED);
                    like.add(finalAforism/2);
                    writeToFile(like,FirstFragment.this.getContext());
                }
            }
        });
        view.findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment);
            }
        });
    }
}