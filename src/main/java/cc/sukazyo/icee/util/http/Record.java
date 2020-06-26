package cc.sukazyo.icee.util.http;

import java.sql.Date;

public class Record {
	
	private int id;
	private String record;
	private String method;
	private String uri;
	private String version;
	private Date visitDate;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getRecord() {
		return record;
	}
	
	public void setRecord(String record) {
		this.record = record;
	}
	
	public Date getVisitDate() {
		return visitDate;
	}
	
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	
}