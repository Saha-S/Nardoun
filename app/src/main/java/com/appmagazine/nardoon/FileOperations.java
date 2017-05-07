package com.appmagazine.nardoon;


        import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {
    public FileOperations() {

    }

    public Boolean write(String fname, String fcontent){
        try {


            String fpath = Environment.getExternalStorageDirectory()+"/Android/data/com.appmagazinenardoon/files/"+fname+".nb";


            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {

                File folderu = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.appmagazinenardoon/files/");
                folderu.mkdirs();





                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess","Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String read(String fname){

        BufferedReader br = null;
        String response = null;

        try {

            StringBuffer output = new StringBuffer();
            String fpath = Environment.getExternalStorageDirectory()+"/Android/data/com.appmagazinenardoon/files/"+fname+".nb";

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            response = output.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "nofile";

        }
        return response;


    }
}
