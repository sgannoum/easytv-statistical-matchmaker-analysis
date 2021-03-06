package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

public class RuleWrapper implements Comparable<RuleWrapper> {
	
	public static class RuleCondition {
		
		protected String uri;
		protected String builtin;
		protected Argument[] args;		
		protected JSONObject json = null;
		
		public static class Argument implements Comparable<Argument> {
			private String xmlType;
			private Object value;
			private JSONObject json = null;
			
			public Argument (String xmlType, Object value){
				this.setXMLType(xmlType);
				this.value = value;
			}
			
			public Argument(JSONObject json){
				this.setJSONObject(json);
			}
			
			public void setXMLType(String xmlType) {
				if(!xmlType.startsWith("http://www.w3.org/2001/XMLSchema#"))
					this.xmlType = "http://www.w3.org/2001/XMLSchema#" + xmlType;
				else 
					this.xmlType = xmlType;
			}
			
			public String getXMLType() {
				return xmlType;
			}
			
			public JSONObject getJSONObject() {
				if(json == null) 
					json = new JSONObject().put("xml-type", xmlType).put("value", value);
				
				return json;
			}
			
			public void setJSONObject(JSONObject json) {
				this.setXMLType(json.getString("xml-type"));
				this.value = json.get("value");
			}
			
			@Override
			public boolean equals(Object obj) {
				if(this == obj) return true;
				if(!Argument.class.isInstance(obj)) return false;
				Argument other = (Argument) obj;
				return xmlType.equals(other.xmlType) && 
						value.equals(other.value) ;
			}
			
			@Override
			public int hashCode() {
				return xmlType.hashCode() + value.hashCode();
			}
			
			@Override
			public int compareTo(Argument o) {
				
				if(xmlType.equalsIgnoreCase(o.xmlType) &&
						value.equals(o.value))
					return 0;
				
				return -1;
			}
			
			@Override
			public String toString() {
				return String.valueOf(value);
			}
		}
		
		public RuleCondition(String uri, String builtin, Object value, String xmlType) {
			this.setUri(uri);
			this.setBuiltin(builtin);
			this.args = new Argument[] {new Argument(xmlType, value)};
		}
		
		public RuleCondition(String uri, String builtin, Argument[] args) {
			this.setUri(uri);
			this.setBuiltin(builtin);
			this.args = args;
		}
		
		public RuleCondition(String uri, String builtin, Argument arg) {
			this.setUri(uri);
			this.setBuiltin(builtin);
			this.args = new Argument[] {arg};
		}
		
		public RuleCondition(String uri, Argument[] args) {
			this.setUri(uri);
			this.setBuiltin("EQ");
			this.args = args;
		}
		
		public RuleCondition(String uri, Argument arg) {
			this.setUri(uri);
			this.setBuiltin("EQ");
			this.args = new Argument[] {arg};
		}
		
		public RuleCondition(String uri, String builtin, String strValue) {
			this.setUri(uri);
			this.setBuiltin(builtin);
			Object value;
			String xmlType = null;
			if(strValue.contains(".")) {
				xmlType = "double";
				value = Double.valueOf(strValue).doubleValue();
			} else if(strValue.contains("\"")) {
				xmlType = "string";
				value = strValue.replaceAll("\"", "");
			} else if(strValue.contains(":")) {
				xmlType = "time";
				value = strValue;
			} else if(strValue.equalsIgnoreCase("false") || strValue.equalsIgnoreCase("true")) {
				xmlType = "boolean";
				value = Boolean.valueOf(strValue).booleanValue();
			} else {
				xmlType = "integer";
				value = Integer.valueOf(strValue).intValue();
			}
			
			this.args = new Argument[] {new Argument(xmlType, value)};
		}
		
		public RuleCondition(JSONObject json) {
			this.setJSONObject(json);
		}
		
		public String getUri() {
			return uri;
		}
		
		public Argument[] getArgs() {
			return args;
		}

		public void setUri(String uri) {
			this.uri = uri.trim();
		}
		
		public String getBuiltin() {
			return builtin;
		}

		public void setBuiltin(String builtin) {
			this.builtin = builtin.trim();
		}
		
		public JSONObject getJSONObject() {
			if(json == null) {
				json = new JSONObject();
				JSONArray jsonArgs = new JSONArray();
				
				for(Argument arg: args) 
					jsonArgs.put(arg.getJSONObject());
				
				json.put("preference", uri);
				json.put("builtin", builtin);
				json.put("args", jsonArgs);
			}
			
			return json;
		}
		
		public void setJSONObject(JSONObject json) {			
			uri = json.getString("preference");
			builtin = json.getString("builtin");
			JSONArray jsonArgs = json.getJSONArray("args");

			args = new Argument[jsonArgs.length()];
			
			for(int i = 0; i < jsonArgs.length(); i ++) 
				args[i] = new Argument(jsonArgs.getJSONObject(i));
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) 
				return true;
			
			if(!RuleCondition.class.isInstance(obj)) 
				return false;
			
			RuleCondition other = (RuleCondition) obj;
			if(!uri.equals(other.uri) ||
				!builtin.equals(other.builtin) ||
				  args.length != other.args.length) 
				return false;
			
			Set<Integer> classifier = new HashSet<Integer>();
			for(Argument arg : args) 
				classifier.add(arg.hashCode()); 
			
			for(Argument arg : other.args) 
				if(classifier.add(arg.hashCode())) 
					return false;

			return true;
		}
		
		@Override
		public int hashCode() {
			int hash = uri.hashCode() + builtin.hashCode();
			
			for(Argument arg : args) 
				hash += arg.hashCode();
			
			return hash;
		}
		
		@Override
		public String toString() {
			
			String conditions = "";
			for(int i = 0; i < args.length; i++)
				conditions += String.format("%s %s %s", uri, 
														builtin.equals("EQ") ? "=" : 
														builtin.equals("GE") ? ">=" :
														builtin.equals("LE") ? "<=" :
														builtin.equals("GT") ? ">" :
														builtin.equals("LT") ? "<" : "!=",
														args[i].toString());
			return conditions;
		}
	}
	
	private abstract static class RuleConditions implements Comparable<RuleConditions>{
		
		protected RuleCondition[] ruleConditions;
		protected long weight;
		protected double support;
		protected JSONArray json = null;
		
		public RuleConditions(JSONArray json) {
			this.setJSONObject(json);
		}
		
		public RuleConditions(String ruleConditions, long weight, double support) {		
			
			StringTokenizer tk = new StringTokenizer(ruleConditions, "^,", false);
			this.ruleConditions = new RuleCondition[tk.countTokens()];
			int index = 0;
						
			while(tk.hasMoreTokens()) {
				String uri = null, builtin, value;
				String pref = tk.nextToken().trim();
				
				StringTokenizer preTk = new StringTokenizer(pref, "<>!=", true);
				while(preTk.hasMoreTokens()) {
					uri = null;
					builtin = null;
					value = null;
					
					try {					
						uri = preTk.nextToken().trim();;
						builtin = preTk.nextToken().trim();
						value = preTk.nextToken().trim();
					} catch(NoSuchElementException e) {
						throw new MissingFormatArgumentException((uri == null ? "missing uri" : builtin == null ? "missing builtin operation": "missing value") + " in " + pref);
					}

					if(value.matches("[<|>|!|=]")){
						builtin += value;
						value = preTk.nextToken().trim();
					}
					
					if(builtin.equals(">"))
						builtin = "GT";
					else if(builtin.equals("<"))
						builtin = "LT";
					else if(builtin.equals(">="))
						builtin = "GE";
					else if(builtin.equals("<="))
						builtin = "LE";
					else if(builtin.equals("!=")) 
						builtin = "NE";
					else if(builtin.equals("=")) 
						builtin = "EQ";
					
					this.ruleConditions[index++] = new RuleCondition(uri, builtin, value);
				}
			}
			
			this.setRuleConditions(this.ruleConditions);
		}
		
		public RuleConditions(RuleCondition[] ruleConditions, long weight, double support) {
			this.setRuleConditions(ruleConditions);
		}

		public RuleCondition[] getRuleConditions() {
			return ruleConditions;
		}
		
		public void setConditions(RuleCondition[] ruleConditions) {
			this.ruleConditions = ruleConditions;
			json = null;
		}
		
		public void add(RuleCondition[] conditions) {			
			RuleCondition[] tmp = new RuleCondition[this.ruleConditions.length + conditions.length];
			
			int i = 0;
			for(int j = 0; j < this.ruleConditions.length; tmp[i] = this.ruleConditions[j], j++, i++);
			for(int j = 0; j < conditions.length; tmp[i] = conditions[j], j++, i++);

			this.ruleConditions = tmp;
				
			json = null;
		}

		public void setRuleConditions(RuleCondition[] ruleConditions) {		
			Set<String> classifier = new HashSet<String>();
			for(RuleCondition cond1 : ruleConditions) { 
				String key = cond1.getUri() +" "+ cond1.getBuiltin();
				if(!classifier.add(key)) 
					throw new IllegalArgumentException("Rule condition with two similar conditions for "+key);
			}

			this.ruleConditions = ruleConditions;
		}

		public long getWeight() {
			return weight;
		}

		public void setWeight(long weight) {
			this.weight = weight;
		}

		public double getSupport() {
			return support;
		}

		public void setSupport(double support) {
			this.support = support;
		}
		
		public JSONArray getJSONObject() {
			if(json == null) {
				json = new JSONArray();
				for(RuleCondition ruleCondition : ruleConditions) 
					json.put(ruleCondition.getJSONObject());
			}
			
			return json;
		}
		
		public void setJSONObject(JSONArray json) {
			ruleConditions = new RuleCondition[json.length()];
		
			for(int i = 0; i < json.length(); ruleConditions[i] = new RuleCondition(json.getJSONObject(i)), i++) ;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) 
				return true;
			
			if(!RuleConditions.class.isInstance(obj)) 
				return false;
			
			RuleConditions other = (RuleConditions) obj;	
			if(ruleConditions.length != other.ruleConditions.length)
				return false;
			
			Set<Integer> classifier = new HashSet<Integer>();
			for(RuleCondition cond1 : ruleConditions)  
				classifier.add(cond1.hashCode()); 
			
			for(RuleCondition cond2 : other.ruleConditions) 
				if(classifier.add(cond2.hashCode())) 
					return false;
				
			return true;
		}
		
		@Override
		public int hashCode() {
			int hash = 0;
			
			for(RuleCondition condition : ruleConditions)
				hash += condition.hashCode();
			
			return hash;
		}
		
		@Override
		public String toString() {
			int i = 0;
			String conditions = "";
			for(i = 0; i < ruleConditions.length - 1; i++)
				conditions += ruleConditions[i].toString() + " ^\r\n";
			
			if(ruleConditions.length > 0) conditions += ruleConditions[i];
			
			return conditions;
		}
		
		@Override
		public int compareTo(RuleConditions other) {
			
			if(ruleConditions.length !=  other.ruleConditions.length)
				return -1;
			
			int counter = 0;
			Set<Integer> classifier = new HashSet<Integer>();
			for(RuleCondition cond1 : ruleConditions) 
				classifier.add(cond1.hashCode()); 
			
			for(RuleCondition cond2 : other.ruleConditions) 
				if(classifier.add(cond2.hashCode())) 
					counter--;
				else 
					counter++;
			
			return counter;
		}

	}
	
	public static class HeadRuleConditions extends RuleConditions {

		public HeadRuleConditions(JSONArray json) {
			super(json);
		}
		
		public HeadRuleConditions(RuleCondition[] ruleConditions, long weight, double support) {
			super(ruleConditions, weight, support);
		}
		
		public HeadRuleConditions(String ruleConditions, long weight, double support) {
			super(ruleConditions, weight, support);
		}
		
	}
	
	public static class BodyRuleConditions extends RuleConditions {

		public BodyRuleConditions(JSONArray json) {
			super(json);
		}
		
		public BodyRuleConditions(RuleCondition[] ruleConditions, long weight, double support) {
			super(ruleConditions, weight, support);
		}
		
		public BodyRuleConditions(String ruleConditions, long weight, double support) {
			super(ruleConditions, weight, support);
		}
	}
	
	
	private HeadRuleConditions head;
	private BodyRuleConditions body;
	private double confidence = 0.0;
	private JSONObject json = null;
	
	
	public RuleWrapper(BodyRuleConditions body, HeadRuleConditions head) {
		this.head = head;
		this.body = body;
	}
	
	public RuleWrapper(BodyRuleConditions body, HeadRuleConditions head, double confidence) {
		this.head = head;
		this.body = body;
		this.setConfidence(confidence);
	}
	
	public RuleWrapper(String rule, long bweight, double bsupport, long hweight, double hsupport, double confidence) {
		
		String[] ruleBodyHead = rule.split("->");
		
		this.body = new BodyRuleConditions(ruleBodyHead[0], bweight, bsupport);
		this.head = new HeadRuleConditions(ruleBodyHead[1], hweight, hsupport);
		this.setConfidence(confidence);
	}
	
	public RuleWrapper(String rule, double confidence) {
		
		String[] ruleBodyHead = rule.split("->");
		
		this.body = new BodyRuleConditions(ruleBodyHead[0], 0, 0);
		this.head = new HeadRuleConditions(ruleBodyHead[1], 0, 0);
		this.setConfidence(confidence);
	}
	
	public RuleWrapper(String rule) {
		
		String[] ruleBodyHead = rule.split("->");
		
		this.body = new BodyRuleConditions(ruleBodyHead[0], 0, 0);
		this.head = new HeadRuleConditions(ruleBodyHead[1], 0, 0);
	}
	
	public RuleWrapper(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public RuleWrapper(File file) throws IOException {		
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) 
			buff.append(line);

		//close file
		reader.close();
		
		this.setJSONObject( new JSONObject(buff.toString()));
	}
	
	public JSONObject getJSONObject() {
		if(json == null) {
			json = new JSONObject();
			json.put("body", body.getJSONObject());
			json.put("head", head.getJSONObject());
			json.put("confidence", confidence);

		}
		return json;
	}
	
	public void setHead(HeadRuleConditions head) {
		this.head = head;
		json = null;
	}
	
	public HeadRuleConditions getHead() {
		return head;
	}
	
	public void setJSONObject(JSONObject json) {
		JSONArray headconditions = json.getJSONArray("head");
		if(head == null)
			head = new HeadRuleConditions(headconditions);
		else 
			head.setJSONObject(headconditions);

		JSONArray bodyConditions = json.getJSONArray("body");
		if(body == null)
			body = new BodyRuleConditions(bodyConditions);
		else 
			body.setJSONObject(bodyConditions);
		
		confidence = json.getDouble("confidence");
	}
	
	/**
	 * Merge both heads
	 * @param other
	 */
	public void merge(HeadRuleConditions other) {
		Map<String, RuleCondition> classifier = new HashMap<String, RuleCondition>();
		
		for(RuleCondition cond1 : head.ruleConditions) 
			classifier.put(cond1.getUri() + cond1.getBuiltin(), cond1);
		
		for(RuleCondition cond2 : other.ruleConditions) {
			String key = cond2.getUri() + cond2.getBuiltin();
			if(!classifier.containsKey(key)) 
				classifier.put(key, cond2);
		}
		
		//add to header
		Collection<RuleCondition> values = classifier.values();
		head.ruleConditions = new RuleCondition[values.toArray().length];
		System.arraycopy(values.toArray(), 0, head.ruleConditions, 0, head.ruleConditions.length);

	}
	
	/**
	 * Two rules are equals when their bodies are equals
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) 
			return true;
		
		if(!RuleWrapper.class.isInstance(obj)) 
			return false;
		
		RuleWrapper other = (RuleWrapper) obj;	
		if(!body.equals(other.body))
			return false;
		
		if(!head.equals(other.head))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return body.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("\r\n[\r\n%s\r\n->\r\n%s\r\n] conf: %.1f ", body.toString(), head.toString(), confidence);
	}

	@Override
	public int compareTo(RuleWrapper rbmmRule) {
		
		//TODO compare two rules instances

		return -1;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

}
