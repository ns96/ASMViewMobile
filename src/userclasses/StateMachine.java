/**
 * Your application code goes here<br>
 * This file was generated by <a href="https://www.codenameone.com/">Codename
 * One</a> for the purpose of building native mobile applications using Java.
 */
package userclasses;

import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.bluetoothle.Bluetooth;
import com.codename1.components.InfiniteProgress;
import generated.StateMachineBase;
import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.codename1.util.Base64;
import com.codename1.util.StringUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Nathan Stevens
 *
 */
public class StateMachine extends StateMachineBase {

    // https://github.com/adafruit/Bluefruit_LE_Connect_Android/blob/master/app/src/main/java/com/adafruit/bluefruit/le/connect/app/UartInterfaceActivity.java
    public static final String UUID_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public static final String UUID_RX = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public static final String UUID_TX = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";

    private Bluetooth bt;
    private Map devices = new HashMap();
    private String bleAddress;
    private boolean connected = false;

    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of the
     * constructor/class scope to avoid race conditions
     */
    protected void initVars(Resources res) {
        bt = new Bluetooth();
        bleAddress = "";
    }

    @Override
    protected void beforeMain(Form f) {
        super.beforeMain(f); //To change body of generated methods, choose Tools | Templates.

        // update the picker
        Picker sp = findSyncPicker(f);
        sp.setStrings("Sync Every 10 min", "Sync Every 15 min", "Sync Every 30 min", "Sync Every 60 min");
        sp.setSelectedStringIndex(1);
    }

    @Override
    protected void onMain_ScanButtonAction(Component c, ActionEvent event) {
        try {
            bt.initialize(true, false, "bluetoothleplugin");
            scanBluetoothDevices();
        } catch (Exception ex) {
            ex.printStackTrace();
            addDummyDevices();
            bt = null;
        }
    }

    private void addDummyDevices() {
        try {
            // add somedum data for testing
            JSONObject obj = new JSONObject();
            obj.put("address", "00:00:00:00:00:00");
            obj.put("name", "Dummy BLE");
            devices.put("00:00:00:00:00:00", obj);

            updateAddressPicker();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Scan for bluetooth devices
     *
     * @throws IOException
     */
    private void scanBluetoothDevices() throws IOException {
        bt.startScan(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    JSONObject res = (JSONObject) evt.getSource();
                    System.out.println("response " + res);

                    if (res.getString("status").equals("scanResult")) {
                        //if this is a new device add it
                        if (!devices.containsKey(res.getString("address"))) {
                            if (!res.getString("name").equals("null")) {
                                devices.put(res.getString("address"), res);
                                updateAddressPicker();
                            }
                        }
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, null, true, Bluetooth.SCAN_MODE_LOW_POWER, Bluetooth.MATCH_MODE_STICKY,
                Bluetooth.MATCH_NUM_MAX_ADVERTISEMENT, Bluetooth.CALLBACK_TYPE_ALL_MATCHES);
    }

    /**
     * Update the address combo boxes
     */
    private void updateAddressPicker() throws JSONException {
        Picker addressPicker = findAddressPicker();
        ArrayList<String> al = new ArrayList<>();

        Set keys = devices.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String address = (String) iterator.next();
            JSONObject obj = (JSONObject) devices.get(address);
            String name = obj.getString("name");
            al.add(name + " || " + address);
        }

        String[] addresses = al.toArray(new String[al.size()]);
        addressPicker.setStrings(addresses);
    }

    @Override
    protected void onMain_AddressPickerAction(Component c, ActionEvent event) {
        Picker addressPicker = (Picker) c;
        String ss = addressPicker.getSelectedString();
        String address = StringUtil.tokenize(ss, "||").get(1).trim();
        connect(address);
    }

    /**
     * Connect to a particular bluetooth device
     *
     * @param address
     */
    private void connect(String address) {
        bleAddress = address;
        final TextArea console = findSensorTextArea();

        if (bt != null && !connected) {
            // start an infit progress
            final Dialog ip = new InfiniteProgress().showInifiniteBlocking();

            try {
                bt.connect(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        ip.dispose();
                        
                        Object obj = evt.getSource();
                        console.setText("Connected to Bluetooth LE device ...\n" + obj);
                        discover(); // must be called on Andriod. Won't do anything on ios though
                        connected = true;
                    }

                }, address);
            } catch (IOException ex) {
                String message = "Error connecting to bluetooth device: " + address;
                console.setText(message + "\n" + ex.getMessage());
            }
        } else {
            String message = "BLE device is null, or already conectede to: " + address;
            console.setText(message);
        }
    }

    /**
     * This method needs to be called on Android other wise the service is not
     * found
     */
    private void discover() {
        final TextArea console = findSensorTextArea();

        try {
            bt.discover(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    console.setText("Bluetooth LE Information obtained ...\n");
                    addSubscriber();
                }

            }, bleAddress);

        } catch (Exception ex) {
            console.setText(ex.getMessage());
        }

        // if we running on is add the subscriber here since the above bt call
        // does nothing?
        if (Display.getInstance().getPlatformName().equals("ios")) {
            console.setText("Adding subscriber for iOS Device");
            addSubscriber();
        }
    }

    /**
     * Method to listen for incoming data
     */
    private void addSubscriber() {
        final TextArea console = findSensorTextArea();

        try {
            bt.subscribe(new ActionListener() {
                StringBuilder sb= new StringBuilder();
                
                @Override
                public void actionPerformed(ActionEvent evt) {
                    JSONObject dataIncoming = (JSONObject) evt.getSource();
                    String base64Value = "";
                    try {
                        if (dataIncoming.getString("status").equals("subscribedResult")) {
                            base64Value = dataIncoming.getString("value");
                        }
                    } catch (JSONException e) {
                        console.setText("Error reading data: " + e.getMessage());
                    }

                    String message = new String(Base64.decode(base64Value.getBytes()));
                    sb.append(message);
                    
                    if(message.endsWith("\r\n")) {
                        console.setText("Data received: " + sb.toString());
                        sb = new StringBuilder();
                    }
                }

            }, bleAddress, UUID_SERVICE, UUID_RX);

            String message = console.getText() + "\n\nSubcriber added ...";
            console.setText(message);
        } catch (IOException ex) {
            String message = "Error subscribing ..." + ex.getMessage();
            console.setText(message);
        }
    }

    /**
     * Send text but first add CR LF and then encode in base64 otherwise doesn't
     * work
     *
     * @param text
     */
    private void sendText(String text) {
        final TextArea console = findSensorTextArea();
        final String data = text + "\r\n";

        try {
            String b64String = Base64.encode(data.getBytes());

            bt.write(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    console.setText("Data sent: " + data);
                }

            }, bleAddress, UUID_SERVICE, UUID_TX, b64String, false);
        } catch (IOException ex) {
            String message = "Error sending: " + text + "\n"
                    + UUID_SERVICE + "\n"
                    + UUID_TX + "\n"
                    + ex.getMessage();
            console.setText(message);
        }
    }

    @Override
    protected void onMain_UpdateButtonAction(Component c, ActionEvent event) {
        if (connected) {
            sendText("ASM Test send ...");
        }
    }

    @Override
    protected void exitMain(Form f) {
        super.exitMain(f);

        if (bt != null) {
            try {
                if (connected) {
                    bt.close(bleAddress);
                    connected = false;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
