package com.example.plantmamaapp_v2;

public class Plant {

    //plant variables
    String plantID, plantName, dateAquired, plantProfilePic, plantHeight, plantType, careNotes, plantDescription, userEmail;


    //plant constructor
    public Plant(String plantID, String plantName, String dateAquired, String plantProfilePic, String plantHeight, String plantType, String careNotes, String plantDescription, String userEmail) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.dateAquired = dateAquired;
        this.plantProfilePic = plantProfilePic;
        this.plantHeight = plantHeight;
        this.plantType = plantType;
        this.careNotes = careNotes;
        this.plantDescription = plantDescription;
        this.userEmail = userEmail;
    }


    //getter and setters for variables
    public String getPlantID() {
        return plantID;
    }


    public void setPlantID(String plantID) {
        this.plantID = plantID;
    }


    public String getPlantName() {
        return plantName;
    }


    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }


    public String getDateAquired() {
        return dateAquired;
    }


    public void setDateAquired(String dateAquired) {
        this.dateAquired = dateAquired;
    }


    public String getPlantProfilePic() {
        return plantProfilePic;
    }


    public void setPlantProfilePic(String plantProfilePic) {
        this.plantProfilePic = plantProfilePic;
    }


    public String getPlantHeight() {
        return plantHeight;
    }


    public void setPlantHeight(String plantHeight) {
        this.plantHeight = plantHeight;
    }


    public String getPlantType() {
        return plantType;
    }


    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }


    public String getCareNotes() {
        return careNotes;
    }


    public void setCareNotes(String careNotes) {
        this.careNotes = careNotes;
    }


    public String getPlantDescription() {
        return plantDescription;
    }


    public void setPlantDescription(String plantDescription) {
        this.plantDescription = plantDescription;
    }


    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
