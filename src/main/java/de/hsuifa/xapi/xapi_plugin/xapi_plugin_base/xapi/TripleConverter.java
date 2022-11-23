package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.xapi;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Node;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;

public class TripleConverter {
	
	public JSONArray tripleListToJson (List<Triple> tripleList) {
		
		// return empty json array if no results have been found
		if (tripleList==null || tripleList.size() == 0) { return new JSONArray().put(JSONObject.NULL); };

		// init
		JSONArray tripleListAsJson = new JSONArray();

		// put all triple in list as json array to upper array
		for (Triple triple: tripleList) {
			tripleListAsJson.put(tripleToJson(triple));
		}

		// return
		return tripleListAsJson;

	}

    private JSONArray tripleToJson(Triple triple) {

		// init
        JSONArray tripleAsJson = new JSONArray();

        // convert s, p, o
        tripleAsJson.put(nodeToJson(triple.getSubject()));
        tripleAsJson.put(nodeToJson(triple.getPredicate()));
        tripleAsJson.put(nodeToJson(triple.getObject()));

        // return
        return tripleAsJson;

    }

    private JSONArray nodeToJson(Node node) {

        JSONArray nodeAsJson = new JSONArray();    
        
		// if any
		if (node.isAny()) {
			nodeAsJson.put(true);
            nodeAsJson.put(JSONObject.NULL);
            nodeAsJson.put(JSONObject.NULL);
		}

		// if uri
		else if (node.isUri()) {
            nodeAsJson.put(false);
            nodeAsJson.put(0);
            nodeAsJson.put(node.getUri());
		}
				
		// if literal 
		else if (node.isLiteral()) {
			
			nodeAsJson.put(false);

			// if error
			if (node.getDataType()==null) { 
				return nullNodeJson();
			}

			// if string
			else if (node.getDataType().equals(String.class)) { 
				nodeAsJson.put(1);
			}

			// if double
			else if (node.getDataType().equals(Double.class)) { 
				nodeAsJson.put(2);
			}

			// if integer
			else if (node.getDataType().equals(Integer.class)) { 
				nodeAsJson.put(3);
			}

			// if boolean
			else if (node.getDataType().equals(Boolean.class)) { 
				nodeAsJson.put(4);
			}

			// if there is no mapping (i.e. the previous 4 types didnt match) return null
			else { 
				return nullNodeJson(); 
			}

			nodeAsJson.put(node.getData()); 

		}

		else {
			// TODO: better error handling
            return nullNodeJson();
		}

        return nodeAsJson;
    
    }

	private JSONArray nullNodeJson() {

		JSONArray nullNodeJson = new JSONArray(); 
		nullNodeJson.put(JSONObject.NULL);
		nullNodeJson.put(JSONObject.NULL);
		nullNodeJson.put(JSONObject.NULL);
		return nullNodeJson;

	}

	public Triple jsonToTriple (JSONArray tripleAsJsonArray) {
		
		// continue only inf exacly 3 entries
		if (tripleAsJsonArray.length()!=3) { return null; }
		
		// get all nodes and return as triple, abort if any node is null
		try {
			Node subject = jsonToNode(tripleAsJsonArray.getJSONArray(0));
			Node predicate = jsonToNode(tripleAsJsonArray.getJSONArray(1));
			Node object = jsonToNode(tripleAsJsonArray.getJSONArray(2));
			if (subject == null | predicate == null | object == null) { return null; }
			return new Triple(subject, predicate, object);
		} 
		// if JSONEception is thrown, return null
		catch (JSONException e) {
			// e.printStackTrace();
			return null;
		}
		
	}

	private Node jsonToNode (JSONArray nodeAsJsonArray) {

		Node returnNode = null;

		// return null if null
		// or if any, as it is not possible in an answer 
		if (nodeAsJsonArray.get(0).equals(JSONObject.NULL)) {
			returnNode = null;
		}

		// if any
		else if (nodeAsJsonArray.getBoolean(0)) {
			returnNode = new Node();
		}

		// if uri or literal
		else if (!(nodeAsJsonArray.getBoolean(0))) {

			switch(nodeAsJsonArray.getInt(1)) {	
				// uri
				case 0:
					String uri = nodeAsJsonArray.getString(2);
					if (uri.equals(null) || uri.equals("")) {
						returnNode = null;
						// handle error
						// TODO: find out why uri "" can be requested by jena???
					} else{
						returnNode = new Node(nodeAsJsonArray.getString(2));
					}
					break;
				// string
				case 1:
					returnNode = new Node(String.class, nodeAsJsonArray.getString(2));
					break;
				// double
				case 2:
					returnNode =  new Node(Double.class, nodeAsJsonArray.getDouble(2));
					break;
				// integer				
				case 3:
					returnNode =  new Node(Integer.class, nodeAsJsonArray.getInt(2));
					break;
				// boolean
				case 4:
					returnNode =  new Node(Boolean.class, nodeAsJsonArray.getBoolean(2));
					break;
				default:
					break;
			}

		}

		return returnNode;

	}

}
