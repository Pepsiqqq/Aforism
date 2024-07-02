package com.example.diplom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

public class ThirdFragment extends Fragment {
    TextView mtextview;
    List<TextView> text = new ArrayList<>();
    List<Button> but = new ArrayList<>();
    List<String> str = new ArrayList<String>();
    List<Integer> like = new ArrayList<Integer>();
    List<Integer> save = new ArrayList<Integer>();
    LinearLayout layout;
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void writeToFile(List<Integer> data, Context context) {
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
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.activity_third_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //load likes
        List<String> p = new ArrayList<String>();
        like.clear();
        p = readLike(this.getContext());
        for(int i = 0; i < p.size();i++){
        like.add(Integer.valueOf(p.get(i)));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStop(){
        super.onStop();
        like.clear();
        for(int i = 0;i< but.size();i++){
            if(but.get(i).getText().equals("Unlike")) {
                like.add(save.get(i));
            }
        }
        writeToFile(like,this.getContext());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPause(){
        super.onPause();
        like.clear();
        for(int i = 0;i< but.size();i++){
            if(but.get(i).getText().equals("Unlike")) {
                like.add(save.get(i));
            }
        }
        writeToFile(like,this.getContext());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        str = readFile();
        layout = view.findViewById(R.id.Linerla2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        params.alignWithParent = true;
        String arr = new String();

         List<String> p = new ArrayList<String>();
        like.clear();
        p = readLike(this.getContext());
        for(int i = 0; i < p.size();i++){
            like.add(Integer.valueOf(p.get(i)));
        }
        for(int i =0;i<str.size()/2;i++){
            for(int j =0;j<like.size();j++) {
                if (i == like.get(j)) {
                    but.add(new Button(this.getContext()));
                    text.add(new TextView(this.getContext()));
                    arr = str.get((i*2))+"\n"+str.get((i*2)+1);
                    text.get(j).setTextSize(24);
                    text.get(j).setTextColor(WHITE);
                    text.get(j).setText(arr);
                    text.get(j).setPadding(0,0,0,0);
                    text.get(j).setLayoutParams(params);
                    text.get(j).setGravity(Gravity.FILL);
                    save.add(i);
                    int finalI = j;
                    but.get(finalI).setBackgroundColor(WHITE);
                    but.get(j).setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View view) {
                            if(but.get(finalI).getText().equals("Unlike")){
                                but.get(finalI).setBackgroundColor(WHITE);
                                but.get(finalI).setText("Like");
                                //like.remove(finalI);
                                like.clear();
                                for(int i = 0;i< but.size();i++){
                                    if(but.get(i).getText().equals("Unlike")) {
                                        like.add(save.get(i));
                                    }
                                }
                                writeToFile(like,ThirdFragment.this.getContext());
                            }
                            else {
                                but.get(finalI).setBackgroundColor(RED);
                                but.get(finalI).setText("Unlike");
                                like.add(finalI);
                                like.clear();
                                for(int i = 0;i< but.size();i++){
                                    if(but.get(i).getText().equals("Unlike")) {
                                        like.add(save.get(i));
                                    }
                                }
                                writeToFile(like,ThirdFragment.this.getContext());
                            }
                        }
                    });
                    but.get(j).setText("Like");
                    layout.addView(text.get(j));
                    layout.addView(but.get(j));
                    //u+=2;
                }
            }
        }
        for (int c = 0; c < like.size(); c++) {
            but.get(c).setBackgroundColor(RED);
            but.get(c).setText("Unlike");
        }
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });
    }

}