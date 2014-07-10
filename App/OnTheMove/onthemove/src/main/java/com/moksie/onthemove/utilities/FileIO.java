package com.moksie.onthemove.utilities;

import android.content.Context;
import android.util.Log;

import com.moksie.onthemove.objects.FlightSerializable;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

/**
 * Nesta classe são implementadas funções estáticas para fazer handling das várias tarefas de
 * escrita e leitura de ficheiros da memória.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */

public class FileIO
{
    /**
     * Verifica se um ficheiro existe
     *
     * @param file Nome do ficheiro
     * @param c Context da Activity de chamada
     * @return True se existe, False se não
     */
    public static boolean fileExists(String file, Context c)
    {
        String filename = c.getExternalFilesDir("")+ "/" + file;
        File f = new File(filename);
        if(f.exists())
            return true;

        return false;
    }

    /**
     * Escrita de um ficheiro em memória
     *
     * @param file Nome do ficheiro
     * @param data Conteúdo do ficheiro
     * @param c Context da Activity de chamada
     */
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

    /**
     * Leitura de um ficheiro em memória
     *
     * @param file Nome do ficheiro
     * @param c Context da Activity de chamada
     * @return Conteúdo do ficheiro
     */
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

    /**
     * Elimina um ficheiro
     *
     * @param file Nome do ficheiro
     * @param c Context da Activity de chamada
     * @return True se removido com sucesso, Flase se não
     */
    public static boolean removeFile(String file, Context c)
    {
        String filename = c.getExternalFilesDir("")+ "/" + file;
        File f = new File(filename);
        return f.delete();
    }

    /**
     * Escrita de um ficheiro serializavel em memória
     *
     * @param file Nome do ficheiro
     * @param obj Objecto Serializable
     * @param c Context da Activity de chamada
     */
    public static void serializeObject(String file, Object obj, Context c)
    {
        FileOutputStream fos = null;
        String filename = c.getExternalFilesDir("")+ "/" + file;
        try
        {
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

    /**
     * leitura de um ficheiro serializaval a partir da memoria
     *
     * @param file Nome do ficheiro
     * @param c Context da Activity de chamada
     * @return Objecto Serializable
     */
    public static FlightSerializable deserializeFlightObject(String file, Context c)
    {
        FileInputStream fis = null;
        FlightSerializable flightSerializable = null;
        String filename = c.getExternalFilesDir("")+ "/" + file;

        try
        {
            //fis = c.openFileInput(filename);
            fis = new FileInputStream (new File(filename));
            ObjectInputStream is = new ObjectInputStream(fis);
            flightSerializable = (FlightSerializable) is.readObject();
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

        return flightSerializable;
    }
}
