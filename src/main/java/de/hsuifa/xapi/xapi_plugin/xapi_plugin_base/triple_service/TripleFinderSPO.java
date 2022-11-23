package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.Collection;
import java.util.List;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntityType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;

public class TripleFinderSPO extends TripleFinder {

    public TripleFinderSPO(TripleHelper tripleHelper) {
        super(tripleHelper);
    }

    @Override
    protected void findSpecific(Triple triple) {  
				
		// get list of all possible relation types for defined relation uri
		// can be multiple as relations are defined 1:1 and not n:m as in RDF
		List<RelationType> relationTypes = tripleHelper.getRelationTypesByUri(triple.getPredicate().getUri());
		if (relationTypes==null) { return; }
		
		Object subject = tripleHelper.getEntityByUri(triple.getSubject().getUri());
		if (subject==null) { return; }
		
		// determine entity type of object
		List<EntityType> objectEntityTypeList = tripleHelper.getEntityTypeListByNode(triple.getObject());
		if (objectEntityTypeList==null) { return; }
		
		for (RelationType relationType: relationTypes) {
							
			Collection<Object> matchingRangeEntities = relationType.getObjectsForDefinedSubject(subject);
			if (matchingRangeEntities==null) { continue; }
			
			for (EntityType objectEntityType: objectEntityTypeList) {
				
				for (Object matchingRangeEntity: matchingRangeEntities) {
					
					if (objectEntityType.nodeMatchesEntity(triple.getObject(), matchingRangeEntity)) {
						
						addToResultList(subject, relationType, matchingRangeEntity);
					
					}
				
				}
			
			}
			
		}

	}

}
