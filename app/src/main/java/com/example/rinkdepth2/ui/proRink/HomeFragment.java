package com.example.rinkdepth2.ui.proRink;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rinkdepth2.R;
import com.example.rinkdepth2.databinding.FragmentHomeBinding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.io.FileWriter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView rinkImg;
        CalendarView datePicker;
        EditText et;
        Button viewButton;
        TextView response;

        String path = "http://192.168.1.8:8080/C:\\Users\\berna\\Documents\\rinkDepth";
        Context cont = root.getContext();
        File file = new File(cont.getFilesDir(), "test.txt");
        String data = "Hello World!";

        rinkImg = root.findViewById(R.id.proRinkImg);
        rinkImg.setImageResource(R.drawable.prorink);
        datePicker = root.findViewById(R.id.datePickerView);
        et = root.findViewById(R.id.editTextDate);
        viewButton = root.findViewById(R.id.viewBtn);
        response = root.findViewById(R.id.textView);

        datePicker.setOnDateChangeListener((calendarView, year, month, day) -> {
            String date = day + " / " + month + " / " + year;
            et.setText(date);
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                    String res = String.valueOf(createFile(cont, file, path, data));
                    response.setText(res);
                }).start();
            }
        });

        return root;
    }

    private int createFile(Context cont, File file, String path, String data)
    {
        boolean newFileCreated = false;
        try
        {
            File newFile = new File(cont.getFilesDir(), file.getName());
            if (!newFile.createNewFile()) {
                newFileCreated = false;
                //appendToFile(cont, file, path, data);
            } else {
                newFileCreated = true;
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return writeToFile(cont,file, path, data);
    }

    private int writeToFile(Context cont, File file, String path, String data)
    {
//        try (FileOutputStream fos = cont.openFileOutput(file.getName(), Context.MODE_PRIVATE))
//        {
//            fos.write(data.toByteArray());
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return uploadDocument(cont, file, path, data);
    }

    private int appendToFile(Context cont, File file, String path, String data)
    {
        return uploadDocument(cont, file, path, data);
    }

    private int uploadDocument(Context cont, File file, String path, String data)
    {
        int response = 0;
        URLConnection urlConn = null;

        try
        {
            URL url = new URL(path);
            //URL url = new URL("https", "192.168.1.8", 8080, "C:\\Users\\berna\\Documents\\rinkDepth\\test.txt");
            urlConn = url.openConnection();
            urlConn.setDoOutput(true);
            if (urlConn instanceof HttpURLConnection)
            {
                ((HttpURLConnection) urlConn).setRequestMethod("PUT");
                urlConn.setRequestProperty("Content-type", "text/plain");
                urlConn.setRequestProperty("Connection", "close");
                urlConn.connect();
            }
            BufferedOutputStream bos = new BufferedOutputStream(urlConn.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()));

            int x;
            while((x = bis.read()) >-1)
            {
                bos.write(x);
            }
            bos.close();
            bis.close();
            assert urlConn instanceof HttpURLConnection;
            response = ((HttpURLConnection)urlConn).getResponseCode();
            ((HttpURLConnection) urlConn).disconnect();
            int y = 0;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}