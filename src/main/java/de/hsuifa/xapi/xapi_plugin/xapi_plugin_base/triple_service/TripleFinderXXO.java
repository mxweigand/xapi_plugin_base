package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.Collection;
import java.util.List;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntityType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;

public class TripleFinderXXO extends TripleFinder {

    public TripleFinderXXO(TripleHelper tripleHelper) {
        super(tripleHelper);
    }

    @Override
    protected void findSpecific(Triple triple) {  
				
		// determine entity type of object
		List<EntityType> objectEntityTypeList = tripleHelper.getEntityTypeListByNode(triple.getObject());
		if (objectEntityTypeList==null) { return; }
		
		// get list of matching relations
		List<RelationType> relationTypes = tripleHelper.getRelationTypesByEntityTypeList(objectEntityTypeList, false);
		if (relationTypes==null) { return; }
		
		for (RelationType relationType: relationTypes) {
								
			// get domain entity type
			EntityType domainEntityType = relationType.getDomainType();

			// get all possible objects for domain type
			Collection<Object> possibleDomainEntities = domainEntityType.getAllEntitiesOfType();
			if (possibleDomainEntities == null ) {  continue; }
			
			for (Object possibleDomainEntity: possibleDomainEntities) {
								
				// get collection of all entities in the range for this specific domain entity via current relation
				Collection<Object> possibleRangeEntities = relationType.getObjectsForDefinedSubject(possibleDomainEntity);
				if (possibleRangeEntities==null) {  continue; }				
				
				for (Object possibleRangeEntity: possibleRangeEntities) {
				
					for (EntityType objectEntityType: objectEntityTypeList) {
						
						if (objectEntityType.nodeMatchesEntity(triple.getObject(), possibleRangeEntity)) {
							
							addToResultList(possibleDomainEntity, relationType, possibleRangeEntity);
							
						}
					
					}
			
				}
				
			}
		
		}
		
	}

}
