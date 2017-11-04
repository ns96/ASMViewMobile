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
import com.codename1.components.ToastBar;
import com.codename1.io.Preferences;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import generated.StateMachineBase;
import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.codename1.util.Base64;
import com.codename1.util.StringUtil;
import com.leaflift.asm.ASMData;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Nathan Stevens
 *
 */
public class StateMachine extends StateMachineBase {

    // https://github.com/adafruit/Bluefruit_LE_Connect_Android/blob/master/app/src/main/java/com/adafruit/bluefruit/le/connect/app/UartInterfaceActivity.java
    private final String UUID_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    private final String UUID_RX = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    private final String UUID_TX = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";

    private int delayTime; // set the delay when reading data

    private Bluetooth bt;
    private Map devices = new HashMap();
    private String bleAddress;
    private boolean connected = false;

    // global gui components
    private Form mainForm;
    private TextArea console;

    private boolean onSimulator;

    private int simulatorCount;

    private boolean autoRead = true;

    private boolean updateSetup = false;

    private boolean getFile = false;

    private JSONObject configJSON;

    private HashMap<String, ASMData> dataFiles;

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

        String os = System.getProperty("os.name");
        if (os != null && os.indexOf("Windows") != -1) {
            System.out.println("Running on simulator");
            bt = null;
            onSimulator = true;
            simulatorCount = 1;
            delayTime = 1000;
        } else {
            onSimulator = false;
            simulatorCount = 0;
            delayTime = 5000;
        }

        // DEBUG
        for (String file : Storage.getInstance().listEntries()) {
            System.out.println("Storage file: " + file);
        }

        // initiate or load the hashmap for storing data
        //Object object = Storage.getInstance().readObject("dataFiles");
        Object object = null;
        try {
            object = Storage.getInstance().readObject("dataFiles");
        } catch (Exception ie) {
            ie.printStackTrace();
        }

        if (object == null) {
            dataFiles = new HashMap<>();
        } else {
            dataFiles = (HashMap<String, ASMData>) object;
        }
    }

    /**
     * Used to enable buttons once connected
     *
     * @param enable
     */
    private void enableButtons(boolean enable) {
        findDisconnectButton().setEnabled(true);
        findUpdateButton().setEnabled(true);
        findReadButton().setEnabled(true);
    }

    @Override
    protected void beforeMain(Form f) {
        super.beforeMain(f); //To change body of generated methods, choose Tools | Templates.

        // the main form
        mainForm = f;

        // set the conole object
        console = findSensorTextArea(f);

        // update the picker
        Picker sp = findSyncPicker(f);
        sp.setStrings("Sync Every 10 min", "Sync Every 15 min", "Sync Every 30 min", "Sync Every 60 min");
        sp.setSelectedStringIndex(1);

        // update the setup panel from stored value
        configJSON = new JSONObject();
        try {
            configJSON.put("location", Preferences.get("location", "GRH55"));
            configJSON.put("hostname", Preferences.get("name", "Node0A"));
            configJSON.put("wifi", Preferences.get("wifi", "GRH55Wifi"));
            configJSON.put("password", Preferences.get("password", "000C00F000"));
            configJSON.put("url", Preferences.get("url", "http://api-quadroponic.rhcloud.com/v1/record/sensordata"));
            configJSON.put("sync", Preferences.get("sync", "30"));
            configJSON.put("online", Preferences.get("online", "?"));

            updateSetup = true;
            processConfigData(configJSON);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        // add any data files which where loaded
        updateListEntry();
    }

    @Override
    protected void onMain_ScanButtonAction(Component c, ActionEvent event) {
        try {
            bt.initialize(true, false, "bluetoothleplugin");
            scanBluetoothDevices();
        } catch (Exception ex) {
            addDummyDevices();
            bt = null;
        }
    }

    private void addDummyDevices() {
        try {
            // add somedum data for testing
            for (int i = 1; i <= 5; i++) {
                JSONObject obj = new JSONObject();
                obj.put("address", "00:00:00:00:00:00");
                obj.put("name", "Dummy BLE" + i);
                devices.put("00:00:00:00:00:0" + i, obj);
            }

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

        if (ss != null) {
            String address = StringUtil.tokenize(ss, "||").get(1).trim();
            connect(address);
        }
    }

    /**
     * Connect to a particular bluetooth device
     *
     * @param address
     */
    private void connect(String address) {
        bleAddress = address;

        if (bt == null) {
            String message = "BLE device not supported ...";
            print(message, false);

            // ***DEBUG CODE***
            enableButtons(true);
            connected = true;
            return;
        }

        if (!connected) {
            // start an infinite progress dialog
            final Dialog ip = new InfiniteProgress().showInifiniteBlocking();

            try {
                bt.connect(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        ip.dispose();

                        Object obj = evt.getSource();
                        print("Connected to Bluetooth LE device ...\n" + obj, true);
                        discover(); // must be called on Andriod. Won't do anything on ios though

                        connected = true;
                        enableButtons(true);
                    }

                }, address);
            } catch (IOException ex) {
                ip.dispose();

                String message = "Error connecting to bluetooth device: " + address;
                print(message + "\n" + ex.getMessage(), false);
            }
        } else {
            String message = "BLE device already connected to: " + address;
            print(message, false);
        }
    }

    /**
     * This method needs to be called on Android other wise the service is not
     * found
     */
    private void discover() {
        try {
            bt.discover(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    print("BLE Information Received ...", true);
                    addSubscriber();
                }

            }, bleAddress);

        } catch (Exception ex) {
            print(ex.getMessage(), false);
        }

        // if we running on is add the subscriber here since the above bt call
        // does nothing?
        if (Display.getInstance().getPlatformName().equals("ios")) {
            print("Adding subscriber for iOS Device", true);
            addSubscriber();
        }
    }

    /**
     * Method to listen for incoming data
     */
    private void addSubscriber() {
        try {
            bt.subscribe(new ActionListener() {
                StringBuilder sb = new StringBuilder();

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

                    if (message.endsWith("\r\n")) {
                        processData(sb.toString());
                        sb = new StringBuilder();
                    }
                }

            }, bleAddress, UUID_SERVICE, UUID_RX);

            String message = console.getText() + "\nSubcriber added ...";
            console.setText(message);
        } catch (IOException ex) {
            String message = "Error Subscribing: " + ex.getMessage();
            console.setText(message);
        }
    }

    private void processData(String data) {
        data = data.trim();

        // cheok what data we have
        if (data.indexOf("config") != -1) {
            JSONObject json = getJSON(data);
            processConfigData(json);
        } else if (data.indexOf("environment") != -1) {
            JSONObject json = getJSON(data);

            if (!getFile) {
                processStatusData(json);
            } else {
                storeStatusData(json);
            }
        } else {
            print("Data received: " + data, true);
        }
    }

    /**
     * Process config data from the asm or which was stored in preferences
     *
     * @param json
     */
    private void processConfigData(JSONObject json) {
        if (updateSetup) {
            updateSetup = false;

            try {
                findLocationTextField(mainForm).setText(json.getString("location"));
                findNameTextField(mainForm).setText(json.getString("hostname"));
                findSsidTextField(mainForm).setText(json.getString("wifi"));
                findPasswordTextField(mainForm).setText(json.getString("password"));
                findUrlTextField(mainForm).setText(json.getString("url"));
                findOnlineLabel(mainForm).setText("Online: " + json.getString("online").toUpperCase());

                // update the picker for settung the sync
                int sync = json.getInt("sync");
                Picker picker = findSyncPicker(mainForm);

                switch (sync) {
                    case 10:
                        picker.setSelectedStringIndex(0);
                        break;
                    case 15:
                        picker.setSelectedStringIndex(1);
                        break;
                    case 30:
                        picker.setSelectedStringIndex(2);
                        break;
                    case 60:
                        picker.setSelectedStringIndex(3);
                        break;
                    default:
                        picker.setSelectedStringIndex(0);
                        break;
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } else {
            print("Device Configured:\n" + json.toString(), false);
        }
    }

    /**
     * Process status data from the ASM sensor
     *
     * @param json
     */
    private void processStatusData(JSONObject json) {
        try {
            StringBuilder sb = new StringBuilder();
            JSONObject edata = json.getJSONObject("environment");
            JSONObject odata = json.getJSONObject("other");

            // get if the device is online and battery level
            sb.append(new Date()).append(" >>\n\n");
            sb.append("Online: ").append(json.getString("online").toUpperCase()).append("\n");
            sb.append("% Battery: ").append(odata.get("battery")).append("\n\n");

            // get environment data
            sb.append("Lux: ").append(edata.getJSONObject("light").get("lux")).append("\n");
            sb.append("Temp 1: ").append(edata.getJSONObject("air-1").get("temp")).append("\n");
            sb.append("Temp 2: ").append(edata.getJSONObject("air-2").get("temp")).append("\n");
            sb.append("% RH 1: ").append(edata.getJSONObject("air-1").get("humidity")).append("\n");
            sb.append("% RH 2: ").append(edata.getJSONObject("air-2").get("humidity")).append("\n");
            sb.append("CO2-K30: ").append(edata.getJSONObject("co2").get("k30")).append("\n");
            sb.append("CO2-MG811: ").append(edata.getJSONObject("co2").get("mg811")).append("\n\n");

            sb.append("analog_1: ").append(odata.get("analog_1")).append("\n");
            sb.append("analog_2: ").append(odata.get("analog_2")).append("\n");
            sb.append("analog_3: ").append(odata.get("analog_3")).append("\n");
            sb.append("analog_4: ").append(odata.get("analog_4")).append("\n");
            sb.append("gpio_1: ").append(odata.get("gpio_1")).append("\n");
            sb.append("gpio_2: ").append(odata.get("gpio_2")).append("\n");
            sb.append("i2c_1: ").append(odata.get("i2c_1")).append("\n");
            sb.append("i2c_2: ").append(odata.get("i2c_2")).append("\n");

            print(sb.toString(), false);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Store status data from the ASM sensor to local storage
     *
     * @param json
     */
    private void storeStatusData(JSONObject json) {
        try {
            // set the time in case the time was set on the device correctly
            Date now = new Date();
            Long time = now.getTime();
            json.put("now", time);

            String location = json.getString("location");
            String name = json.getString("hostname");
            if (onSimulator) {
                name += "_" + simulatorCount;
            }

            String key = location + "." + name;

            ASMData data = null;
            if (dataFiles.containsKey(key)) {
                data = dataFiles.get(key);
            } else {
                data = new ASMData(location, name);
                dataFiles.put(key, data);
            }

            data.add(json.toString(), time);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Update the data list UI
     */
    private void updateListEntry() {
        Display.getInstance().callSerially(new Runnable() {
            @Override
            public void run() {
                MultiList list = findDataMultiList(mainForm);
                DefaultListModel model = new DefaultListModel();

                SortedSet<String> keys = new TreeSet<>(dataFiles.keySet());
                for (String key : keys) {
                    ASMData data = dataFiles.get(key);

                    String name = data.toString();
                    String info = data.getDataRange();

                    Map<String, Object> entry = new HashMap<>();
                    entry.put("Line1", name);
                    entry.put("Line2", info);
                    model.addItem(entry);
                }

                list.setModel(model);
            }
        });
    }

    /**
     * Method to convert json string into json object
     *
     * @param data
     * @return
     */
    private JSONObject getJSON(String data) {
        try {
            return (new JSONObject(data));
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Method to read a file from the resource file
     *
     * @param filename
     * @return
     */
    private String readFileFromResource(String filename) {
        InputStream in = fetchResourceFile().getData(filename);
        try {
            String text = Util.readToString(in);
            in.close();
            return text;
        } catch (IOException ex) {
            return "File Read Error";
        }
    }

    /**
     * Send text but first then encode in base64 otherwise it doesn't work
     *
     * @param text
     */
    private void sendText(final String data) {

        // check to make sure we not running on simulator
        if (bt == null) {
            System.out.println("Data Sent: " + data);
            return;
        }

        try {
            String b64String = Base64.encode(data.getBytes());

            bt.write(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (data.endsWith("\r\n")) {
                        print("Data sent ...", true);
                    }
                }

            }, bleAddress, UUID_SERVICE, UUID_TX, b64String, false);
        } catch (IOException ex) {
            String message = "Error sending: " + data + "\n"
                    + UUID_SERVICE + "\n"
                    + UUID_TX + "\n"
                    + ex.getMessage();
            print(message, false);
        }
    }

    /**
     * Used for send the long text since apparent BLE you can only send 23 bytes
     * at a time. As such split ascii text into chunks of 20 characters to
     * prevent any issues
     *
     */
    private void splitAndSend(String text) {
        text += "\r\n";

        // first split data in chunk size of 20 chracters
        ArrayList<String> chunks = new ArrayList<>();

        char[] data = text.toCharArray();
        int len = data.length;
        int chunkSize = 20;

        for (int i = 0; i < len; i += chunkSize) {
            chunks.add(new String(data, i, Math.min(chunkSize, len - i)));
        }

        // now send chunks amd wait 100 ms to prevent any erros
        for (String chunk : chunks) {
            sendText(chunk);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    protected void onMain_UpdateButtonAction(Component c, ActionEvent event) {
        if (connected) {
            Date now = new Date();
            splitAndSend("SET TIME " + now.getTime());

            String text = findLocationTextField().getText();
            Preferences.set("location", text);
            splitAndSend("SET LOCATION " + text);

            text = findNameTextField().getText();
            Preferences.set("name", text);
            splitAndSend("SET NAME " + text);

            text = findSsidTextField().getText();
            Preferences.set("wifi", text);
            splitAndSend("SET WIFI " + text);

            text = findPasswordTextField().getText();
            Preferences.set("password", text);
            splitAndSend("SET PASSWORD " + text);

            text = findUrlTextField().getText();
            Preferences.set("url", text);
            splitAndSend("SET URL " + text);

            text = StringUtil.tokenize(findSyncPicker().getSelectedString(), " ").get(2);
            Preferences.set("sync", text);
            splitAndSend("SET SYNC " + text);

            // Sleep for a short time then send command to get status
            try {
                Thread.sleep(200);
                splitAndSend("GET CONFIG");
            } catch (InterruptedException ex) {
            }

            if (onSimulator) {
                processConfigData(configJSON);
            }
        }
    }

    @Override
    protected void exitMain(Form f) {
        super.exitMain(f);

        if (bt != null) {
            try {
                if (connected) {
                    bt.close(bleAddress);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onMain_DisconnectButtonAction(Component c, ActionEvent event) {
        if (connected) {
            try {
                if (bt != null) {
                    bt.close(bleAddress);
                }

                connected = false;
                enableButtons(false);

                console.setText("BLE Disconnected By User");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Print method to append to the console
     *
     * @param text
     */
    private void print(String text, boolean append) {
        Display.getInstance().callSerially(new Runnable() {
            @Override
            public void run() {
                if (append) {
                    String oldText = console.getText();
                    String newText = oldText + "\n" + text;

                    console.setText(newText);
                } else {
                    console.setText(text);
                }
            }
        });
    }

    @Override
    protected void onMain_AutoReadCheckBoxAction(Component c, ActionEvent event) {
        CheckBox cb = findAutoReadCheckBox();
        if (cb.isSelected()) {
            autoRead = true;
            print("Reading sensor data ...", false);

            // start thread to send data here every 5 seconds
            Thread thread = new Thread("Data Read Thread") {
                public void run() {
                    while (true) {
                        if (!autoRead) {
                            break;
                        }

                        // request data by send get status
                        splitAndSend("GET STATUS");

                        if (onSimulator) {
                            String jsonText = readFileFromResource("asm.json");
                            processData(jsonText);
                        }

                        try {
                            // sleep for 5 seconds before requesting more data
                            Thread.sleep(delayTime);
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                }
            };

            thread.start();
        } else {
            autoRead = false;
        }
    }

    @Override
    protected void onMain_ReadButtonAction(Component c, ActionEvent event) {
        updateSetup = true;
        splitAndSend("GET CONFIG");

        if (onSimulator) {
            String jsonText = readFileFromResource("config.json");
            processData(jsonText);
        }
    }

    @Override
    protected void onMain_GetFilesButtonAction(Component c, ActionEvent event) {
        // stop the auto read thread if it's active
        autoRead = false;
        getFile = true;

        findAutoReadCheckBox().setSelected(false);
        findGetFilesButton().setEnabled(false);

        final ToastBar.Status status = ToastBar.getInstance().createStatus();
        status.setMessage("Loading files ...");
        //status.setProgress(0);
        status.setShowProgressIndicator(true);
        status.show();

        Thread thread = new Thread("File Read Thread") {
            public void run() {
                int count = Integer.parseInt(findNumberOfFilesTextField().getText());
                for (int i = 1; i <= count; i++) {
                    if (!getFile) {
                        break;
                    }

                    splitAndSend("GET FILE " + i);
                    status.setMessage("Getting file " + i + " out of " + count);
                    status.show();

                    if (onSimulator) {
                        String jsonText = readFileFromResource("asm.json");
                        processData(jsonText);
                    }

                    // wait 5 seconds hopefully data was loaded by then
                    try {
                        Thread.sleep(delayTime);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                
                // save the hashmap to storage now
                //String key = dataFiles.keySet().toArray()[0].toString();
                Storage.getInstance().writeObject("dataFiles", dataFiles);
                System.out.println("Saving Data file object");
                
                getFile = false;
                status.clear();
                updateListEntry();
                findGetFilesButton().setEnabled(true);

                if (onSimulator) {
                    simulatorCount++;
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onMain_StopButtonAction(Component c, ActionEvent event) {
        getFile = false;
    }

    @Override
    protected void onMain_DeleteButtonAction(Component c, ActionEvent event) {
        Storage.getInstance().deleteStorageFile("dataFiles");
        dataFiles.clear();
        updateListEntry();
        System.out.println("Storage file deleted ..." + dataFiles.size());
    }
}
