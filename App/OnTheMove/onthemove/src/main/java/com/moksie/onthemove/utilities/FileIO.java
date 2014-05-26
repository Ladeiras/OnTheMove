package com.moksie.onthemove.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.moksie.onthemove.objects.Aeroporto;
import com.moksie.onthemove.objects.Voo;
import com.moksie.onthemove.objects.VooSerializable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
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

    public static void serializeObject(String file, Object obj, Context c)
    {
        FileOutputStream fos = null;
        String filename = c.getExternalFilesDir("")+ "/" + file;
        try
        {
            //fos = c.openFileOutput(filename, Context.MODE_PRIVATE);
            fos = new FileOutputStream (new File(filename));
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(obj);
            os.close();
        }
        catch (FileNotFoundException e)
        {
            Log.e("ERROR", "File not found: " + e.toString());
        }
        catch (IOException e)
        {
            Log.e("ERROR", "Can not write file: " + e.toString());
        }
    }

    public static VooSerializable deserializeVooObject(String file, Context c)
    {
        FileInputStream fis = null;
        VooSerializable vooSerializable = null;
        String filename = c.getExternalFilesDir("")+ "/" + file;

        try
        {
            //fis = c.openFileInput(filename);
            fis = new FileInputStream (new File(filename));
            ObjectInputStream is = new ObjectInputStream(fis);
            vooSerializable = (VooSerializable) is.readObject();
            is.close();
        }
        catch (FileNotFoundException e)
        {
            Log.e("ERROR", "File not found: " + e.toString());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("ERROR", "Class not found: " + e.toString());
        }
        catch (OptionalDataException e)
        {
            Log.e("ERROR", "Optional Data: " + e.toString());
        }
        catch (StreamCorruptedException e)
        {
            Log.e("ERROR", "Stream Corrupted: " + e.toString());
        }
        catch (IOException e)
        {
            Log.e("ERROR", "Can not read file: " + e.toString());
        }

        return vooSerializable;
    }
}
