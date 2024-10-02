package com.example.rinkdepth2;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class HttpFile
{
    public boolean checkConnection(URLConnection urlConn)
    {
        try
        {;
            urlConn.setDoOutput(urlConn.getDoOutput());
            return false;
        }catch (IllegalStateException e)
        {
            return true;
        }
    }

    public int uploadDocument(Context cont, File file, String path, String data)
    {
        int response = 0;
        URLConnection urlConn = null;
        try
        {
            URL url = new URL(path);
            urlConn = url.openConnection();
            urlConn.setDoOutput(true);
            //boolean connected = checkConnection(urlConn);
            //if (!connected)
            //{
                if (urlConn instanceof HttpURLConnection)
                {
                    ((HttpURLConnection) urlConn).setRequestMethod("POST");
                    urlConn.setRequestProperty("Content-type", "text/plain");
                    urlConn.setRequestProperty("Connection", "close");
                    urlConn.setRequestProperty("http.keepAlive", "false");
                    urlConn.connect();
                }
            //}
            BufferedOutputStream bos = new BufferedOutputStream(urlConn.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()));

            int x;
            while((x = bis.read()) >-1)
            {
                bos.write(x);
            }
            String mess = ((HttpURLConnection)urlConn).getResponseMessage();
            bos.close();
            bis.close();
            //response = ((HttpURLConnection)urlConn).getResponseCode();

            ((HttpURLConnection) urlConn).disconnect();
            int y = 0;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public int createFile(Context cont, File file, String path, String data)
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

    public int writeToFile(Context cont, File file, String path, String data)
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

    public int appendToFile(Context cont, File file, String path, String data)
    {
        return uploadDocument(cont, file, path, data);
    }
}
