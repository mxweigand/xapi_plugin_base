package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph;

public class Triple {

	private Node subject;
	private Node predicate;
	private Node object;

	public Triple(Node subject, Node predicate, Node object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	public Node getSubject() {
		return subject;
	}

	public Node getPredicate() {
		return predicate;
	}

	public Node getObject() {
		return object;
	}

	public String getTripleAsString() {
		return getSubject().getNodeAsString() + "-" + getPredicate().getNodeAsString() + "-" + getObject().getNodeAsString();
	}

}