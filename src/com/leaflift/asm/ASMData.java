package com.leaflift.asm;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Store json data from asm sensor so it can be saved to phone
 * @author nathan
 */
public class ASMData implements Externalizable {
   private static final int VERSION = 1;
   
   private String location;
   private String nodeName;
   private List<String> dataList = new ArrayList<>();
   private Long minDate = 0L;
   private Long maxDate = 0L;
   
   public ASMData() {
       location = "GRH001";
       nodeName = "Node1A";
   }
   
   public ASMData(String location, String nodeName) {
       this.location = location;
       this.nodeName = nodeName;
   }
   
   public int getFileCount() {
       return dataList.size();
   }
   
   public String getDataRange() {
       Date min = new Date(minDate);
       Date max = new Date(maxDate);
       return min.toString() + " - " + max.toString();
   }
   
   public void add(String jsonText, Long date) {
       if(minDate == 0 && maxDate == 0) {
           minDate = date;
           maxDate = date;
       } else if(date < minDate) {
           minDate = date;
       } else if(date > maxDate) {
           maxDate = date;
       }
       
       dataList.add(jsonText);
   }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public void externalize(DataOutputStream out) throws IOException {
        Util.writeUTF(location, out);
        Util.writeUTF(nodeName, out);
        Util.writeObject(dataList, out);
        Util.writeObject(minDate, out);
        Util.writeObject(maxDate, out);
    }

    @Override
    public void internalize(int version, DataInputStream in) throws IOException {
        location = Util.readUTF(in);
        nodeName = Util.readUTF(in);
        dataList = (List<String>) Util.readObject(in);
        minDate = (Long)Util.readObject(in);
        maxDate = (Long)Util.readObject(in);
    }

    @Override
    public String getObjectId() {
        return "ASMData";
    }
    
    @Override
    public String toString() {
        return location + "." + nodeName + " (" + getFileCount() + ")";
    }
}
