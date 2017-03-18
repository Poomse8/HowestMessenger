package gcm.play.android.samples.com.gcmquickstart.models;


import android.content.Context;
import android.content.ContextWrapper;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by Nicol on 5/05/2016.
 */
public class Message implements Parcelable {

    private String messageTitle;
    private String messageText;
    private String messageAuteur;
    private String messageVak;
    private Date timeStamp;

    public Message(String title, String message, String auteur, int vak){
        setMessageTitle(title);
        setMessageText(message);
        setMessageAuteur(auteur);
        setTimeStamp(Calendar.getInstance().getTime());
        setMessageVak(Vakken.values()[vak].toString());
    }

    //GETTERS & SETTERS
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getMessageTitle() {
        return messageTitle;
    }
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
    public String getMessageAuteur() {
        return messageAuteur;
    }
    public void setMessageAuteur(String messageAuteur) {
        this.messageAuteur = messageAuteur;
    }
    public String getMessageVak() {
        return messageVak;
    }
    public void setMessageVak(String messageVak) {
        this.messageVak = messageVak;
    }
    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public static ArrayList<Message> Dummy(){
        ArrayList<Message> list = new ArrayList<>();

        Message test1 = new Message("Lessen", "Alle lessen geschrapt wegens mooi weer.", "Frederik Duchi", 5);
        Message test2 = new Message("SSA", "Theorie gaat door in Minor ipv Major.", "Dieter De Preester", 1);
        Message test3 = new Message("MAD Examen", "Examens gestolen - Iedereen noodgedwongen geslaagd.", "Stijn Walcarius", 0);

        list.add(test1);
        list.add(test2);
        list.add(test3);

        return list;
    }


    //parcelable ////////////////////////////////////////////
    //DO
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getMessageTitle());
        dest.writeString(this.getMessageText());
        dest.writeString(this.getMessageAuteur());
        dest.writeString((this.getMessageVak()));
        dest.writeLong(this.getTimeStamp().getTime());
    }
    //UNDO
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>(){

        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    protected Message(Parcel in){
        messageTitle = in.readString();
        messageText = in.readString();
        messageAuteur = in.readString();
        messageVak = in.readString();
        timeStamp = new Date(in.readLong());
    }

    //einde parcelable //////////////////////////////////////

    //categoryENUM
    public enum Vakken {
        MAD("Mobile App Developement"), SSA("Server Side Advanced"), Pro("Project"), Datacom("Datacom"), Datacenter("Datacenter Technology"), NewMedia("New Media"), VenA("Video en Audio"), Algemeen("Algemeen");
        private final String stringValue;
        Vakken(final String s) { stringValue = s; }
        public String toString() { return stringValue; }
    }


}
