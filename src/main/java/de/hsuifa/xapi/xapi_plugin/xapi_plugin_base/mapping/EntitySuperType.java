package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping;

import java.util.Collection;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Node;

public class EntitySuperType extends EntityType {

	private EntityType definedEntityType;
	
	public EntitySuperType(String baseUri, EntityType definedEntityType) {
		
		super(baseUri, null, null, false);
		this.definedEntityType = definedEntityType;
		
	}

	@Override
	public String getSpecificUri(Object specificElement) {
		return null; 
	}
    
    @Override
    protected String getSpecificUriImplSpecific(Object specificElement) {
        return null;
    }

	@Override
	public String getEntityTypeUri() {
		
		return definedEntityType.getEntityTypeUri();
		
	}
	
	@Override
	public Node createNode(Object objectEntity) {
		
		return new Node(getEntityTypeUri());
		
	}
	
	@Override
	public Collection<Object> getAllEntitiesOfType() {
		return null;
	}
    
    @Override
    protected Collection<Object> getAllEntitiesOfTypeImplSpecific() {
        return null;
    }
	
	@Override
	public boolean nodeMatchesEntity (Node middlewareNode, Object entity) {
		
		if (middlewareNode.isUri() && getEntityTypeUri().equals(middlewareNode.getUri()) && entity instanceof DummyRangeObjectRDFType ) { 
			return true; 
		}
		else { return false; }		
		
	}

}
