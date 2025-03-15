package nss.cviceni2.client;

import java.io.FileNotFoundException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import nss.cviceni2.compute.ServerInterface;

public class Client {

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		String host;
		int PORT;
		String conf_file;

		if (args.length != 3) {
			// <IP_adresa> <cislo_portu> <konf_soubor>
			host = "localhost"; //host = "127.0.0.1";
			PORT = 7777; 
			conf_file = "./data/conf.csv"; // konfig. soubor
		} else {
			host = args[0]; 
			PORT = Integer.valueOf(args[1]); 
			conf_file = args[2]; 
		}

		try {
			// vyhledani vzdaleneho objektu
			ServerInterface serveri;
			String name = "muj_server";
			Registry registry = LocateRegistry.getRegistry(host, PORT);

			 serveri = (ServerInterface) registry.lookup(name);

			 //TODO HERE continue.. :-)

		} catch (Exception e) {
			System.err.println("Data exception: " + e.getMessage());
		}
	}
}
