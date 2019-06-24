import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TaskJavaApp {

    public void init() throws NoSuchFieldException, IllegalAccessException {
        String relativeWay = relativeWay();
        FileReader file = null;
        PrintWriter writerErr = null;
        try {
            writerErr = new PrintWriter("file_err.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            file = new FileReader(relativeWay);
        } catch (FileNotFoundException e) {
            writerErr.println(e.toString());
        }
        Scanner in = new Scanner(file);

        int countEqually = 0;
        int countLine = 0;

        while (in.hasNextLine()) {
            String fullLine = in.nextLine();
            countLine++;
            if (fullLine.split("=").length == 2) {
                countEqually++;
            }
        }
        try {
            file.close();
        } catch (IOException e) {
            writerErr.println(e.toString());
        }
        cmdCommand();
        workWithFile(countEqually, countLine, file, relativeWay);
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        TaskJavaApp taskJavaApp = new TaskJavaApp();
        taskJavaApp.init();
    }

    private Map<String, String> readCommand() {

        String fullfcmd = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                fullfcmd = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        String[] consoleTokens = fullfcmd.split(" ");
        if (consoleTokens.length != 2) {
            throw new IllegalArgumentException();
        }

        String key = consoleTokens[0];
        String command = consoleTokens[1];


        Map<String, String> map = new HashMap<String, String>();
        map.put(key, command);

        return map;
    }

    private void cmdCommand() throws NoSuchFieldException, IllegalAccessException {

        System.setProperty("file.encoding","UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null,null);

        String cmdCommand = readCommand().get("-cmd");
        String[] command = {"cmd",};
        Process p;
        try {

            p = Runtime.getRuntime().exec(command);
            FileOutputStream file = new FileOutputStream("cmd_out.txt");
            FileOutputStream filErr = new FileOutputStream("cmd_err.txt" );
            new Thread(new SyncPipe(p.getErrorStream(), filErr)).start();
            new Thread(new SyncPipe(p.getInputStream(), file)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println(cmdCommand);
            stdin.close();
            p.waitFor();
            file.close();
            filErr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String relativeWay() {
        String way = readCommand().get("-f");
        String relativeWay = System.getProperty("user.dir");

        if (!way.substring(1, 2).equals(":")) {
            if (way.substring(0, 1).equals("/") || way.substring(0, 1).equals("\\")) {
                relativeWay = relativeWay.concat(way);
            } else {
                relativeWay = relativeWay.concat("\\");
                relativeWay = relativeWay.concat(way);
            }
        } else {
            relativeWay = way;
        }
        return relativeWay;
    }

    private void workWithFile(int countEqually, int countLine, FileReader file, String relativeWay) {

        if (countEqually == countLine) {
            fileWriter(file, relativeWay);
        } else if (countEqually == 0) {
            badFileWriter(file, relativeWay);
        }
    }

    private void fileWriter(FileReader file, String relativeWay) {
        PrintWriter writerErr = null;
        try {
            writerErr = new PrintWriter("file_err.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            file = new FileReader(relativeWay);
        } catch (FileNotFoundException e) {
            writerErr.println(e.toString());
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("file_out.txt");
        } catch (IOException e) {
            writerErr.println(e.toString());
        }
        Scanner in = new Scanner(file);
        while (in.hasNextLine()) {
            String fullLine = in.nextLine();
            String[] fileTokens = fullLine.split("=");
            if (fileTokens.length != 2) {
                throw new IllegalArgumentException();
            }
            String keyFile = fileTokens[0];
            String command1File = fileTokens[1];
            try {
                writer.write(keyFile + "\n");
                writer.write(command1File + "\n");
            } catch (IOException e) {
                writerErr.println(e.toString());
            }
        }
        try {
            file.close();
            writer.close();
        } catch (IOException e) {
            writerErr.println(e.toString());

        }
    }

    private void badFileWriter(FileReader file, String relativeWay) {
        PrintWriter writerErr = null;
        try {
            writerErr = new PrintWriter("file_err.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            file = new FileReader(relativeWay);
        } catch (FileNotFoundException e) {
            writerErr.println(e.toString());
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("file_out.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner(file);
        while (in.hasNextLine()) {
            String fullLine = in.nextLine();
            try {
                writer.write(fullLine + "\n");
            } catch (IOException e) {
                writerErr.println(e.toString());
            }
        }
        try {
            file.close();
            writer.close();
        } catch (IOException e) {
            writerErr.println(e.toString());
        }
    }

}

