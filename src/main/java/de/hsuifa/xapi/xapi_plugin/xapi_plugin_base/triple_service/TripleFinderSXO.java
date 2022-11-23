package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.Collection;
import java.util.List;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntityType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;

public class TripleFinderSXO extends TripleFinder {

    public TripleFinderSXO(TripleHelper tripleHelper) {
        super(tripleHelper);
    }

    @Override
    protected void findSpecific(Triple triple) {  
				
		Object subject = tripleHelper.getEntityByUri(triple.getSubject().getUri());
		if (subject==null) { return ; }	
		
		// determine entity type of object
		List<EntityType> objectEntityTypeList = tripleHelper.getEntityTypeListByNode(triple.getObject());
		if (objectEntityTypeList==null) { return; }
		
		List<RelationType> relationTypes = tripleHelper.getAllRelationTypes();
		if (relationTypes==null) { return; }
		
		for (RelationType relationType: relationTypes) {
			
			Collection<Object> matchingRangeEntities = relationType.getObjectsForDefinedSubject(subject);
			if (matchingRangeEntities == null ) { continue; }
			
			for (Object matchingRangeEntity: matchingRangeEntities) {
				
				for (EntityType objectEntityType: objectEntityTypeList) {
					
					if (objectEntityType.nodeMatchesEntity(triple.getObject(), matchingRangeEntity)) {
						
						addToResultList(subject, relationType, matchingRangeEntity);
						
					}
				
				}
		
			}
			
		}
		
	}

}
