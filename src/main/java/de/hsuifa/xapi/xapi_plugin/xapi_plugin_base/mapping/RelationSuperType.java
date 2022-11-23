package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping;

import java.util.Collection;
import java.util.Collections;

public class RelationSuperType extends RelationType {

	public RelationSuperType(EntityType domainEntityType, EntityType rangeEntityType) {
		super(null, null, domainEntityType, rangeEntityType);
	}
	
	@Override
	public boolean isDataProperty() {
		return false;
	}
	
	@Override
	public String getRelationTypeUri() {
		return "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	}
	
	@Override
	public Collection<Object> getObjectsForDefinedSubject(Object subject) {
				
		if (getDomainType().getAssoiciatedClass().isInstance(subject)) { 
			return (Collection<Object>) Collections.singleton((Object) new DummyRangeObjectRDFType() );
		} else { 
			return null; 
		}
		
	}

    @Override
    public Object getObjectsForDefinedSubjectAppSpecific(Object subject) {
        return null;
    }

}
