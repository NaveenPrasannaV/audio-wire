package com.tdm.audiowire;

public class AutovoiceCommand {

    public static final String PING = "PING";
    public static final String LOAD = "OP_AUTOVOICE_LOAD";
    public static final String PLAY_PRE = "OP_AUTOVOICE_PLAY_PRE";
    public static final String PLAY_POST = "OP_AUTOVOICE_PLAY_POST";
    public static final String UNLOAD = "OP_AUTOVOICE_UNLOAD";
    public static final String EXIT = "OP_AUTOVOCIE_EXIT";

    private AutovoiceCommand() {
        //Prevent Object creation
    }

}
