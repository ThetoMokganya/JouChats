package com.illinodes.jouchats.Model;

public class User
{
    //Declarations
    private String mId, mUsername, mImageURL, mStatus;

    public User(String id, String username, String imageURL, String status)
    {
        mId = id;
        mUsername = username;
        mImageURL = imageURL;
        mStatus = status;
    }

    public User()
    {
    }

    public String getId()
    {
        return mId;
    }

    public void setId(String id)
    {
        mId = id;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public void setUsername(String username)
    {
        mUsername = username;
    }

    public String getImageURL()
    {
        return mImageURL;
    }

    public void setImageURL(String imageURL)
    {
        mImageURL = imageURL;
    }

    public String getStatus()
    {
        return mStatus;
    }

    public void setStatus(String status)
    {
        mStatus = status;
    }
}
