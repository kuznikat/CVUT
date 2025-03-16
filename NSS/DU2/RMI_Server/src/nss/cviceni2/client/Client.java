package nss.cviceni2.client;


import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

import nss.cviceni2.compute.ServerInterface;
import nss.cviceni2.server.CsvReader;
import java.io.FileNotFoundException;
import java.rmi.RMISecurityManager;
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

			FileReader fr = new FileReader("data/conf.csv");
			CsvReader reader = new CsvReader(fr, ',');


			while (reader.readRecord()){
				int colCount = reader.getColumnCount();
				String [] content = reader.getValues();

				if(colCount == 0 || content.length == 0) continue;

				String command = content[0].trim();
				System.out.println(">> " + String.join(" ", content));

				try {
					switch (command) {
						case "listdb":
							System.out.println("<< Databases: " + serveri.listDB());
							break;
						case "createdb":
							if(content.length > 1){
								String dbName = content[1].trim();
								serveri.createDB(dbName);
								System.out.println("<< Database \"" + dbName + "\" created");
							}else {
								System.out.println("<< ERROR - provide Database name");
							}
							break;
//						case "insert":
//							if(content.length > 3){
//								String dbName = content[1].trim();
//								String dbId = content[2].trim();
//								String dbData = content[3].trim();
//								System.out.println(">> Inserting into database \"" + dbName + "\" record [\"" + dbId + "\";\"" + dbData + "\"]");
//                                Integer inserted = null;
//                                try {
//                                    inserted = serveri.insert(dbName, Integer.valueOf(dbId), dbData);
//                                } catch (RemoteException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                                if (inserted != null ) {
//									System.out.println("<< DB \"" + dbName + "\" - record inserted");
//								} else {
//									System.out.println("<< ERROR - Database \"" + dbName + "\" does not exist");
//								}
//							} else {
//								System.err.println("<< ERROR - Incorrect 'insert' syntax");
//							}
//							break;
						case "insert":
							System.out.println("Processing insert: " + Arrays.toString(content));
							if (content.length >=3) {
								String dbName = content[1].trim();
								try {
									int dbId = Integer.parseInt(content[2].trim());
									String dbData = content[3].trim();
									System.out.println(">> Inserting into database '" + dbName + "' record [" + dbId + "; " + dbData + "]");
									int insertedKey = serveri.insert(dbName, dbId, dbData);
									System.out.println("<< Record inserted with key: " + insertedKey);
								} catch (NumberFormatException e) {
									System.out.println("<< ERROR - 'insert' failed: ID must be a number");
								}
							} else {
								System.out.println("<< ERROR - Incorrect 'insert' syntax: " + Arrays.toString(content));
							}
							break;

						case "update":
							if (content.length > 3) {
								String dbName = content[1].trim();
								String dbId = content[2].trim();
								String dbData = content[3].trim();
								System.out.println(">> Updating record \"" + dbId + "\" in database \"" + dbName + "\" with \"" + dbData + "\"");
								Integer updated = serveri.update(dbName, Integer.valueOf(dbId), dbData);
								if (updated != null) {
									System.out.println("<< DB \"" + dbName + "\" - record updated");
								} else {
									System.out.println("<< ERROR - Record \"" + dbId + "\" not found in DB \"" + dbName + "\"");
								}
							} else {
								System.err.println("<< ERROR - Incorrect 'update' syntax");
							}
							break;
						default:
							System.out.println("<< ERROR – command \"" + command + "\" not implemented");
					}
				} catch (Exception e) {
					System.err.println("<< ERROR executing command: " + command + " - " + e.getMessage());
				}
			}

	}
}
