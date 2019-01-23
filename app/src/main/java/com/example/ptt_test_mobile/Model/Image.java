package com.example.ptt_test_mobile.Model;

public class Image {
    private int image_id;
    private byte[] image;
    private int user_id;

    public Image(){};

    public Image(int image_id, byte[] image, int user_id) {
        this.image_id = image_id;
        this.image = image;
        this.user_id = user_id;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
