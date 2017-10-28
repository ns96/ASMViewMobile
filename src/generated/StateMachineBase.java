/**
 * This class contains generated code from the Codename One Designer, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://codenameone.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.codename1.ui.*;
import com.codename1.ui.util.*;
import com.codename1.ui.plaf.*;
import java.util.Hashtable;
import com.codename1.ui.events.*;

public abstract class StateMachineBase extends UIBuilder {
    private Container aboutToShowThisContainer;
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    /**
    * @deprecated use the version that accepts a resource as an argument instead
    
**/
    protected void initVars() {}

    protected void initVars(Resources res) {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        UIBuilder.registerCustomComponent("Table", com.codename1.ui.table.Table.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("Picker", com.codename1.ui.spinner.Picker.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("Tabs", com.codename1.ui.Tabs.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    if(resPath.endsWith(".res")) {
                        res = Resources.open(resPath);
                        System.out.println("Warning: you should construct the state machine without the .res extension to allow theme overlays");
                    } else {
                        res = Resources.openLayered(resPath);
                    }
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        if(res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            initVars(res);
            return showForm(getFirstFormName(), null);
        } else {
            Form f = (Form)createContainer(resPath, getFirstFormName());
            initVars(fetchResourceFile());
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    protected String getFirstFormName() {
        return "Main";
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        UIBuilder.registerCustomComponent("Table", com.codename1.ui.table.Table.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("Picker", com.codename1.ui.spinner.Picker.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("Tabs", com.codename1.ui.Tabs.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.openLayered(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        return createContainer(resPath, "Main");
    }

    protected void initTheme(Resources res) {
            String[] themes = res.getThemeResourceNames();
            if(themes != null && themes.length > 0) {
                UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
            }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.codename1.ui.TextField findPasswordTextField(Component root) {
        return (com.codename1.ui.TextField)findByName("passwordTextField", root);
    }

    public com.codename1.ui.TextField findPasswordTextField() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("passwordTextField", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("passwordTextField", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findSensorTextArea(Component root) {
        return (com.codename1.ui.TextArea)findByName("sensorTextArea", root);
    }

    public com.codename1.ui.TextArea findSensorTextArea() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("sensorTextArea", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("sensorTextArea", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.spinner.Picker findAddressPicker(Component root) {
        return (com.codename1.ui.spinner.Picker)findByName("addressPicker", root);
    }

    public com.codename1.ui.spinner.Picker findAddressPicker() {
        com.codename1.ui.spinner.Picker cmp = (com.codename1.ui.spinner.Picker)findByName("addressPicker", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.spinner.Picker)findByName("addressPicker", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer3(Component root) {
        return (com.codename1.ui.Container)findByName("Container3", root);
    }

    public com.codename1.ui.Container findContainer3() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container3", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container3", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findSsidTextField(Component root) {
        return (com.codename1.ui.TextField)findByName("ssidTextField", root);
    }

    public com.codename1.ui.TextField findSsidTextField() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("ssidTextField", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("ssidTextField", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer4(Component root) {
        return (com.codename1.ui.Container)findByName("Container4", root);
    }

    public com.codename1.ui.Container findContainer4() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container4", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container4", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer1(Component root) {
        return (com.codename1.ui.Container)findByName("Container1", root);
    }

    public com.codename1.ui.Container findContainer1() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer2(Component root) {
        return (com.codename1.ui.Container)findByName("Container2", root);
    }

    public com.codename1.ui.Container findContainer2() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container2", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container2", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer5(Component root) {
        return (com.codename1.ui.Container)findByName("Container5", root);
    }

    public com.codename1.ui.Container findContainer5() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container5", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container5", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findPasswordLabel(Component root) {
        return (com.codename1.ui.Label)findByName("passwordLabel", root);
    }

    public com.codename1.ui.Label findPasswordLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("passwordLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("passwordLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findUrlLabel(Component root) {
        return (com.codename1.ui.Label)findByName("urlLabel", root);
    }

    public com.codename1.ui.Label findUrlLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("urlLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("urlLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer(Component root) {
        return (com.codename1.ui.Container)findByName("Container", root);
    }

    public com.codename1.ui.Container findContainer() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.spinner.Picker findSyncPicker(Component root) {
        return (com.codename1.ui.spinner.Picker)findByName("syncPicker", root);
    }

    public com.codename1.ui.spinner.Picker findSyncPicker() {
        com.codename1.ui.spinner.Picker cmp = (com.codename1.ui.spinner.Picker)findByName("syncPicker", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.spinner.Picker)findByName("syncPicker", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findReadButton(Component root) {
        return (com.codename1.ui.Button)findByName("readButton", root);
    }

    public com.codename1.ui.Button findReadButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("readButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("readButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.table.Table findDataFilesTable(Component root) {
        return (com.codename1.ui.table.Table)findByName("dataFilesTable", root);
    }

    public com.codename1.ui.table.Table findDataFilesTable() {
        com.codename1.ui.table.Table cmp = (com.codename1.ui.table.Table)findByName("dataFilesTable", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.table.Table)findByName("dataFilesTable", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findUpdateButton(Component root) {
        return (com.codename1.ui.Button)findByName("updateButton", root);
    }

    public com.codename1.ui.Button findUpdateButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("updateButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("updateButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Tabs findTabs1(Component root) {
        return (com.codename1.ui.Tabs)findByName("Tabs1", root);
    }

    public com.codename1.ui.Tabs findTabs1() {
        com.codename1.ui.Tabs cmp = (com.codename1.ui.Tabs)findByName("Tabs1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Tabs)findByName("Tabs1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findNameLabel(Component root) {
        return (com.codename1.ui.Label)findByName("nameLabel", root);
    }

    public com.codename1.ui.Label findNameLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("nameLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("nameLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findDisconnectButton(Component root) {
        return (com.codename1.ui.Button)findByName("disconnectButton", root);
    }

    public com.codename1.ui.Button findDisconnectButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("disconnectButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("disconnectButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findUrlTextField(Component root) {
        return (com.codename1.ui.TextField)findByName("urlTextField", root);
    }

    public com.codename1.ui.TextField findUrlTextField() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("urlTextField", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("urlTextField", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findNameTextField(Component root) {
        return (com.codename1.ui.TextField)findByName("nameTextField", root);
    }

    public com.codename1.ui.TextField findNameTextField() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("nameTextField", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("nameTextField", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findDataLabel(Component root) {
        return (com.codename1.ui.Label)findByName("dataLabel", root);
    }

    public com.codename1.ui.Label findDataLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("dataLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("dataLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.CheckBox findAutoReadCheckBox(Component root) {
        return (com.codename1.ui.CheckBox)findByName("autoReadCheckBox", root);
    }

    public com.codename1.ui.CheckBox findAutoReadCheckBox() {
        com.codename1.ui.CheckBox cmp = (com.codename1.ui.CheckBox)findByName("autoReadCheckBox", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.CheckBox)findByName("autoReadCheckBox", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLocationLabel(Component root) {
        return (com.codename1.ui.Label)findByName("locationLabel", root);
    }

    public com.codename1.ui.Label findLocationLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("locationLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("locationLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findLocationTextField(Component root) {
        return (com.codename1.ui.TextField)findByName("locationTextField", root);
    }

    public com.codename1.ui.TextField findLocationTextField() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("locationTextField", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("locationTextField", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findSsidLabel(Component root) {
        return (com.codename1.ui.Label)findByName("ssidLabel", root);
    }

    public com.codename1.ui.Label findSsidLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("ssidLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("ssidLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findFilesButton(Component root) {
        return (com.codename1.ui.Button)findByName("filesButton", root);
    }

    public com.codename1.ui.Button findFilesButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("filesButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("filesButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findScanButton(Component root) {
        return (com.codename1.ui.Button)findByName("scanButton", root);
    }

    public com.codename1.ui.Button findScanButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("scanButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("scanButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findPostButton(Component root) {
        return (com.codename1.ui.Button)findByName("postButton", root);
    }

    public com.codename1.ui.Button findPostButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("postButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("postButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    protected void exitForm(Form f) {
        if("Main".equals(f.getName())) {
            exitMain(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void exitMain(Form f) {
    }

    protected void beforeShow(Form f) {
    aboutToShowThisContainer = f;
        if("Main".equals(f.getName())) {
            beforeMain(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeMain(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        aboutToShowThisContainer = c;
        if("Main".equals(c.getName())) {
            beforeContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeContainerMain(Container c) {
    }

    protected void postShow(Form f) {
        if("Main".equals(f.getName())) {
            postMain(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postMain(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("Main".equals(c.getName())) {
            postContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postContainerMain(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("Main".equals(rootName)) {
            onCreateMain();
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void onCreateMain() {
    }

    protected Hashtable getFormState(Form f) {
        Hashtable h = super.getFormState(f);
        if("Main".equals(f.getName())) {
            getStateMain(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

            return h;
    }


    protected void getStateMain(Form f, Hashtable h) {
    }

    protected void setFormState(Form f, Hashtable state) {
        super.setFormState(f, state);
        if("Main".equals(f.getName())) {
            setStateMain(f, state);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void setStateMain(Form f, Hashtable state) {
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        Container leadParentContainer = c.getParent().getLeadParent();
        if(leadParentContainer != null && leadParentContainer.getClass() != Container.class) {
            c = c.getParent().getLeadParent();
        }
        if(rootContainerName == null) return;
        if(rootContainerName.equals("Main")) {
            if("scanButton".equals(c.getName())) {
                onMain_ScanButtonAction(c, event);
                return;
            }
            if("addressPicker".equals(c.getName())) {
                onMain_AddressPickerAction(c, event);
                return;
            }
            if("locationTextField".equals(c.getName())) {
                onMain_LocationTextFieldAction(c, event);
                return;
            }
            if("nameTextField".equals(c.getName())) {
                onMain_NameTextFieldAction(c, event);
                return;
            }
            if("ssidTextField".equals(c.getName())) {
                onMain_SsidTextFieldAction(c, event);
                return;
            }
            if("passwordTextField".equals(c.getName())) {
                onMain_PasswordTextFieldAction(c, event);
                return;
            }
            if("urlTextField".equals(c.getName())) {
                onMain_UrlTextFieldAction(c, event);
                return;
            }
            if("syncPicker".equals(c.getName())) {
                onMain_SyncPickerAction(c, event);
                return;
            }
            if("readButton".equals(c.getName())) {
                onMain_ReadButtonAction(c, event);
                return;
            }
            if("updateButton".equals(c.getName())) {
                onMain_UpdateButtonAction(c, event);
                return;
            }
            if("disconnectButton".equals(c.getName())) {
                onMain_DisconnectButtonAction(c, event);
                return;
            }
            if("sensorTextArea".equals(c.getName())) {
                onMain_SensorTextAreaAction(c, event);
                return;
            }
            if("autoReadCheckBox".equals(c.getName())) {
                onMain_AutoReadCheckBoxAction(c, event);
                return;
            }
            if("filesButton".equals(c.getName())) {
                onMain_FilesButtonAction(c, event);
                return;
            }
            if("postButton".equals(c.getName())) {
                onMain_PostButtonAction(c, event);
                return;
            }
        }
    }

      protected void onMain_ScanButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_AddressPickerAction(Component c, ActionEvent event) {
      }

      protected void onMain_LocationTextFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_NameTextFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_SsidTextFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_PasswordTextFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_UrlTextFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_SyncPickerAction(Component c, ActionEvent event) {
      }

      protected void onMain_ReadButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_UpdateButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_DisconnectButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_SensorTextAreaAction(Component c, ActionEvent event) {
      }

      protected void onMain_AutoReadCheckBoxAction(Component c, ActionEvent event) {
      }

      protected void onMain_FilesButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_PostButtonAction(Component c, ActionEvent event) {
      }

}
