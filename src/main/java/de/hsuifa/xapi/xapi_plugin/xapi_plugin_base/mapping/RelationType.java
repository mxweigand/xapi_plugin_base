package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class RelationType {
    
    private String relationTypeName;
    protected EntityType domainEntityType;
	private EntityType rangeEntityType;
	private boolean isDataProperty;
	private Class<?> inputClass;
	private Class<?> outputClass;
    private String baseUri;
	
	// constructor
	public RelationType(String baseUri, String relationTypeName, EntityType domainEntityType, EntityType rangeEntityType) {
		
		//
		this.baseUri = baseUri;
		this.relationTypeName = relationTypeName;
		this.domainEntityType = domainEntityType;
		this.rangeEntityType = rangeEntityType;
		
		// determine whether data or object property
		if (rangeEntityType.isLiteral()) {
			this.isDataProperty = true;
		} else {
			this.isDataProperty = false;
		}
		
		// get input/output class from domain/range entity type
		this.inputClass = domainEntityType.getAssoiciatedClass();
		this.outputClass = rangeEntityType.getAssoiciatedClass();
		
	}
	
	// to be overridden at instantiation
	public abstract Object getObjectsForDefinedSubjectAppSpecific(Object subject);
	
	public String getRelationTypeUri() {
		
		return baseUri + "#" + relationTypeName;
		
	}
	
	public boolean isDataProperty() {
		
		return isDataProperty;
		
	}
	
	public EntityType getDomainType() {
		
		return domainEntityType; 
		
	}
	
	public EntityType getRangeType() {
		
		return rangeEntityType; 
		
	}
	
    /**
     * check the types/number of found object(s)/subject(s) and return correct one(s)
     * TODO: Collection/List/Iterator??? -> check if collection+list is sufficient
     * @param subject
     * @return
     */
    public Collection<Object> getObjectsForDefinedSubject(Object subject) {
				
		// abort if input class is not correct
		if (!(inputClass.isInstance(subject))) { return null; }
		
		// app specific function can now safely be called
		Object uncheckedResult = getObjectsForDefinedSubjectAppSpecific(subject);
		
		// abort if result is null
		if (uncheckedResult == null) { return null;  }
				
		// if list or collection
		if (uncheckedResult instanceof Collection<?> | uncheckedResult instanceof List<?>) {
			
			// abort if empty list or collection (always cast to collection) 
			if (((Collection<?>) uncheckedResult).isEmpty()) { return null; }
					
			// create new empty collection
			List<Object> resultList = new ArrayList<Object>();
			
			((Collection<?>) uncheckedResult).forEach(singleResult -> {
				if (outputClass.isInstance(singleResult)) {
					resultList.add(singleResult);
				}
			});
			
			// if result list is empty or null return null
			if (resultList == null || resultList.isEmpty()) { return null; }

			// else return remaining collection
			else { return (Collection<Object>) resultList; }
			
		}
			
		// if no collection or list, i.e. single object, return it if class is correct
		else if (outputClass.isInstance(uncheckedResult)) {	return (Collection<Object>) Collections.singleton(uncheckedResult); } 
		
		// else return null
		else { return null; }

	}

}
