package org.dync.teammeeting.ui.swipe;

import org.dync.teammeeting.ui.swipe.SwipeLayout.Status;

public interface SwipeLayoutInterface {

	Status getCurrentStatus();
	
	void close();
	
	void open();
}
