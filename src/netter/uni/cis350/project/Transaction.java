package netter.uni.cis350.project;

import java.util.HashMap;

public class Transaction {
	HashMap<String, Integer> purchases;
	HashMap<String, Integer> payments;
	String gender;
	String age;
	Double donation;
	Double bargin_amt;
	String id;

	public Transaction(HashMap<String,Integer> purch, HashMap<String,Integer> pays, String g, String a, Double dona, Double bara, String which) {
		purchases = purch;
		payments = pays;
		gender = g;
		age = a;
		donation = dona;
		bargin_amt = bara;
		id = which;
	}
	
}
