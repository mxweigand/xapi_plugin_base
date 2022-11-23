package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping;

import java.util.ArrayList;
import java.util.List;

public abstract class TypeList {

    protected String baseUri;
	protected List<EntityType> entityTypeList = new ArrayList<EntityType>();
	protected List<RelationType> relationTypeList = new ArrayList<RelationType>();

    public TypeList(String baseUri) {

        //
        this.baseUri = baseUri;

        // add all EntityTypes and RelationTypes
        addTypes();

        // add all SuperEntityTypes and SuperRelationTypes
        addSuperTypes();

    }

    public List<EntityType> getEntityTypeList() { 
        return entityTypeList;
    }
	
    public List<RelationType> getRelationTypeList() { 
        return relationTypeList;
    }

    /**
     * 
     * @return
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * adds all existing types (entities and realtions) to the type list
     * this needs to be overridden by application-specific implementation
     */
    protected abstract void addTypes();

    private void addSuperTypes() {

        List<EntityType> definitionEntityTypeList = new ArrayList<EntityType>();
		
		for (EntityType entityType: entityTypeList) {
			
			if (entityType.isLiteral()) { continue; }
			
			EntityType definitionEntityType = new EntitySuperType(baseUri, entityType);
			definitionEntityTypeList.add(definitionEntityType);
			
			RelationType definitionRelationType = new RelationSuperType(entityType,definitionEntityType);
			relationTypeList.add(definitionRelationType);
			
		}
		
		entityTypeList.addAll(definitionEntityTypeList);

    }

}
