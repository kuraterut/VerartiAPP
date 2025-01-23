package src.admin.connection;

import src.admin.utils.*;

import org.json.simple.*;
import org.json.simple.parser.*;
import javafx.scene.image.*;
import java.util.*;
import java.nio.file.*;
import java.net.*;
import java.io.*;
import java.time.*;

public class Connection{
	private static HttpURLConnection connection;

	public static void getConnection(String urlAddr) throws Exception{
		URL url = new URL(urlAddr);
        connection = (HttpURLConnection) url.openConnection();
	}

	public static ArrayList<String[]> getResourcesList(String token) {
		try{
			getConnection("http://localhost:8000/api/master/resource/all");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
	        JSONObject data = getJson();

	        ArrayList<String[]> result = new ArrayList<>();

			JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[3];
	            strArr[0] = String.valueOf((int)jsonObjArr.get("id"));
	            strArr[1] = (String)jsonObjArr.get("name");
	            strArr[2] = (String)jsonObjArr.get("description");
	            result.add(strArr);
	        }
	        return result;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static ArrayList<String[]> getResourcesRequestsList(String token) {
		try{
			getConnection("http://localhost:8000/api/master/resource/request");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
	        JSONObject data = getJson();

	        ArrayList<String[]> result = new ArrayList<>();

			JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[4];
	            strArr[0] = String.valueOf((int)jsonObjArr.get("id"));
	            strArr[1] = (String)jsonObjArr.get("name");
	            strArr[2] = String.valueOf((int)jsonObjArr.get("count"));
	            int status = (int)jsonObjArr.get("status");
	            switch(status){
	            	case 0:
	            		strArr[3] = "Отказано";
	            		break;
            		case 1:
            			strArr[3] = "Ожидает обработки администратором";
	            		break;
	            	case 2:
            			strArr[3] = "В пути";
	            		break;
	            	case 3:
            			strArr[3] = "Готово к получению";
	            		break;
	            	case 4:
	            		continue;
	            } 
	            
	            result.add(strArr);
	        }
	        return result;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static Map<String, String> getProfileInfo(String token){
		try{
			getConnection("http://localhost:8000/api/admin/profile/");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			Map<String, String> masterInfoMap = new HashMap<>();
			JSONObject data = getJson();

			masterInfoMap.put("name", (String)data.get("name"));
			masterInfoMap.put("surname", (String)data.get("surname"));
			masterInfoMap.put("patronymic", (String)data.get("patronymic"));
			masterInfoMap.put("phone", (String)data.get("phone"));
			masterInfoMap.put("email", (String)data.get("email"));
			masterInfoMap.put("birhday", (String)data.get("birthday"));
			masterInfoMap.put("bio", (String)data.get("bio"));
			masterInfoMap.put("role", (String)data.get("role"));
			masterInfoMap.put("photo", (String)data.get("photo"));
			
			return masterInfoMap;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static int changeProfileInfo(String token, String name, String surname, String patronymic, String email, String phone, String bio){
		try{
			getConnection("http://localhost:8000/api/master/profile/info");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("name", name);
			outJson.put("surname", surname);
			outJson.put("patronymic", patronymic);
			outJson.put("email", email);
			outJson.put("phone", phone);
			outJson.put("bio", bio);

			sendJson(outJson);
			
			int status = connection.getResponseCode();

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}

	public static int createResourceRequest(String token, int id, int count){
		try{
			getConnection("http://localhost:8000/api/master/resource/request");

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("id", id);
			outJson.put("count", count);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}

	public static String[] getResourceInfoByID(String token, int id){
		try{
			getConnection("http://localhost:8000/api/master/resource/"+id);

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			
			JSONObject data = getJson();
			String[] result = new String[2];
			result[0] = (String)data.get("name");
			result[1] = (String)data.get("description");

			return result;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static int changeProfilePassword(String token, String oldPassword, String newPassword){
		try{
			getConnection("http://localhost:8000/api/master/profile/password");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("oldPassword", oldPassword);
			outJson.put("newPassword", newPassword);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}

	public static int changeProfilePhoto(String token, File file){
		try{
			getConnection("http://localhost:8000/api/master/profile/photo");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			byte[] byteArray = Files.readAllBytes(file.toPath());
			String encodedPhoto = Base64.getEncoder().encodeToString(byteArray);
			outJson.put("photo", encodedPhoto);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}
	
	public static Image getProfilePhoto(String avatarURL){
		try{
			getConnection(avatarURL);
				
			String avatarFileName = "client/photos/"+"avatar.png";
			//TODO получить filename из URL

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			BufferedWriter writter = new BufferedWriter(new FileWriter(avatarFileName));

			String line;

	        while (!(line = reader.readLine()).equals("exit")) {
	            writter.write(line);
	        }

	        Image avatarImage = new Image(new FileInputStream("client/photos/avatar.png"));
	        return avatarImage;
		}
		catch(Exception ex){
			System.out.println(ex);
			return null;
		}
	}



	public static String[][] getTimetableByYM(int year, int month, String token){
		try{
			getConnection("http://localhost:8000/api/master/shedule/month");

			connection.setRequestMethod("GET");

			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("year", year);
			outJson.put("month", month);

			sendJson(outJson);

	        JSONObject data = getJson();

	        String[][] result = new String[32][2];

	        JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[2];
	            String dayNumStr = ((String)jsonObjArr.get("date")).substring(0, 2);
	            int dayIndex = Integer.parseInt(dayNumStr)-1;

	            strArr[0] = (String)jsonObjArr.get("count");
	            strArr[1] = (String)jsonObjArr.get("time");
	            result[dayIndex] = strArr;
	            
	        }
	        return result;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}


	public static ArrayList<ClientInfo> getAllClientsInfo(String token){
		try{
			getConnection("http://localhost:8000/api/admin/clients/");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);


			ArrayList<ClientInfo> clients = new ArrayList<>();
			
			JSONObject data = getJson();
			JSONArray jsonArr = (JSONArray) data.get("users");
	        
	        for(Object elemObj : jsonArr) {
	            JSONObject elem = (JSONObject)elemObj;
	            ClientInfo client = new ClientInfo();

	            Long id = (Long)elem.get("id");
	            String name = (String)elem.get("name");
	            String surname = (String)elem.get("surname");
	            String patronymic = (String)elem.get("patronymic");
	            String phone = (String)elem.get("phone");
	            String comment = (String)elem.get("comment");
	            String[] birthdayStr = ((String)elem.get("birthday")).split("-");
	            LocalDate birthday = LocalDate.of(Integer.valueOf(birthdayStr[0]), Integer.valueOf(birthdayStr[1]), Integer.valueOf(birthdayStr[2]));

	            client.setId(id);
	            client.setName(name);
	            client.setSurname(surname);
	            client.setPatronymic(patronymic);
	            client.setPhone(phone);
	            client.setComment(comment);
	            client.setBirthday(birthday);

	            clients.add(client);
	        }

			return clients;
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	} 

	

	public static ServiceInfo getServiceById(String token, Long id){
		try{
			getConnection("http://localhost:8000/api/admin/service/" + id);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject data = getJson();

			ServiceInfo service = new ServiceInfo();
			
			String name = (String)data.get("name");
			String description = (String)data.get("description");
			Double price = (Double)data.get("price");
			String[] timeStr = ((String)data.get("date")).split(":");
			LocalTime time = LocalTime.of(Integer.valueOf(timeStr[0]), Integer.valueOf(timeStr[1]));

			service.setId(id);
			service.setName(name);
			service.setDescription(description);
			service.setPrice(price);
			service.setTime(time);

			return service;

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static MasterInfo getMasterById(String token, Long id){
		try{
			getConnection("http://localhost:8000/api/admin/master/" + id);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject data = getJson();

			MasterInfo master = new MasterInfo();
			
			String name = (String)data.get("name");
			String surname = (String)data.get("surname");
			String patronymic = (String)data.get("patronymic");
			String bio = (String)data.get("bio");


			master.setId(id);
			master.setName(name);
			master.setSurname(surname);
			master.setPatronymic(patronymic);
			master.setBio(bio);

			return master;

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}


	public static ClientInfo getClientById(String token, Long id){
		try{
			getConnection("http://localhost:8000/api/admin/clients/" + id);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject data = getJson();

			ClientInfo client = new ClientInfo();
			
			String name = (String)data.get("name");
			String surname = (String)data.get("surname");
			String patronymic = (String)data.get("patronymic");
			String phone = (String)data.get("phone");
			String comment = (String)data.get("comment");
			String[] birthdayStr = ((String)data.get("birthday")).split("-");
			LocalDate birthday = LocalDate.of(Integer.valueOf(birthdayStr[0]), Integer.valueOf(birthdayStr[1]), Integer.valueOf(birthdayStr[2]));


			client.setId(id);
			client.setName(name);
			client.setSurname(surname);
			client.setPatronymic(patronymic);
			client.setPhone(phone);
			client.setComment(comment);
			client.setBirthday(birthday);

			return client;

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static Appointment getAppointmentById(String token, Long id){
		try{
			getConnection("http://localhost:8000/api/admin/appointment/" + id);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject data = getJson();

			Appointment appointment = new Appointment();
			
			List<ServiceInfo> services = new ArrayList<>();

            Long masterId 			= (Long)data.get("master_id");
            Long clientId 			= (Long)data.get("client_id");
			String comment 			= (String)data.get("comment");
            String[] startTimeStr 	= ((String)data.get("start_time")).split(":");
            LocalTime startTime 	= LocalTime.of(Integer.valueOf(startTimeStr[0]), Integer.valueOf(startTimeStr[1]));
			String[] dateStr = ((String)data.get("date")).split("-");
			LocalDate date = LocalDate.of(Integer.valueOf(dateStr[0]), Integer.valueOf(dateStr[1]), Integer.valueOf(dateStr[2]));
			
			JSONArray servicesArr = (JSONArray)data.get("services");
			for(Object serviceObj: servicesArr){
				Long serviceId = (Long)serviceObj;
				ServiceInfo service = getServiceById(token, serviceId);
				services.add(service);
			}

			MasterInfo master = getMasterById(token, masterId);
			ClientInfo client = getClientById(token, clientId);
			
			appointment.setId(id);
			appointment.setStartTime(startTime);
			appointment.setClient(client);
			appointment.setMaster(master);
			appointment.setDate(date);
			appointment.setServices(services);
			appointment.setComment(comment);

			return appointment;

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static Map<Long, List<Appointment>> getDayInfoMapMaster(String token, LocalDate date){
		try{
			getConnection("http://localhost:8000/api/admin/shedule/day");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			String dateStr = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
			
			JSONObject outJson = new JSONObject();
			outJson.put("date", dateStr);

			sendJson(outJson);

			Map<Long, List<Appointment>> appointments = new HashMap<>();
			
			JSONObject data = getJson();
			JSONArray jsonArr = (JSONArray) data.get("data");
	        
	        for (Object elemObj: jsonArr){
	            Appointment appointment = new Appointment();
				List<ServiceInfo> services = new ArrayList<>();
	            
	            JSONObject elem = (JSONObject)elemObj;

	            Long id 				= (Long)elem.get("id");
	            Long masterId 			= (Long)elem.get("master_id");
	            Long clientId 			= (Long)elem.get("client_id");
				String comment 			= (String)elem.get("comment");
	            String[] startTimeStr 	= ((String)elem.get("start_time")).split(":");
	            LocalTime startTime 	= LocalTime.of(Integer.valueOf(startTimeStr[0]), Integer.valueOf(startTimeStr[1]));
				
				
				JSONArray servicesArr = (JSONArray)elem.get("services");
				for(Object serviceObj: servicesArr){
					Long serviceId = (Long)serviceObj;
					ServiceInfo service = getServiceById(token, serviceId);
					services.add(service);
				}

				MasterInfo master = getMasterById(token, masterId);
				ClientInfo client = getClientById(token, clientId);
				
				appointment.setId(id);
				appointment.setStartTime(startTime);
				appointment.setClient(client);
				appointment.setMaster(master);
				appointment.setDate(date);
				appointment.setServices(services);
				appointment.setComment(comment);

				List<Appointment> appointmentsList = null;
	            if(appointments.containsKey(masterId)){
	            	appointmentsList = appointments.get(masterId);
	            }
	            else{
	            	appointmentsList = new ArrayList<>();
	            }

	            appointmentsList.add(appointment);
	            appointments.put(masterId, appointmentsList);
	        }

			return appointments;
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return new HashMap<>();
	    }
	}




	private static void sendJson(JSONObject outJson){
		try{
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
	        writer.write(outJson.toString());
	        writer.flush();
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return;
	    }
	}


	private static JSONObject getJson(){
		try{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

	        String line;
	        StringBuilder response = new StringBuilder();

	        while ((line = reader.readLine()) != null) {
	            response.append(line);
	        }

	        reader.close();

	        String responseStr = response.toString();

	        Object obj = new JSONParser().parse(responseStr);
	        return (JSONObject) obj;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}


}