package com.egs.wogal.camera_app.For_Sale_Object;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by wogal on 3/5/2017.
 */

public class F_S_ObjectClass implements Serializable {

    private String saleItemName = "";
    private UUID uniqueId;
    private List<Bitmap> imageList;
    private List<String> voiceTagList;
    private Date startDateOfPost;

    public F_S_ObjectClass(List<Bitmap> imageList, String saleItemName, Date startDateOfPost, UUID uniqueId, List<String> voiceTagList) {
        this.imageList = imageList;
        this.saleItemName = saleItemName;
        this.startDateOfPost = startDateOfPost;
        this.uniqueId = uniqueId;
        this.voiceTagList = voiceTagList;
    }

    public List<Bitmap> getImageList() {
        return imageList;
    }

    public String getSaleItemName() {
        return saleItemName;
    }

    public Date getStartDateOfPost() {
        return startDateOfPost;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public List<String> getVoiceTagList() {
        return voiceTagList;
    }
}
