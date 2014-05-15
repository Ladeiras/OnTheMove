package com.moksie.onthemove.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.moksie.onthemove.objects.Aeroporto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Comparator;

/**
 * Created by belh0 on 30-04-2014.
 */
public class FileIO
{
    public static boolean fileExists(String file, Context c)
    {
        String filename = c.getExternalFilesDir("")+ "/" + file;
        File f = new File(filename);
        if(f.exists())
            return true;

        return false;
    }

    public static void writeToFile(String file, String data, Context c)
    {
        String filename = c.getExternalFilesDir("")+ "/" + file;
        FileWriter write;
        try {
            write = new FileWriter(filename, true);
            write.append(data);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String file, Context c)
    {
        String filename = c.getExternalFilesDir("")+ "/" + file;
        File f = new File(filename);
        String ret = "";

        try {
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = buffer.readLine()) != null)
            {
                stringBuilder.append(receiveString);
            }
            in.close();
            ret = stringBuilder.toString();
        }
        catch (FileNotFoundException e) {
            Log.e("ERROR", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("ERROR", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static boolean removeFile(String file, Context c)
    {
        String filename = c.getExternalFilesDir("")+ "/" + file;
        File f = new File(filename);
        return f.delete();
    }
}
