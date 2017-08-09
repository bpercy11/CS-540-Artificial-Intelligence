import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier {

	//THESE VARIABLES ARE OPTIONAL TO USE, but HashMaps will make your life much, much easier on this assignment.
	 int numOfTragedyDocs = 0;
	 int numOfComedyDocs = 0;
	 int numOfHistoryDocs = 0;
	//dictionaries of form word:frequency that store the number of times word w has been seen in documents of type label
	//for example, comedyCounts["mirth"] should store the total number of "mirth" tokens that appear in comedy documents
   private HashMap<String, Integer> tragedyCounts = new HashMap<String, Integer>();
   private HashMap<String, Integer> comedyCounts = new HashMap<String, Integer>();
   private HashMap<String, Integer> historyCounts = new HashMap<String, Integer>();
   
   //prior probabilities, ie. P(T), P(C), and P(H)
   //use the training set for the numerator and denominator
   private double tragedyPrior;
   private double comedyPrior;
   private double historyPrior;
   
   //total number of word TOKENS for each type of document in the training set, ie. the sum of the length of all documents with a given label
   private int tTokenSum;
   private int cTokenSum;
   private int hTokenSum;
   
   //full vocabulary, update in training, cardinality is necessary for smoothing
   private HashSet<String> vocabulary = new HashSet<String>();
   

  /**
   * Trains the classifier with the provided training data
   Should iterate through the training instances, and, for each word in the documents, update the variables above appropriately.
   The dictionary of frequencies and prior probabilites can then be used at classification time.
   */
  @Override
  public void train(Instance[] trainingData) {
    // TODO : Implement
	  for(Instance instance : trainingData){
		  if(instance.label == Label.TRAGEDY){
			  numOfTragedyDocs++;
			  for(int j = 0; j < instance.words.length; j++){
					String key = instance.words[j];
					if(!tragedyCounts.containsKey(key)) //word has not been seen yet in hashMap
					{
						tragedyCounts.put(key, 1);
						vocabulary.add(key);
						tTokenSum++;
					}
					else  //word is already in the hashMap
					{
						tragedyCounts.put(key, tragedyCounts.get(key) + 1);
						tTokenSum++;
					}
			  }
		  }
		  if(instance.label == Label.COMEDY){
			  numOfComedyDocs++;
			  for(int j = 0; j < instance.words.length; j++){
					String key = instance.words[j];
					if(!comedyCounts.containsKey(key))
					{
						comedyCounts.put(key, 1);
						vocabulary.add(key);
						cTokenSum++;
					}
					else
					{
						comedyCounts.put(key, comedyCounts.get(key) + 1);
						cTokenSum++;
					}
			  }
		  }
		  if(instance.label == Label.HISTORY){
			  numOfHistoryDocs++;
			  for(int j = 0; j < instance.words.length; j++){
					String key = instance.words[j];
					if(!historyCounts.containsKey(key))
					{
						historyCounts.put(key, 1);
						vocabulary.add(key);
						hTokenSum++;
					}
					else
					{
						historyCounts.put(key, historyCounts.get(key) + 1);
						hTokenSum++;
					}
			  }
		  }
	  }
	  /*
	  System.out.println("TRAGEDY: " +tTokenSum);
	  System.out.println("COMEDY: "+ cTokenSum);
	  System.out.println("HISTORY: "+ hTokenSum);
	  System.out.println("TRAGEDY: "+ tragedyCounts.size());
	  System.out.println("COMEDY: "+ comedyCounts.size());
	  System.out.println("HISTORY: "+ historyCounts.size());
	  */
  }

  /*
   * Prints out the number of documents for each label
   * A sanity check method
   */
  //FINISHED
  public void documents_per_label_count(){
    // TODO : Implement

	  System.out.println("TRAGEDY="+ numOfTragedyDocs);
	  System.out.println("COMEDY="+ numOfComedyDocs);
	  System.out.println("HISTORY="+ numOfHistoryDocs); 
  }

  /*
   * Prints out the number of words for each label
	Another sanity check method
   */
  public void words_per_label_count(){
    // TODO : Implement
	  System.out.println("TRAGEDY="+ tTokenSum);
	  System.out.println("COMEDY="+ cTokenSum);
	  System.out.println("HISTORY="+ hTokenSum); 
  }

  /**
   * Returns the prior probability of the label parameter, i.e. P(COMEDY) or P(TRAGEDY)
   */
  @Override
  public double p_l(Label label) {
    // TODO : Implement
	  double sum = tTokenSum+cTokenSum+hTokenSum;
	  if(label == Label.TRAGEDY){
		  tragedyPrior = tTokenSum/sum; 
		  return tragedyPrior;
	  }
	  if(label == Label.COMEDY){
		  comedyPrior = cTokenSum/sum;
		  return comedyPrior;
	  }
	  if(label == Label.HISTORY){
		  historyPrior = hTokenSum/sum;
		  return historyPrior;
	  }
	  else return -1;
	  	
  }

  /**
   * Returns the smoothed conditional probability of the word given the label, i.e. P(word|COMEDY) or
   * P(word|HISTORY)
   */
  @Override
  public double p_w_given_l(String word, Label label) {
    // TODO : Implement
	  if(label == Label.TRAGEDY)
		{
			if(!tragedyCounts.containsKey(word))
			{
				return 0.00001 / (vocabulary.size() * 0.00001 + tTokenSum);	
			}
			else
			{
				return ((double) tragedyCounts.get(word) + 0.00001) /
						(vocabulary.size() * 0.00001 + tTokenSum);
			}
		}
		if(label == Label.COMEDY)
		{
			if(!comedyCounts.containsKey(word))
			{
				return 0.00001 / (vocabulary.size() * 0.00001 + cTokenSum);	
			}
			else
			{
				return ((double) comedyCounts.get(word) + 0.00001) /
						(vocabulary.size() * 0.00001 + cTokenSum);
			}
		}
		if(label == Label.HISTORY)
		{
			if(!historyCounts.containsKey(word))
			{
				return 0.00001 / (vocabulary.size() * 0.00001 + hTokenSum);	
			}
			else
			{
				return ((double) historyCounts.get(word) + 0.00001) /
						(vocabulary.size() * 0.00001 + hTokenSum);
			}
		}
		return 0;
	  
  }

  /**
   * Classifies a document as either a Comedy, History, or Tragedy.
   Break ties in favor of labels with higher prior probabilities.
   */
  @Override
  public Label classify(Instance ins) {
	double log_prob_tragedy = Math.log(p_l(Label.TRAGEDY));
	double log_prob_comedy = Math.log(p_l(Label.COMEDY));
	double log_prob_history = Math.log(p_l(Label.HISTORY));
	
	for(String word : ins.words){
		log_prob_tragedy += Math.log(p_w_given_l(word, Label.TRAGEDY));
		log_prob_comedy += Math.log(p_w_given_l(word, Label.COMEDY));
		log_prob_history += Math.log(p_w_given_l(word, Label.HISTORY));
	}

	//tie checking
	//if(log_prob_comedy == log_prob_history){
	//	if(comedyPrior > historyPrior)
			
	//}
	
	double max;
	max = (maximal(log_prob_comedy, log_prob_history, log_prob_tragedy));
	  
	if(max == log_prob_comedy)
		return Label.COMEDY;
	if(max == log_prob_history)
		return Label.HISTORY;
	if(max == log_prob_tragedy)
		return Label.TRAGEDY;
	else
		return null;
	//Initialize sum probabilities for each label
	//For each word w in document ins
		//compute the log (base e or default java log) probability of w|label for all labels (COMEDY, TRAGEDY, HISTORY)
		//add to appropriate sum
	//Return the Label of the maximal sum probability


  }

  private double maximal(double log_prob_comedy, double log_prob_history,double log_prob_tragedy) {
	  // TODO Auto-generated method stub
	  double maxi;
	  //boolean equal = false;
	  boolean com = false;
	  boolean hist = false;
	  boolean trag = false;
	  //if(log_prob_comedy == log_prob_history || log_prob_history == log_prob_tragedy || log_prob_comedy == log_prob_tragedy){
	  //	equal = true;
	  //}
	  if (log_prob_comedy == log_prob_history && log_prob_comedy > log_prob_tragedy){
		  if(comedyPrior > historyPrior)
			  com = true;
		  else
			  hist = true;
	  }
	  if (log_prob_history == log_prob_tragedy && log_prob_history > log_prob_comedy){
		  if (historyPrior > tragedyPrior)
			  hist = true;
		  else
			  trag = true;
	  }
	  if (log_prob_comedy == log_prob_tragedy && log_prob_comedy > log_prob_history){
		  if(comedyPrior > tragedyPrior)
			  com = true;
		  else
			  trag = true;
	  }

	  if(com)
		  return log_prob_comedy;
	  if(hist)
		  return log_prob_history;
	  if(trag)
		  return log_prob_tragedy;
	  else{
		  maxi = Math.max(log_prob_comedy, log_prob_history);
		  maxi = Math.max(maxi, log_prob_tragedy);
		  return maxi;	
	  }
}
  
  
}
