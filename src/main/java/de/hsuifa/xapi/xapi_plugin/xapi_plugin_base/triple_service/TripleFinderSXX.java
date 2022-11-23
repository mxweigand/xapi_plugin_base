package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.Collection;
import java.util.List;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntityType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;

public class TripleFinderSXX extends TripleFinder {

    public TripleFinderSXX(TripleHelper tripleHelper) {
        super(tripleHelper);
    }

    @Override
    protected void findSpecific(Triple triple) {  
				
		Object subject = tripleHelper.getEntityByUri(triple.getSubject().getUri());
		if (subject==null) { return ; }
		
		List<EntityType> entityTypes = tripleHelper.getEntityTypeListByNode(triple.getSubject());
		if (entityTypes==null) { return; }

		List<RelationType> relationTypes = tripleHelper.getRelationTypesByEntityTypeList(entityTypes, true);
				
		for (RelationType relationType: relationTypes) {
			
			// get all matching entities in range and add them
			Collection<Object> matchingRangeEntities = relationType.getObjectsForDefinedSubject(subject);
			addCollectionToResultList(subject, relationType, matchingRangeEntities);
			
		}
		
	}

}
