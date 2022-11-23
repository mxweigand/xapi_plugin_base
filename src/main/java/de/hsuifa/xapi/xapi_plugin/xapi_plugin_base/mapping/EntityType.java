package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping;

import java.util.Collection;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Node;

public abstract class EntityType {
    
    private boolean isLiteral;
	private String entityTypeName;
	private Class<?> associatedClass;
    protected String baseUri;
	
    /**
     * constructor for ressource literals
     * @param entityTypeName
     * @param associatedClass
     */
	public EntityType(String baseUri, String entityTypeName, Class<?> associatedClass) {

        this.baseUri = baseUri;
		this.entityTypeName = entityTypeName;
		this.associatedClass = associatedClass;
		this.isLiteral = false;
		
	}
	
    /**
     * constructor for literal entities
     * @param entityTypeName
     * @param associatedClass
     * @param isLiteral
     */
	public EntityType(String baseUri, String entityTypeName, Class<?> associatedClass, boolean isLiteral) {
		
        this.baseUri = baseUri;
		this.entityTypeName = entityTypeName;
		this.associatedClass = associatedClass;
		this.isLiteral = isLiteral;

	}
		
    /**
     * get the class associated to the entity type
     * TODO: fix typo getAssoiciatedClass ---> getAssociatedClass
     * @return
     */
    public Class<?> getAssoiciatedClass() {
		
		return associatedClass;
		
	}
	
    /**
     * get the entity types name
     * @return
     */
	public String getEntityTypeName() {
		
		return entityTypeName;
		
	}
	
    /**
     * 
     * @return
     */
	public String getEntityTypeUri() {
		
		// abort if literal
		if (isLiteral()) { return null; }
		
		return baseUri + "#" + entityTypeName;
		
	}
	
    /**
     * check if entity is literal type
     * @return
     */
	public boolean isLiteral() {
		
		return isLiteral;
		
	}
	
    /**
     * combines some unique identifier of the given specific entity and the base uri to form an complete uri
     * @param specificElement
     * @return
     */
    public String getSpecificUri(Object specificElement) {

        // abort if literal
        if (isLiteral()) { 
            return null; 
        }

        return getSpecificUriImplSpecific(specificElement);

    };

	/**
     * needs to be overridden by the application specific implementation
     * <p> combines some unique identifier of the given specific entity and the base uri to form an complete uri
     * @param specificElement
     * @return
     */
	protected abstract String getSpecificUriImplSpecific(Object specificElement);

	/**
     * 
     * @param objectEntity
     * @return
     */
	public Node createNode(Object objectEntity) {
		
		if (isLiteral()) {
			return new Node(getAssoiciatedClass(),objectEntity);
		} 
		
		else if (!(isLiteral())) {
			return new Node(getSpecificUri(objectEntity));
		}
		
		else  { return null; }
		
	}
	
    /**
     * returns all entities of the present entity type
     * @return
     */
	public Collection<Object> getAllEntitiesOfType() {

		// abort if literal
		// method can only be used to get all subjects (which can never be literals)
		if (isLiteral()) { 
            return null; 
        }
	
        return getAllEntitiesOfTypeImplSpecific();

	}
	
    /**
     * needs to be overridden by the application specific implementation
     * <p> returns all entities of the present entity type
     * @return
     */
    protected abstract Collection<Object> getAllEntitiesOfTypeImplSpecific();

    /**
     * checks if a given node matches a given entity
     * <p> handles uri and literal nodes
     * @param middlewareNode
     * @param entity
     * @return
     */
	public boolean nodeMatchesEntity (Node middlewareNode, Object entity) {
		
		// uri
		if (middlewareNode.isUri() && getSpecificUri(entity).equals(middlewareNode.getUri())) 
		{
			return true;
		}
		
		// literal
		else if ( middlewareNode.isLiteral() &&  entity.equals(middlewareNode.getData()) ) 
		{
			return true;
		}

		// any theoretically not possible
		else if (middlewareNode.isAny()) { return false; }
		
		else { return false; }		
		
	}

}
