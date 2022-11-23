package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.TypeList;

public abstract class TripleService {
    
	protected TripleHelper tripleHelper;

	protected TripleFinderSPO tripleFinderSPO;
	protected TripleFinderXPO tripleFinderXPO;
	protected TripleFinderSXO tripleFinderSXO;
	protected TripleFinderSPX tripleFinderSPX;
	protected TripleFinderSXX tripleFinderSXX;
	protected TripleFinderXPX tripleFinderXPX;
	protected TripleFinderXXO tripleFinderXXO;
	protected TripleFinderXXX tripleFinderXXX;

	/**
	 * constructor
	 * @param tripleHelper
	 */
	public TripleService(TripleHelper tripleHelper) {

		this.tripleHelper = tripleHelper;

		this.tripleFinderSPO = new TripleFinderSPO(tripleHelper);
		this.tripleFinderXPO = new TripleFinderXPO(tripleHelper);
		this.tripleFinderSXO = new TripleFinderSXO(tripleHelper);
		this.tripleFinderSPX = new TripleFinderSPX(tripleHelper);
		this.tripleFinderSXX = new TripleFinderSXX(tripleHelper);
		this.tripleFinderXPX = new TripleFinderXPX(tripleHelper);
		this.tripleFinderXXO = new TripleFinderXXO(tripleHelper);
		this.tripleFinderXXX = new TripleFinderXXX(tripleHelper);

	}

	public TypeList getTypeList() {

		return tripleHelper.getTypeList();
		
	}

	/**
	 * main method to find triples
	 * @param triple
	 * @return
	 */
	public List<Triple> findTriple(Triple triple) {

		// abort if requested triple is null
		if (triple == null) {return null;}

        // get unbound nodes
		boolean s = !(triple.getSubject().isAny());
		boolean p = !(triple.getPredicate().isAny());
		boolean o = !(triple.getObject().isAny());
		
		// init
		List<Triple> resultTripleList = new ArrayList<Triple>();

		// redirect to right finder service
		if      ( s &  p &  o) { resultTripleList = tripleFinderSPO.find(triple); }
		else if (!s &  p &  o) { resultTripleList = tripleFinderXPO.find(triple); }	
		else if ( s & !p &  o) { resultTripleList = tripleFinderSXO.find(triple); }	
		else if ( s &  p & !o) { resultTripleList = tripleFinderSPX.find(triple); }	
		else if ( s & !p & !o) { resultTripleList = tripleFinderSXX.find(triple); }	
		else if (!s &  p & !o) { resultTripleList = tripleFinderXPX.find(triple); }
		else if (!s & !p &  o) { resultTripleList = tripleFinderXXO.find(triple); }	
		else if (!s & !p & !o) { resultTripleList = tripleFinderXXX.find(triple); }

		// abort if null
		if (resultTripleList == null || resultTripleList.isEmpty() ) {return null;}

		// remove duplicates and nulls 
		List<Triple> checkedMatches = resultTripleList.stream()
				// remove duplicates
				.distinct()
				// filter nulls
				.filter(match -> (match != null))
				// collect as list
				.collect(Collectors.toList());

		// return if not null or empty
		if (checkedMatches == null || checkedMatches.isEmpty()) { return null; }
		else { return checkedMatches; }

    }

}
