/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
 * This is a naive implementation of the Database class
 * that only supports MalaCards, Biomodels and NCBI's pubmed and PMC databases
 * 
*/

package database;

import persistentdatabase.model.Article;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

public class DataBase {
	
	public enum DBType {
		PUBMED, PUBMED_CENTRAL, 
		MALA_CARDS, BIO_MODELS, MeSH, NLM_catalog,
		STRUCTURE, GENE, PROTEIN, MONGODB;
	}
	
	private DBType _dbType;
	
	public DataBase (DBType dbType){
		_dbType = dbType;
	}

	public String getPath(){
		if      (_dbType.equals(DBType.PUBMED))         {return "pubmed";}
		else if (_dbType.equals(DBType.PUBMED_CENTRAL)) {return "pmc";   }
		else if (_dbType.equals(DBType.MeSH)) 			{return "mesh";   }
		else if (_dbType.equals(DBType.NLM_catalog)) 	{return "nlmcatalog";   }
		else if (_dbType.equals(DBType.STRUCTURE)) 		{return "structure";   }
		else if (_dbType.equals(DBType.PROTEIN)) 		{return "protein";   }
		else if (_dbType.equals(DBType.GENE)) 			{return "gene";   }
		else if (_dbType.equals(DBType.MALA_CARDS))     {return "http://malacards.org/search/results?query=";}
		else if (_dbType.equals(DBType.BIO_MODELS))		{return "https://www.ebi.ac.uk/biomodels-main/";}
		//else if (_dbType.equals(DBType.MONGODB))		{return "mongodb://BioProg:BioProg@cluster0-shard-00-00-qvbue.mongodb.net:27017,cluster0-shard-00-01-qvbue.mongodb.net:27017,cluster0-shard-00-02-qvbue.mongodb.net:27017/<DATABASE>?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";}
		else return null;

	}

	public String getUniqueId(){
		if      (_dbType.equals(DBType.PUBMED))         {return "PMID";}
		else if (_dbType.equals(DBType.STRUCTURE)) 		{return "_PDBid";   }
		else if (_dbType.equals(DBType.PROTEIN)) 		{return "ProteinID";   }
		else if (_dbType.equals(DBType.GENE)) 			{return "GeneID";   }
		else if (_dbType.equals(DBType.MALA_CARDS))     {return "MCID";}
		else return null;

	}

	public String getCollectionName(){
		if      (_dbType.equals(DBType.PUBMED))         {return (new Article().getEntityName());}
		else if (_dbType.equals(DBType.STRUCTURE)) 		{return (new Structure().getEntityName());   }
		else if (_dbType.equals(DBType.PROTEIN)) 		{return (new Protein().getEntityName());   }
		else if (_dbType.equals(DBType.GENE)) 			{return (new Gene().getEntityName());   }
		else if (_dbType.equals(DBType.MALA_CARDS))     {return (new Disease().getEntityName());}
		else return null;

	}

	
	
	public String getValue(){
		if      (_dbType.equals(DBType.STRUCTURE))      {return "structure";}
		else if (_dbType.equals(DBType.PUBMED)) 		{return "pubmed";}
		else if (_dbType.equals(DBType.PROTEIN)) 		{return "protein";}
		else if (_dbType.equals(DBType.GENE)) 			{return "gene";}
		else if (_dbType.equals(DBType.MALA_CARDS))     {return "malacards";}
		else return "00";
	}
	
	public DBType getType(){
		return _dbType;
	}
	
}
