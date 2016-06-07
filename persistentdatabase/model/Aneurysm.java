/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package persistentdatabase.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Aneurysm implements Persistable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	
	public static String ENTITY_NAME = "Aneurysm";
	
	private String patientID;
	private String institution;
	private String modality;
	private String age;
	private String sex;
	private String aneurysmType;
	private String aneurysmLocation;
	private String raptureStatus;
	private String multipleAneurysm;
	
	private String image;
	private String STLmodel;
	
	private List<Disease> diseases = new ArrayList<Disease>();
	private List<Article> articles = new ArrayList<Article>();
	private List<Model>   models   = new ArrayList<Model>();
	
	public void setId(String id)              { patientID        = id;   }
	public void setinstitution(String inst)   { institution      = inst; }
	public void setModality(String mod)       { modality         = mod;  }
	public void setAge(String a)              { age              = a;    }
	public void setSex(String s)              { sex              = s;    }
	public void setAneurysmType(String a)     { aneurysmType     = a;    }
	public void setAneurysmLocation(String a) { aneurysmLocation = a;    }
	public void setRaptureStatus(String r)    { raptureStatus    = r;    }
	public void setMultipleAneurysm(String m) { multipleAneurysm = m;    }
	
	public void setImage(String img)          { image            = img;  }
	public void setSTLmodel(String stl)       { STLmodel         = stl;  }
	
	public String getId() { return patientID;}
	
	public String toString(){
		
		return "Patient ID: " + patientID + "\n" +
			   "SEX: " + sex + ", AGE: " + age + "\n" + 
			   "Aneurysm type: " + aneurysmType + 
			   ", location:" + aneurysmLocation + 
			   ", status: " + raptureStatus;
	}
	
	public void addDisease(Disease disease) { diseases.add(disease); }
	public void addArticle(Article article) { articles.add(article); }
	public void addModel(Model model)       { models.add(model);     }
	
	public List<Disease> getLinkedDiseases() { return diseases; }
	public List<Article> getLinkedArticles() { return articles; }
	public List<Model>   getLinkedModels()   { return models;   }
	
	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	@Override
	public String getIdIdentidier() {
		return "patientID";
	}
	
	
	

}
