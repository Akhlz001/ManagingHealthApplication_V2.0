package com.example.managinghealthapplicationv1;

public class MedicalInfo {

    //CLASS CREATED FOR USE IN 'AddMedicalIDActivity' TO ALLOW SIMPLER AND MORE ORGANISED CODE WHERE DATA IS SENT TO A FIREBASE REAL-TIME DATABASE

    //variables declared

    private String Name, Bloodtype, Medcondition, Medreaction, Medmedication;
    private Integer Age;
    private Float Height, Weight;

    public MedicalInfo() {
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBloodtype() {
        return Bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        Bloodtype = bloodtype;
    }

    public String getMedcondition() {
        return Medcondition;
    }

    public void setMedcondition(String medcondition) {
        Medcondition = medcondition;
    }

    public String getMedreaction() {
        return Medreaction;
    }

    public void setMedreaction(String medreaction) {
        Medreaction = medreaction;
    }

    public String getMedmedication() {
        return Medmedication;
    }

    public void setMedmedication(String medmedication) {
        Medmedication = medmedication;
    }

    public Float getHeight() {
        return Height;
    }

    public void setHeight(Float height) {
        Height = height;
    }

    public Float getWeight() {
        return Weight;
    }

    public void setWeight(Float weight) {
        Weight = weight;
    }
}
