package de.uulm.sp.pvs.exceptions;

/**
 * crated a own exception so i dont have to use 100 different ones
 * */
public class SokobanException extends Exception{

    public SokobanException(String msg){super(msg);}
    public SokobanException(String msg, Throwable cause){super(msg,cause);}
}
