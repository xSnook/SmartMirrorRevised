package code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WeatherComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	private String baseLocationURL = "https://api.ipdata.co/";
	private String locationAPIKey = "309a92ed90fc74bff0f5a0e4bdb9ce3173f5d021e89c585fcdef3fd0";

	private String baseWeatherURL = "http://api.openweathermap.org/data/2.5/forecast?";
	private String weatherURL = "";
	private String weatherAPIKey = "802bb415079ab7777270c812b021b7d9";

	private String IPv6 = "";
	private String latitude = "";
	private String longitude = "";
	private String ZIPCode = "";

	public WeatherComponent() {

		setLocationData(buildLocationURL(getIPv6()));
		System.out.println(latitude + " " + longitude + " " + ZIPCode);
		System.out.println(ZIPCode);
		buildWeatherURL();
		getCurrentWeatherData();
	}
	/**
	 * Converts given times (sunrise/sunset) from UTC to the JVM's current/default time zone
	 * 
	 * @param vals -
	 * 		array of the two times to convert to current time zone
	 * @return
	 * 		array containing the converted times
	 */
	private String[] adjustSunTimes(String[] vals) {
		String sunrise = vals[0];
		String sunset = vals[1];
		DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		DateFormat currentFormat = new SimpleDateFormat("hh:mm a");
		currentFormat.setTimeZone(TimeZone.getDefault());
		Date d1 = null, d2 = null;
		try {
			d1 = utcFormat.parse(sunrise);
			d2 = utcFormat.parse(sunset);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arr = {currentFormat.format(d1).toLowerCase(), currentFormat.format(d2).toLowerCase()};
		return arr;
	}

	private void getCurrentWeatherData() {
		try {
			// File inputFile = new File("input.txt");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(weatherURL);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("time");
			String[] arr = {doc.getElementsByTagName("sun").item(0).getAttributes().item(0).getTextContent(),
					doc.getElementsByTagName("sun").item(0).getAttributes().item(1).getTextContent()};
			System.out.println("Sunrise : " + arr[0]);
			System.out.println("Sunset : " + arr[1]);
			arr = adjustSunTimes(arr);
			System.out.println("Sunrise : " + arr[0]);
			System.out.println("Sunset : " + arr[1]);
			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("Current Element : " + nNode.getNodeName() + " - " + temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("from : " + eElement.getAttribute("from"));
					System.out.println("to : " + eElement.getAttribute("to"));
					System.out.println("Temperature : " + eElement.getElementsByTagName("temperature").item(0)
							.getAttributes().item(1).getTextContent());
					System.out.println("Status : "
							+ eElement.getElementsByTagName("symbol").item(0).getAttributes().item(0).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setLocationData(String locationURL) {
		URL url;
		try {
			url = new URL(locationURL);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.contains("postal"))
					ZIPCode = inputLine.split(":")[1].trim().replaceAll("\"", "").replace(",", "");
				if (inputLine.contains("latitude"))
					latitude = inputLine.split(":")[1].trim().replace(",", "");
				if (inputLine.contains("longitude"))
					longitude = inputLine.split(":")[1].trim().replace(",", "");
			}
			br.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	/**
	 * Returns a ZIP code relative to the computer's location.
	 * 
	 * @return the gathered ZIP code
	 */

	public String getZIP() {
		return this.ZIPCode;
	}

	private String buildLocationURL(String ip) {
		return baseLocationURL + ip + "?api-key=" + locationAPIKey;
	}

	/**
	 * Builds a url to access openWeather's API using either a ZIP code or longitude
	 * and latitude as a fall back
	 * 
	 * Program will exit if these are all somehow null at this point
	 * 
	 * @return a String containing the weather API's URL
	 */

	private void buildWeatherURL() {
		if (weatherURL.equalsIgnoreCase("") || weatherURL == null) {
			if (ZIPCode != null) {
				weatherURL = baseWeatherURL + "zip=" + ZIPCode + ",us&appid=" + weatherAPIKey + "&mode=xml";
			} else if (latitude != null && longitude != null) {
				weatherURL = baseWeatherURL + "lat=" + latitude + "&lon=" + longitude + "&mode=xml";
			} else {
				System.out.println("Data gathering failed");
				System.exit(1);
			}
		}
	}
////////////////////
	private String getIPv6() {
		if (IPv6.equalsIgnoreCase("") || IPv6 == null) {
			InetAddress[] addr = null;
			try {
				// System.out.println("getLocalHost: " + InetAddress.getLocalHost().toString());
				addr = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
				for (InetAddress a : addr) {
					System.out.println(a.toString());
				}
			} catch (UnknownHostException _e) {
				_e.printStackTrace();
			}
			String Ip1 = addr[addr.length - 1].toString().split("/")[1];
			String Ip2 = addr[addr.length - 2].toString().split("/")[1];
			String IPToUse = !isIPv6(Ip1) || Ip1.contains("%")
					? (isIPv6(Ip2) && !Ip2.contains("%")
							? addr[addr.length - 2].toString().split("/")[1]
							: null)
					: addr[addr.length - 1].toString().split("/")[1];

			System.out.println(IPToUse);
			if (IPToUse != null) {
				this.IPv6 = IPToUse;
				return IPToUse;
			}
			return null;
		}
		return this.IPv6;
	}

	public boolean isIPv6(String addr) {
		if (addr.matches(
				"((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param addr
	 * @return whether IP is valid IPv4 (or IPv6)
	 */
	public boolean isIPv4(String addr) {
		return isIPv6(addr);
	}
}
