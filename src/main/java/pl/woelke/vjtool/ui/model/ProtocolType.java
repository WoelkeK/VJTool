package pl.woelke.vjtool.ui.model;

public enum ProtocolType {
    WSI("wsi"),
    ESI("esi"),
    ZIPHER("zipher");

    final String protocolType;

    ProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }
    public String getProtocolType(){
        return protocolType;
    }
}