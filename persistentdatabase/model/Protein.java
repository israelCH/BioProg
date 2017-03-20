package persistentdatabase.model;

import java.io.Serializable;

import javax.persistence.Id;

public class Protein implements Persistable, Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	public static String ENTITY_NAME = "Protein";
	public Protein() {
		super ();
	}
		
		private String ProteinID;


		private String _caption;
		private String _title;
		private int _gi;
		private String _createDate;
		private String _updateDate;
		private int _flags;
		private int _taxID;
		private int _length;
		private String _status;
		private String _replacedBy;
		private String _comment;
		private String _accessionVersion;

		public void setProtID(String id) {
			ProteinID = id;
		}
		
		public void setCaption(String caption) {
			_caption = caption;
		}
		
		public void setTitle(String title) {
			_title = title;
		}
		
		public void setGi(int gi) {
			_gi = gi;
		}
		
		public void setCreateDate(String createDate) {
			_createDate = createDate;
		}

		public void setUpdateDate(String updateDate) {
			_updateDate = updateDate;
		}		
		
		public void setFlags(int flags) {
			_flags = flags;
		}
		
		public void setTaxID(int taxID) {
			_taxID = taxID;
		}
		
		public void setLength(int length) {
			_length = length;
		}
		
		public void setStatus(String stat) {
			_status = stat;
		}
		
		public void setReplacedBy(String replacedBy) {
			_replacedBy = replacedBy;
		}
		
		public void setComment(String comment) {
			_comment = comment;
		}
		

		public void setAccessionVersion(String accessionVersion) {
			_accessionVersion = accessionVersion;
		}
		
		

		
		//-----------------------------------------
		
		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return ProteinID;
		}
		
		public String getCaption() {
			return _caption;
		}
		
		public String getTitle() {
			return _title;
		}
		
		public int getGi() {
			return _gi;
		}
		
		public String getCreateDate() {
			return _createDate;
		}
		
		public String getUpdateDate() {
			return _updateDate;
		}
		
		public Integer getFlags() {
			return _flags;
		}
		
		public Integer getTaxID() {
			return _taxID;
		}
		
		public int getLength() {
			return _length;
		}
		
		public String getStatud() {
			return _status;
		}
		
			public String getReplacedBy() {
			return _replacedBy;
		}
		
		public String getComment() {
			return _comment;
		}
		
		public String getAccessionVersion() {
			return _accessionVersion;
		}

		@Override
		public String getIdIdentifier() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getEntityName() {
			// TODO Auto-generated method stub
			return ENTITY_NAME;
		}
		
		
		
		
		}