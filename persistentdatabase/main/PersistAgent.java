/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package persistentdatabase.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import persistentdatabase.model.Persistable;

public class PersistAgent {
	
	private final String PERSISTENCE_UNIT_NAME = "people";
	private EntityManagerFactory factory;
	private EntityManager entityManager;
	
	public PersistAgent(){	
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = factory.createEntityManager();
	}
	
	public void PersistObject(Persistable object){
		// Begin a new local transaction so that we can persist a new entity
		entityManager.getTransaction().begin();	
		
		Query q = entityManager.createQuery
				("SELECT p FROM " + object.getEntityName()+ " p WHERE p." + object.getIdIdentifier() 
				+ " = :" + object.getIdIdentifier());
		q. setParameter(object.getIdIdentifier(), object.getId());
		
		boolean isNotExist = (q.getResultList().size() == 0);

		if (isNotExist)
		    entityManager.persist(object);
		else 
			System.err.println("Entry: " + object.getId() + " is already exist");
		
		entityManager.getTransaction().commit();
	}
	
	/*
	public void PersistAneurysm(Aneurysm aneurysm){
		// Begin a new local transaction so that we can persist a new entity
		entityManager.getTransaction().begin();	
		
		Query q = entityManager.createQuery
				("SELECT p FROM Aneurysm p WHERE p.patientID = :patientID");
		q. setParameter("patientID", aneurysm.getId());
		
		boolean isNotExist = (q.getResultList().size() == 0);

		if (isNotExist)
		    entityManager.persist(aneurysm);
		else 
			System.err.println("Entry: " + aneurysm.getId() + " is already exist");
		
		entityManager.getTransaction().commit();
	}
	
	public void PersistArticle(Article article){
		
		// Begin a new local transaction so that we can persist a new entity
		entityManager.getTransaction().begin();	
		
		Query q = entityManager.createQuery
				("SELECT p FROM Article p WHERE p.PMID = :cPMID");
		q. setParameter("cPMID", article.getPMID());
		
		boolean isNotExist = (q.getResultList().size() == 0);

		if (isNotExist)
		    entityManager.persist(article);
		else 
			System.err.println("Entry: " + article.getPMID() + " is already exist");
		
		entityManager.getTransaction().commit();
	}
	
	public void PersistDisease(Disease disease){
		
		// Begin a new local transaction so that we can persist a new entity
		entityManager.getTransaction().begin();	
		
		Query q = entityManager.createQuery
				("SELECT p FROM Disease p WHERE p.name = :cName");
		q. setParameter("cName", disease.getName());
		
		boolean isNotExist = (q.getResultList().size() == 0);

		if (isNotExist)
		    entityManager.persist(disease);
		else 
			System.err.println("Entry: " + disease.getName() + " is already exist");
		
		entityManager.getTransaction().commit();
	}
	
	public void PersistModel(Model model){
		// Begin a new local transaction so that we can persist a new entity
		entityManager.getTransaction().begin();	
		
		Query q = entityManager.createQuery
				("SELECT p FROM Model p WHERE p.id = :cId");
		q. setParameter("cId", model.getID());
		
		boolean isNotExist = (q.getResultList().size() == 0);

		if (isNotExist)
		    entityManager.persist(model);
		else 
			System.err.println("Entry: " + model.getID() + " is already exist");
		
		entityManager.getTransaction().commit();
	}
	
	*/
	
	public void showObjects(String className){
		
		Query q = entityManager.createQuery("SELECT m FROM " + className + "  m");
	    System.out.println(q);
	    
	    @SuppressWarnings("unchecked")
		List<Persistable> objectList = q.getResultList();
	    for (Persistable object : objectList) {
	      System.out.println("---------------------------------------");
	      System.out.println(object);
	    }
	    System.out.println("---------------------------------------");
		
	}
	
	/*
	public void showArticles(){
		Query q = entityManager.createQuery("SELECT m FROM Article m");
	    System.out.println(q);
	    
	    List<Article> articleList = q.getResultList();
	    for (Article article : articleList) {
	      System.out.println("---------------------------------------");
	      System.out.println(article);
	    }
	    System.out.println("---------------------------------------");
	}
	
	public void showAneurysms(){
		Query q = entityManager.createQuery("SELECT m FROM Aneurysm m");
	    System.out.println(q);
	    
	    List<Aneurysm> aneurysmList = q.getResultList();
	    for (Aneurysm aneurysm : aneurysmList) {
	      System.out.println("---------------------------------------");
	      System.out.println(aneurysm);
	    }
	    System.out.println("---------------------------------------");
	}
	
	public void showDiseases(){
		Query q = entityManager.createQuery("SELECT m FROM Disease m");
	    System.out.println(q);
	    
	    List<Disease> diseaseList = q.getResultList();
	    for (Disease disease : diseaseList) {
	      System.out.println("---------------------------------------");
	      System.out.println(disease);
	    }
	    System.out.println("---------------------------------------");
	}
	
	public void showModels(){
		Query q = entityManager.createQuery("SELECT m FROM Model m");
	    System.out.println(q);
	    
	    List<Model> modelList = q.getResultList();
	    for (Model model : modelList) {
	      System.out.println("---------------------------------------");
	      System.out.println(model);
	    }
	    System.out.println("---------------------------------------");
	}
	
	*/
	
	@SuppressWarnings("unchecked")
	public List<Persistable> getObjectsList(String className){
		Query q = entityManager.createQuery("SELECT m FROM " + className + " m");
	    return q.getResultList();
	}
	
	/*
	
	public List<Article> getArticles(){
		Query q = entityManager.createQuery("SELECT m FROM Article m");
	    return q.getResultList();
	}
	
	public List<Aneurysm> getAneurysms(){
		Query q = entityManager.createQuery("SELECT m FROM Aneurysm m");
	    return q.getResultList();
	}
	*/
		
}

