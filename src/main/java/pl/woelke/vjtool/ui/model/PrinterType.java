package pl.woelke.vjtool.ui.model;

public enum PrinterType {
    VJ15X0("1510"),
    VJ16X0("1610"),
    VJ1580("1580"),
    VJ1880("1880"),
    VJ6530("6530"),
    VJ9550("9550");

    final String printerModel;

    PrinterType(String printerModel) {
        this.printerModel = printerModel;
    }
    public String getPrinterType(){
        return printerModel;
    }
}