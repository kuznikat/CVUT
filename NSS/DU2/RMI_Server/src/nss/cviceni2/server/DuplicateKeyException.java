package nss.cviceni2.server;

import java.rmi.RemoteException;

public class DuplicateKeyException extends RemoteException {

	private static final long serialVersionUID = -1937161867341487386L;

	public DuplicateKeyException(String message) {
		super(message);
	}

}
