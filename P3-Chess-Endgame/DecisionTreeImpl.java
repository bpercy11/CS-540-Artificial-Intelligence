import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 * 
 * You must add code for the 1 member and 5 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
	private DecTreeNode root;
	//root = new DecTreeNode(null,null,null,null);
	//ordered list of class labels
	private List<String> labels; 
	//ordered list of attributes
	private List<String> attributes; 
	//map to ordered discrete values taken by attributes
	private Map<String, List<String>> attributeValues; 
	DataSet trainingSet;
	int globalAttLeft;
	ArrayList<Integer> attributesLeft;
	private List<Instance> instances;
	/**
	 * Answers static questions about decision trees.
	 */
	DecisionTreeImpl() {
		// no code necessary this is void purposefully
	}

	/**
	 * Build a decision tree given only a training set.
	 * 
	 * @param train: the training set
	 */
	//mode #1
	DecisionTreeImpl(DataSet train) {
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		// TODO: add code here

		trainingSet = train;
		List<Instance>instances = train.instances;

		trainingSet.instances = instances;
		
		assert(instances.size() > 0);

		attributesLeft = new ArrayList<Integer>();
		for (int i = 0; i < attributes.size(); i++) {
			attributesLeft.add(i);
		}

		globalAttLeft = attributesLeft.size();

		//System.out.println(getAttributeIndex(attributes.get(9)));
		
		//if((getAttributeIndex(attributes.get(9)) < (getAttributeIndex(attributes.get(10))))){
		//	System.out.println("LOWER");
		//}
		//System.out.println("ACCURACY: "+instances.get(3).label);
		
		id3(null, instances, attributesLeft, "", null, "");
	}


	/**
	 * Build a decision tree given a training set then prune it using a tuning set.
	 * 
	 * @param train: the training set
	 * @param tune: the tuning set
	 */
	DecisionTreeImpl(DataSet train, DataSet tune) {
		this(train);
		//tune.instances = instances;
		prune(tune);
		
	}

	//trouble with the while loop
	//chooses best candidateNode through 1st loop correctly, need to loop
	private DecTreeNode prune(DataSet tune){
		//boolean bool = true;
		while(true){
			double currAccuracy = getAccuracy(tune);
			//double newAccuracy;
			ArrayList<Double> accList = new ArrayList<Double>();
			ArrayList<DecTreeNode> innerNodes = new ArrayList<DecTreeNode>();
			innerNodesFinder(root, innerNodes);
			DecTreeNode candidateNode = null;
			
			for(DecTreeNode node : innerNodes) {
				node.terminal = true;
				double acc = getAccuracy(tune);
				node.terminal = false;
				accList.add(acc);
			}
			double bestAccuracy = -1;
			int bestIndex = -1;
			for(int i = 0; i < accList.size(); i++){
				double currAcc = accList.get(i);
				if(currAcc >= bestAccuracy){
					bestAccuracy = accList.get(i);
					bestIndex= i;
				}
			}
			if(bestAccuracy >= currAccuracy){
					innerNodes.get(bestIndex).terminal = true;
					//currAccuracy = bestAccuracy;
			}
			else{
				break;
			}
			
		}
		
		return root;
	}
	
	private ArrayList<DecTreeNode> innerNodesFinder(DecTreeNode node, ArrayList<DecTreeNode> childNodes){
		if (!node.terminal) {
			childNodes.add(node);
			for(DecTreeNode child : node.children){
				childNodes = innerNodesFinder(child, childNodes);
			}
		} 
		else {
			return childNodes;
		}
		//for (DecTreeNode currNode : node.children) {
		//	if (currNode.children != null && currNode.children.size() != 0){
		//		innerNodesFinder(currNode, basket);
		//	}
		//}
		return childNodes;
		
	}

	
	
	//possibly grabbing wrong 'lower-indexed attribute' when there is a tie
	private DecTreeNode id3(DecTreeNode parent, List<Instance> instances, List<Integer> attributesLeft, String attributeName, String majorityLabel, String parVal){
		DecTreeNode currNode;
		
		//if examples(empty) return default label
		if(instances.size()==0){
			//System.out.println("INSTANCES = 0 | Results in a: " + majorityLabel);
			//System.out.println("");
			currNode = new DecTreeNode(majorityLabelFn(), "", parVal, true);
			currNode.label = getMajority(instances, labels.size());
			parent.addChild(currNode);
			return currNode;
		}
		//if (examples all have same label y) return label y
		if(pure(instances)){
			//System.out.println("PURE INSTANCE | Results in a: "+getMajority(instances, trainingSet.labels.size()));
			//System.out.println("");
			currNode = new DecTreeNode(getMajority(instances, trainingSet.labels.size()), "", parVal, true);
			
			parent.addChild(currNode);
			return currNode;
		}
		//if empty(attributes) return majority class of examples
		if(attributesLeft.size()==0){
			//System.out.println("ATTRIBUTESLEFT = 0 | Results in a: "+getMajority(instances, trainingSet.labels.size()));
			//System.out.println("");
			currNode = new DecTreeNode(getMajority(instances, trainingSet.labels.size()), "", parVal, true);
			currNode.label = getMajority(instances, labels.size());
			parent.addChild(currNode);
			return currNode;
		}
		
		// q=maxInfoGain(examples, attributes)
		double entropy = calculateEntropy(instances, trainingSet.labels.size());
		int highestEntropyID = -1;
		double highestInfoGain = -1;
		for (int i = 0; i < attributesLeft.size(); i++) {
			double conditionalEntropy = calculateConditionalEntropy(attributesLeft.get(i), instances, trainingSet.attributeValues.get(trainingSet.attributes.get(attributesLeft.get(i))).size(), trainingSet.labels.size());
			double mutualInformation = entropy - conditionalEntropy;

			//working correctly??
			if ((mutualInformation > highestInfoGain)){
				if (mutualInformation == highestInfoGain && ((getAttributeIndex(attributes.get(i))) < (getAttributeIndex(attributes.get(highestEntropyID))))){
					highestInfoGain = mutualInformation;
					highestEntropyID = attributesLeft.get(i);
					
				}
				else{
					highestInfoGain = mutualInformation;
					highestEntropyID = attributesLeft.get(i);
				}
			} 
			
		}
		//if (highestInfoGain == 0){
		//		System.out.println("highest == 0");
		//	}
		//System.out.println("AttributeName: "+trainingSet.attributes.get(highestEntropyID)+" Entropy: "+entropy+" , highestEntropyID: "+highestEntropyID+" , highestInfoGain: "+highestInfoGain);

		// tree = create node with attribute q
		if (parent == null){
			root = new DecTreeNode(getMajority(instances, trainingSet.labels.size()), trainingSet.attributes.get(highestEntropyID), "", pure(instances) || attributesLeft.size()==1);
			currNode = root;
		} else {
			currNode = new DecTreeNode(getMajority(instances, trainingSet.labels.size()), trainingSet.attributes.get(highestEntropyID), parVal, pure(instances) || attributesLeft.size()==0);
			parent.addChild(currNode);
		}
		
		int noOfChildren = trainingSet.attributeValues.get(trainingSet.attributes.get(highestEntropyID)).size();
		ArrayList<ArrayList<Instance>> labelInstances = new ArrayList<ArrayList<Instance>>();
		int totalInstances = instances.size();
		//System.out.println("InstacesSize: "+totalInstances);
		
		for(int j = 0; j < noOfChildren; j++){
			ArrayList<Instance> temp = new ArrayList<Instance>();
			temp.clear();
			for(int n = 0; n < totalInstances; n++){
				if(instances.get(n).attributes.get(highestEntropyID).equals(trainingSet.attributeValues.get(attributes.get(highestEntropyID)).get(j))){
					temp.add(instances.get(n));
				}
			}
			labelInstances.add(temp);	
			//System.out.print(labelInstances.get(j).size()+": ");
			//for(int m = 0; m < labelInstances.get(j).size(); m++) {
			//	System.out.print(labelInstances.get(j).get(m).label+" ");
			//}
			//System.out.println("");
		}
		

		for (int i = 0; i < noOfChildren; i++) {
			//System.out.println("Child# "+ i + " of Attribute: "+attributes.get(highestEntropyID));
			List<Integer> attributesLeftCopy = new ArrayList<Integer>(attributesLeft);
			//attributesLeftCopy = attributesLeft;

			attributesLeftCopy.remove(attributesLeftCopy.indexOf(highestEntropyID));
			String decisionMade = trainingSet.attributeValues.get(attributes.get(highestEntropyID)).get(i);	
			
			id3(currNode, labelInstances.get(i), attributesLeftCopy, trainingSet.attributes.get(highestEntropyID), getMajority(instances, trainingSet.labels.size()), decisionMade);
				
			//parent.addChild(currNode);
			
			// add arc from tree to subtree
			
			//attributesLeftCopy.add(highestEntropyID);
			//System.out.println("Attribute added");

		}	

		
		return root;
	}



	/**
	 * checks to see if an instance is purely one type
	 * 
	 * @param instances
	 * @return
	 */
	private boolean pure(List<Instance> instances){
		if (instances.size() == 0){
			return false;
		}

		String first = instances.get(0).label;
		for(int i = 0; i < instances.size(); i++){
			if(!(instances.get(i).label.equals(first))){
				return false;
			}
		}
		return true;
	}

	/**
	 * gets majority
	 * 
	 * @param instances
	 * @param noOfLabelTypes
	 * @return
	 */
	private String getMajority(List<Instance> instances, int noOfLabelTypes){
		int[] labelCounts = new int[noOfLabelTypes];
		int numInstances = instances.size();
		if(numInstances == 0){
			return labels.get(0);
		}
		for(int i = 0; i < numInstances; i ++){
			for(int k = 0; k < noOfLabelTypes; k++){
				if(instances.get(i).label.equals(labels.get(k))){
					labelCounts[k]++;
				}
			}
		}
		int majorityCandidate = -1;
		int candidateCount = -1;
		for (int i = 0; i < labelCounts.length; i++) {
			if(labelCounts[i] > candidateCount){
				majorityCandidate = i;
				candidateCount = labelCounts[i];
			}
		}
		return labels.get(majorityCandidate);
	}

	private String majorityLabelFn(){
		Map<String, Integer> count= new HashMap<String, Integer>();
		for(int x = 0; x < labels.size(); x++){
			if(count.containsKey(this.labels.get(x)))
				count.put(this.labels.get(x), count.get(x) + 1);
			else
				count.put(this.labels.get(x), 1);	
		}
		
		String majLabel = "";
		int largest = 0;
		for(Map.Entry<String, Integer> entry: count.entrySet()){
			String temp = entry.getKey();
			int val = entry.getValue();
			if(val > largest){
				largest = val;
				majLabel = temp;
			}
			else if (val == largest){
				if(getLabelIndex(majLabel) > getLabelIndex(temp)){
					largest = val;
					majLabel = temp;
				}
			}
		}
		return majLabel;
		
	}
	
	
	@Override
	public String classify(Instance instance) {
	//System.out.println("ENTERED");
	
    // TODO: add code here

	  	Map<String, Integer> attributeMap = new HashMap<String, Integer>();
		for (int i = 0; i < trainingSet.attributes.size(); i++) {
			attributeMap.put(trainingSet.attributes.get(i), i);
		}
		
		DecTreeNode currNode = root;
		int counter = 0;
		while(true){
			if (currNode.terminal){
				break;
			} else {
				String currAttr = currNode.attribute;
				String instanceValue = instance.attributes.get(getAttributeIndex(currAttr));
				DecTreeNode foundNode = null;
				for (DecTreeNode node : currNode.children) {
					if (node.parentAttributeValue.equals(instanceValue)){
						foundNode = node;
						break;
					}
				}
				currNode = foundNode;
			}
		}
		return currNode.label;		
	}

	@Override
	public void rootInfoGain(DataSet train) {
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		this.instances = train.instances;

		
		//ACTUAL CODE
		double entropy = calculateEntropy(instances, labels.size());
		double infoGain = 0.0;
		for (int i = 0; i < attributes.size(); i++) { //For each attribute
			double conditionalEntropy = calculateConditionalEntropy(i, instances, attributeValues.get(attributes.get(i)).size(), labels.size());
			infoGain = entropy - conditionalEntropy;
			System.out.printf("%s %.5f\n", attributes.get(i) + "", infoGain);
		}
	}


	private double calculateEntropy(List<Instance> instances, int numOfLabels) {
		//need # of instances and # of different labels (2 in this case)
		int numInstances = instances.size();
		int labelCounts[] = new int[numOfLabels];

		for(int i=0; i<numInstances; i++){
			for(int j=0; j<numOfLabels; j++){
				if(instances.get(i).label.equals(labels.get(j))){
					labelCounts[j]++;
				}
			}  
		}

		double entropy = 0;
		for(int i=0; i<labelCounts.length; i++){
			if(numInstances != 0 && labelCounts[i] != 0){
				double prob = Double.valueOf(labelCounts[i])/Double.valueOf(numInstances);
				entropy += prob * Math.log10(prob)/Math.log10(2);
			}
		}
		return entropy*-1;
	}

	private double calculateConditionalEntropy(int attributeId, List<Instance> instances, int noOfAtrributeTypes, int noOfLabelTypes){
		int[] labelCounts = new int[noOfAtrributeTypes];

		ArrayList<ArrayList<Instance>> labelInstances = new ArrayList<ArrayList<Instance>>();

		int totalInstances = instances.size();

		int count = 0;
		for(int j=0; j<noOfAtrributeTypes; j++){
			ArrayList<Instance> temp = new ArrayList<Instance>();

			for(int n = 0; n < totalInstances; n++){	
				if(instances.get(n).attributes.get(attributeId).equals(attributeValues.get(attributes.get(attributeId)).get(j))){
					temp.add(instances.get(n));
					labelCounts[j]++;
					count++;
				}
			}
			labelInstances.add(temp);
		}

		double conditionalEntropy = 0;
		for(int i = 0; i < labelCounts.length; i++){
			if(totalInstances != 0 && labelCounts[i] != 0){
				double prob = Double.valueOf(labelCounts[i]) / Double.valueOf(totalInstances);
				double subCondEntropy = calculateEntropy(labelInstances.get(i), noOfLabelTypes);
				conditionalEntropy += prob * subCondEntropy;
			}
		}
		return conditionalEntropy;
	}



	@Override
	/**
	 * Print the decision tree in the specified format
	 */
	public void print() {

		printTreeNode(root, null, 0);
	}

	/**
	 * Prints the subtree of the node with each line prefixed by 4 * k spaces.
	 */
	public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < k; i++) {
			sb.append("    ");
		}
		String value;
		if (parent == null) {
			value = "ROOT";
		} else {
			int attributeValueIndex = this.getAttributeValueIndex(parent.attribute, p.parentAttributeValue);
			value = attributeValues.get(parent.attribute).get(attributeValueIndex);
		}
		sb.append(value);
		if (p.terminal) {
			sb.append(" (" + p.label + ")");
			System.out.println(sb.toString());
		} else {
			sb.append(" {" + p.attribute + "?}");
			System.out.println(sb.toString());
			for (DecTreeNode child : p.children) {
				printTreeNode(child, p, k + 1);
			}
		}
	}

	/**
	 * Helper function to get the index of the label in labels list
	 */
	private int getLabelIndex(String label) {
		for (int i = 0; i < this.labels.size(); i++) {
			if (label.equals(this.labels.get(i))) {
				return i;
			}
		}
		return -1;
	}

	private int getAttIndex(int attr){
		for (int i = 0; i < this.attributes.size(); i++) {
			if (attr == (attributesLeft.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Helper function to get the index of the attribute in attributes list
	 */
	private int getAttributeIndex(String attr) {
		for (int i = 0; i < this.attributes.size(); i++) {
			if (attr.equals(this.attributes.get(i))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Helper function to get the index of the attributeValue in the list for the attribute key in the attributeValues map
	 */
	private int getAttributeValueIndex(String attr, String value) {
		for (int i = 0; i < attributeValues.get(attr).size(); i++) {
			if (value.equals(attributeValues.get(attr).get(i))) {
				return i;
			}
		}
		return -1;
	}


	/**
   /* Returns the accuracy of the decision tree on a given DataSet.
	 */
	@Override
	public double getAccuracy(DataSet ds){
		
		int correctClassification = 0;
		for (Instance instance : ds.instances) {
			if(classify(instance).equals(ds.labels.get(getLabelIndex(instance.label)))){
				correctClassification++;
			}
		}
		double accuracy = Double.valueOf(correctClassification)/Double.valueOf(ds.instances.size());
		return accuracy;
		
		

	}
	
}
