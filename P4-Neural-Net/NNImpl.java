/**
 * The main class that handles the entire network
 * Has multiple attributes each with its own use
 * 
 */

import java.util.*;


public class NNImpl{
	public ArrayList<Node> inputNodes=null;//list of the output layer nodes.
	public ArrayList<Node> hiddenNodes=null;//list of the hidden layer nodes
	public Node outputNode=null;// single output node that represents the result of the regression
	
	public ArrayList<Instance> trainingSet=null;//the training set
	
	Double learningRate=1.0; // variable to store the learning rate
	int maxEpoch=1; // variable to store the maximum number of epochs
	
	
	/**
 	* This constructor creates the nodes necessary for the neural network
 	* Also connects the nodes of different layers
 	* After calling the constructor the last node of both inputNodes and  
 	* hiddenNodes will be bias nodes. 
 	*/
	
	public NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Double [][]hiddenWeights, Double[] outputWeights)
	{
		this.trainingSet=trainingSet;
		this.learningRate=learningRate;
		this.maxEpoch=maxEpoch;
		
		//input layer nodes
		inputNodes = new ArrayList<Node>();
		int inputNodeCount = trainingSet.get(0).attributes.size();
		int outputNodeCount=1;
		
		for(int i=0;i<inputNodeCount;i++)
		{
			Node node=new Node(0);
			inputNodes.add(node);	
		}
		
		//bias node from input layer to hidden
		Node biasToHidden=new Node(1);
		inputNodes.add(biasToHidden);
		
		//hidden layer nodes
		hiddenNodes=new ArrayList<Node> ();
		for(int i=0;i<hiddenNodeCount;i++)
		{
			Node node=new Node(2);
			//Connecting hidden layer nodes with input layer nodes
			for(int j=0;j<inputNodes.size();j++)
			{
				NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j),hiddenWeights[i][j]);
				node.parents.add(nwp);
			}
			hiddenNodes.add(node);
		}
		
		//bias node from hidden layer to output
		Node biasToOutput=new Node(3);
		hiddenNodes.add(biasToOutput);
			


		Node node = new Node(4);
		//Connecting output node with hidden layer nodes
		for(int j=0;j<hiddenNodes.size();j++)
		{
			NodeWeightPair nwp=new NodeWeightPair(hiddenNodes.get(j), outputWeights[j]);
			node.parents.add(nwp);
		}	
		outputNode = node;
			
	}
	
	/**
	 * Get the output from the neural network for a single instance. That is, set the values of the training instance to
	the appropriate input nodes, percolate them through the network, then return the activation value at the single output
	node. This is your estimate of y. 
	 */
	public double calculateOutputForInstance(Instance inst)
	{
				//activation values at inputs
				for(int i = 0; i < inst.attributes.size(); i++) {
					Node inputNode = this.inputNodes.get(i);
					inputNode.setInput(inst.attributes.get(i));
				}
				
				//outputs at hidden nodes (activation values)
				for(Node hiddenNode : this.hiddenNodes) {
					hiddenNode.calculateOutput();
					//System.out.println("NODE OUT: "+hiddenNode.getOutput());
				}
				
				//System.out.println(outputNode.calculateOutput());
				
				//activation value at output
				outputNode.calculateOutput();;
				//System.out.println("here: "+outputNode.getOutput());
				//System.out.println(outputNode.getOutput());
				return outputNode.getOutput();
				
	}
	

	
	
	
	/**
	 * Trains a neural network with the parameters initialized in the constructor for the number of epochs specified in the instance variable maxEpoch.
	 * The parameters are stored as attributes of this class, namely learningRate (alpha) and trainingSet.
	 * Implement stochastic graident descent: update the network weights using the deltas computed after each the error of each training instance is computed.
	 * An single epoch looks at each instance training set once, so you should update weights n times per epoch if you have n instances in the training set.
	 */
	
	public void train()
	{
		/*
		// SINGLE INSTANCE FOR CHECKING
		double calcOutput = this.calculateOutputForInstance(trainingSet.get(0));
		System.out.println("1 "+calcOutput);
		System.out.println("2 "+trainingSet.get(0).output);
		double error = trainingSet.get(0).output - calcOutput;
		System.out.println("3 "+error);
		 */


		int currentEpoch = 1;
		while( currentEpoch <= this.maxEpoch ) {

			for(Instance example : this.trainingSet) {

				double calcOutput = this.calculateOutputForInstance(example);
				double error = example.output - calcOutput;


				// NEED TO CALCULATE DELTAS FOR WEIGHTS AND THEN APPLY THEM THEN DONE //

				//2D array to store hidden to output node ∆W
				double[] hiddenToOutputWeights = new double[this.hiddenNodes.size()];

				for(int j = 0; j < outputNode.parents.size(); j++) {
					NodeWeightPair hiddenNodePair = outputNode.parents.get(j);
					Node hiddenNode = hiddenNodePair.node;

					double deltaWJK = this.learningRate * hiddenNode.getOutput() * error
							* this.derivativeOfReLU(outputNode.getOutput());

					hiddenToOutputWeights[j] = deltaWJK;
				}
				
				// find sumWJKTKOKGPrimeIn
				double[] sumWJKTKOKGPrimeIn = new double[this.hiddenNodes.size()];
				for(int j = 0; j < outputNode.parents.size(); j++) {
					NodeWeightPair hiddenNodePair = outputNode.parents.get(j);
					sumWJKTKOKGPrimeIn[j] += hiddenNodePair.weight * error * this.derivativeOfReLU(outputNode.getOutput());
				}


				//2D array to store input to hidden node ∆W
				double[][] inputToHiddenWeights = new double[this.inputNodes.size()][this.hiddenNodes.size()];		
				
				//deltaWIJ
				for(int j = 0; j < this.hiddenNodes.size() - 1; j++) {
					Node hiddenNode = this.hiddenNodes.get(j);

					for(int i = 0; i < hiddenNode.parents.size(); i++) {
						NodeWeightPair inputNodePair = hiddenNode.parents.get(i);
						Node inputNode = inputNodePair.node;

						double deltaWIJ = this.learningRate * inputNode.getOutput() * this.derivativeOfReLU(hiddenNode.getOutput()) * sumWJKTKOKGPrimeIn[j];

						inputToHiddenWeights[i][j] = deltaWIJ;
					}
				}



				// hidden to output weights updated
				for(int j = 0; j < outputNode.parents.size(); j++) {
					NodeWeightPair hiddenNodePair = outputNode.parents.get(j);
					hiddenNodePair.weight += hiddenToOutputWeights[j];
				}


				// input to hidden weights updated
				for(int j = 0; j < this.hiddenNodes.size() - 1; j++) {
					Node hiddenNode = this.hiddenNodes.get(j);

					for(int i = 0; i < hiddenNode.parents.size(); i++) {
						NodeWeightPair inputNodePair = hiddenNode.parents.get(i);
						inputNodePair.weight += inputToHiddenWeights[i][j];
					}
				}
			}
			currentEpoch++;
		}
	}


	/**
	 * Returns the mean squared error of a dataset. That is, the sum of the squared error (T-O) for each instance
	in the dataset divided by the number of instances in the dataset.
	 */
	public double getMeanSquaredError(List<Instance> dataset){
		//TODO: add code here
		double sum = 0;
		
		for(Instance example : dataset){
			double predicted = calculateOutputForInstance(example);
			double actual = example.output;
			double error = actual - predicted;
			double squaredError = Math.pow(error, 2);
			sum += squaredError;
		}
		
		double meanSquaredError = sum / dataset.size();
		
		return meanSquaredError;
	}
	
	public double derivativeOfReLU(double num) {
		if (num > 0)
			return 1;
		else
			return 0;
	}
	
}
