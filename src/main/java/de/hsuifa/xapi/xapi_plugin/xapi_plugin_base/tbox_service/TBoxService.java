package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.tbox_service;

import java.util.ArrayList;
import java.util.List;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Node;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntitySuperType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.EntityType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationSuperType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.RelationType;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.mapping.TypeList;

public class TBoxService {
    
    private String tboxTTLString;
    private List<Triple> tboxTripleList = new ArrayList<Triple>();
    private List<EntityType> entityTypeList;
    private List<RelationType> relationTypeList;

    public TBoxService(TypeList typeList) {

        this.entityTypeList = typeList.getEntityTypeList();
        this.relationTypeList = typeList.getRelationTypeList();
        generateTboxTTLString();
        generateTboxTripleList();

    }

    /**
     * get the tbox as list of triples
     * @return
     */
    public List<Triple> getTboxTripleList() {
        return tboxTripleList;
    };

    /**
     * get the tbox as string in TTL syntax
     * @deprecated
     */
    public String getTboxTTLString() {
        return tboxTTLString;
    };

    /**
     * method to initially generate the tbox as list of triples 
     */
    private void generateTboxTripleList() {

        // entities
        for (EntityType entityType: entityTypeList) {

            // skip if defintion type or literal
            if (entityType instanceof EntitySuperType || entityType.isLiteral()) { continue; }

            // define entity as rdf:type
            tboxTripleList.add(new Triple(
                new Node(entityType.getEntityTypeUri()), 
                new Node("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                new Node("http://www.w3.org/2002/07/owl#Class") 
            ));
        
        }

        // relations   
        for (RelationType relationType: relationTypeList) {

            // skip if defintion type
            if (relationType instanceof RelationSuperType ) { continue; }

            // for data properties
            if (relationType.isDataProperty()) {
                // define as data property
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                    new Node("http://www.w3.org/2002/07/owl#DatatypeProperty") 
                ));
                // subproperty of top data property
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/2000/01/rdf-schema#subPropertyOf"),
                    new Node("http://www.w3.org/2002/07/owl#topDataProperty") 
                ));
                // domain
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/2000/01/rdf-schema#domain"),
                    new Node(relationType.getDomainType().getEntityTypeUri()) 
                ));
                // range
                // TODO create universal mapping function in middleware package and reference it here
                // xsd string 
                String xsdTypeString = null;
                if ( relationType.getRangeType().getAssoiciatedClass().equals(String.class) ) {
                    xsdTypeString = "string";
                }
                // xsd double 
                if ( relationType.getRangeType().getAssoiciatedClass().equals(Double.class) ) {
                    xsdTypeString = "double";
                }
                // xsd literal 
                if ( relationType.getRangeType().getAssoiciatedClass().equals(Integer.class) ) {
                    xsdTypeString = "integer";
                }
                // xsd boolean 
                if ( relationType.getRangeType().getAssoiciatedClass().equals(Boolean.class) ) {
                    xsdTypeString = "boolean";
                }
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/2000/01/rdf-schema#range"),
                    new Node("http://www.w3.org/2001/XMLSchema#xsd:" + xsdTypeString) 
                ));

            }

            // for obejct properties
            else {
                // define as object property
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                    new Node("http://www.w3.org/2002/07/owl#ObjectProperty") 
                ));
                // subproperty of top object property
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/2000/01/rdf-schema#subPropertyOf"),
                    new Node("http://www.w3.org/2002/07/owl#topObjectProperty") 
                ));
                // domain
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/2000/01/rdf-schema#domain"),
                    new Node(relationType.getDomainType().getEntityTypeUri()) 
                ));
                // range
                tboxTripleList.add(new Triple(
                    new Node(relationType.getRelationTypeUri()), 
                    new Node("http://www.w3.org/2000/01/rdf-schema#range"),
                    new Node(relationType.getRangeType().getEntityTypeUri()) 
                ));

            }

        }

    }

    /**
     * method to initially generate the tbox as string in TTL syntax
     * @deprecated
     */
    private void generateTboxTTLString() {

         StringBuilder stringBuilder = new StringBuilder();
        
        // add prefixes
        stringBuilder.append(
            "@prefix owl:  <http://www.w3.org/2002/07/owl#> . " + "\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . " + "\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " + "\n" +
            "@prefix xsd:  <http://www.w3.org/2001/XMLSchema#> . " + "\n"
            );

        // entities
        for (EntityType entityType: entityTypeList) {

            // skip if defintion type or literal
            if (entityType instanceof EntitySuperType || entityType.isLiteral()) { continue; }

            // define entity as rdf:type
            stringBuilder.append(
                "<" + 
                entityType.getEntityTypeUri() + 
                ">" + 
                " rdf:type owl:Class . " + 
                "\n"
                );
        
        }

        // relations   
        for (RelationType relationType: relationTypeList) {

            // skip if defintion type
            if (relationType instanceof RelationSuperType ) { continue; }

            // for data properties
            if (relationType.isDataProperty()) {
                
                stringBuilder.append(
                    // define realtion as data property
                    "<" + relationType.getRelationTypeUri() + "> rdf:type owl:DatatypeProperty . \n" +
                    "<" + relationType.getRelationTypeUri() + "> rdfs:subPropertyOf owl:topDataProperty . \n" +
                    // domain
                    "<" + relationType.getRelationTypeUri() + "> rdfs:domain <" + relationType.getDomainType().getEntityTypeUri() + "> . \n"
                );

                // range
                // TODO create universal mapping function in middleware package and reference it here
                // xsd string 
                if ( relationType.getRangeType().getAssoiciatedClass().equals(String.class) ) {
                    stringBuilder.append("<" + relationType.getRelationTypeUri() + "> rdfs:range xsd:string . \n");
                }
                // xsd double 
                if ( relationType.getRangeType().getAssoiciatedClass().equals(Double.class) ) {
                    stringBuilder.append("<" + relationType.getRelationTypeUri() + "> rdfs:range xsd:double . \n");
                }
                // xsd literal 
                if ( relationType.getRangeType().getAssoiciatedClass().equals(Integer.class) ) {
                    stringBuilder.append("<" + relationType.getRelationTypeUri() + "> rdfs:range xsd:integer . \n");
                }
                // xsd boolean 
                if ( relationType.getRangeType().getAssoiciatedClass().equals(Boolean.class) ) {
                    stringBuilder.append("<" + relationType.getRelationTypeUri() + "> rdfs:range xsd:boolean . \n");
                }

            }

            // for obejct properties
            else {
                stringBuilder.append(
                    // define realtion as object property
                    "<" + relationType.getRelationTypeUri() + "> rdf:type owl:ObjectProperty . \n" +
                    "<" + relationType.getRelationTypeUri() + "> rdfs:subPropertyOf owl:topObjectProperty . \n" +
                    // domain
                    "<" + relationType.getRelationTypeUri() + "> rdfs:domain <" + relationType.getDomainType().getEntityTypeUri() + "> . \n" +
                    // range
                    "<" + relationType.getRelationTypeUri() + "> rdfs:range <" + relationType.getRangeType().getEntityTypeUri() + "> . \n"
                    );
            }

        }

        // wirte as one string
        tboxTTLString = stringBuilder.toString();

    };

}
