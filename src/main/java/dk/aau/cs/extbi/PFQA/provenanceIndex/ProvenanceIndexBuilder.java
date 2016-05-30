package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.Instant;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.tdb.TDBFactory;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.logger.Logger;

public class ProvenanceIndexBuilder {
	
	private String indexPath;

	public ProvenanceIndexBuilder(String indexPath) {
		this.indexPath = indexPath+".ser";
	}
	
	public ProvenanceIndex build() {
//		ProvenanceIndex index = null;
		if (getFileName(indexPath).equals("contextTreeIndex")) {
//			try {  
//				Logger logger = Logger.getInstance();
//				FileInputStream fileIn = new FileInputStream(Config.getDatasetLocation()+indexPath);
//				ObjectInputStream in = new ObjectInputStream(fileIn);
//				
//				logger.startReadIndex();
//				index = (ProvenanceIndex) in.readObject();
//				logger.endReadIndex();
//				
//				in.close();
//				fileIn.close();
//				return index; 
//		 	} catch (IOException e) {
		 		return buildNewContextTreeIndex();
//		 	} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
		} else if (getFileName(indexPath).equals("none")) {
			return new none();
		}
		throw new IllegalArgumentException("The supplied index is not of known type ("+getFileName(indexPath)+")");
	}

	private ProvenanceIndex buildNewContextTreeIndex() {
		// Tree is hardcoded to SSB qb4olap structure
		Logger logger = Logger.getInstance();
		logger.startBuildIndex();
		Instant start = Instant.now();
		ContextTreeIndexNode<String> root = new ContextTreeIndexNode<String>("root");
		{
			root.addChild(createURI("quantity"));
            root.addChild(createURI("extendedprice"));
            root.addChild(createURI("ordtotalprice"));
            root.addChild(createURI("discount"));
            root.addChild(createURI("revenue"));
            root.addChild(createURI("supplycost"));
            root.addChild(createURI("tax"));
            
            ContextTreeIndexNode<String> customer = root.addChild(createURI("customer"));
            {
                customer.addChild(createURI("custkey"));
                customer.addChild(createURI("name"));
                customer.addChild(createURI("address"));
                customer.addChild(createURI("city"));
                customer.addChild(createURI("nation"));
                customer.addChild(createURI("region"));
                customer.addChild(createURI("phone"));
                customer.addChild(createURI("mktsegment"));
                
            }
            ContextTreeIndexNode<String> part = root.addChild(createURI("part"));
            {
                part.addChild(createURI("partkey"));
                part.addChild(createURI("name"));
                part.addChild(createURI("mfgr"));
                part.addChild(createURI("category"));
                part.addChild(createURI("brand"));
                part.addChild(createURI("color"));
                part.addChild(createURI("type"));
                part.addChild(createURI("size"));
                part.addChild(createURI("container"));
            }
            ContextTreeIndexNode<String> supplier = root.addChild(createURI("supplier"));
            {
                supplier.addChild(createURI("supkey"));
                supplier.addChild(createURI("name"));
                supplier.addChild(createURI("address"));
                supplier.addChild(createURI("city"));
                supplier.addChild(createURI("nation"));
                supplier.addChild(createURI("region"));
                supplier.addChild(createURI("phone"));
            }
            ContextTreeIndexNode<String> orderDate = root.addChild(createURI("orderdate"));
            {
                orderDate.addChild(createURI("datekey"));
                orderDate.addChild(createURI("date"));
                orderDate.addChild(createURI("dayofweek"));
                orderDate.addChild(createURI("month"));
                orderDate.addChild(createURI("year"));
                orderDate.addChild(createURI("yeamonthnum"));
                orderDate.addChild(createURI("yearmonth"));
                orderDate.addChild(createURI("daynumweek"));
                orderDate.addChild(createURI("daynummonth"));
                orderDate.addChild(createURI("daynumyear"));
                orderDate.addChild(createURI("monthnuminyear"));
                orderDate.addChild(createURI("weeknuminyear"));
                orderDate.addChild(createURI("sellingseason"));
                orderDate.addChild(createURI("lastdayinweek"));
                orderDate.addChild(createURI("notlastdayinmonth"));
                orderDate.addChild(createURI("holiday"));
                orderDate.addChild(createURI("weekday"));
            }
            ContextTreeIndexNode<String> commitDate = root.addChild(createURI("commitdate"));
            {
                commitDate.addChild(createURI("datekey"));
                commitDate.addChild(createURI("date"));
                commitDate.addChild(createURI("dayofweek"));
                commitDate.addChild(createURI("month"));
                commitDate.addChild(createURI("year"));
                commitDate.addChild(createURI("yeamonthnum"));
                commitDate.addChild(createURI("yearmonth"));
                commitDate.addChild(createURI("daynumweek"));
                commitDate.addChild(createURI("daynummonth"));
                commitDate.addChild(createURI("daynumyear"));
                commitDate.addChild(createURI("monthnuminyear"));
                commitDate.addChild(createURI("weeknuminyear"));
                commitDate.addChild(createURI("sellingseason"));
                commitDate.addChild(createURI("lastdayinweek"));
                commitDate.addChild(createURI("notlastdayinmonth"));
                commitDate.addChild(createURI("holiday"));
                commitDate.addChild(createURI("weekday"));
            }
		}
		
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.READ) ;
		//get cube
		for (ContextTreeIndexNode<String> parent : root.getChildren()) {
			if (parent.getChildren().size() == 0) {
				addContextValues(dataset, parent);
			}
			else {
				for (ContextTreeIndexNode<String> child : parent.getChildren()) {
					addContextValues(dataset, child);
				}
			}
		}
		dataset.end();
		logger.endBuildIndex();
		System.out.println("build index: ContextTreeIndex, Time: "+ Duration.between(start, Instant.now()).toMillis());
	
		ProvenanceIndex pi = new ContextTreeIndex(root);
//		try {
//			SerilizeIndex(pi);
//		} catch (IOException e) {
//			System.out.println("Problem serializing: " + e);
//			e.printStackTrace();
//		}
		return pi;
	}

	private String createURI(String string) {
		return Config.getNamespace()+string;
	}

	private void SerilizeIndex(ProvenanceIndex pi) throws IOException {
		Logger logger = Logger.getInstance();
		FileOutputStream fileOut = new FileOutputStream(Config.getDatasetLocation()+indexPath);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		logger.startWriteIndexToDisk();
		out.writeObject(pi);
		logger.endWriteIndexToDisk();
		out.close();
		fileOut.close();
	}

	private void addContextValues(Dataset dataset,ContextTreeIndexNode<String> parent) {
		Query query = QueryFactory.create("SELECT ?G { GRAPH ?G { ?s <"+ parent.getData() +"> ?o . } } ");
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		ResultSet results = qexec.execSelect() ;
		for ( ; results.hasNext() ; ) {
		  QuerySolution soln = results.nextSolution() ;
		  parent.addChild(soln.get("?G").toString());
		}
	}

	private String getFileName(String indexPath) {
		String[] split1 = indexPath.split("/");
		String[] split2 = split1[split1.length-1].split("\\.");
		return split2[0];
	}
}
