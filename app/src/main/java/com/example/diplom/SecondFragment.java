package com.example.diplom;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.RED;

public class SecondFragment extends Fragment {
    TextView mtextview;
    List<TextView> text = new ArrayList<>();
    List<Button> but = new ArrayList<>();
    List<String> str = new ArrayList<String>();
    List<Integer> like = new ArrayList<Integer>();
    LinearLayout layout;
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
    @Override
    public void onStart() {
        super.onStart();
        //load likes
        List <String> p = new ArrayList<String>();
        like.clear();
        p = readLike(this.getContext());
        for(String s : p ) like.add(Integer.valueOf(s));

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStop(){
        super.onStop();
        writeToFile(like,this.getContext());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPause(){
        super.onPause();
        writeToFile(like,this.getContext());
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List <String> p = new ArrayList<String>();
        like.clear();
        final int[] check = {0};
        p = readLike(this.getContext());
        for(String s : p ) like.add(Integer.valueOf(s));
        //TextView textView = new TextView(this.getContext());
        str = readFile();
        layout = view.findViewById(R.id.Linerla);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        params.alignWithParent = true;
        final String[] arr = {new String()};
        final int[] u = {0};
        for(int i =0;i<str.size()/2;i++){
            but.add(new Button(this.getContext()));
            text.add(new TextView(this.getContext()));
            arr[0] = str.get(u[0])+"\n"+str.get(u[0] +1);
            text.get(i).setTextSize(24);
            text.get(i).setTextColor(WHITE);
            text.get(i).setText(arr[0]);
            text.get(i).setPadding(0,0,0,0);
            text.get(i).setLayoutParams(params);
            text.get(i).setGravity(Gravity.FILL);
            //but.get(i).setId(100+i);
            //but.get(i).getId();
            int finalI = i;
            but.get(finalI).setBackgroundColor(WHITE);
            but.get(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    if(but.get(finalI).getText().equals("Unlike")){
                        but.get(finalI).setBackgroundColor(WHITE);
                        but.get(finalI).setText("Like");
                        int o = like.indexOf(finalI);
                        like.remove(o);
                        writeToFile(like,SecondFragment.this.getContext());
                    }
                    else {
                        but.get(finalI).setBackgroundColor(RED);
                        but.get(finalI).setText("Unlike");
                        like.add(finalI);
                        writeToFile(like,SecondFragment.this.getContext());
                    }
                }
            });
            but.get(i).setText("Like");
            layout.addView(text.get(i));
            layout.addView(but.get(i));
            u[0] +=2;
        }
        for(int i=0;i<like.size();i++){
            but.get(like.get(i)).setBackgroundColor(RED);
            but.get(like.get(i)).setText("Unlike");
        }
        //mtextview = view.findViewById(R.id.textView22);
       /* view.findViewById(but.get(0).getId()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    but.get(view.getId()).setBackgroundColor(RED);
                }
        });*/

        EditText editText = view.findViewById(R.id.editTextTextPersonName);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    but.clear();
                    text.clear();
                    layout.removeAllViews();
                    u[0] =0;
                    int text_counter = 0;
                    String sr = new String();
                    sr = editText.getText().toString();
                    for(int i =0;i<str.size()/2;i++){
                        if(str.get(i*2+ check[0]).toLowerCase().contains(sr.toLowerCase())){
                            but.add(new Button(SecondFragment.this.getContext()));
                            text.add(new TextView(SecondFragment.this.getContext()));
                            arr[0] = str.get(u[0])+"\n"+str.get(u[0] +1);
                            text.get(text_counter).setTextSize(24);
                            text.get(text_counter).setTextColor(WHITE);
                            text.get(text_counter).setText(arr[0]);
                            text.get(text_counter).setPadding(0,0,0,0);
                            text.get(text_counter).setLayoutParams(params);
                            text.get(text_counter).setGravity(Gravity.FILL);
                            //but.get(i).setId(100+i);
                            //but.get(i).getId();
                            int finalI = text_counter;
                            but.get(finalI).setBackgroundColor(WHITE);
                            int f1 = i;
                            but.get(text_counter).setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onClick(View view) {
                                    if(but.get(finalI).getText().equals("Unlike")){
                                        but.get(finalI).setBackgroundColor(WHITE);
                                        but.get(finalI).setText("Like");
                                        int o = like.indexOf(f1);
                                        like.remove(o);
                                        writeToFile(like,SecondFragment.this.getContext());
                                    }
                                    else {
                                        but.get(finalI).setBackgroundColor(RED);
                                        but.get(finalI).setText("Unlike");
                                        like.add(f1);
                                        writeToFile(like,SecondFragment.this.getContext());
                                    }
                                }
                            });
                            but.get(text_counter).setText("Like");
                            layout.addView(text.get(text_counter));
                            layout.addView(but.get(text_counter));
                            text_counter++;
                            for(int j =0;j<like.size();j++) {
                                if (i == like.get(j)) {
                                    but.get(finalI).setBackgroundColor(RED);
                                    but.get(finalI).setText("Unlike");
                                }
                            }
                        }
                        u[0] +=2;
                    }
                }
        };
        editText.addTextChangedListener(watcher);
        view.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check[0] ==0){
                    check[0] = 1;
                    ((Button)view.findViewById(R.id.button5)).setText("Автор");
                }
                else {
                    check[0] = 0;
                    ((Button)view.findViewById(R.id.button5)).setText("Афоризм");
                }
            }
        });
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment);
            }
        });
    }
}