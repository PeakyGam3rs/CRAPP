package com.svgs.framework.reader;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.fazecast.jSerialComm.SerialPort;
import com.svgs.framework.app.DataProperty;
import com.svgs.framework.app.DataRegistry;
import com.svgs.framework.app.ParseRegistry;

public class ReaderInterface {
    public static SerialPort socket;
    public static InputStream inputStream;
    public static OutputStream outputStream;

    // need data declared + initialized at the start so data can be recorded properly as it runs. 
    public static final ArrayList<DataProperty> dataBucket = DataRegistry.createData();
    public static ArrayList<Runnable> gaugesToUse = new ArrayList<>();

    public static void startobdRead(){
        try {
            socket = SerialPort.getCommPort("COM6"); //outputstream
            socket.setBaudRate(9600);
            if(!socket.openPort()){
                System.out.println("Didn't open port!!");
                return;
            }
                System.out.println("buh");

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
                System.out.println("streams");

            rawCommand(inputStream, outputStream, "ATZ");
            rawCommand(inputStream, outputStream, "ATE0");
            rawCommand(inputStream, outputStream, "ATL0");
            rawCommand(inputStream, outputStream, "ATS0");
            rawCommand(inputStream, outputStream, "ATAT1");
            rawCommand(inputStream, outputStream, "ATSP0");
           

            System.out.println("Connected yo");
        } catch (Exception e) {
            System.out.println(e);
        }
        Poller.poller = Poller.createPoller();
    }

    private static void clearInput(InputStream in) throws Exception {
        while (in.available() > 0) {
            in.read();
        }
    }

    private static void updateDataBucketValue(DataProperty property, double value) {
        for (DataProperty d : dataBucket) {
            if (property.getTitleProperty().equals(d.getTitleProperty())) {
                d.writeValue(value);
            }
        }
    }

    public static void readDouble(InputStream inputStream, OutputStream outputStream, String cmd, DataProperty property) {
        try {
            String rawCMD = rawCommand(inputStream, outputStream, property.getReadProperty());
            Parser parser = new Parser(rawCMD, property.getDataOffset());
            double value = ParseRegistry.execute(property.getTitleProperty(), parser.getA(), parser.getB());
            updateDataBucketValue(property, value);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String getValueReadout() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append(",");

        for (DataProperty d : dataBucket) {
            sb.append(d.read());
            sb.append(",");
        }
        return sb.toString();
    }

    private static String rawCommand(InputStream in, OutputStream out, String cmd) throws Exception {
        clearInput(in);

        out.write((cmd + "\r").getBytes());
        out.flush();

        StringBuilder response = new StringBuilder(); //found this thingy on google, but it's basically just a string
        long end = System.currentTimeMillis() + 5000;

        while (System.currentTimeMillis() < end) {
            while (in.available() > 0) {
                char c = (char) in.read();
                response.append(c);
                if (c == '>') {
                    String result = response.toString();
                  //  System.out.println(cmd + " -> " + result);
                    return result;
                }
            }
           Thread.sleep(50);
        }

        throw new RuntimeException("Timeout waiting for response to " + cmd +
                ". Partial: " + response);
    }

}
