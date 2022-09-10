package pl.woelke.vjtool.ui.model;

public enum ProtocolType {
    WSI("WSI"),
    ESI("ESI"),
    ZIPHER("ZIPHER");

    final String protocolType;

    ProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }
    public String getProtocolType(){
        return protocolType;
    }
}