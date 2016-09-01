package com.example.mastermind.praktikumandroid;

import com.example.mastermind.praktikumandroid.ical.CalEntry;
import com.example.mastermind.praktikumandroid.rss.RssEntry;

/**
 * Created by Acer on 29-Aug-16.
 */
public class EntryContainer {

    protected Entry slot;

    protected static final int flagRssEntry = 1;
    protected static final int flagICalEntry = 2;
    protected int slotFlag;



    public Entry getSlot() {
        return slot;
    }

    public void setSlot(RssEntry rssSlot) {
        this.slot = rssSlot;
        this.setSlotFlag(flagRssEntry);
    }

    public void setSlot(CalEntry iCalSlot) {
        this.slot = iCalSlot;
        this.setSlotFlag(flagICalEntry);
    }


    protected int getSlotFlag() {
        return slotFlag;
    }

    protected void setSlotFlag(int slotFlag) {
        this.slotFlag = slotFlag;
    }



    public EntryContainer(RssEntry entry){
        this.setSlot(entry);
    }

    public EntryContainer(CalEntry entry){
        this.setSlot(entry);
    }
}
