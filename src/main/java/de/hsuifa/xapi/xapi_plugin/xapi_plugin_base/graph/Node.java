package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph;

public class Node {

	private boolean isAny;
	private boolean isUri;
	private boolean isLiteral;
	private boolean hasLanguageString;
	private String uri;
	private Class<?> dataType;
	private Object data;
	private String languageString;
	
	
	// constructor to create any node
	public Node() {
		this.isAny = true;
		this.isUri = false;
		this.isLiteral = false;
		this.uri = null;
		this.dataType = null;
		this.data = null;
		this.languageString = null;
		this.hasLanguageString = false;
	}
	
	// constructor to create uri node
	public Node(String uri) {
		this.isAny = false;
		this.isUri = true;
		this.isLiteral = false;
		this.uri = uri;
		this.dataType = null;
		this.data = null;
		this.languageString = null;
		this.hasLanguageString = false;
	} 
	
	// constructor to create literal node
	public Node(Class<?> dataType, Object data) {
		this.isAny = false;
		this.isUri = false;
		this.isLiteral = true;
		this.uri = null;
		this.dataType = dataType;
		this.data = data;
		this.languageString = null;
		this.hasLanguageString = false;
	}
	
	// constructor to create literal node with language string
	public Node(Class<?> dataType, Object data, String languageString) {
		this.isAny = false;
		this.isUri = false;
		this.isLiteral = true;
		this.uri = null;
		this.dataType = dataType;
		this.data = data;
		this.languageString = languageString;
		this.hasLanguageString = true;
	}
	
	public boolean isAny() {
		return isAny;
	}
	
	public boolean isUri() {
		return isUri;
	}

	public boolean isLiteral() {
		return isLiteral;
	}
	
	public String getNodeAsString() {
		if (isAny) { 
			return "[any|?]"; 	
		} 
		else if (isUri) {
			return "[uri|" + uri + "]";			
		} 
		else if (isLiteral&hasLanguageString) {	
			return "[literal|" + data.toString() + "@" + languageString + "]";
		} 
		else if (isLiteral&!hasLanguageString) {	
			return "[literal|" + data.toString() + "]";
		}  
		else {
			return null;
		}
	}
	
	public String getUri() {
		if (isUri) { 
			return uri;	
		} 
		else {
			return null;			
		} 
	}
	
	public Class<?> getDataType() {
		if (isLiteral) { 
			return dataType;	
		} 
		else {
			return null;			
		} 
	}
	
	public Object getData() {
		if (isLiteral) { 
			return data;	
		} 
		else {
			return null;			
		} 
	}
	
	public boolean hasLanguageString() {
		return hasLanguageString;	
	}
	
	public String getLanguageString() {
		if (hasLanguageString) { 
			return languageString;	
		} 
		else {
			return null;			
		} 
	}

}