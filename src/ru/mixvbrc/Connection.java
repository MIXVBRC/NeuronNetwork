package ru.mixvbrc;

class Connection {

    private double weight;

    Connection(boolean bias)
    {
        this.weight = bias ? 1 : NetworkMath.getRandom(-0.9,0.9);
    }

    public double getWeight() { return this.weight; }
    public void setWeight(double weight) { this.weight = weight; }
}