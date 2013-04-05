package ifm9.items;

public class TI {
	/*----------------------------
	 * Class fields
		----------------------------*/
	// Id
	long fileId;
	
	// Path
	String file_path;
	
	// Name
	String file_name;

	// Date
	long date_added;
	
	// Modified
	long date_modified;
	
	// Memo
	String memo;
	
	// Tags
	String tags;
	
	long last_viewed_at;
	
	String table_name;

	/*----------------------------
	 * Constructor
		----------------------------*/
	public TI(long fileId, String file_path, String file_name, 
												long date_added, long date_modified) {
		//
		this.fileId = fileId;
		
		this.file_path = file_path;
		
		this.file_name = file_name;
				
		this.date_added = date_added;
		
		this.date_modified = date_modified;
		
	}//public ThumbnailItem(long fileId, String file_path, long date_added, long date_modified)
	
	/*----------------------------
	 * Methods
		----------------------------*/
	// Num of params => 7
	public TI(long fileId, String file_path, String file_name, 
			long date_added, long date_modified, String memo,
			String tags) {
		//
		this.fileId = fileId;
		
		this.file_path = file_path;
		this.file_name = file_name;
				
		this.date_added = date_added;
		this.date_modified = date_modified;
		
		this.memo = memo;
		this.tags = tags;
		
	}//public ThumbnailItem()

	// Num of params => 8
	public TI(long fileId, String file_path, String file_name, 
			long date_added, long date_modified, String memo,
			String tags, long last_viewed_at) {
		//
		this.fileId = fileId;
		
		this.file_path = file_path;
		this.file_name = file_name;
				
		this.date_added = date_added;
		this.date_modified = date_modified;
		
		this.memo = memo;
		this.tags = tags;
		
		this.last_viewed_at = last_viewed_at;
		
	}//public ThumbnailItem()

	// Num of params => 9
	public TI(long fileId, String file_path, String file_name, 
			long date_added, long date_modified, String memo,
			String tags, long last_viewed_at, String table_name) {
		//
		this.fileId = fileId;
		
		this.file_path = file_path;
		this.file_name = file_name;
				
		this.date_added = date_added;
		this.date_modified = date_modified;
		
		this.memo = memo;
		this.tags = tags;
		
		this.last_viewed_at = last_viewed_at;
		
		this.table_name = table_name;
		
	}//public ThumbnailItem()

	public long getFileId() {
		return fileId;
	}

	public String getFile_path() {
		return file_path;
	}

	public long getDate_added() {
		return date_added;
	}

	public long getDate_modified() {
		return date_modified;
	}

	public String getMemo() {
		return memo;
	}

	public String getTags() {
		return tags;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public void setDate_added(long date_added) {
		this.date_added = date_added;
	}

	public void setDate_modified(long date_modified) {
		this.date_modified = date_modified;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getFile_name() {
		return file_name;
	}


	public long getLast_viewed_at() {
		return last_viewed_at;
	}

	
	public void setLast_viewed_at(long last_viewed_at) {
		this.last_viewed_at = last_viewed_at;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	
	
}//public class ThumbnailItem
