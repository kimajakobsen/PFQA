package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.tdb.TDBFactory;

import com.google.gson.Gson;

import dk.aau.cs.extbi.PFQA.helper.Config;

public class ProvenanceIndexBuilder {
	
	private String indexPath;

	public ProvenanceIndexBuilder(String indexPath) {
		this.indexPath = indexPath;
	}
	
	public ProvenanceIndex build() {
		if (getFileName(indexPath).equals("contextTreeIndex")) {
			Gson gson = new Gson();  
		     
			try {  
				BufferedReader br = new BufferedReader( new FileReader(indexPath));  
				ContextTreeIndex index = gson.fromJson(br, ContextTreeIndex.class);  
				return index; 
		 	} catch (IOException e) {
		 		return buildNewContextTreeIndex();
		 	}
		}
		return null;
	}

	private ProvenanceIndex buildNewContextTreeIndex() {
		// Tree is hardcoded to SSB qb4olap structure
		ContextTreeIndexNode<Resource> root = new ContextTreeIndexNode<Resource>(createResource("root"));
		{
			root.addChild(createResource("quantity"));
            root.addChild(createResource("extendedprice"));
            root.addChild(createResource("ordtotalprice"));
            root.addChild(createResource("discount"));
            root.addChild(createResource("revenue"));
            root.addChild(createResource("supplycost"));
            root.addChild(createResource("tax"));
            
            ContextTreeIndexNode<Resource> customer = root.addChild(createResource("customer"));
            {
                customer.addChild(createResource("custkey"));
                customer.addChild(createResource("name"));
                customer.addChild(createResource("address"));
                customer.addChild(createResource("city"));
                customer.addChild(createResource("nation"));
                customer.addChild(createResource("region"));
                customer.addChild(createResource("phone"));
                customer.addChild(createResource("mktsegment"));
                
            }
            ContextTreeIndexNode<Resource> part = root.addChild(createResource("part"));
            {
                part.addChild(createResource("partkey"));
                part.addChild(createResource("name"));
                part.addChild(createResource("mfgr"));
                part.addChild(createResource("category"));
                part.addChild(createResource("brand"));
                part.addChild(createResource("color"));
                part.addChild(createResource("type"));
                part.addChild(createResource("size"));
                part.addChild(createResource("container"));
            }
            ContextTreeIndexNode<Resource> supplier = root.addChild(createResource("supplier"));
            {
                supplier.addChild(createResource("supkey"));
                supplier.addChild(createResource("name"));
                supplier.addChild(createResource("address"));
                supplier.addChild(createResource("city"));
                supplier.addChild(createResource("nation"));
                supplier.addChild(createResource("region"));
                supplier.addChild(createResource("phone"));
            }
            ContextTreeIndexNode<Resource> orderDate = root.addChild(createResource("orderDate"));
            {
                orderDate.addChild(createResource("datekey"));
                orderDate.addChild(createResource("date"));
                orderDate.addChild(createResource("dayofweek"));
                orderDate.addChild(createResource("month"));
                orderDate.addChild(createResource("year"));
                orderDate.addChild(createResource("yeamonthnum"));
                orderDate.addChild(createResource("yearmonth"));
                orderDate.addChild(createResource("daynumweek"));
                orderDate.addChild(createResource("daynummonth"));
                orderDate.addChild(createResource("daynumyear"));
                orderDate.addChild(createResource("monthnuminyear"));
                orderDate.addChild(createResource("weeknuminyear"));
                orderDate.addChild(createResource("sellingseason"));
                orderDate.addChild(createResource("lastdayinweek"));
                orderDate.addChild(createResource("notlastdayinmonth"));
                orderDate.addChild(createResource("holiday"));
                orderDate.addChild(createResource("weekday"));
            }
            ContextTreeIndexNode<Resource> commitDate = root.addChild(createResource("commitDate"));
            {
                commitDate.addChild(createResource("datekey"));
                commitDate.addChild(createResource("date"));
                commitDate.addChild(createResource("dayofweek"));
                commitDate.addChild(createResource("month"));
                commitDate.addChild(createResource("year"));
                commitDate.addChild(createResource("yeamonthnum"));
                commitDate.addChild(createResource("yearmonth"));
                commitDate.addChild(createResource("daynumweek"));
                commitDate.addChild(createResource("daynummonth"));
                commitDate.addChild(createResource("daynumyear"));
                commitDate.addChild(createResource("monthnuminyear"));
                commitDate.addChild(createResource("weeknuminyear"));
                commitDate.addChild(createResource("sellingseason"));
                commitDate.addChild(createResource("lastdayinweek"));
                commitDate.addChild(createResource("notlastdayinmonth"));
                commitDate.addChild(createResource("holiday"));
                commitDate.addChild(createResource("weekday"));
		    	
		    }
		}
		
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.READ) ;
		//get cube
		for (ContextTreeIndexNode<Resource> parent : root.getChildren()) {
			parent.getData().getURI();
			if (parent.getChildren().size() == 0) {
				addContextValues(dataset, parent);
			}
			else {
				for (ContextTreeIndexNode<Resource> child : parent.getChildren()) {
					addContextValues(dataset, child);
				}
			}
		}
		dataset.end();
	
		ProvenanceIndex pi = new ContextTreeIndex(root);
		//Serialize to json and return the object
		return pi;
	}

	private void addContextValues(Dataset dataset,
			ContextTreeIndexNode<Resource> parent) {
		Query query = QueryFactory.create("SELECT ?G { GRAPH ?G { ?s <"+ parent.getData().getURI() +"> ?o . } } ");
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		ResultSet results = qexec.execSelect() ;
		for ( ; results.hasNext() ; ) {
		  QuerySolution soln = results.nextSolution() ;
		  Resource node  = ResourceFactory.createResource(soln.get("?G").toString());
		  parent.addChild(node);
		}
	}

	private Resource createResource(String name) {
		return ResourceFactory.createResource(Config.getNamespace()+name);
	}

	private String getFileName(String indexPath) {
		String[] split1 = indexPath.split("/");
		String[] split2 = split1[split1.length-1].split("\\.");
		return split2[0];
	}
}
