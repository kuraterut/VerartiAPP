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
	public static HttpURLConnection connection;

	public static void getConnection(String urlAddr) throws Exception{
		URL url = new URL(urlAddr);
        connection = (HttpURLConnection) url.openConnection();
	}


	public static List<Appointment> getClientAppointmentsById(String token, Long clientId){
		try{
			getConnection("http://localhost:8000/api/admin/shedule/clients/" + clientId);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			List<Appointment> list = new ArrayList<>();

			JSONObject data = getJson();

			JSONArray jsonArr = (JSONArray)data.get("shedules");
			for(Object elem : jsonArr){
				JSONObject obj = (JSONObject)elem;
				
				Long appointmentId = (Long)obj.get("id");
				Appointment appointment = getAppointmentById(token, appointmentId);
				// String appointmentStatus = (String)obj.get("status");
				// String[] appointmentStartTimeStr = ((String)obj.get("start_time")).split(":");
	            // LocalTime appointmentStartTime 	= LocalTime.of(Integer.valueOf(startTimeStr[0]), Integer.valueOf(startTimeStr[1]));
				// String[] appointmentDateStr = ((String)data.get("date")).split("-");
				// LocalDate appointmentDate = LocalDate.of(Integer.valueOf(dateStr[0]), Integer.valueOf(dateStr[1]), Integer.valueOf(dateStr[2]));

				// JSONObject clientJSON = (JSONObject)obj.get("client");
				// JSONObject masterJSON = (JSONObject)obj.get("master");
				// JSONObject serviceJSON = (JSONObject)obj.get("appointment");

				// ClientInfo client = new ClientInfo();
				// MasterInfo master = new MasterInfo();
				// ServiceInfo service = new ServiceInfo();

				// client.setId((Long)clientJSON.get("id"));
				// client.setName((String)clientJSON.get("name"));
				// client.setSurname((String)clientJSON.get("surname"));
				// client.setPatronymic((String)clientJSON.get("patronymic"));
				// client.setEmail((String)clientJSON.get("email"));
				// client.setPhone((String)clientJSON.get("phone"));
				// client.setComment((String)clientJSON.get("comment"));
				// client.setBirthdayStr((String)clientJSON.get("birthday"));

				// master.setId((Long)masterJSON.get("id"));
				// master.setName((String)masterJSON.get("name"));
				// master.setSurname((String)masterJSON.get("surname"));
				// master.setPatronymic((String)masterJSON.get("patronymic"));
				


				list.add(appointment);
			}	

			return list;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static Response addNewClient(String token, ClientInfo client){
		try{
			getConnection("http://localhost:8000/api/admin/clients/");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);


			String name = client.getName();
			String surname = client.getSurname();
			String patronymic = client.getPatronymic();
			String phone = client.getPhone();
			LocalDate birthday = client.getBirthday();
			
			String birthdayStr = birthday.getYear() + "-" + birthday.getMonthValue() + "-" + birthday.getDayOfMonth();
			
			JSONObject outJson = new JSONObject();
			outJson.put("birthday", birthdayStr);
			outJson.put("name", name);
			outJson.put("surname", surname);
			outJson.put("patronymic", patronymic);
			outJson.put("phone", phone);

			sendJson(outJson);

			int status = connection.getResponseCode();
			if(status == 200){
				return new Response(200, "");
			}
			return new Response(status, "Ошибка");

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response(404, "Ошибка подключения к серверу");
	    }
	}


	public static AdminInfo getAdminByDate(String token, LocalDate date){
		try{
			getConnection("http://localhost:8000/api/admin/shedule/admin/month");

			connection.setRequestMethod("GET");

			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			String dateStr = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
			outJson.put("date", dateStr);

			sendJson(outJson);

	        JSONObject data = getJson();

	        AdminInfo admin = null; 

	        Long adminId = (Long) data.get("admin_id");
	        if(adminId != -1){
	        	admin = getAdminById(token, adminId);
	        }
	        else{
	        	admin = new AdminInfo();
	        	admin.setName("");
	        	admin.setSurname("Не назначен");
	        }
	        
	        return admin;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	


	public static Set<Long> getMastersIdsSetByDate(String token, LocalDate date){
		try{
			getConnection("http://localhost:8000/api/admin/shedule/masters/month");

			connection.setRequestMethod("GET");

			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			String dateStr = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
			outJson.put("date", dateStr);

			sendJson(outJson);

	        JSONObject data = getJson();

	        Set<Long> masters = new HashSet<>(); 

	        JSONArray jsonArr = (JSONArray) data.get("data");
	        for (Object elem: jsonArr) {
	           	Long masterId = (Long)elem;
	           	masters.add(masterId);
	        }
	        return masters;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static List<MasterInfo> getMastersListByDate(String token, LocalDate date){
		try{
			getConnection("http://localhost:8000/api/admin/shedule/masters/month");

			connection.setRequestMethod("GET");

			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			String dateStr = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
			outJson.put("date", dateStr);

			sendJson(outJson);

	        JSONObject data = getJson();

	        List<MasterInfo> masters = new ArrayList<>(); 

	        JSONArray jsonArr = (JSONArray) data.get("data");
	        for (Object elem: jsonArr) {
	           	Long masterId = (Long)elem;
	           	masters.add(getMasterById(token, masterId));
	        }
	        return masters;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static List<MasterInfo> getAllMasters(String token){
		try{
			getConnection("http://localhost:8000/api/admin/users/masters");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);


			List<MasterInfo> masters = new ArrayList<>();
			
			JSONObject data = getJson();
			JSONArray jsonArr = (JSONArray) data.get("masters");

			for(Object elem: jsonArr){
				Long masterId = (Long) elem;

				masters.add(getMasterById(token, masterId));
			}
			return masters;


		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}


	public static List<ClientInfo> getAllClients(String token){
		try{
			getConnection("http://localhost:8000/api/admin/clients/");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);


			List<ClientInfo> clients = new ArrayList<>();
			
			JSONObject data = getJson();
			JSONArray jsonArr = (JSONArray) data.get("clients");
	        
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


	public static AdminInfo getAdminById(String token, Long id){
		try{
			getConnection("http://localhost:8000/api/admin/users/" + id);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject data = getJson();

			AdminInfo admin = new AdminInfo();
			
			String name = (String)data.get("name");
			String surname = (String)data.get("surname");
			String patronymic = (String)data.get("patronymic");
			String bio = (String)data.get("bio");

			admin.setId(id);
			admin.setName(name);
			admin.setSurname(surname);
			admin.setPatronymic(patronymic);
			admin.setBio(bio);

			return admin;

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
			String phone = (String)data.get("phone");

			List<ServiceInfo> services = new ArrayList<>();
			JSONArray servicesArr = (JSONArray)data.get("services_id");
			for(Object elem: servicesArr){
				Long serviceId = (Long)elem;
				ServiceInfo service = getServiceById(token, serviceId);
				services.add(service);
			}


			master.setId(id);
			master.setName(name);
			master.setSurname(surname);
			master.setPatronymic(patronymic);
			master.setBio(bio);
			master.setPhone(phone);
			master.setServices(services);

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


	public static ClientInfo getClientByPhone(String token, String phone){
		try{
			getConnection("http://localhost:8000/api/admin/clients/phone");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject outJson = new JSONObject();
			outJson.put("phone", phone);
			sendJson(outJson);

			JSONObject data = getJson();

			ClientInfo client = new ClientInfo();
			
			Long id = (Long)data.get("id");
			String name = (String)data.get("name");
			String surname = (String)data.get("surname");
			String patronymic = (String)data.get("patronymic");
			// String phone = (String)data.get("phone");
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


	public static Response createAppointment(String token, Appointment appointment){
		try{
			getConnection("http://localhost:8000/api/admin/appointment");
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject outJson = new JSONObject();
			

			LocalDate date = appointment.getDate();
			String dateStr = date.getYear()+"-";
			dateStr += date.getMonthValue()+"-";
			dateStr += date.getDayOfMonth();

			LocalTime startTime = appointment.getStartTime();
			String startTimeStr = startTime.getHour()+":"+startTime.getMinute();

			JSONArray servicesJSON = new JSONArray();
			for(ServiceInfo service : appointment.getServices()){
				servicesJSON.add(service.getId());
			}

			Long clientId = appointment.getClient().getId();
			Long masterId = appointment.getMaster().getId();
			String comment = appointment.getComment();
			
			outJson.put("date", dateStr);
			outJson.put("start_time", startTimeStr);
			outJson.put("services", servicesJSON);
			outJson.put("client_id", clientId);
			outJson.put("master_id", masterId);
			outJson.put("comment", comment);

			sendJson(outJson);

			Response response = new Response();
			response.setCode(connection.getResponseCode());

			return response;

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	
	    	Response response = new Response();
	    	return response;
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


	public static Map<Long, List<Appointment>> getMastersByDate(String token, LocalDate date){
		try{
			getConnection("http://localhost:8000/api/admin/appointment/masters");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			Map<Long, List<Appointment>> mastersMap = new HashMap<>();

			JSONObject data = getJson();
			JSONArray mastersArr = (JSONArray)data.get("master_ids");
			for(Object elem: mastersArr){
				Long id = (Long)elem;
				mastersMap.put(id, new ArrayList<>());
			}

			return mastersMap;

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new HashMap<>();
	    }
	}

	public static Map<Long, List<Appointment>> getMastersAppointmentsByDate(String token, LocalDate date){
		try{
			getConnection("http://localhost:8000/api/admin/shedule/day");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			String dateStr = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
			
			JSONObject outJson = new JSONObject();
			outJson.put("date", dateStr);

			sendJson(outJson);

			Map<Long, List<Appointment>> appointments = getMastersByDate(token, date);
			
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


	


	public static Response getDayTransactions(String token, LocalDate date){
		try{
			getConnection("http://localhost:8000/api/admin/transactions");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			String dateStr = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
			
			JSONObject outJson = new JSONObject();
			outJson.put("date", dateStr);

			sendJson(outJson);

			JSONObject data = getJson();

			Double cash = (Double)data.get("cash");
			Double card = (Double)data.get("card");
			Double goods = (Double)data.get("goods");


			DayTransactions dayTransactions = new DayTransactions();
			
			dayTransactions.setDate(date);
			dayTransactions.setCash(cash);
			dayTransactions.setCard(card);
			dayTransactions.setGoods(goods);

			return dayTransactions;

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response(404, "Ошибка подключения к серверу");
	    }
	}

	public static Response setDayTransactions(String token, DayTransactions dayTransactions){
		try{
			getConnection("http://localhost:8000/api/admin/transactions");
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			LocalDate date = dayTransactions.getDate();
			Double cash = dayTransactions.getCash();
			Double card = dayTransactions.getCard();
			Double goods = dayTransactions.getGoods();

			String dateStr = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
			
			JSONObject outJson = new JSONObject();
			outJson.put("date", dateStr);
			outJson.put("cash", cash);
			outJson.put("card", card);
			outJson.put("goods", goods);

			sendJson(outJson);

			int status = connection.getResponseCode();
			if(status == 200){
				return new Response(200, "");
			}
			String msg = (String)getJson().get("msg");
			return new Response(status, msg); 
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response(404, "Ошибка подключения к серверу");
	    }
	}



	

	public static void sendJson(JSONObject outJson){
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


	public static JSONObject getJson(){
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