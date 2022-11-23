package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.Collection;
import java.util.List;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntityType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;

public class TripleFinderXPX extends TripleFinder {

    public TripleFinderXPX(TripleHelper tripleHelper) {
        super(tripleHelper);
    }

    @Override
    protected void findSpecific(Triple triple) {  
				
        List<RelationType> relationTypes = tripleHelper.getRelationTypesByUri(triple.getPredicate().getUri());
		if (relationTypes==null) { return; }
		
		for (RelationType relationType: relationTypes) {
			
			EntityType domainEntityType = relationType.getDomainType();

			Collection<Object> possibleDomainEntities = domainEntityType.getAllEntitiesOfType();
			if (possibleDomainEntities == null ) { continue; }
						
			for (Object possibleDomainEntity: possibleDomainEntities) {

				// get all matching entities in range and add them
				Collection<Object> matchingRangeEntities = relationType.getObjectsForDefinedSubject(possibleDomainEntity);
				addCollectionToResultList(possibleDomainEntity, relationType, matchingRangeEntities);
				
			}
		
		}
		
	}

}
