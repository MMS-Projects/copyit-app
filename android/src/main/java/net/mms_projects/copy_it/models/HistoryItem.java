package net.mms_projects.copy_it.models;

import java.util.Date;

public class HistoryItem {

	public String content;
	public Date date;
	public Change change;

	public HistoryItem(String content, Date date, Change change) {
		this.content = content;
		this.date = date;
		this.change = change;
	}

	public enum Change {
		PULLED, PUSHED, SEND_TO_APP, RECEIVED_FROM_APP;
	}

}
