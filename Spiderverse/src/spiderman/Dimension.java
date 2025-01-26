package spiderman;

import java.util.ArrayList;

public class Dimension {
    public int dimensionNumber;
    private int canonEvents;
    private int dimensionWeight;
    private ArrayList<Spiderverse> residents;

    public Dimension(int dimensionNumber, int canonEvents, int dimensionWeight) {
        this.dimensionNumber = dimensionNumber;
        this.canonEvents = canonEvents;
        this.dimensionWeight = dimensionWeight;
        this.residents = new ArrayList<>();
    }

    public void addResident(Spiderverse person) {
        residents.add(person);
    }

    public int getDimensionNumber() {
        return dimensionNumber;
    }

    public void setDimensionNumber(int dimensionNumber) {
        this.dimensionNumber = dimensionNumber;
    }

    public int getCanonEvents() {
        return canonEvents;
    }

    public void setCanonEvents(int canonEvents) {
        this.canonEvents = canonEvents;
    }

    public int getDimensionWeight() {
        return dimensionWeight;
    }

    public void setDimensionWeight(int dimensionWeight) {
        this.dimensionWeight = dimensionWeight;
    }

    public ArrayList<Spiderverse> getResidents() {
        return residents;
    }

    @Override
    public String toString() {
        return "Dimension{" + "dimensionNumber=" + dimensionNumber + ", canonEvents=" + canonEvents + ", dimensionWeight=" + dimensionWeight + ", residents=" + residents + '}';
    }
}