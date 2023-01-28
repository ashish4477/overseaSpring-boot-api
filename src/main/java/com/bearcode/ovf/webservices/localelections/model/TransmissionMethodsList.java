package com.bearcode.ovf.webservices.localelections.model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by dhughes 08/31/2016.
 */
public class TransmissionMethodsList implements Serializable {
    private static final long serialVersionUID = 8206997939092475610L;
    private long id = 0;
    private VoterType voterType;
    private String additionalInfo;
    private String additionalInfoType;
    private List<TransmissionMethodsListItem> items;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public VoterType getVoterType(){
        return voterType;
    }

    public void setVoterType(VoterType voterType){
        this.voterType = voterType;
    }

    public String getAdditionalInfo(){
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo){
        this.additionalInfo = additionalInfo;
    }

    public String getAdditionalInfoType(){
        return additionalInfoType;
    }

    public void setAdditionalInfoType(String additionalInfoType){
        this.additionalInfoType = additionalInfoType;
    }

    public List<TransmissionMethodsListItem> getItems(){
        return items;
    }

    public void setItems(List<TransmissionMethodsListItem> items){
        this.items = items;
    }

    /**
     * Groups the items list by DocumentType.
     *
     * @return TreeMap. A Map where each key is a DocumentType.name and each value is a Map where
     * each key is a DocumentTransmissionMethod.name and each value is a boolean indicating whether
     * the DocumentTransmissionMethod method is applicable for the DocumentType.
     */
    public Map getGroupedItems() {
        Map<String,TreeMap<String,Boolean>> ret = new LinkedHashMap<String, TreeMap<String, Boolean>>();
        Collections.sort( items, new Comparator<TransmissionMethodsListItem>() {
            @Override
            public int compare( TransmissionMethodsListItem o1, TransmissionMethodsListItem o2 ) {
                return Double.compare( o1.getDocumentType().getId(), o2.getDocumentType().getId() );
            }
        } );
            for ( TransmissionMethodsListItem item : items){
                String key2 = item.getDocumentType().getName();
                if(ret.get(key2) == null){
                    ret.put(key2, new TreeMap<String, Boolean>());
                }
                ret.get(key2).put(item.getDocumentTransmissionMethod().getName(), item.isAllowed());
            }
        return ret;
    }

}
