package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Node;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntitySuperType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntityType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.TypeList;
/**
 * helper class with methods used by multiple TripleFinders
 * implementations deliver additional app-specific helper methods
 */
public abstract class TripleHelper {

	protected TypeList typeList;
	protected List<EntityType> entityTypeList;
	protected List<RelationType> relationTypeList;
	protected String baseUri;

	public TripleHelper(TypeList typeList) {
		this.typeList = typeList;
		this.baseUri = typeList.getBaseUri();
		this.entityTypeList = typeList.getEntityTypeList();
        this.relationTypeList = typeList.getRelationTypeList();
	}

	/**
	 * 
	 * @return
	 */
	public String getBaseUri() {
		return baseUri;
	};

	protected abstract Object getEntityByUri(String uri);

	protected TypeList getTypeList() {
		return typeList;
	}

	/**
	 * 
	 * @return
	 */
	public List<RelationType> getAllRelationTypes() {
		
		// abort if empty or null
		if (relationTypeList.isEmpty()||relationTypeList==null) { 
			throw new Error("no type list defined");
		}
		
		// just return list of relation types
		return relationTypeList;
		
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public List<EntityType> getEntityTypeListByNode(Node node) {
		
		// if uri node
		if (node.isUri()) {
			return getEntityTypeByEntityUri(node.getUri());
		}
		
		// if literal node
		else if (node.isLiteral()) {
			return getEntityTypeByDataType(node.getDataType());
		}
		
		// else
		else { return null; }
		
	}

	private List<EntityType> getEntityTypeByEntityUri(String entityUri) {
		
		// abort if incorrect uri was given
		if (!(getBaseUri().equals(entityUri.substring(0, getBaseUri().length())))) { return null; }
		
		// init empty list
		List<EntityType> returnList = new ArrayList<EntityType>();
		
		// look up if uri is type definition (rdf:type) 
		for (EntityType entityType: entityTypeList) {
			
			if (entityType instanceof EntitySuperType && entityType.getEntityTypeUri().equals(entityUri)) {
				return Collections.singletonList(entityType);
			}
			
			else if (!(entityType instanceof EntitySuperType) && !(entityType.isLiteral()) && entityType.getAssoiciatedClass().isInstance(getEntityByUri(entityUri))) {
				returnList.add(entityType);
			}
			
		}
		
		// return if not empty
		if (returnList.isEmpty()) { return null; }
		else { return returnList; }

	}

	private List<EntityType> getEntityTypeByDataType(Class<?> dataType) {
		
		EntityType returnEntityType = entityTypeList.stream().filter(entityType -> dataType.equals(entityType.getAssoiciatedClass())).findAny().orElse(null);
		
		if (returnEntityType==null) { return null; }
		else { return Collections.singletonList(returnEntityType); }
	
	}

	public List<RelationType> getRelationTypesByEntityTypeList(List<EntityType> entityTypeList, boolean subjectEntityTypeGiven) {
		
		List<RelationType> returnList = new ArrayList<RelationType>();
		
		for (EntityType entityType: entityTypeList) {
			
			List<RelationType> subReturnList = getRelationTypesByEntityType(entityType,subjectEntityTypeGiven);
			
			if (subReturnList==null) { continue; }
			else { returnList.addAll(subReturnList); }
			
		}
		
		if (returnList.isEmpty()) { return null; }
		else { return returnList; }		
		
	}

	// relation types from relation type list
	// returns a list, because relation type is 1-to-1
	// whereas in RDF object properties are n-to-m 
	public List<RelationType> getRelationTypesByUri(String uri) {
			
		List<RelationType> returnList = relationTypeList.stream().filter(relationTypeFromList -> uri.equals(relationTypeFromList.getRelationTypeUri())).collect(Collectors.toList());		
		if (returnList.isEmpty()) { return null; } 
		return returnList;
		
	}

	// find all matching relation types for subject or object entity type
	private List<RelationType> getRelationTypesByEntityType(EntityType entityType, boolean subjectEntityTypeGiven) {
		
		// if subject class is given 
		if (subjectEntityTypeGiven) {
			
			// filter list of all relation types
			List<RelationType> returnList = relationTypeList.stream()
					.filter(relationTypeFromList -> relationTypeFromList.getDomainType().equals(entityType))
					.collect(Collectors.toList());	
			
			// abort if empty or null
			if (returnList.isEmpty()||returnList==null) { return null; }
			else { return returnList; }
			
		}
		
		// if object class is given
		else {
			
			// filter list of all relation types
			List<RelationType> returnList = relationTypeList.stream()
					.filter(relationTypeFromList -> relationTypeFromList.getRangeType().equals(entityType))
					.collect(Collectors.toList());
			
			// abort if empty or null
			if (returnList.isEmpty()||returnList==null) { return null; }
			else { return returnList; }
			
		}
		
	}

}
