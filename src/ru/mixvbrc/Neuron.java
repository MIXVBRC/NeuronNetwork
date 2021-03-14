package ru.mixvbrc;

import java.util.HashMap;
import java.util.Map;

class Neuron {

    private boolean input = false;
    private boolean bias = false;

    private double data;
    private double error;
    private double answer;
    private double speed;

    HashMap<Neuron,Connection> iConn = new HashMap<>();
    HashMap<Neuron,Connection> oConn = new HashMap<>();

    public void setNeuronInputData()
    {
        if (bias) return;

        // Set new neuron data
        this.data = 0;

        for (Map.Entry<Neuron,Connection> iConn : this.iConn.entrySet())
        {
            this.data += iConn.getKey().getData() * iConn.getValue().getWeight();
        }

        this.data = NetworkMath.sigmoid(this.data);
    }

    public void setConnectionsWeight()
    {
        // Calculate error neuron in hidden layers
        if (!input)
        {
            this.error = 0;

            for (Map.Entry<Neuron,Connection> oConn : oConn.entrySet())
            {
                if (oConn.getKey().bias) continue;
                this.error += oConn.getValue().getWeight() * oConn.getKey().getError();
            }

            this.error *= NetworkMath.derivativeSigmoid(this.data);
        }

        // Calculate and set new weight from output connections
        for (Map.Entry<Neuron,Connection> oConn : oConn.entrySet())
        {
            oConn.getValue().setWeight(oConn.getValue().getWeight() + this.speed * oConn.getKey().getError() * this.data);
        }
    }

    public double getData() { return this.data; }
    public void setData(double data) { this.data = data; }

    public double getAnswer() { return this.answer; }
    public void setAnswer(double answer) { this.answer = answer; }

    public double getError() { return this.error; }
    public void setError(double error) { this.error = error; }

    public double getSpeed() { return this.speed; }
    public void setSpeed(double epsilon) { this.speed = epsilon; }

    public boolean isBias() { return this.bias; }
    public void setBias() { this.bias = true; }

    public boolean isInput() { return this.input; }
    public void setInput() { this.input = true; }

    public void addInputConnections(Neuron neuron, Connection connection) { this.iConn.put(neuron, connection); }
    public void addOutputConnections(Neuron neuron, Connection connection) { this.oConn.put(neuron, connection); }

    public Connection getInputConnection(Neuron neuron) { return this.iConn.get(neuron); }
    public Connection getOutputConnection(Neuron neuron) { return this.oConn.get(neuron); }


}
