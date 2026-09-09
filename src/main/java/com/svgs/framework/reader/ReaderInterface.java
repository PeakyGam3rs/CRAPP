package com.svgs.framework.reader;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fazecast.jSerialComm.SerialPort;
import com.svgs.framework.app.DataProperty;
import com.svgs.framework.app.DataRegistry;
import com.svgs.framework.app.ParseRegistry;

public class ReaderInterface {
    public static final String PORT_NAME = "COM6";
    public static final int BAUD_RATE = 9600;

    public static SerialPort socket;
    public static InputStream inputStream;
    public static OutputStream outputStream;

    // need data declared + initialized at the start so data can be recorded
    // properly as it runs.
    public static final ArrayList<DataProperty> dataBucket = DataRegistry.createData();

    // the poller thread walks this while the FX thread adds to it, so it has to be
    // a copy-on-write list or we get a ConcurrentModificationException mid-drive.
    private static final CopyOnWriteArrayList<Runnable> pollTasks = new CopyOnWriteArrayList<>();
    private static final Set<String> polledTitles = ConcurrentHashMap.newKeySet();

    /** Finds the live metric behind a value title, e.g. "boostValue". */
    public static DataProperty getProperty(String title) {
        for (DataProperty d : dataBucket) {
            if (d.getTitleProperty().equals(title)) {
                return d;
            }
        }
        throw new IllegalArgumentException("No data property registered for '" + title + "'");
    }

    /**
     * Starts polling a metric. Adding the same gauge twice shouldn't double the
     * traffic on the OBD link, so each title only ever registers once.
     */
    public static void registerPoll(DataProperty property) {
        if (polledTitles.add(property.getTitleProperty())) {
            pollTasks.add(() -> readDouble(property));
        }
    }

    public static List<Runnable> getPollTasks() {
        return pollTasks;
    }

    /** @return true if the adapter answered and polling has started. */
    public static boolean startobdRead() {
        try {
            socket = SerialPort.getCommPort(PORT_NAME);
            socket.setBaudRate(BAUD_RATE);
            if (!socket.openPort()) {
                System.out.println("Couldn't open " + PORT_NAME);
                return false;
            }

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            rawCommand(inputStream, outputStream, "ATZ");
            rawCommand(inputStream, outputStream, "ATE0");
            rawCommand(inputStream, outputStream, "ATL0");
            rawCommand(inputStream, outputStream, "ATS0");
            rawCommand(inputStream, outputStream, "ATAT1");
            rawCommand(inputStream, outputStream, "ATSP0");

            System.out.println("Connected yo");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        Poller.start();
        return true;
    }

    /** Stops polling and releases the serial port. */
    public static void stopobdRead() {
        Poller.stop();
        inputStream = null;
        outputStream = null;
        if (socket != null && socket.isOpen()) {
            socket.closePort();
        }
    }

    private static void clearInput(InputStream in) throws Exception {
        while (in.available() > 0) {
            in.read();
        }
    }

    /** Reads one metric off the adapter and pushes it to its property. */
    public static void readDouble(DataProperty property) {
        InputStream in = inputStream;
        OutputStream out = outputStream;
        if (in == null || out == null) {
            return;
        }

        try {
            String rawCMD = rawCommand(in, out, property.getReadProperty());
            Parser parser = new Parser(rawCMD, property.getDataOffset());
            double value = ParseRegistry.execute(property.getTitleProperty(), parser.getA(), parser.getB());
            property.writeValue(value);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * One CSV row: timestamp first, then every metric in {@link DataRegistry}
     * order.
     * SaveViewer relies on that order to pick columns back out.
     */
    public static String getValueReadout() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());

        for (DataProperty d : dataBucket) {
            sb.append(",");
            sb.append(d.currentValue());
        }
        return sb.toString();
    }

    /** Header matching {@link #getValueReadout()}. */
    public static String getHeaderRow() {
        StringBuilder sb = new StringBuilder("timeMillis");
        for (DataProperty d : dataBucket) {
            sb.append(",");
            sb.append(d.getTitleProperty());
        }
        return sb.toString();
    }

    private static String rawCommand(InputStream in, OutputStream out, String cmd) throws Exception {
        clearInput(in);

        out.write((cmd + "\r").getBytes());
        out.flush();

        StringBuilder response = new StringBuilder(); // found this thingy on google, but it's basically just a string
        long end = System.currentTimeMillis() + 5000;

        while (System.currentTimeMillis() < end) {
            while (in.available() > 0) {
                char c = (char) in.read();
                response.append(c);
                if (c == '>') {
                    return response.toString();
                }
            }
            Thread.sleep(50);
        }

        throw new RuntimeException("Timeout waiting for response to " + cmd +
                ". Partial: " + response);
    }

}
