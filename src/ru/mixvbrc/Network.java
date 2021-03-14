package ru.mixvbrc;

import java.util.ArrayList;

public class Network {

    {
        this.speed = 1;
        this.globalError = 0;
        this.warning = false;
    }

    private ArrayList<Double> localErrorList = new ArrayList<>();

    private double globalError;
    private double localError;

    private double speed;
    private boolean warning;

    double minData;
    double maxData;
    double minNormalize;
    double maxNormalize;

    private boolean showMessages = true;

    private final ArrayList<ArrayList<Neuron>> network = new ArrayList<>();

    Network(int[] layers, boolean biasActive)
    {

        // Layers and neurons generation
        for (int l = 0; l < layers.length; l++)
        {
            int biasNeuron = l < layers.length-1 && biasActive ? 1 : 0;

            ArrayList<Neuron> neuronsLayer = new ArrayList<>();

            for (int n = 0; n < layers[l] + biasNeuron; n++)
            {
                Neuron neuron = new Neuron();
                neuron.setSpeed(speed);

                // Inputs neurons
                if (l == 0) neuron.setInput();

                // Bias neurons
                if (n >= layers[l] && biasActive)
                {
                    neuron.setBias();
                    neuron.setData(1);
                }

                neuronsLayer.add(neuron);
            }

            this.network.add(neuronsLayer);
        }

        // Connections generate
        for (int l = 0; l < this.network.size(); l++)
        {
            for (int n = 0; n < this.network.get(l).size(); n++)
            {
                if (l < this.network.size()-1)
                {
                    // Generate output connections
                    for (Neuron nextLayerNeuron : this.network.get(l+1))
                    {
                        this.network.get(l).get(n).addOutputConnections(nextLayerNeuron, new Connection(this.network.get(l).get(n).isBias()));
                    }
                }

                if (l > 0)
                {
                    // Get output connections in previous neurons and put them in input connection list (map)
                    for (Neuron previousLayerNeuron : network.get(l-1))
                    {
                        if (!network.get(l).get(n).isBias())
                            this.network.get(l).get(n).addInputConnections(previousLayerNeuron, previousLayerNeuron.getOutputConnection(network.get(l).get(n)));
                    }
                }
            }
        }

        sendMessage("\nNetwork assembled!\n");
    }

    public boolean learn(double[][] inputsData, double[][] answersData, int cycles)
    {
        if (this.warning) return false;

        this.globalError = 0;

        for (int c = 0; c < cycles; c++)
        {
            localError = 0;

            for (int i = 0; i < inputsData.length; i++)
            {
                if (!setInputsData(inputsData[i]) || !setAnswer(answersData[i])) return false;

                setOutputNeuronsData();
                setInputError();
                backPropagation();
            }

            this.localErrorList.add(localError);

            System.out.println(this.localError);
        }

        this.globalError /= cycles;



        return true;
    }

    public boolean setInputsData(double[] inputsData)
    {
        int inputsQuantity = network.get(0).size() - 1;
        if (inputsData.length == inputsQuantity)
        {
            for (int n = 0; n < inputsData.length; n++)
            {
                network.get(0).get(n).setData(NetworkMath.normalizeData(inputsData[n],this.minData, this.maxData, this.minNormalize, this.maxNormalize));
            }

            this.warning = false;
            return true;
        } else {
            if (!this.warning) sendMessage("\nError: Array of inputs data quantity ["+inputsData.length+"] not equals neurons quantity ["+inputsQuantity+"] in first layer!\n");
            this.warning = true;
            return false;
        }
    }

    private boolean setAnswer(double[] outputsAnswers)
    {
        int outputsQuantity = network.get(network.size()-1).size();
        if (outputsAnswers.length == outputsQuantity)
        {
            for (int n = 0; n < outputsAnswers.length; n++)
            {
                network.get(network.size()-1).get(n).setAnswer(NetworkMath.normalizeData(outputsAnswers[n],this.minData, this.maxData, this.minNormalize, this.maxNormalize));
            }

            this.warning = false;
            return true;
        } else {
            if (!this.warning) sendMessage("\nError: Array of output answers data quantity ["+outputsAnswers.length+"] not equals neurons quantity ["+outputsQuantity+"] in last output layer!\n");
            this.warning = true;
            return false;
        }
    }

    public void setOutputNeuronsData()
    {
        for (int l = 1; l < network.size(); l++)
        {
            for (Neuron neuron : network.get(l))
            {
                neuron.setNeuronInputData();
            }
        }
    }

    public double[] getOutputsData()
    {
        if (this.warning) return new double[] {0};

        setOutputNeuronsData();

        double[] outputsData = new double[network.get(network.size()-1).size()];

        for (int n = 0; n < network.get(network.size()-1).size(); n++)
        {
            outputsData[n] = NetworkMath.backNormalizeData(network.get(network.size()-1).get(n).getData(),this.minData, this.maxData, this.minNormalize, this.maxNormalize);
        }

        return outputsData;
    }

    private void setInputError()
    {
        for (Neuron neuron : network.get(network.size()-1))
        {
            double error = ( neuron.getAnswer() - neuron.getData() );
            neuron.setError( error * NetworkMath.derivativeSigmoid(neuron.getData()) );

            localError += Math.abs(error);
            this.globalError += Math.abs(error);
        }
    }

    public boolean setInputError(double[] outputsErrorData)
    {
        int outputsQuantity = network.get(network.size()-1).size();
        if (outputsErrorData.length == outputsQuantity)
        {
            for (int i = 0; i < outputsErrorData.length; i++)
            {
                this.network.get(this.network.size()-1).get(i).setError(outputsErrorData[i]);
            }

            this.warning = false;
            return true;
        } else {
            if (!this.warning) sendMessage("\nError: Array of output errors data quantity ["+outputsErrorData.length+"] not equals neurons quantity ["+outputsQuantity+"] in last output layer!\n");
            this.warning = true;
            return false;
        }
    }

    // Back propagation
    private void backPropagation()
    {
        for (int l = network.size()-1; l > 1; l--)
        {
            for (Neuron neuron : network.get(l-1))
            {
                if (neuron.isBias()) continue;

                neuron.setConnectionsWeight();
            }
        }
    }

    private void sendMessage(String message) { if (this.showMessages) System.out.println(message); }

    public void setSpeed(double speed) { this.speed = speed; }

    public double getGlobalError() { return this.globalError; }

    public ArrayList<Double> getLocalErrorList() { return this.localErrorList; }

    public void showMessages(boolean showMessages) { this.showMessages = showMessages; }

    public void setMinData(double min) { this.minData = min; }
    public void setMaxData(double max) { this.maxData = max; }
    public void setMinNormalize(double min) { this.minNormalize = min; }
    public void setMaxNormalize(double max) { this.maxNormalize = max; }

    public void setSigmoid() {

    }
}