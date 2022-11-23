package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Node;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;

public abstract class TripleFinder {
 
    protected List<Triple> resultTripleList = new ArrayList<Triple>();
    protected TripleHelper tripleHelper;

    public TripleFinder (TripleHelper tripleHelper) {
        this.tripleHelper = tripleHelper;
    }

    /**
     * find triple
     * @param Triple
     */
    public List<Triple> find(Triple triple) {

        resultTripleList.clear();
        findSpecific(triple);
        return resultTripleList;

    };
 
    /**
     * this method actually finds triples
     * @param triple
     * @return
     */
    protected abstract void findSpecific(Triple triple);

	/**
     * add multiple triples from results and add to result triple list
     * @param subjectEntity
     * @param predicateRelationType
     * @param objectEntities
     */
    protected void addCollectionToResultList (
			Object subjectEntity,
			RelationType predicateRelationType,
			Collection<Object> objectEntities) {
		
		// TODO: add some other checks?
		if (objectEntities == null || objectEntities.isEmpty()) { return; }
		
		for (Object objectEntity: objectEntities) {
			
			addToResultList(subjectEntity,predicateRelationType,objectEntity);
						
		}
		
	}
	
	/**
     * add single result
     * @param subjectEntity
     * @param predicateRelationType
     * @param objectEntity
     */
    protected void addToResultList (
			Object subjectEntity,
			RelationType predicateRelationType,
			Object objectEntity) {
		
		// TODO: add some other checks?
		if (objectEntity == null) { return; }
		
		// create nodes for subject, predicate, object
		Node subjectNode = new Node(predicateRelationType.getDomainType().getSpecificUri(subjectEntity)); 
		Node predicateNode = new Node(predicateRelationType.getRelationTypeUri());
		Node objectNode = predicateRelationType.getRangeType().createNode(objectEntity);
		
		// abort if any of them is null
		if ( subjectNode == null || predicateNode == null || objectNode == null ) { return; }
		
		// create triple and add to result list
		this.resultTripleList.add(new Triple(subjectNode,predicateNode,objectNode));
		
	}

}
