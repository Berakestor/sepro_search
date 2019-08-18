package de.sepro.search;

import de.sepro.DeleteME.Partner;
import de.sepro.appointment.Appointment;
import de.sepro.repository.AppointmentRepository;
import org.apache.catalina.mapper.Mapper;
import org.h2.store.fs.FileChannelInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.rmi.CORBA.ValueHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Searcher {
	private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	/*
	
	BESSER!
	IDEE 1: Viele kleine Anfragen
	
	1. Suche nach Partner die Service anbieten (Offen und Employees vorhanden)
	2. Klick auf Partner
		-> Suche nach allen belegten Termine am Tag X vom Partner Y
		-> Anzeige Kalender
	
	----------------
	
	IDEE 2: Eine große Anfrage (oder 2)
	
		1. Kunde sucht in der Umgebung nach freien Terminen am Tag X (Optional: Zeitraum)
		2. Programm zeigt alle Partner an
			- Filterung
				- Bietet Service an
				- An dem Tag geöffnet
				- Voll Belegte raus
				- Hat Employee Zeit und Service
					-> Jeder Employee hat eigenen Spalte für Termine
				
				
				
		3. Kunde klickt auf einen Partner und sieht Kalender des Tages von Öffnung bis Schluss
		4. Belegte Termine werden markiert
		5. Kunde kann Zeitraum raussuchen und Anfrage auf Termin senden
		6.
	 */
	
	
	//TODO: PROTOTYPE
	public void search (Long service_id, LocalDate date, LocalTime spanStart, LocalTime spanEnd) {
		
		//Get duration of service
		Properties properties = loadProperties();
		String durationUri = properties.getProperty("durationUri");
		
		String uri = "?serviceId="+service_id;
		RestTemplate restTemplate = new RestTemplate();
		Integer forObject = restTemplate.getForObject(uri, Integer.class);
		
		int duration = 0;
		if (forObject != null) {
			duration = forObject;
		} else {
			log.error("Duration is zero!");
		}
		
		//Check which partners offer wanted service
		// From REST -->  /partner/getAllWithService?serviceId=serviceId
		
		String supportsServiceUri = properties.getProperty("partnerUri");
		String partnerIdsSupportingService = restTemplate.getForObject(supportsServiceUri, String.class);
		
		//TODO: Was wird zurückgegeben?
		/*
			Direkt der Partner? (Ergo alle infos) --> Partner class wird benötigt
			Nur die ID und dannach Abfrage der Appointments am Tag anhand der ID?
		 */
		
		List<Partner> partnerList = new ArrayList<>(); //List with Partners that offer the service
		
		
		//Saves taken appointments of each partner
		HashMap<Partner,List<Appointment>> paMap = new HashMap<>();
		
		for (Partner p : partnerList) {
			Long id = p.getId();
			List<Appointment> takenAppointments = repository.findByPartnerIdAndDate(id, date);
			
			paMap.put(p,takenAppointments);
		}
		
		
		//Algorithm that shit, so only free Spaces are left
		
		
		
		//Sortieren/Anzeigen der Partner in finaler Liste nach Entfernung zum Standort
	
	}
	
	private Properties loadProperties() {
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath(); //TODO: Works?
		String pathToProperties = rootPath + "sepro_custom/config.properties";
		Properties properties = new Properties();
		try {
			properties.loadFromXML(new FileInputStream(pathToProperties));
		} catch (IOException e) {
			log.error("Configuration file not found.", e);
		}
		return properties;
	}
	
}
