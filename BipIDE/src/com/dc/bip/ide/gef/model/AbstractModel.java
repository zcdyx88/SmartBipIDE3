package com.dc.bip.ide.gef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AbstractModel {
	
 private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
 
 
 public void  addPropertyChangeListener(PropertyChangeListener listener)
 {
	 listeners.addPropertyChangeListener(listener);
 }
 
 
 public void removePropertyChangeListener(PropertyChangeListener listener)
 {
	 listeners.removePropertyChangeListener(listener);
 }
 
 public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
 {
	 listeners.firePropertyChange(propertyName, oldValue, newValue);
 }
}
