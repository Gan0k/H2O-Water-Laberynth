package com.mygdx.sorryFib.net;

import java.util.Observer;

public interface NetworkActionResolver {
	public void send(Object obj) throws NetworkException;
	public void connect();
	public void startServer();
	public boolean isServer();
	
	public void addObserver(Observer observer);
}