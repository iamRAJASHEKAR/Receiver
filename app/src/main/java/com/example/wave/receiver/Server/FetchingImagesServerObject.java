package com.example.wave.receiver.Server;

import com.example.wave.receiver.ModelClasses.ImageUrls;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mahi on 3/22/2018.
 */

public class FetchingImagesServerObject {


    public ArrayList<ImageUrls> getUserurls() {
        return userurls;
    }

    public void setUserurls(ArrayList<ImageUrls> userurls) {
        this.userurls = userurls;
    }

    @SerializedName("user images")
    public ArrayList<ImageUrls> userurls;
    //public List<ImageUrls> userurls;
    /*public String[] getUserimages()
    {
        return userimages;
    }*/

    public void setUserimages(JSONObject userimages) {
        this.userimages = userimages;
    }

    @SerializedName("userimages")
    private JSONObject userimages;


    //for registration
    @SerializedName("regdetails")
    private JSONObject registrationdetails;

    //for login
    @SerializedName("logindetails")
    private JSONObject logindetails;


    //for forgot password
    @SerializedName("emailaddress")

    private JSONObject emailaddress;


    //for change password
    @SerializedName("changepassworddetails")
    private JSONObject changepassworddetails;

    // for feedback
    @SerializedName("feedback")
    private JSONObject feedback;

    public JSONObject getFeedback() {
        return feedback;
    }

    public void setFeedback(JSONObject feedback) {
        this.feedback = feedback;
    }

    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("UserName")
    private String username;

    @SerializedName("EmailAddress")
    private String emailid;

    @SerializedName("Country")
    private String country;

    @SerializedName("id")
    private String id;
    @SerializedName("version")
    private String version;
    @SerializedName("URL")
    private String URL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("FirstName")
    private String firstname;
    @SerializedName("LastName")
    private String lastname;
    @SerializedName("Password")
    private String password;

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public JSONObject getRegistrationdetails() {
        return registrationdetails;
    }

    public void setRegistrationdetails(JSONObject registrationdetails) {
        this.registrationdetails = registrationdetails;
    }

    public JSONObject getLogindetails() {
        return logindetails;
    }

    public void setLogindetails(JSONObject logindetails) {
        this.logindetails = logindetails;
    }

    public JSONObject getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(JSONObject emailaddress) {
        this.emailaddress = emailaddress;
    }

    public JSONObject getChangepassworddetails() {
        return changepassworddetails;
    }

    public void setChangepassworddetails(JSONObject changepassworddetails) {
        this.changepassworddetails = changepassworddetails;
    }
}
